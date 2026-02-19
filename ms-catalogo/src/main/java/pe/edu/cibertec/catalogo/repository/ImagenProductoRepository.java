package pe.edu.cibertec.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.catalogo.model.ImagenProducto;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad ImagenProducto
 *
 * Gestiona las imágenes asociadas a los productos,
 * incluyendo la lógica de imagen principal y ordenamiento.
 */
@Repository
public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, Integer> {


    List<ImagenProducto> findByProducto_IdProducto(Integer idProducto);


    List<ImagenProducto> findByProducto_IdProductoOrderByOrdenAsc(Integer idProducto);


    Optional<ImagenProducto> findByProducto_IdProductoAndEsPrincipalTrue(Integer idProducto);


    boolean existsByProducto_IdProductoAndEsPrincipalTrue(Integer idProducto);

    long countByProducto_IdProducto(Integer idProducto);


    void deleteByProducto_IdProducto(Integer idProducto);


    @Modifying
    @Query("UPDATE ImagenProducto i SET i.esPrincipal = false WHERE i.producto.idProducto = :idProducto")
    void desmarcarImagenesPrincipales(@Param("idProducto") Integer idProducto);


    Optional<ImagenProducto> findFirstByProducto_IdProductoOrderByOrdenAsc(Integer idProducto);
}