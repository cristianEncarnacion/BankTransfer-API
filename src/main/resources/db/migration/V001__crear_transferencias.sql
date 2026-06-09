CREATE TABLE transferencias(
    id BIGSERIAL PRIMARY KEY,
    cuenta_origen varchar(20) NOT NULL,
    cuenta_destino varchar(20) NOT NULL,
    monto DECIMAL(15,2) NOT NULL,
    estado varchar(20) DEFAULT 'PENDIENTE',
    fecha TIMESTAMP DEFAULT NOW()
);