package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
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
        return "User [id=" + id + ", nombre=" + nombre + ", password=" + password + "]";
    }

    public static User findByNombre(String username) {
        return find("nombre", username).firstResult();
    }

    public static boolean verificarCredenciales(String username, String password) {
        User user = findByNombre(username);
        return user != null && user.password.equals(password);
    }

    public static boolean existeUsuario(String username) {
        User user = findByNombre(username);
        return user != null;
    }

    public static void crearUsuario(String username, String password) {
        User user = new User(username, password);
        user.persist();
    }
}
