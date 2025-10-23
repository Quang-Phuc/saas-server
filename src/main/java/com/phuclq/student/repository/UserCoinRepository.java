package com.phuclq.student.repository;

import com.phuclq.student.domain.UserCoin;
import com.phuclq.student.dto.UserPaymentInfor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCoinRepository extends JpaRepository<UserCoin, Integer> {

    UserCoin findByUserId(Integer userId);

    @Query(value = "select u.user_name as username, u.email as email, uc.total_coin as totalCoin "
            + "from user u inner join user_coin uc on u.id = uc.user_id "
            + "where u.id = ?1", nativeQuery = true)
    UserPaymentInfor getUserCoinInfor(Integer userId);

}
