package pe.edu.cibertec.msseguridad.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.msseguridad.model.Rol;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombreRol(String nombreRol);

    List<Rol> findByEstado(Boolean estado);

    boolean existsByNombreRol(String nombreRol);
}
