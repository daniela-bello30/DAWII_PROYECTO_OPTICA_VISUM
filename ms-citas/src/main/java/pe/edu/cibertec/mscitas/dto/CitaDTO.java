package pe.edu.cibertec.mscitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {

    private Integer idCita;

    private String numeroCita;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El ID de sucursal es obligatorio")
    private Integer idSucursal;

    private String nombreSucursal;
    private String direccionSucursal;

    @NotNull(message = "El ID del tipo de servicio es obligatorio")
    private Integer idTipoServicio;

    private String nombreServicio;
    private Integer duracionMinutos;

    @NotNull(message = "La fecha de la cita es obligatoria")
    private LocalDate fechaCita;

    @NotNull(message = "La hora de la cita es obligatoria")
    private LocalTime horaCita;

    private String estadoCita;

    private String notas;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}