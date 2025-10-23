package com.phuclq.student.repository;

import com.phuclq.student.domain.UserHistoryFile;
import com.phuclq.student.dto.UserHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryFileRepository extends JpaRepository<UserHistoryFile, Integer> {
    UserHistoryFile findByUserHisotyIdAndFileId(Integer historyId, Integer fileId);

    @Query(value = "select uhf.* from user_history_file uhf inner join user_history uh on uhf.user_hisoty_id = uh.id "
            + "where uhf.file_id = :fileId and uh.user_id = :userId and uh.activity_id = :activityId", nativeQuery = true)
    List<UserHistoryFile> findFileHistory(@Param("activityId") Integer activityId, @Param("fileId") Integer fileId,
                                          @Param("userId") Integer userId);

    List<UserHistoryFile> findByUserHisotyId(Integer userHisotyId);

    @Query(value = "select uhf.file_id as fileId,uh.activity_id as  activityId from user_history_file uhf inner join user_history uh on uhf.user_hisoty_id = uh.id "
            + "where  uh.user_id = :userId ", nativeQuery = true)
    List<UserHistoryDTO> findFileHistoryHome(@Param("userId") Integer userId);


    @Query(value = "select uhf.* from user_history_file uhf inner join user_history uh on uhf.user_hisoty_id = uh.id "
            + "where uhf.file_id in :fileId and uh.user_id = :userId and uh.activity_id = :activityId", nativeQuery = true)
    List<UserHistoryFile> findFileHistoryByUser(@Param("activityId") Integer activityId, @Param("fileId") List<Integer> fileIds,
                                                @Param("userId") Integer userId);

    @Query(value = "select uhf.* from user_history_file uhf inner join user_history uh on uhf.user_hisoty_id = uh.id "
            + "where uhf.file_id in :fileId  and uh.activity_id = :activityId", nativeQuery = true)
    List<UserHistoryFile> findFileHistory(@Param("activityId") Integer activityId, @Param("fileId") List<Integer> fileIds);

}
