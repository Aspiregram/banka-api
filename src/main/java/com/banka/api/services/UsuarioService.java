package com.banka.api.services;

import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.exceptions.ForbiddenOperationException;
import com.banka.api.exceptions.ResourceConflictException;
import com.banka.api.models.Usuario;
import com.banka.api.records.usuario.UsuarioCreateDto;
import com.banka.api.records.usuario.UsuarioResponseDto;
import com.banka.api.records.usuario.UsuarioUpdateDto;
import com.banka.api.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository usuRepo;
    private final PasswordEncoder passEncod;

    public UsuarioService(UsuarioRepository usuRepo, PasswordEncoder passEncod) {
        this.usuRepo = usuRepo;
        this.passEncod = passEncod;
    }

    // POST
    @Transactional
    public UsuarioResponseDto save(UsuarioCreateDto usuCreateDto) {
        if (usuRepo.existsByEmail(usuCreateDto.email()))
            throw new ResourceConflictException
                    ("Um usuário já possui esse email");

        Usuario usu = fromCreateDto(usuCreateDto);
        Usuario usuarioSalvo = usuRepo.save(usu);

        return toResponseDto(usuarioSalvo);
    }

    // GET
    public List<UsuarioResponseDto> findAll() {
        List<Usuario> usuarios = usuRepo.findAll();

        if (usuarios.isEmpty())
            throw new EntityNotFoundException
                    ("Não há usuários registrados");

        return usuarios.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // GET
    public UsuarioResponseDto findById(UUID id) {
        Usuario usuarioEncontrado = usuRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O usuário com ID \"" + id + "\" não pode ser encontrado"));

        return toResponseDto(usuarioEncontrado);
    }

    // PUT
    @Transactional
    public UsuarioResponseDto update(UUID id, UsuarioUpdateDto usuUptDto) {
        Usuario usuarioEncontrado = usuRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O usuário com ID \"" + id + "\" não pode ser encontrado"));

        usuarioEncontrado.setUsername(usuUptDto.username());
        usuarioEncontrado.setNome(usuUptDto.nome());
        usuarioEncontrado.setSobrenome(usuUptDto.sobrenome());
        usuarioEncontrado.setEmail(usuUptDto.email());
        usuarioEncontrado.setSenha(
                passEncod.encode(usuUptDto.senha()));

        Usuario usuarioAtualizado = usuRepo.save(usuarioEncontrado);

        return toResponseDto(usuarioAtualizado);
    }

    // DELETE
    public void deleteAll() {
        List<Usuario> usuarios = usuRepo.findAll();

        if (usuarios.isEmpty())
            throw new EntityNotFoundException
                    ("Não há usuários registrados");

        List<Usuario> usuariosParaDeletar = usuarios.stream()
                .filter(u -> !u.getUsername().equals("system"))
                .toList();

        usuRepo.deleteAll(usuariosParaDeletar);
    }

    // DELETE
    public void deleteById(UUID id) {
        Usuario usuarioEncontrado = usuRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O usuário com ID \"" + id + "\" não pode ser encontrado"));

        if (usuarioEncontrado.getUsername().equals("system"))
            throw new ForbiddenOperationException
                    ("O usuário com ID \"" + usuarioEncontrado.getUsername() + "\" não pode ser deletado");

        usuRepo.deleteById(id);
    }

    private Usuario fromCreateDto(UsuarioCreateDto usuCreateDto) {
        return new Usuario(
                null,
                usuCreateDto.username(),
                usuCreateDto.nome(),
                usuCreateDto.sobrenome(),
                usuCreateDto.email(),
                passEncod.encode(usuCreateDto.senha()),
                null,
                null,
                null,
                null
        );
    }

    private UsuarioResponseDto toResponseDto(Usuario usu) {
        return new UsuarioResponseDto(
                usu.getId(),
                usu.getUsername(),
                usu.getNome(),
                usu.getSobrenome(),
                usu.getEmail(),
                usu.getRole(),
                usu.getFaceHash(),
                usu.getCriadoEm(),
                usu.getUltimoLogin()
        );
    }
}
