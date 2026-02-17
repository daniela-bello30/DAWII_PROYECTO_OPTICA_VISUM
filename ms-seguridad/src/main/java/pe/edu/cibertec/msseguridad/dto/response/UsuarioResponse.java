package pe.edu.cibertec.msseguridad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.cibertec.msseguridad.model.enums.TipoDocumento;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String documentoIdentidad;
    private TipoDocumento tipoDocumento;
    private LocalDate fechaNacimiento;
    private String nombreRol;
    private Long idRol;
    private Boolean estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;
}
