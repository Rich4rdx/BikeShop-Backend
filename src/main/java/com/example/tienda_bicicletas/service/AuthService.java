package com.example.tienda_bicicletas.service;

import com.example.tienda_bicicletas.dto.*;
import com.example.tienda_bicicletas.entity.Usuario;
import com.example.tienda_bicicletas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return LoginResponseDto.builder()
                .token(jwtService.generarToken(usuario.getUsername()))
                .nombre(usuario.getNombre())
                .username(usuario.getUsername())
                .rol(usuario.getRol())
                .build();
    }

    public void registrarEmpleado(RegistroRequestDto dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }
        Usuario usuario = Usuario.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nombre(dto.getNombre())
                .rol(dto.getRol() != null ? dto.getRol() : com.example.tienda_bicicletas.enums.Rol.TRABAJADOR)
                .build();
        usuarioRepository.save(usuario);
    }
}
