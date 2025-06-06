package com.ombudsman.ombudsman.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ombudsman.ombudsman.dtos.manifestacoesDto.RequestDto;
import com.ombudsman.ombudsman.entities.Reclamacao;
import com.ombudsman.ombudsman.entities.User;
import com.ombudsman.ombudsman.repositories.ReclamacaoRepository;

@Service
public class ReclamacaoService {

    @Autowired
    private ReclamacaoRepository reclamacaoRepository;

    @Autowired
    private UserService userService;

    // Método responsável por cadastrar uma reclamação
    public Reclamacao createReclamacao(RequestDto reclamacaoDto) {
        User user = userService.getAuthenticatedUser();

        Reclamacao reclamacao = new Reclamacao();
        reclamacao.setTitulo(reclamacaoDto.titulo());
        reclamacao.setDescricao(reclamacaoDto.descricao());
        reclamacao.setUser(user);

        return reclamacaoRepository.save(reclamacao);
    }

    // Método responsável por buscar todas as reclamações do usuário autenticado
    public List<Reclamacao> getAllReclamacoes() {
        User user = userService.getAuthenticatedUser();
        return reclamacaoRepository.findByUser(user);
    }

    // Método responsável por buscar reclamação por ID
    public Optional<Reclamacao> findById(Long id) {
        userService.getAuthenticatedUser();
        return reclamacaoRepository.findById(id);
    }

    // Método responsável por salvar uma reclamação
    public Reclamacao salvar(Reclamacao reclamacao) {
        return reclamacaoRepository.save(reclamacao);
    }

    // Atualizar reclamação por ID
    public Reclamacao updateReclamacao(Long reclamacaoId, RequestDto reclamacaoDto) {
        User usuario = userService.getAuthenticatedUser();
        Reclamacao reclamacao = reclamacaoRepository.findById(reclamacaoId)
                .orElseThrow(() -> new RuntimeException("Reclamação não encontrada."));

        // Verifica se a reclamação pertence ao usuário autenticado
        if (!reclamacao.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta reclamação.");
        }

        reclamacao.setTitulo(reclamacaoDto.titulo());
        reclamacao.setDescricao(reclamacaoDto.descricao());

        return reclamacaoRepository.save(reclamacao);
    }

    // Deletar reclamação por ID
    public void deleteReclamacaoByID(Long reclamacaoId) {
        User usuario = userService.getAuthenticatedUser();
        Reclamacao reclamacao = reclamacaoRepository.findById(reclamacaoId)
                .orElseThrow(() -> new RuntimeException("Reclamação não encontrada."));

        // Verifica se a reclamação pertence ao usuário autenticado
        if (!reclamacao.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar esta reclamação.");
        }

        reclamacaoRepository.delete(reclamacao);
    }
}
