package pe.edu.cibertec.msseguridad.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateTokenResponse {

    private Boolean valid;
    private String email;
    private Long userId;
    private String rol;
    private String message;
}
