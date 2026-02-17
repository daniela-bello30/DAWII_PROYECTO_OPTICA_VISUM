package pe.edu.cibertec.msseguridad.service.interfaces;

import pe.edu.cibertec.msseguridad.dto.requests.CrearRolRequest;
import pe.edu.cibertec.msseguridad.dto.response.RolResponse;

import java.util.List;

public interface RolService {

    RolResponse crearRol(CrearRolRequest request);

    List<RolResponse> listarRoles();

    List<RolResponse> listarRolesActivos();

    RolResponse obtenerRolPorId(Long idRol);

    RolResponse actualizarRol(Long idRol, CrearRolRequest request);

    void desactivarRol(Long idRol);
}
