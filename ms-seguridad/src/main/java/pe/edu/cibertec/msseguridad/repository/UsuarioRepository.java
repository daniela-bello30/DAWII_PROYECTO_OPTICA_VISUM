package pe.edu.cibertec.msseguridad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.msseguridad.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByDocumentoIdentidad(String documentoIdentidad);

    List<Usuario> findByEstado(Boolean estado);

    boolean existsByEmail(String email);

    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    List<Usuario> findByRol_IdRol(Long idRol);
}
