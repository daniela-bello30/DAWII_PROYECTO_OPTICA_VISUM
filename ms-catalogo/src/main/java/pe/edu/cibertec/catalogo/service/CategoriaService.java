package pe.edu.cibertec.catalogo.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.catalogo.dto.CategoriaDTO;
import pe.edu.cibertec.catalogo.model.Categoria;
import pe.edu.cibertec.catalogo.repository.CategoriaRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerTodas() {
        return categoriaRepository.findAll().stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerActivas() {
        return categoriaRepository.findByActivo(true).stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaDTO.class))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public CategoriaDTO obtenerPorId(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        return modelMapper.map(categoria, CategoriaDTO.class);
    }


    @Transactional(readOnly = true)
    public List<CategoriaDTO> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaDTO.class))
                .collect(Collectors.toList());
    }

    public CategoriaDTO crear(CategoriaDTO categoriaDTO) {
        // Validar que no exista una categoría con el mismo nombre
        if (categoriaRepository.existsByNombre(categoriaDTO.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
        }

        Categoria categoria = modelMapper.map(categoriaDTO, Categoria.class);

        if (categoria.getActivo() == null) {
            categoria.setActivo(true);
        }

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return modelMapper.map(categoriaGuardada, CategoriaDTO.class);
    }


    public CategoriaDTO actualizar(Integer id, CategoriaDTO categoriaDTO) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        // Verificar si el nuevo nombre ya existe en otra categoría
        if (!categoriaExistente.getNombre().equals(categoriaDTO.getNombre()) &&
                categoriaRepository.existsByNombre(categoriaDTO.getNombre())) {
            throw new RuntimeException("Ya existe otra categoría con el nombre: " + categoriaDTO.getNombre());
        }

        categoriaExistente.setNombre(categoriaDTO.getNombre());
        categoriaExistente.setDescripcion(categoriaDTO.getDescripcion());

        if (categoriaDTO.getActivo() != null) {
            categoriaExistente.setActivo(categoriaDTO.getActivo());
        }

        Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);
        return modelMapper.map(categoriaActualizada, CategoriaDTO.class);
    }


    public void eliminar(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }


    public void eliminarPermanente(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }


    public CategoriaDTO reactivar(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        categoria.setActivo(true);
        Categoria categoriaReactivada = categoriaRepository.save(categoria);
        return modelMapper.map(categoriaReactivada, CategoriaDTO.class);
    }
}