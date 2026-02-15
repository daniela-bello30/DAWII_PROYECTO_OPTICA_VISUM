package pe.edu.cibertec.mscitas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.mscitas.client.AutenticacionClient; // NUEVO
import pe.edu.cibertec.mscitas.dto.CitaDTO;
import pe.edu.cibertec.mscitas.dto.CitaEventoDTO;
import pe.edu.cibertec.mscitas.dto.UsuarioDTO; // NUEVO
import pe.edu.cibertec.mscitas.model.Cita;
import pe.edu.cibertec.mscitas.model.Sucursal;
import pe.edu.cibertec.mscitas.model.TipoServicio;
import pe.edu.cibertec.mscitas.repository.CitaRepository;
import pe.edu.cibertec.mscitas.repository.SucursalRepository;
import pe.edu.cibertec.mscitas.repository.TipoServicioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CitaService {

    private final CitaRepository citaRepository;
    private final SucursalRepository sucursalRepository;
    private final TipoServicioRepository tipoServicioRepository;
    private final CitaEventoPublisher eventoPublisher;
    private final AutenticacionClient autenticacionClient; // INYECTADO

    /**
     * Obtener todas las citas
     */
    public List<CitaDTO> obtenerTodas() {
        return citaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener cita por ID
     */
    public CitaDTO obtenerPorId(Integer id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
        return convertirADTO(cita);
    }

    /**
     * Obtener citas por usuario
     */
    public List<CitaDTO> obtenerPorUsuario(Integer idUsuario) {
        return citaRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Crear nueva cita y publicar evento
     */
    @Transactional
    public CitaDTO crear(CitaDTO citaDTO) {
        log.info("Validando usuario ID: {} con ms-autenticacion", citaDTO.getIdUsuario());

        // 1. VALIDACIÓN EXTERNA (Síncrona via Feign)
        try {
            UsuarioDTO usuario = autenticacionClient.obtenerUsuario(citaDTO.getIdUsuario());
            if (usuario == null) {
                throw new RuntimeException("El usuario no existe en el sistema de autenticación");
            }
            log.info("Usuario validado: {} {}", usuario.getNombres(), usuario.getApellidos());
        } catch (Exception e) {
            log.error("Error de comunicación con ms-autenticacion: {}", e.getMessage());
            throw new RuntimeException("No se pudo validar el usuario. Verifique si ms-autenticacion está activo.");
        }

        // 2. VALIDACIONES INTERNAS (Sucursal y Servicio)
        Sucursal sucursal = sucursalRepository.findById(citaDTO.getIdSucursal())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        TipoServicio tipoServicio = tipoServicioRepository.findById(citaDTO.getIdTipoServicio())
                .orElseThrow(() -> new RuntimeException("Tipo de servicio no encontrado"));

        // 3. CREAR ENTIDAD
        Cita cita = new Cita();
        cita.setIdUsuario(citaDTO.getIdUsuario());
        cita.setSucursal(sucursal);
        cita.setTipoServicio(tipoServicio);
        cita.setFechaCita(citaDTO.getFechaCita());
        cita.setHoraCita(citaDTO.getHoraCita());
        cita.setEstadoCita(Cita.EstadoCita.Programada);
        cita.setNotas(citaDTO.getNotas());

        // Guardar en BD
        Cita citaGuardada = citaRepository.save(cita);

        // 4. PUBLICAR EVENTO (Asíncrono via RabbitMQ)
        CitaEventoDTO evento = crearEvento(citaGuardada, "CREADA");
        eventoPublisher.publicarEvento(evento);

        return convertirADTO(citaGuardada);
    }

    /**
     * Actualizar cita existente y publicar evento
     */
    @Transactional
    public CitaDTO actualizar(Integer id, CitaDTO citaDTO) {
        log.info("Actualizando cita ID: {}", id);

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (citaDTO.getIdSucursal() != null) {
            Sucursal sucursal = sucursalRepository.findById(citaDTO.getIdSucursal())
                    .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));
            cita.setSucursal(sucursal);
        }

        if (citaDTO.getIdTipoServicio() != null) {
            TipoServicio tipoServicio = tipoServicioRepository.findById(citaDTO.getIdTipoServicio())
                    .orElseThrow(() -> new RuntimeException("Tipo de servicio no encontrado"));
            cita.setTipoServicio(tipoServicio);
        }

        if (citaDTO.getFechaCita() != null) {
            cita.setFechaCita(citaDTO.getFechaCita());
        }

        if (citaDTO.getHoraCita() != null) {
            cita.setHoraCita(citaDTO.getHoraCita());
        }

        if (citaDTO.getEstadoCita() != null) {
            try {
                cita.setEstadoCita(Cita.EstadoCita.valueOf(citaDTO.getEstadoCita()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Estado de cita inválido: " + citaDTO.getEstadoCita());
            }
        }

        if (citaDTO.getNotas() != null) {
            cita.setNotas(citaDTO.getNotas());
        }

        Cita citaActualizada = citaRepository.save(cita);

        CitaEventoDTO evento = crearEvento(citaActualizada, "ACTUALIZADA");
        eventoPublisher.publicarEvento(evento);

        return convertirADTO(citaActualizada);
    }

    /**
     * Cancelar cita y publicar evento
     */
    @Transactional
    public void cancelar(Integer id) {
        log.info("Cancelando cita ID: {}", id);

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        cita.setEstadoCita(Cita.EstadoCita.Cancelada);
        citaRepository.save(cita);

        CitaEventoDTO evento = crearEvento(cita, "CANCELADA");
        eventoPublisher.publicarEvento(evento);
    }

    private CitaDTO convertirADTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setNumeroCita(cita.getNumeroCita());
        dto.setIdUsuario(cita.getIdUsuario());
        dto.setIdSucursal(cita.getSucursal().getIdSucursal());
        dto.setNombreSucursal(cita.getSucursal().getNombreSucursal());
        dto.setDireccionSucursal(cita.getSucursal().getDireccion());
        dto.setIdTipoServicio(cita.getTipoServicio().getIdTipoServicio());
        dto.setNombreServicio(cita.getTipoServicio().getNombreServicio());
        dto.setDuracionMinutos(cita.getTipoServicio().getDuracionMinutos());
        dto.setFechaCita(cita.getFechaCita());
        dto.setHoraCita(cita.getHoraCita());
        dto.setEstadoCita(cita.getEstadoCita().name());
        dto.setNotas(cita.getNotas());
        dto.setFechaCreacion(cita.getFechaCreacion());
        dto.setFechaActualizacion(cita.getFechaActualizacion());
        return dto;
    }

    private CitaEventoDTO crearEvento(Cita cita, String tipoEvento) {
        CitaEventoDTO evento = new CitaEventoDTO();
        evento.setIdCita(cita.getIdCita());
        evento.setNumeroCita(cita.getNumeroCita());
        evento.setIdUsuario(cita.getIdUsuario());
        evento.setFechaCita(cita.getFechaCita());
        evento.setHoraCita(cita.getHoraCita());
        evento.setNombreServicio(cita.getTipoServicio().getNombreServicio());
        evento.setDuracionMinutos(cita.getTipoServicio().getDuracionMinutos());
        evento.setNombreSucursal(cita.getSucursal().getNombreSucursal());
        evento.setDireccionSucursal(cita.getSucursal().getDireccion());
        evento.setTelefonoSucursal(cita.getSucursal().getTelefono());
        evento.setEmailSucursal(cita.getSucursal().getEmail());
        evento.setEstadoCita(cita.getEstadoCita().name());
        evento.setTipoEvento(tipoEvento);
        evento.setFechaEvento(LocalDateTime.now());
        evento.setNotas(cita.getNotas());
        return evento;
    }
}