package pe.edu.cibertec.msventas.repository;

import pe.edu.cibertec.msventas.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByIdUsuario(Integer idUsuario);
    List<Pedido> findByEstadoPedido(String estadoPedido);
}