package pe.edu.cibertec.catalogo.exception;


public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}