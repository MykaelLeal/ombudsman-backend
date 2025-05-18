package com.ombudsman.ombudsman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ombudsman.ombudsman.entitie.Sugestao;
import com.ombudsman.ombudsman.entitie.User;

@Repository
public interface SugestaoRepository extends JpaRepository<Sugestao, Long> {
    List<Sugestao> findByUser(User user);
}
