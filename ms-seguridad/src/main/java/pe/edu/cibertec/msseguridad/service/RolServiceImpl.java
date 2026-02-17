package pe.edu.cibertec.msseguridad.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.msseguridad.dto.requests.CrearRolRequest;
import pe.edu.cibertec.msseguridad.dto.response.RolResponse;
import pe.edu.cibertec.msseguridad.exception.BusinessException;
import pe.edu.cibertec.msseguridad.exception.ResourceNotFoundException;
import pe.edu.cibertec.msseguridad.model.Rol;
import pe.edu.cibertec.msseguridad.repository.RolRepository;
import pe.edu.cibertec.msseguridad.service.interfaces.RolService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RolResponse crearRol(CrearRolRequest request) {
        log.info("Creando nuevo rol: {}", request.getNombreRol());

        if (rolRepository.existsByNombreRol(request.getNombreRol())) {
            throw new BusinessException("Ya existe un rol con ese nombre");
        }

        Rol rol = Rol.builder()
                .nombreRol(request.getNombreRol())
                .descripcion(request.getDescripcion())
                .estado(true)
                .build();

        Rol rolGuardado = rolRepository.save(rol);
        log.info("Rol creado exitosamente con ID: {}", rolGuardado.getIdRol());

        return modelMapper.map(rolGuardado, RolResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolResponse> listarRoles() {
        log.info("Listando todos los roles");
        return rolRepository.findAll().stream()
                .map(rol -> modelMapper.map(rol, RolResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolResponse> listarRolesActivos() {
        log.info("Listando roles activos");
        return rolRepository.findByEstado(true).stream()
                .map(rol -> modelMapper.map(rol, RolResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RolResponse obtenerRolPorId(Long idRol) {
        log.info("Obteniendo rol con ID: {}", idRol);
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", idRol));
        return modelMapper.map(rol, RolResponse.class);
    }

    @Override
    @Transactional
    public RolResponse actualizarRol(Long idRol, CrearRolRequest request) {
        log.info("Actualizando rol con ID: {}", idRol);

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", idRol));

        if (!rol.getNombreRol().equals(request.getNombreRol()) &&
                rolRepository.existsByNombreRol(request.getNombreRol())) {
            throw new BusinessException("Ya existe un rol con ese nombre");
        }

        rol.setNombreRol(request.getNombreRol());
        rol.setDescripcion(request.getDescripcion());

        Rol rolActualizado = rolRepository.save(rol);
        log.info("Rol actualizado exitosamente");

        return modelMapper.map(rolActualizado, RolResponse.class);
    }

    @Override
    @Transactional
    public void desactivarRol(Long idRol) {
        log.info("Desactivando rol con ID: {}", idRol);

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", idRol));

        rol.setEstado(false);
        rolRepository.save(rol);

        log.info("Rol desactivado exitosamente");
    }
}
