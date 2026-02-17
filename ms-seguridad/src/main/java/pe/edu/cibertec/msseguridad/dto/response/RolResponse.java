package pe.edu.cibertec.msseguridad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolResponse {

    private Long idRol;
    private String nombreRol;
    private String descripcion;
    private Boolean estado;
    private LocalDateTime fechaCreacion;
}
