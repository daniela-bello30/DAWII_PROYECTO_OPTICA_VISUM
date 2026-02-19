package pe.edu.cibertec.catalogo.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.catalogo.dto.MarcaDTO;
import pe.edu.cibertec.catalogo.model.Marca;
import pe.edu.cibertec.catalogo.repository.MarcaRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<MarcaDTO> obtenerTodas() {
        return marcaRepository.findAll().stream()
                .map(marca -> modelMapper.map(marca, MarcaDTO.class))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<MarcaDTO> obtenerActivas() {
        return marcaRepository.findByActivo(true).stream()
                .map(marca -> modelMapper.map(marca, MarcaDTO.class))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public MarcaDTO obtenerPorId(Integer id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + id));
        return modelMapper.map(marca, MarcaDTO.class);
    }


    @Transactional(readOnly = true)
    public List<MarcaDTO> buscarPorNombre(String nombre) {
        return marcaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(marca -> modelMapper.map(marca, MarcaDTO.class))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<MarcaDTO> buscarPorPais(String pais) {
        return marcaRepository.findByPaisOrigen(pais).stream()
                .map(marca -> modelMapper.map(marca, MarcaDTO.class))
                .collect(Collectors.toList());
    }


    public MarcaDTO crear(MarcaDTO marcaDTO) {
        // Validar que no exista una marca con el mismo nombre
        if (marcaRepository.existsByNombre(marcaDTO.getNombre())) {
            throw new RuntimeException("Ya existe una marca con el nombre: " + marcaDTO.getNombre());
        }

        Marca marca = modelMapper.map(marcaDTO, Marca.class);

        // Establecer valores por defecto
        if (marca.getActivo() == null) {
            marca.setActivo(true);
        }

        Marca marcaGuardada = marcaRepository.save(marca);
        return modelMapper.map(marcaGuardada, MarcaDTO.class);
    }


    public MarcaDTO actualizar(Integer id, MarcaDTO marcaDTO) {
        Marca marcaExistente = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + id));

        // Verificar si el nuevo nombre ya existe en otra marca
        if (!marcaExistente.getNombre().equals(marcaDTO.getNombre()) &&
                marcaRepository.existsByNombre(marcaDTO.getNombre())) {
            throw new RuntimeException("Ya existe otra marca con el nombre: " + marcaDTO.getNombre());
        }

        // Actualizar campos
        marcaExistente.setNombre(marcaDTO.getNombre());
        marcaExistente.setPaisOrigen(marcaDTO.getPaisOrigen());
        marcaExistente.setDescripcion(marcaDTO.getDescripcion());

        if (marcaDTO.getActivo() != null) {
            marcaExistente.setActivo(marcaDTO.getActivo());
        }

        Marca marcaActualizada = marcaRepository.save(marcaExistente);
        return modelMapper.map(marcaActualizada, MarcaDTO.class);
    }


    public void eliminar(Integer id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + id));

        // Soft delete: solo desactivamos
        marca.setActivo(false);
        marcaRepository.save(marca);
    }


    public void eliminarPermanente(Integer id) {
        if (!marcaRepository.existsById(id)) {
            throw new RuntimeException("Marca no encontrada con ID: " + id);
        }
        marcaRepository.deleteById(id);
    }


    public MarcaDTO reactivar(Integer id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + id));

        marca.setActivo(true);
        Marca marcaReactivada = marcaRepository.save(marca);
        return modelMapper.map(marcaReactivada, MarcaDTO.class);
    }
}