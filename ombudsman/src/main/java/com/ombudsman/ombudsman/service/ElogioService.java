package com.ombudsman.ombudsman.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ombudsman.ombudsman.entitie.Elogio;
import com.ombudsman.ombudsman.entitie.User;
import com.ombudsman.ombudsman.repository.ElogioRepository;

@Service
public class ElogioService {

    @Autowired
    private UserService userService;

    @Autowired
    private ElogioRepository elogioRepository;

    // Método responsável por cadastrar um elogio
    public Elogio createElogio(String titulo, String descricao, LocalDateTime dataCriacao) {
        // Busca o usuario autenticado
        User user = userService.getAuthenticatedUser();
        // Cadastra a um elogio
        Elogio elogio = new Elogio();
        elogio.setTitulo(titulo);
        elogio.setDescricao(descricao);
        elogio.setDataCriacao(dataCriacao);
        elogio.setUser(user);
        return elogioRepository.save(elogio);
    }


    // Método responsável por buscar todos os elogios
    public List<Elogio> getAllElogios() {
        User user = userService.getAuthenticatedUser();
        return elogioRepository.findByUser(user);
    }


    // Atualizar elogio por ID
    public Elogio updateElogio(Long elogioId, String novoTitulo, String novaDescricao) {
        User usuario = userService.getAuthenticatedUser();
        Elogio elogio = elogioRepository.findById(elogioId)
                .orElseThrow(() -> new RuntimeException("Elogio não encontrado."));

        // Verifica se o elogio é do usuário autenticado
        if (!elogio.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar este elogio.");
        }

        elogio.setTitulo(novoTitulo);
        elogio.setDescricao(novaDescricao);
        return elogioRepository.save(elogio);
    }


    // Deletar elogio por ID
    public Elogio deleteElogio(Long elogioId) {
        User usuario = userService.getAuthenticatedUser();
        Elogio elogio = elogioRepository.findById(elogioId)
                .orElseThrow(() -> new RuntimeException("Elogio não encontrado."));

        // Verifica se o elogio é do usuário autenticado
        if (!elogio.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar este elogio.");
        }

        elogioRepository.delete(elogio);
        return elogio;
    }


}
