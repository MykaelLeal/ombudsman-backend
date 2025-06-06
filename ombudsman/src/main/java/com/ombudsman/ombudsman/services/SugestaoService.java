package com.ombudsman.ombudsman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ombudsman.ombudsman.dtos.manifestacoesDto.RequestDto;
import com.ombudsman.ombudsman.entities.Sugestao;
import com.ombudsman.ombudsman.entities.User;
import com.ombudsman.ombudsman.repositories.SugestaoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SugestaoService {

    @Autowired
    private SugestaoRepository sugestaoRepository;

    @Autowired
    private UserService userService;

    // Método responsável por cadastrar uma sugestão
    public Sugestao createSugestao(RequestDto sugestaoDto) {
        User user = userService.getAuthenticatedUser();

        Sugestao sugestao = new Sugestao();
        sugestao.setTitulo(sugestaoDto.titulo());
        sugestao.setDescricao(sugestaoDto.descricao());
        sugestao.setUser(user);

        return sugestaoRepository.save(sugestao);
    }

    // Método responsável por buscar todas as sugestões do usuário autenticado
    public List<Sugestao> getAllSugestoes() {
        User user = userService.getAuthenticatedUser();
        return sugestaoRepository.findByUser(user);
    }

    // Método responsável por buscar sugestão por ID
    public Optional<Sugestao> findById(Long id) {
        userService.getAuthenticatedUser();
        return sugestaoRepository.findById(id);
    }

    // Método responsável por salvar uma sugestão
    public Sugestao salvar(Sugestao sugestao) {
        return sugestaoRepository.save(sugestao);
    }

    // Atualizar sugestão por ID
    public Sugestao updateSugestao(Long sugestaoId, RequestDto sugestaoDto) {
        User usuario = userService.getAuthenticatedUser();
        Sugestao sugestao = sugestaoRepository.findById(sugestaoId)
                .orElseThrow(() -> new RuntimeException("Sugestão não encontrada."));

        // Verifica se a sugestão pertence ao usuário autenticado
        if (!sugestao.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta sugestão.");
        }

        sugestao.setTitulo(sugestaoDto.titulo());
        sugestao.setDescricao(sugestaoDto.descricao());

        return sugestaoRepository.save(sugestao);
    }

    // Deletar sugestão por ID
    public void deleteSugestaoByID(Long sugestaoId) {
        User usuario = userService.getAuthenticatedUser();
        Sugestao sugestao = sugestaoRepository.findById(sugestaoId)
                .orElseThrow(() -> new RuntimeException("Sugestão não encontrada."));

        // Verifica se a sugestão pertence ao usuário autenticado
        if (!sugestao.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar esta sugestão.");
        }

        sugestaoRepository.delete(sugestao);
    }
}
