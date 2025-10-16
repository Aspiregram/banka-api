package com.banka.api.services;

import com.banka.api.enums.Role;
import com.banka.api.models.Usuario;
import com.banka.api.records.UsuarioDto;
import com.banka.api.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuRepo;
    private final PasswordEncoder passEncod;

    public UsuarioService(UsuarioRepository usuRepo, PasswordEncoder passEncod) {
        this.usuRepo = usuRepo;
        this.passEncod = passEncod;
    }

    @Transactional
    public UsuarioDto save(UsuarioDto usuDto) {
        if (usuRepo.existsByEmail(usuDto.email()))
            throw new RuntimeException("Usuário já cadastrado com este email");

        String senhaCodificada = passEncod.encode(usuDto.senha());

        // Este é o construtor completo da sua Entidade Usuario
        Usuario usu = new Usuario(
                null,
                usuDto.nome(),
                usuDto.sobrenome(),
                usuDto.email(),
                senhaCodificada,
                usuDto.documento(),
                usuDto.paisOrigem(),
                usuDto.paisResidencia(),
                usuDto.ong(),
                Role.ROLE_USER,
                true,
                null,
                null,
                null
        );

        Usuario usuSalvo = usuRepo.save(usu);

        return toDto(usuSalvo);
    }

    public List<UsuarioDto> findAll() {
        if (usuRepo.findAll().isEmpty())
            throw new RuntimeException("Não há nenhum usuário cadastrado");

        List<Usuario> usus = usuRepo.findAll();

        return usus.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Usuario findEntityById(String id) {
        return usuRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioDto findById(String id) {
        return toDto(findEntityById(id));
    }

    public UsuarioDto findByEmail(String email) {
        Usuario usuEncontrado = usuRepo.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        return toDto(usuEncontrado);
    }


    @Transactional
    public UsuarioDto update(String id, UsuarioDto usuDto) {
        Usuario usuEncontrado = findEntityById(id);

        usuEncontrado.setNome(usuDto.nome());
        usuEncontrado.setSobrenome(usuDto.sobrenome());
        usuEncontrado.setEmail(usuDto.email());

        if (usuDto.senha() != null && !usuDto.senha().isEmpty()) {
            usuEncontrado.setSenha(passEncod.encode(usuDto.senha()));
        }

        usuEncontrado.setDocumento(usuDto.documento());
        usuEncontrado.setPaisOrigem(usuDto.paisOrigem());
        usuEncontrado.setPaisResidencia(usuDto.paisResidencia());
        usuEncontrado.setOng(usuDto.ong());

        Usuario usuAtualizado = usuRepo.save(usuEncontrado);

        return toDto(usuAtualizado);
    }

    public void deleteById(String id) {
        if (!usuRepo.existsById(id))
            throw new RuntimeException("Usuário não existe");

        usuRepo.deleteById(id);
    }

    private UsuarioDto toDto(Usuario usu) {
        return new UsuarioDto(
                usu.getId(),
                usu.getNome(),
                usu.getSobrenome(),
                usu.getEmail(),
                null,
                usu.getRole(),
                usu.getDocumento(),
                usu.getPaisOrigem(),
                usu.getPaisResidencia(),
                usu.getOng()
        );
    }
}