package pe.edu.cibertec.mscitas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.mscitas.model.Cita;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByIdUsuario(Integer idUsuario);

    List<Cita> findByEstadoCita(Cita.EstadoCita estadoCita);

    List<Cita> findByFechaCitaBetween(LocalDate inicio, LocalDate fin);

    List<Cita> findBySucursal_IdSucursal(Integer idSucursal);

    List<Cita> findByTipoServicio_IdTipoServicio(Integer idTipoServicio);
}