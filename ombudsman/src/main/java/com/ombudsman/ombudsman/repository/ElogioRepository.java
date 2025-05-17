package com.ombudsman.ombudsman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ombudsman.ombudsman.entitie.Elogio;

@Repository
public interface ElogioRepository extends JpaRepository<Elogio, Long> {
}

