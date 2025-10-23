package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ActivityController {
    private final Logger log = LoggerFactory.getLogger(ActivityController.class);
    @Autowired
    private ActivityService activityService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("/activity/like_file/{Id}")
    public ResponseEntity<?> likeFile(@PathVariable int Id) {
        System.out.println("Test");
        activityService.updateLikeFile(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }


}
