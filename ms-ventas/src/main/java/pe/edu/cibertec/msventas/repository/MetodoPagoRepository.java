package pe.edu.cibertec.msventas.repository;

import pe.edu.cibertec.msventas.model.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {
    List<MetodoPago> findByEstado(Boolean estado);
}