package com.ombudsman.ombudsman.services;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ombudsman.ombudsman.dtos.manifestacoesDto.RequestDto;
import com.ombudsman.ombudsman.entities.Elogio;
import com.ombudsman.ombudsman.entities.User;
import com.ombudsman.ombudsman.repositories.ElogioRepository;

@Service
public class ElogioService {

    @Autowired
    private UserService userService;

    @Autowired
    private ElogioRepository elogioRepository;

    // Método responsável por cadastrar um elogio
    public Elogio createElogio(RequestDto elogioDto) {
        User user = userService.getAuthenticatedUser();

        Elogio elogio = new Elogio();
        elogio.setTitulo(elogioDto.titulo());
        elogio.setDescricao(elogioDto.descricao());
        elogio.setUser(user);

        return elogioRepository.save(elogio);
    }

    
    // Método responsável por buscar todos os elogios
    public List<Elogio> getAllElogios() {
        User user = userService.getAuthenticatedUser();
        return elogioRepository.findByUser(user);
    }


    // Método responsável por buscar elogio por ID
    public Optional<Elogio> findById(Long id) {
       userService.getAuthenticatedUser();
       return elogioRepository.findById(id);

    }


    // Método responsável por salvar um elogio
    public Elogio salvar(Elogio elogio) {
        return elogioRepository.save(elogio);
    }


    // Atualizar elogio por ID
    public Elogio updateElogio(Long elogioId, RequestDto elogioDto) {
        User usuario = userService.getAuthenticatedUser();
        Elogio elogio = elogioRepository.findById(elogioId)
                .orElseThrow(() -> new RuntimeException("Elogio não encontrado."));

        // Verifica se o elogio é do usuário autenticado
        if (!elogio.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar este elogio.");
        }

        elogio.setTitulo(elogioDto.titulo());
        elogio.setDescricao(elogioDto.descricao());

        return elogioRepository.save(elogio);
    }


    // Deletar elogio por ID
    public void deleteElogioByID(Long elogioId) {
        User usuario = userService.getAuthenticatedUser();
        Elogio elogio = elogioRepository.findById(elogioId)
                .orElseThrow(() -> new RuntimeException("Elogio não encontrado."));

        // Verifica se o elogio é do usuário autenticado
        if (!elogio.getUser().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar este elogio.");
        }

        elogioRepository.delete(elogio);
       
    }


}
