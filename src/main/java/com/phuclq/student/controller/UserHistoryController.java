package com.phuclq.student.controller;

import com.phuclq.student.domain.UserHistory;
import com.phuclq.student.service.UserHistoryService;
import com.phuclq.student.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserHistoryController {

    @Autowired
    private UserHistoryService userHistoryService;

    @GetMapping("/user/{id}/history/activities")
    public ResponseEntity<List<UserHistory>> getAllActivitiesByUser(@PathVariable("id") Integer id, Pageable pageable) {
        Page<UserHistory> page = userHistoryService.getAllActivitiesByUser(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/user/{userId}/history/activity/{activityId}")
    public ResponseEntity<List<UserHistory>> getAllActivitiesByUser(@PathVariable("userId") Integer userId, @PathVariable("activityId") Integer activityId, Pageable pageable) {
        Page<UserHistory> page = userHistoryService.getAllActivitiesByUserAndActivity(userId, activityId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
