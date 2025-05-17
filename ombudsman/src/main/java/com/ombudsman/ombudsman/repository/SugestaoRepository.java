package com.ombudsman.ombudsman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ombudsman.ombudsman.entitie.Sugestao;

@Repository
public interface SugestaoRepository extends JpaRepository<Sugestao, Long> {
}
