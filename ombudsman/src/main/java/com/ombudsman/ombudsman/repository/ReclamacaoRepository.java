package com.ombudsman.ombudsman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ombudsman.ombudsman.entitie.Reclamacao;
import com.ombudsman.ombudsman.entitie.User;

@Repository
public interface ReclamacaoRepository extends JpaRepository<Reclamacao, Long> {
    List<Reclamacao> findByUser(User user);
}

