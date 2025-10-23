package com.phuclq.student.repository;

import com.phuclq.student.domain.UserInformation;
import com.phuclq.student.dto.UserInformationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, Integer> {
    UserInformation findAllByUserId(Integer Id);

    @Query(value = "SELECT u.user_name as userName , birth_date as birthDate, uf.gender as gender , uf.address as address ,u.phone as phone,uf.specialized as specialized ,uf.yourself as yourself,uf.school_id as schoolId from user_information uf inner join user u on uf.user_id = u.id where u.id =:Id"
            , nativeQuery = true)
    List<UserInformationResult> findAllById(@Param("Id") Integer Id);

}
