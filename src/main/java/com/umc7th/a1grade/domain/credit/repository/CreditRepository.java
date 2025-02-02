package com.umc7th.a1grade.domain.credit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umc7th.a1grade.domain.credit.entity.Credit;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
  Optional<Credit> findByUser_Email(String email);
}
