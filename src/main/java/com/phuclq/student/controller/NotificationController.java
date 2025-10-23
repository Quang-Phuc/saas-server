package com.phuclq.student.controller;

import com.phuclq.student.component.RestEntityResponse;
import com.phuclq.student.domain.Notification;
import com.phuclq.student.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RestEntityResponse restEntityRes;

    @GetMapping("")
    public ResponseEntity<?> search() {

        List<Notification> result = notificationService.search(false);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(result).getResponse();
    }

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody Notification notification)
            throws IOException {
        Notification categorySave = notificationService.save(notification);
        return restEntityRes.setHttpStatus(HttpStatus.OK).setDataResponse(categorySave).getResponse();
    }


    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable Long Id) {

        notificationService.deleteById(Id);
        return restEntityRes.setHttpStatus(HttpStatus.OK).getResponse();
    }


}
