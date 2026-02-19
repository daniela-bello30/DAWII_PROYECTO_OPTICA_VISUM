package pe.edu.cibertec.catalogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.catalogo.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {


    Optional<Producto> findBySku(String sku);


    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByActivo(Boolean activo);

    boolean existsBySku(String sku);


    List<Producto> findByMarcaIdMarca(Integer idMarca);


    List<Producto> findByCategoria_IdCategoria(Integer idCategoria);


    List<Producto> findByMarca_IdMarcaAndCategoria_IdCategoria(Integer idMarca, Integer idCategoria);


    List<Producto> findByPrecioLessThanEqual(BigDecimal precio);


    List<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);

    List<Producto> findByStockGreaterThan(Integer cantidad);


    List<Producto> findByStock(Integer stock);



    Page<Producto> findByActivo(Boolean activo, Pageable pageable);


    Page<Producto> findByCategoria_IdCategoria(Integer idCategoria, Pageable pageable);


    Page<Producto> findByMarca_IdMarca(Integer idMarca, Pageable pageable);


    @Query("SELECT p FROM Producto p WHERE " +
            "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.marca.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.categoria.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Producto> busquedaAvanzada(@Param("searchTerm") String searchTerm, Pageable pageable);


    @Query("SELECT p FROM Producto p WHERE p.stock > 0 AND p.activo = true")
    List<Producto> findProductosConStock();


    @Query("SELECT p FROM Producto p WHERE p.activo = true ORDER BY p.stock ASC")
    Page<Producto> findProductosPopulares(Pageable pageable);


    @Query("SELECT p FROM Producto p WHERE " +
            "(:idMarca IS NULL OR p.marca.idMarca = :idMarca) AND " +
            "(:idCategoria IS NULL OR p.categoria.idCategoria = :idCategoria) AND " +
            "(:precioMin IS NULL OR p.precio >= :precioMin) AND " +
            "(:precioMax IS NULL OR p.precio <= :precioMax) AND " +
            "p.activo = true")
    Page<Producto> filtrarProductos(
            @Param("idMarca") Integer idMarca,
            @Param("idCategoria") Integer idCategoria,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            Pageable pageable
    );
}