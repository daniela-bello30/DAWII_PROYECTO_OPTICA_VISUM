package pe.edu.cibertec.msseguridad.service.interfaces;

import pe.edu.cibertec.msseguridad.dto.requests.ActualizarUsuarioRequest;
import pe.edu.cibertec.msseguridad.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse obtenerUsuarioPorId(Long idUsuario);

    List<UsuarioResponse> listarUsuarios();

    List<UsuarioResponse> listarUsuariosActivos();

    UsuarioResponse actualizarUsuario(Long idUsuario, ActualizarUsuarioRequest request);

    void desactivarUsuario(Long idUsuario);
}
