package com.devsuperior.dslist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dslist.entities.Belonging;

public interface BelongingRepository extends JpaRepository<Belonging, Long> {
  
}
