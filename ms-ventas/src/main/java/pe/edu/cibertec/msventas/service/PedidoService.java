package pe.edu.cibertec.msventas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.msventas.dto.ProductoDTO;
import pe.edu.cibertec.msventas.feign.CatalogoFeignClient;
import pe.edu.cibertec.msventas.messaging.EventPublisher;
import pe.edu.cibertec.msventas.model.DetallePedido;
import pe.edu.cibertec.msventas.model.Pedido;
import pe.edu.cibertec.msventas.repository.PedidoRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CatalogoFeignClient catalogoClient;
    private final EventPublisher eventPublisher;

    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        log.info("🛒 Creando pedido para usuario: {}", pedido.getIdUsuario());

        // 1. Validar productos y stock via Feign
        for (DetallePedido detalle : pedido.getDetalles()) {
            log.info("📦 Verificando producto ID: {}", detalle.getIdProducto());

            ProductoDTO producto = catalogoClient.obtenerProducto(detalle.getIdProducto());

            if (!producto.getEstado()) {
                throw new RuntimeException("Producto inactivo: " + producto.getNombreProducto());
            }

            Boolean hayStock = catalogoClient.verificarStock(
                    detalle.getIdProducto(),
                    detalle.getCantidad()
            );

            if (!hayStock) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombreProducto());
            }

            detalle.setPrecioUnitario(producto.getPrecioUnitario());
            detalle.calcularSubtotal();
        }

        // 2. Configurar pedido
        pedido.setEstadoPedido("Pendiente");
        pedido.setEstadoPago("Pendiente");
        pedido.calcularTotal();

        // 3. Guardar
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 4. Publicar evento
        eventPublisher.publicarPedidoCreado(pedidoGuardado);

        log.info("✅ Pedido creado: {}", pedidoGuardado.getNumeroPedido());
        return pedidoGuardado;
    }

    @Transactional
    public Pedido confirmarPedido(Integer idPedido) {
        log.info("✅ Confirmando pedido ID: {}", idPedido);

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Reducir stock en ms-catalogo
        for (DetallePedido detalle : pedido.getDetalles()) {
            log.info("📉 Reduciendo stock - Producto: {}, Cantidad: {}",
                    detalle.getIdProducto(), detalle.getCantidad());

            catalogoClient.reducirStock(detalle.getIdProducto(), detalle.getCantidad());
        }

        pedido.confirmar();
        Pedido confirmado = pedidoRepository.save(pedido);

        eventPublisher.publicarPedidoConfirmado(confirmado);

        log.info("✅ Pedido confirmado: {}", pedido.getNumeroPedido());
        return confirmado;
    }

    @Transactional
    public void cancelarPedido(Integer idPedido) {
        log.info("❌ Cancelando pedido ID: {}", idPedido);

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        boolean debeRestaurarStock = "Confirmado".equals(pedido.getEstadoPedido());

        if (debeRestaurarStock) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                log.info("📈 Restaurando stock - Producto: {}, Cantidad: {}",
                        detalle.getIdProducto(), detalle.getCantidad());

                catalogoClient.aumentarStock(detalle.getIdProducto(), detalle.getCantidad());
            }
        }

        pedido.cancelar();
        pedidoRepository.save(pedido);

        eventPublisher.publicarPedidoCancelado(pedido);

        log.info("✅ Pedido cancelado: {}", pedido.getNumeroPedido());
    }

    public Pedido obtenerPedido(Integer idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<Pedido> obtenerPedidosPorUsuario(Integer idUsuario) {
        return pedidoRepository.findByIdUsuario(idUsuario);
    }

    public List<Pedido> obtenerTodosPedidos() {
        return pedidoRepository.findAll();
    }
}