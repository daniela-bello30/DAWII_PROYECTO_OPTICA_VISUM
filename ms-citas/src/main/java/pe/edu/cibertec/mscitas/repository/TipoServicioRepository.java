package pe.edu.cibertec.mscitas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.mscitas.model.TipoServicio;

import java.util.List;

@Repository
public interface TipoServicioRepository extends JpaRepository<TipoServicio, Integer> {

    List<TipoServicio> findByEstadoTrue();
}