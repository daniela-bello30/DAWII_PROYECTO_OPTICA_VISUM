package pe.edu.cibertec.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.catalogo.model.Marca;

import java.util.List;
import java.util.Optional;


@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {

    Optional<Marca> findByNombre(String nombre);


    List<Marca> findByNombreContainingIgnoreCase(String nombre);


    List<Marca> findByActivo(Boolean activo);


    List<Marca> findByPaisOrigen(String paisOrigen);


    boolean existsByNombre(String nombre);
}