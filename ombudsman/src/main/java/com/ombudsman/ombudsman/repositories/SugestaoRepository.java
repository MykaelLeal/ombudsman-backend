package com.ombudsman.ombudsman.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ombudsman.ombudsman.entities.Sugestao;
import com.ombudsman.ombudsman.entities.User;

@Repository
public interface SugestaoRepository extends JpaRepository<Sugestao, Long> {
    List<Sugestao> findByUser(User user);
}
