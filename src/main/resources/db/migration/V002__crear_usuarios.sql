CREATE TABLE usuarios(
    id BIGSERIAL PRIMARY KEY,
    nombre varchar(20) NOT NULL,
    password varchar(20) NOT NULL
);

INSERT INTO usuarios (nombre, password) VALUES ('admin', 'admin123');