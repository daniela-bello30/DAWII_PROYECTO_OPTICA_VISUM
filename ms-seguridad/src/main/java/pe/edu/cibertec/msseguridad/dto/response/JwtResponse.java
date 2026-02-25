package pe.edu.cibertec.msseguridad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    private String refreshToken;
    @Builder.Default
    private String type = "Bearer";
    private Long idUsuario;
    private String email;
    private String nombres;
    private String apellidos;
    private String rol;
    private Long expiresIn; // En milisegundos
}
