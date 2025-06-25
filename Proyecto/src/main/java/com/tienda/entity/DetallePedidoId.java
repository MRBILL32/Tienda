package com.tienda.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class DetallePedidoId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "idPedido")
    private Integer idPedido;

    @Column(name = "idProd")
    private Integer idProd;

    // Constructor vacío obligatorio
    public DetallePedidoId() {}

    public DetallePedidoId(Integer idPedido, Integer idProd) {
        this.idPedido = idPedido;
        this.idProd = idProd;
    }

    // equals() y hashCode() obligatorios para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetallePedidoId)) return false;
        DetallePedidoId that = (DetallePedidoId) o;
        return Objects.equals(idPedido, that.idPedido) &&
               Objects.equals(idProd, that.idProd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedido, idProd);
    }
}
