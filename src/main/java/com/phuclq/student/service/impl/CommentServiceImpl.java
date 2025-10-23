package com.phuclq.student.service.impl;

import com.phuclq.student.domain.*;
import com.phuclq.student.dto.UserDTO;
import com.phuclq.student.exception.BusinessException;
import com.phuclq.student.exception.ExceptionUtils;
import com.phuclq.student.exception.NotFoundException;
import com.phuclq.student.repository.CommentRepository;
import com.phuclq.student.repository.FileRepository;
import com.phuclq.student.repository.LikeCommentRepository;
import com.phuclq.student.repository.SchoolRepository;
import com.phuclq.student.service.CommentService;
import com.phuclq.student.service.UserService;
import com.phuclq.student.types.CommentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.phuclq.student.types.CommentType.COMMENT_FILE;
import static com.phuclq.student.types.CommentType.COMMENT_SCHOOL;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserService userService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private LikeCommentRepository likeCommentRepository;

    @Override
    public Comment comment(Comment comment) {

        UserDTO userLogin1 = userService.getUserResultLogin();
        Comment commentSave = new Comment();
        commentSave.setCreatedBy(userLogin1.getId().toString());
        commentSave.setIsAnonymous(comment.getIsAnonymous());
        if(Objects.nonNull(comment.getIsAnonymous())&& comment.getIsAnonymous()) {
            commentSave.setUserName(userLogin1.getUserName());
            commentSave.setImageUser(userLogin1.getImage());
        }
        commentSave.setCreatedDate(LocalDateTime.now());
        commentSave.setRequestId(comment.getRequestId());
        commentSave.setType(comment.getType());
        commentSave.setContent(comment.getContent());
        commentSave.setIsDelete(false);
        if (comment.getType().equals(COMMENT_FILE.getName())) {
            File byId = fileRepository.findByIdUrl(comment.getRequestId()).orElseThrow(() -> new BusinessException(
                    ExceptionUtils.REQUEST_NOT_EXIST));
            byId.setTotalComment(Objects.isNull(byId.getTotalComment()) ? 1 : byId.getTotalComment() + 1);
            fileRepository.save(byId);
        }
        if (comment.getType().equals(COMMENT_SCHOOL.getName())) {
            School byId = schoolRepository.findByIdUrl(comment.getRequestId()).orElseThrow(() -> new BusinessException(
                    ExceptionUtils.REQUEST_NOT_EXIST));
            byId.setTotalComment(Objects.isNull(byId.getTotalComment()) ? 1 : byId.getTotalComment() + 1);
            schoolRepository.save(byId);
        }
        return commentRepository.save(commentSave);
    }

    @Override
    public Comment like(Integer id, String type) {

        User userLogin = userService.getUserLogin();
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundException::new);
        LikeComment likeComments = likeCommentRepository.findAllByCommentIdAndCommentTypeAndCreatedBy(id.longValue(), type, userLogin.getId().toString());

        if (Objects.nonNull(likeComments)) {
            likeCommentRepository.delete(likeComments);
        } else {
            comment.setTotalLike(Objects.nonNull(comment.getTotalLike()) ? comment.getTotalLike() + 1 : 1);

        }
        LikeComment likeComment = new LikeComment();
        likeComment.setCommentId(id.longValue());
        likeComment.setCommentType(type);
        likeCommentRepository.save(likeComment);
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Integer id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new BusinessException(
                ExceptionUtils.REQUEST_NOT_EXIST));

        if (comment.getType().equals(COMMENT_FILE.getName())) {
            File byId = fileRepository.findByIdUrl(comment.getRequestId()).orElseThrow(() -> new BusinessException(
                    ExceptionUtils.REQUEST_NOT_EXIST));
            byId.setTotalComment(Objects.isNull(byId.getTotalComment()) ? 0 : byId.getTotalComment() - 1);
            fileRepository.save(byId);
        }
        if (comment.getType().equals(COMMENT_SCHOOL.getName())) {
            School byId = schoolRepository.findByIdUrl(comment.getRequestId()).orElseThrow(() -> new BusinessException(
                    ExceptionUtils.REQUEST_NOT_EXIST));
            byId.setTotalComment(Objects.isNull(byId.getTotalComment()) ? 0 : byId.getTotalComment() - 1);
            schoolRepository.save(byId);
        }
        commentRepository.delete(comment);


    }

    @Override
    public List<Comment> commentByIdAndType(String idUrl, String type) {
        List<Comment> listComment = commentRepository.findAllByRequestIdAndTypeOrderByIdDesc(
                idUrl, type);
        User userLogin = userService.getUserLogin();

        List<Long> collect = listComment.stream().map(x -> x.getId().longValue()).collect(Collectors.toList());
        List<LikeComment> likeComments = likeCommentRepository.findAllByCommentIdInAndCommentTypeAndCreatedBy(collect, type, Objects.nonNull(userLogin) && Objects.nonNull(userLogin.getId())  ? userLogin.getId().toString() : "0");

        listComment.forEach(y -> {
            if (Objects.nonNull(userLogin)) {
                Integer loginId = userLogin.getId();

                y.setIsDelete(Objects.nonNull(loginId) && y.getCreatedBy().equals(
                        Objects.requireNonNull(loginId).toString()));

                y.setIsLike(likeComments.stream().anyMatch(x -> x.getCommentId().equals(y.getId().longValue())));
            }

        });
        return listComment;
    }

    @Override
    public Comment unlike(Integer id, String type) {
        User userLogin = userService.getUserLogin();
        Comment comment = commentRepository.findById(id).orElseThrow(NotFoundException::new);
        likeCommentRepository.findAllByCommentIdAndCommentTypeAndCreatedBy(id.longValue(), type, userLogin.getId().toString());
        comment.setTotalLike(Objects.nonNull(comment.getTotalLike()) ? comment.getTotalLike() - 1 : 0);
        return commentRepository.save(comment);
    }
}

