package com.banka.api.services;

import com.banka.api.models.Conta;
import com.banka.api.models.Usuario;
import com.banka.api.models.Moeda;
import com.banka.api.records.ContaDto;
import com.banka.api.repositories.ContaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContaService {

    private final ContaRepository contaRepo;
    private final UsuarioService usuarioService;
    private final MoedaService moedaService;

    public ContaService(ContaRepository contaRepo, UsuarioService usuarioService, MoedaService moedaService) {
        this.contaRepo = contaRepo;
        this.usuarioService = usuarioService;
        this.moedaService = moedaService;
    }

    @Transactional
    public ContaDto save(ContaDto contaDto) {
        // Validação: Busca as entidades Usuario e Moeda
        Usuario usuario = usuarioService.findEntityById(contaDto.usuarioId());
        Moeda moeda = moedaService.findEntityById(contaDto.moedaId());

        if (contaRepo.existsByUsuarioIdAndMoedaId(usuario.getId(), moeda.getId())) {
            throw new RuntimeException("O usuário já possui uma conta nesta moeda.");
        }

        // CORREÇÃO: Criação do objeto Conta
        Conta conta = new Conta(
                null, // ID será gerado
                usuario,
                moeda,
                BigDecimal.ZERO, // Saldo inicial sempre ZERO
                null, // dataCriacao é preenchida no @PrePersist
                null, // transacoesEnviadas
                null  // transacoesRecebidas
        );

        Conta contaSalva = contaRepo.save(conta);

        return toDto(contaSalva);
    }

    public List<ContaDto> findAll() {
        return contaRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Método para ser usado por outros serviços (como TransacaoService)
    public Conta findEntityById(String id) {
        return contaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));
    }

    public ContaDto findById(String id) {
        return toDto(findEntityById(id));
    }


    @Transactional
    public ContaDto updateSaldo(String id, BigDecimal valor) {
        Conta conta = findEntityById(id);

        if (conta.getSaldo().add(valor).compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Saldo insuficiente para esta operação.");
        }

        conta.setSaldo(conta.getSaldo().add(valor));
        Conta contaAtualizada = contaRepo.save(conta);

        return toDto(contaAtualizada);
    }

    public void deleteById(String id) {
        if (!contaRepo.existsById(id))
            throw new RuntimeException("Conta não existe");

        contaRepo.deleteById(id);
    }

    private ContaDto toDto(Conta conta) {
        return new ContaDto(
                conta.getId(),
                conta.getUsuario().getId(),
                conta.getMoeda().getId(),
                conta.getSaldo()
        );
    }
}