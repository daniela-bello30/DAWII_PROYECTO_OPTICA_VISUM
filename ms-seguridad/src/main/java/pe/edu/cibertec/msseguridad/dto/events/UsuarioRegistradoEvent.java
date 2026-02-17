package pe.edu.cibertec.msseguridad.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRegistradoEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String nombreRol;
    private LocalDateTime fechaRegistro;
}
