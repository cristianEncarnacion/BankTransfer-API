package org.acme;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "transferencias")
public class Transfer extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "cuenta_origen")
    public String cuentaOrigen;
    @Column(name = "cuenta_destino")
    public String cuentaDestino;
    public BigDecimal monto;
    public String estado = "PENDIENTE";
    public LocalDateTime fecha = LocalDateTime.now();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cuentaOrigen == null) ? 0 : cuentaOrigen.hashCode());
        result = prime * result + ((cuentaDestino == null) ? 0 : cuentaDestino.hashCode());
        result = prime * result + ((monto == null) ? 0 : monto.hashCode());
        result = prime * result + ((estado == null) ? 0 : estado.hashCode());
        result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
        return result;
    }

    public Transfer() {
    }

    public Transfer(String cuentaOrigen, String cuentaDestino, BigDecimal monto, String estado, LocalDateTime fecha) {
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.estado = estado;
        this.fecha = fecha;
    }

    @Override
    public void delete() {
        // TODO Auto-generated method stub
        super.delete();
    }

    @Override
    public boolean isPersistent() {
        // TODO Auto-generated method stub
        return super.isPersistent();
    }

    @Override
    public void persist() {
        // TODO Auto-generated method stub
        super.persist();
    }

    @Override
    public void persistAndFlush() {
        // TODO Auto-generated method stub
        super.persistAndFlush();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
    }

    @Override
    public String toString() {
        return "Transfer [cuentaOrigen=" + cuentaOrigen + ", cuentaDestino=" + cuentaDestino + ", monto=" + monto
                + ", estado=" + estado + ", fecha=" + fecha + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transfer other = (Transfer) obj;
        if (cuentaOrigen == null) {
            if (other.cuentaOrigen != null)
                return false;
        } else if (!cuentaOrigen.equals(other.cuentaOrigen))
            return false;
        if (cuentaDestino == null) {
            if (other.cuentaDestino != null)
                return false;
        } else if (!cuentaDestino.equals(other.cuentaDestino))
            return false;
        if (monto == null) {
            if (other.monto != null)
                return false;
        } else if (!monto.equals(other.monto))
            return false;
        if (estado == null) {
            if (other.estado != null)
                return false;
        } else if (!estado.equals(other.estado))
            return false;
        if (fecha == null) {
            if (other.fecha != null)
                return false;
        } else if (!fecha.equals(other.fecha))
            return false;
        return true;
    }

    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setCuentaOrigen(String cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public void setCuentaDestino(String cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

}
