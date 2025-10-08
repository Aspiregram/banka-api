package com.banka.api.services;

import com.banka.api.enums.Role;
import com.banka.api.models.Usuario;
import com.banka.api.records.UsuarioDto;
import com.banka.api.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public UsuarioDto save(UsuarioDto usuDto) {
        String senhaCodificada = passEncod.encode(usuDto.senha());

        Usuario usu = new Usuario(
                usuDto.id(),
                usuDto.nome(),
                usuDto.sobrenome(),
                usuDto.email(),
                senhaCodificada,
                Role.ROLE_USER,
                usuDto.pais(),
                usuDto.ong()
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

    public UsuarioDto findById(Long id) {
        Usuario usuEncontrado = usuRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        return toDto(usuEncontrado);
    }

    public UsuarioDto findByEmail(String email) {
        Usuario usuEncontrado = usuRepo.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        return toDto(usuEncontrado);
    }

    public UsuarioDto update(Long id, UsuarioDto usuDto) {
        Usuario usuEncontrado = usuRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        usuEncontrado.setNome(usuDto.nome());
        usuEncontrado.setSobrenome(usuDto.sobrenome());
        usuEncontrado.setSenha(usuDto.senha());
        usuEncontrado.setPais(usuDto.pais());
        usuEncontrado.setOng(usuDto.ong());

        Usuario usuAtualizado = usuRepo.save(usuEncontrado);

        return toDto(usuAtualizado);
    }

    public void deleteById(Long id) {
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
                usu.getSenha(),
                usu.getRole(),
                usu.getPais(),
                usu.getOng()
        );
    }

}
