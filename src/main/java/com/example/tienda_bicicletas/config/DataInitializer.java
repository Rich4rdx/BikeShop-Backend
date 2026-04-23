package com.example.tienda_bicicletas.config;

import com.example.tienda_bicicletas.entity.Usuario;
import com.example.tienda_bicicletas.enums.Rol;
import com.example.tienda_bicicletas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == null)
                .forEach(u -> {
                    u.setRol(Rol.ADMIN);
                    usuarioRepository.save(u);
                });

        // Crear o actualizar usuario admin
        Optional<Usuario> adminExistente = usuarioRepository.findByUsername("admin");
        if (adminExistente.isEmpty()) {
            usuarioRepository.save(Usuario.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .nombre("Administrador")
                    .rol(Rol.ADMIN)
                    .build());
        } else {
            Usuario admin = adminExistente.get();
            admin.setRol(Rol.ADMIN);
            admin.setPassword(passwordEncoder.encode("admin123"));
            usuarioRepository.save(admin);
        }


        if (!usuarioRepository.existsByUsername("trabajador")) {
            usuarioRepository.save(Usuario.builder()
                    .username("trabajador")
                    .password(passwordEncoder.encode("trab123"))
                    .nombre("Empleado")
                    .rol(Rol.TRABAJADOR)
                    .build());
        }
    }
}
