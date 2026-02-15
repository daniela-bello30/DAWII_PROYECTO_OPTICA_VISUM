package pe.edu.cibertec.mscitas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.mscitas.model.Sucursal;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    List<Sucursal> findByEstadoTrue();

    List<Sucursal> findByDepartamento(String departamento);

    List<Sucursal> findByDistrito(String distrito);
}