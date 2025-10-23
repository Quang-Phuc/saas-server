package com.phuclq.student.repository;

import com.phuclq.student.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByRequestIdAndTypeOrderByIdDesc(String requestId, String type);

    List<Comment> findAllByIdUrlAndType(String idUrl, String type);

    List<Comment> findAllByRequestIdInAndType(List<String> requestId, String type);


}
