package com.ombudsman.ombudsman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ombudsman.ombudsman.entitie.Sugestao;
import com.ombudsman.ombudsman.entitie.User;
import com.ombudsman.ombudsman.repository.SugestaoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SugestaoService {

    @Autowired
    private UserService userService;

    @Autowired
    private SugestaoRepository sugestaoRepository;

    // Cadastrar sugestão
    public Sugestao createSugestao(String titulo, String descricao, LocalDateTime dataCriacao) {
        User user = userService.getAuthenticatedUser();
        Sugestao sugestao = new Sugestao();
        sugestao.setTitulo(titulo);
        sugestao.setDescricao(descricao);
        sugestao.setDataCriacao(dataCriacao);
        sugestao.setUser(user);
        return sugestaoRepository.save(sugestao);
    }

    // Listar todas as sugestões do usuário autenticado
    public List<Sugestao> getAllSugestoes() {
        User user = userService.getAuthenticatedUser();
        return sugestaoRepository.findByUser(user);
    }

    // Atualizar sugestão por ID
    public Sugestao updateSugestao(Long sugestaoId, String novoTitulo, String novaDescricao) {
        User usuario = userService.getAuthenticatedUser();
        Sugestao sugestao = sugestaoRepository.findById(sugestaoId)
                .orElseThrow(() -> new RuntimeException("Sugestão não encontrada."));

        if (!sugestao.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta sugestão.");
        }

        sugestao.setTitulo(novoTitulo);
        sugestao.setDescricao(novaDescricao);
        return sugestaoRepository.save(sugestao);
    }

    // Deletar sugestão por ID
    public Sugestao deleteSugestao(Long sugestaoId) {
        User usuario = userService.getAuthenticatedUser();
        Sugestao sugestao = sugestaoRepository.findById(sugestaoId)
                .orElseThrow(() -> new RuntimeException("Sugestão não encontrada."));

        if (!sugestao.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar esta sugestão.");
        }

        sugestaoRepository.delete(sugestao);
        return sugestao;
    }
}
