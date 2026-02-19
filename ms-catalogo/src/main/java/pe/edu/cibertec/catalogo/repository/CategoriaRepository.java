package pe.edu.cibertec.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.catalogo.model.Categoria;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {


    Optional<Categoria> findByNombre(String nombre);


    List<Categoria> findByNombreContainingIgnoreCase(String nombre);


    List<Categoria> findByActivo(Boolean activo);

    boolean existsByNombre(String nombre);
}