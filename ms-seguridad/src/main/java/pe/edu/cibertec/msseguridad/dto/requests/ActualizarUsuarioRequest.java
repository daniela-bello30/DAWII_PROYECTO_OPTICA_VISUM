package pe.edu.cibertec.msseguridad.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.cibertec.msseguridad.model.enums.TipoDocumento;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarUsuarioRequest {

    @Size(min = 2, max = 100)
    private String nombres;

    @Size(min = 2, max = 100)
    private String apellidos;

    @Email(message = "Debe ser un email válido")
    private String email;

    @Pattern(regexp = "^[0-9]{7,20}$", message = "El teléfono debe contener solo números")
    private String telefono;

    private String documentoIdentidad;

    private TipoDocumento tipoDocumento;

    private LocalDate fechaNacimiento;

    private Long idRol;
}
