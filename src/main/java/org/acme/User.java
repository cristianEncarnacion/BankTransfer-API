package org.acme;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class User extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String nombre;
    /** Hash BCrypt de la contraseña. Nunca se guarda en texto plano. */
    public String password;

    public User() {
    }

    public User(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", nombre=" + nombre + "]";
    }

    /** Crea un usuario (sin persistir) con la contraseña hasheada con BCrypt. */
    public static User nuevoUsuario(String username, String passwordPlano) {
        return new User(username, BcryptUtil.bcryptHash(passwordPlano));
    }

    public static User findByNombre(String username) {
        return find("nombre", username).firstResult();
    }

    public static boolean verificarCredenciales(String username, String passwordPlano) {
        User user = findByNombre(username);
        return user != null && BcryptUtil.matches(passwordPlano, user.password);
    }

    public static boolean existeUsuario(String username) {
        return findByNombre(username) != null;
    }

    public static void crearUsuario(String username, String passwordPlano) {
        nuevoUsuario(username, passwordPlano).persist();
    }
}
