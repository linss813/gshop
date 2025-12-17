package com.linss.gshop.repository;

import com.linss.gshop.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    Optional<VerificationCode> findTopByEmailOrderByCreatedAtDesc(String email);

    @Query("SELECT v FROM VerificationCode v WHERE v.email = :email AND v.code = :code")
    Optional<VerificationCode> findByEmailAndCode(@Param("email") String email, @Param("code") String code);
}
