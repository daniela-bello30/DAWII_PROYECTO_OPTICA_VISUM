package pe.edu.cibertec.msventas.repository;

import pe.edu.cibertec.msventas.model.DireccionEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Integer> {
    List<DireccionEnvio> findByIdUsuario(Integer idUsuario);
}