package pe.edu.cibertec.mscitas.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String email;
}