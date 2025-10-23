package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Comment;
import com.phuclq.student.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @PostMapping("/comment")
    public ResponseEntity<?> comment(@RequestBody Comment comment) {
        Comment comment1 = commentService.comment(comment);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(comment1).getResponse();
    }

    @GetMapping("/comment/like/{id}/{type}")
    public ResponseEntity<?> like(@PathVariable("id") Integer id,@PathVariable("type") String type) {
        Comment like = commentService.like(id,type);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(like).getResponse();
    }

    @GetMapping("/comment/unlike/{id}/{type}")
    public ResponseEntity<?> unlike(@PathVariable("id") Integer id,@PathVariable("type") String type) {
        Comment like = commentService.unlike(id,type);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(like).getResponse();
    }

    @GetMapping("/comment/detail/{idUrl}/{type}")
    public ResponseEntity<?> commentByIdAndType(@PathVariable("idUrl") String id, @PathVariable("type") String type) {
        List<Comment> commentList = commentService.commentByIdAndType(id, type);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(commentList).getResponse();
    }

    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        commentService.delete(id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }


}
