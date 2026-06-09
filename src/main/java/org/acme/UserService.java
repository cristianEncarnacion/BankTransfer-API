package org.acme;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped

public class UserService {

    public User findByNombre(String username) {
        return User.findByNombre(username);
    }

    public boolean verificarCredenciales(String username, String password) {
        return User.verificarCredenciales(username, password);
    }

    public boolean existeUsuario(String username) {
        return User.existeUsuario(username);
    }

    public void crearUsuario(String username, String password) {
        User.crearUsuario(username, password);
    }

}
