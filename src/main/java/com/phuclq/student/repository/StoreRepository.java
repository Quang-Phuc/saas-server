package com.phuclq.student.repository;

import com.phuclq.student.domain.Store;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByIdIn(List<Long> ids);
@Query
    (
            value = "SELECT s.id AS store_id, " +
                    "s.name AS store_name, " +
                    "s.address AS store_address, " +
                    "s.note AS note, " +
                    "u.id AS user_id, " +
                    "u.full_name AS user_full_name, " +
                    "u.phone AS user_phone, " +
                    "u.email AS user_email, " +
                    "u.role_id AS user_role_id, " +
                    "s.created_date AS created_date " +
                    "FROM store s " +
                    "JOIN users_stores us ON us.store_id = s.id " +
                    "JOIN user u ON u.id = us.user_id and u.role_id = 2 " +
                    "WHERE (:keyword IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                    "AND (:userId IS NULL OR u.id = :userId)",
            countQuery = "SELECT COUNT(*) " +
                    "FROM store s " +
                    "JOIN users_stores us ON us.store_id = s.id " +
                    "JOIN user u ON u.id = us.user_id and u.role_id = 2" +
                    "WHERE (:keyword IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                    "AND (:userId IS NULL OR u.id = :userId)",
            nativeQuery = true
    )
    Page<Map<String, Object>> searchStoresWithUser(@Param("keyword") String keyword,
                                                   @Param("userId") Integer userId,
                                                   Pageable pageable);

    @Query(
            value = "SELECT DISTINCT s.id, s.name, s.address " +
                    "FROM store s " +
                    "JOIN users_stores us ON us.store_id = s.id " +
                    "WHERE (:userId IS NULL OR us.user_id = :userId)",
            nativeQuery = true
    )
    List<Map<String, Object>> findStoresByUserId(@Param("userId") Integer userId);

}
