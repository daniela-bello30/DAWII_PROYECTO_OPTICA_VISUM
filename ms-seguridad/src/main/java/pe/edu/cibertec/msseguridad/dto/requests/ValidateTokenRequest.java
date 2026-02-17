package pe.edu.cibertec.msseguridad.dto.requests;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateTokenRequest {

    @NotBlank(message = "El token es obligatorio")
    private String token;
}
