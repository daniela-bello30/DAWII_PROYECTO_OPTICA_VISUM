package pe.edu.cibertec.mscitas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO para enviar eventos de citas a través de RabbitMQ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaEventoDTO {

    private Integer idCita;
    private String numeroCita;
    private Integer idUsuario;
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private String nombreServicio;
    private Integer duracionMinutos;
    private String nombreSucursal;
    private String direccionSucursal;
    private String telefonoSucursal;
    private String emailSucursal;
    private String estadoCita;
    private String tipoEvento; // CREADA, ACTUALIZADA, CANCELADA
    private LocalDateTime fechaEvento;
    private String notas;
}