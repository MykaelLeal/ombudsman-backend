package com.ombudsman.ombudsman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ombudsman.ombudsman.entitie.Elogio;
import com.ombudsman.ombudsman.entitie.User;

@Repository
public interface ElogioRepository extends JpaRepository<Elogio, Long> {
    List<Elogio> findByUser(User user);
}


