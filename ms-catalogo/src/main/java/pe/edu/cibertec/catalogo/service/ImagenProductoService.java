package pe.edu.cibertec.catalogo.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.catalogo.dto.ImagenProductoDTO;
import pe.edu.cibertec.catalogo.model.ImagenProducto;
import pe.edu.cibertec.catalogo.model.Producto;
import pe.edu.cibertec.catalogo.repository.ImagenProductoRepository;
import pe.edu.cibertec.catalogo.repository.ProductoRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ImagenProductoService {

    private final ImagenProductoRepository imagenProductoRepository;
    private final ProductoRepository productoRepository;
    private final ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<ImagenProductoDTO> obtenerImagenesDeProducto(Integer idProducto) {
        return imagenProductoRepository.findByProducto_IdProductoOrderByOrdenAsc(idProducto).stream()
                .map(imagen -> modelMapper.map(imagen, ImagenProductoDTO.class))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ImagenProductoDTO obtenerImagenPrincipal(Integer idProducto) {
        ImagenProducto imagen = imagenProductoRepository
                .findByProducto_IdProductoAndEsPrincipalTrue(idProducto)
                .orElse(null);

        // Si no hay imagen principal, retornar la primera
        if (imagen == null) {
            imagen = imagenProductoRepository
                    .findFirstByProducto_IdProductoOrderByOrdenAsc(idProducto)
                    .orElse(null);
        }

        return imagen != null ? modelMapper.map(imagen, ImagenProductoDTO.class) : null;
    }

    public ImagenProductoDTO agregarImagen(ImagenProductoDTO imagenDTO) {
        Producto producto = productoRepository.findById(imagenDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ImagenProducto imagen = new ImagenProducto();
        imagen.setProducto(producto);
        imagen.setUrlImagen(imagenDTO.getUrlImagen());
        imagen.setEsPrincipal(imagenDTO.getEsPrincipal() != null ? imagenDTO.getEsPrincipal() : false);

        // Calcular orden si no se especifica
        if (imagenDTO.getOrden() == null) {
            long cantidadImagenes = imagenProductoRepository.countByProducto_IdProducto(imagenDTO.getIdProducto());
            imagen.setOrden((int) cantidadImagenes);
        } else {
            imagen.setOrden(imagenDTO.getOrden());
        }

        // Si es principal, desmarcar las demás
        if (imagen.getEsPrincipal()) {
            imagenProductoRepository.desmarcarImagenesPrincipales(imagenDTO.getIdProducto());
        }

        ImagenProducto imagenGuardada = imagenProductoRepository.save(imagen);
        return modelMapper.map(imagenGuardada, ImagenProductoDTO.class);
    }


    public ImagenProductoDTO establecerComoPrincipal(Integer idImagen) {
        ImagenProducto imagen = imagenProductoRepository.findById(idImagen)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));


        imagenProductoRepository.desmarcarImagenesPrincipales(imagen.getProducto().getIdProducto());


        imagen.marcarComoPrincipal();
        ImagenProducto imagenActualizada = imagenProductoRepository.save(imagen);

        return modelMapper.map(imagenActualizada, ImagenProductoDTO.class);
    }


    public ImagenProductoDTO actualizarOrden(Integer idImagen, Integer nuevoOrden) {
        ImagenProducto imagen = imagenProductoRepository.findById(idImagen)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        imagen.setOrden(nuevoOrden);
        ImagenProducto imagenActualizada = imagenProductoRepository.save(imagen);

        return modelMapper.map(imagenActualizada, ImagenProductoDTO.class);
    }


    public void eliminarImagen(Integer idImagen) {
        if (!imagenProductoRepository.existsById(idImagen)) {
            throw new RuntimeException("Imagen no encontrada");
        }
        imagenProductoRepository.deleteById(idImagen);
    }


    public void eliminarTodasLasImagenesDeProducto(Integer idProducto) {
        imagenProductoRepository.deleteByProducto_IdProducto(idProducto);
    }
}