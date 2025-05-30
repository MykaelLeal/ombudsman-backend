package com.ombudsman.ombudsman.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ombudsman.ombudsman.entitie.Reclamacao;
import com.ombudsman.ombudsman.entitie.User;
import com.ombudsman.ombudsman.repository.ReclamacaoRepository;

@Service
public class ReclamacaoService {

    @Autowired
    private UserService userService;

    @Autowired
    private ReclamacaoRepository reclamacaoRepository;

    // Método responsável por cadastrar uma reclamacao
    public Reclamacao createReclamacao(String titulo, String descricao, LocalDateTime dataCriacao) {
        // Busca o usuario autenticado
        User user = userService.getAuthenticatedUser();
        // Cadastra a uma Reclamacao
        Reclamacao reclamacao = new Reclamacao();
        reclamacao.setTitulo(titulo);
        reclamacao.setDescricao(descricao);
        reclamacao.setDataCriacao(dataCriacao);
        reclamacao.setUser(user);
        return reclamacaoRepository.save(reclamacao);
    }


    // Método responsável por buscar todas as reclamações
    public List<Reclamacao> getAllReclamacoes() {
        User user = userService.getAuthenticatedUser();
        return reclamacaoRepository.findByUser(user);
    }

    // Método responsável por buscar reclamações por ID
    public Optional<Reclamacao> findById(Long id) {
       userService.getAuthenticatedUser();
       return reclamacaoRepository.findById(id);

    }


    // Método responsável por salvar uma Reclamação
    public Reclamacao salvar(Reclamacao reclamacao) {
        return reclamacaoRepository.save(reclamacao);
    }


    // Atualizar reclamaçoes por ID
    public Reclamacao updateReclamacao(Long reclamacaoId, String novoTitulo, String novaDescricao) {
        User usuario = userService.getAuthenticatedUser();
        Reclamacao reclamacao = reclamacaoRepository.findById(reclamacaoId)
                .orElseThrow(() -> new RuntimeException("Reclamação não encontrada."));

        // Verifica se o elogio é do usuário autenticado
        if (!reclamacao.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta reclamação.");
        }

        reclamacao.setTitulo(novoTitulo);
        reclamacao.setDescricao(novaDescricao);
        return reclamacaoRepository.save(reclamacao);
    }


    // Deletar Reclamação por ID
    public void deleteReclamacao(Long reclamacaoId) {
        User usuario = userService.getAuthenticatedUser();
        Reclamacao reclamacao = reclamacaoRepository.findById(reclamacaoId)
                .orElseThrow(() -> new RuntimeException("Reclamação não encontrada."));

        // Verifica se a reclamação é do usuário autenticado
        if (!reclamacao.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar esta reclamação.");
        }

        reclamacaoRepository.delete(reclamacao);

    }


}
