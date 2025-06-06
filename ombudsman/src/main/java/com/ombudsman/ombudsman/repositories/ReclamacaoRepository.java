package com.ombudsman.ombudsman.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ombudsman.ombudsman.entities.Reclamacao;
import com.ombudsman.ombudsman.entities.User;

@Repository
public interface ReclamacaoRepository extends JpaRepository<Reclamacao, Long> {
    List<Reclamacao> findByUser(User user);
}

