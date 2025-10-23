package com.phuclq.student.repository;

import com.phuclq.student.domain.TokenFireBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenFireBaseRepository extends JpaRepository<TokenFireBase, Integer> {


    List<TokenFireBase> findAllByToken(String token);

    TokenFireBase findAllByTokenAndUserId(String token, Integer userId);

    List<TokenFireBase> findAllByUserId(Integer userId);

}
