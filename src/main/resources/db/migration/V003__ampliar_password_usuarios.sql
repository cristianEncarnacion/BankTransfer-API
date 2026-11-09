-- Las contraseñas ahora se guardan como hash BCrypt (60 caracteres)
ALTER TABLE usuarios ALTER COLUMN password TYPE VARCHAR(100);
