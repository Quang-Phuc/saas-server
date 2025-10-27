package com.phuclq.student.repository;

import com.phuclq.student.domain.File;
import com.phuclq.student.domain.User;
import com.phuclq.student.dto.UserInfoResult;
import com.phuclq.student.dto.UserResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    @Query(value = "select f from file f inner join user_history_file uhf on f.id = uhf.file_id inner join user_history uh on uhf.user_hisoty_id = uh.id\n" +
            "inner join activity ac on uh.activity_id = ac.id where uh.user_id = :userId and ac.id =1", nativeQuery = true)
    Page<File> findFilesByUser(@Param("userId") Integer userId, Pageable pageable);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    User findUserByUserFaceIdAndIsDeletedFalse(String userId);

    User findByUserFaceId(String userId);

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserNameIgnoreCaseAndIsDeletedFalse(String userName);

    Optional<User> findByPhone(String phone);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    User findUserByEmailIgnoreCaseAndIsDeletedFalseAndUserFaceIdIsNull(String email);


    @Query(value = "select u.*,u.REFERRAL_CODE as referralCode,u.birth_day as birthDay,u.industry_id as industryId,  uc.total_coin,u.full_name as fullName, a.url as image from user u left join user_coin uc on uc.user_id = u.id left join attachment a on u.id = a.request_id and a.file_type =?2 where (u.email = ?1 or u.USER_FACE_ID = ?1) "
            + " ", nativeQuery = true)
    UserResult findUserResultByEmail(String email, String type);

    @Query("SELECT c FROM User c WHERE (:userName is null or c.userName = :userName) and (:email is null"
            + " or c.email = :email) and (:phone is null or c.phone = :phone)")
    Page<User> findUserByUserNameAndEmail(@Param("userName") String userName, @Param("email") String email, @Param("phone") String phone, Pageable pageable);

    @Query("SELECT c FROM User c WHERE (:userName is null or c.userName = :userName) and (:email is null or c.email = :email) and (:phone is null or c.phone = :phone) and c.createdDate >= :dateFrom and c.createdDate <= :dateTo ")
    Page<User> findUserByUserNameAndEmailAndTime(@Param("userName") String userName, @Param("email") String email, @Param("phone") String phone, @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo, Pageable pageable);

    @Query(value = "select u.*, uc.total_coin from user u left join user_coin uc on uc.user_id = u.id ", nativeQuery = true)
    Page<UserResult> findUserResult(Pageable pageable);

    @Query(value = "select u.user_name as userName , u.email , ui.birth_date as birthDate from user u inner join user_information ui on u.id = ui.user_id\r\n" +
            "where DAY(CURDATE()) = DAY(ui.birth_date) and MONTH(CURDATE()) = MONTH (ui.birth_date)", nativeQuery = true)
    List<UserInfoResult> findUserInfoResult();

    List<User> findUserByRoleIdAndIsDeleted(Integer roleId, Boolean isDeleted);

    @Query(value = "SELECT u.id as id ,a.url as image FROM user u  left join attachment a on u.id = a.request_id and a.file_type =:type order by id desc LIMIT 9  ", nativeQuery = true)
    List<UserInfoResult> findTop10OrderByIdDesc(@Param("type") String type);

    User findTopByOrderByIdDesc();

}
