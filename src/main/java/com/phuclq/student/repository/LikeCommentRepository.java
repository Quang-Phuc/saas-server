package com.phuclq.student.repository;

import com.phuclq.student.domain.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {

    LikeComment findAllByCommentIdAndCommentTypeAndCreatedBy(Long commentId,String type , String createBy);

    List<LikeComment> findAllByCommentIdInAndCommentTypeAndCreatedBy(List<Long> commentId, String type , String createBy);

}
