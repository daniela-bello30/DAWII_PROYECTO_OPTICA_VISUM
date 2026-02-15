package pe.edu.cibertec.mscitas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.mscitas.model.Sucursal;
import pe.edu.cibertec.mscitas.repository.SucursalRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SucursalService {

    private final SucursalRepository sucursalRepository;

    /**
     * Obtener todas las sucursales
     */
    public List<Sucursal> obtenerTodas() {
        return sucursalRepository.findAll();
    }

    /**
     * Obtener sucursales activas (estado = true)
     */
    public List<Sucursal> obtenerActivas() {
        return sucursalRepository.findByEstadoTrue();
    }

    /**
     * Obtener sucursal por ID
     */
    public Sucursal obtenerPorId(Integer id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));
    }
}