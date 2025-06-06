package com.ombudsman.ombudsman.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ombudsman.ombudsman.entities.Elogio;
import com.ombudsman.ombudsman.entities.User;

@Repository
public interface ElogioRepository extends JpaRepository<Elogio, Long> {
    List<Elogio> findByUser(User user);
}


