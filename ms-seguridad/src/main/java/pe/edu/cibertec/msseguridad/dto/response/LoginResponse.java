package pe.edu.cibertec.msseguridad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String email;
    private String nombreRol;
    private String mensaje;
}
