package com.example.tienda_bicicletas.config;

import com.example.tienda_bicicletas.entity.Usuario;
import com.example.tienda_bicicletas.enums.Rol;
import com.example.tienda_bicicletas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Value("${app.trabajador.password:}")
    private String trabajadorPassword;

    @Override
    public void run(String... args) {

        usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == null)
                .forEach(u -> {
                    u.setRol(Rol.ADMIN);
                    usuarioRepository.save(u);
                });

        crearOActualizar("admin", "Administrador", Rol.ADMIN, adminPassword);
        crearOActualizar("trabajador", "Empleado", Rol.TRABAJADOR, trabajadorPassword);
    }

    /**
     * Crea el usuario si no existe. Si la contraseña viene de una variable de entorno,
     * la aplica; si no, conserva la que ya tiene o genera una aleatoria para usuarios nuevos.
     */
    private void crearOActualizar(String username, String nombre, Rol rol, String passwordConfigurada) {
        boolean tienePassword = passwordConfigurada != null && !passwordConfigurada.isBlank();
        Optional<Usuario> existente = usuarioRepository.findByUsername(username);

        if (existente.isPresent()) {
            Usuario usuario = existente.get();
            usuario.setRol(rol);
            if (tienePassword) {
                usuario.setPassword(passwordEncoder.encode(passwordConfigurada));
            }
            usuarioRepository.save(usuario);
            return;
        }

        String password = tienePassword ? passwordConfigurada : UUID.randomUUID().toString();
        usuarioRepository.save(Usuario.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nombre(nombre)
                .rol(rol)
                .build());
        if (!tienePassword) {
            log.warn("Usuario '{}' creado con contraseña aleatoria: {}. Configura la variable de entorno para fijarla.",
                    username, password);
        }
    }
}
