package com.phuclq.student.service;

import com.phuclq.student.domain.Comment;

import java.util.List;

public interface CommentService {

    Comment comment(Comment comment);

    Comment like(Integer id, String type);


    void delete(Integer id);

    List<Comment> commentByIdAndType(String id, String type);

    Comment unlike(Integer id, String type);
}
