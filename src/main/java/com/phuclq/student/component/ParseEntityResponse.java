package com.phuclq.student.component;

import com.phuclq.student.response.EntityResponse;
import com.phuclq.student.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;


@Component
public class ParseEntityResponse<T> {

    @Autowired
    private EntityResponse<T> response;

    @Value("0.0.1")
    private String sercurityVersion;

    @PostConstruct
    private void init() {
        String dateTime = DateTimeUtils.toDateString(new Date(), DateTimeUtils.yyyyMMddThhMMssZ);
        response.setTimeStamp(dateTime);
        response.setSecurityVersion(sercurityVersion);
        response.setResult("success");
    }

    public EntityResponse<T> get() {
        return response;
    }

    public ParseEntityResponse<T> parse(T reponse) {
        response.setData(reponse);

        return this;
    }

    public ParseEntityResponse<T> parse(HttpStatus httpStatus) {
        response.setErrorCode(String.valueOf(httpStatus.value()));
        return this;
    }

    public ParseEntityResponse<T> parse(T data, HttpStatus httpStatus) {

        response.setErrorCode(String.valueOf(httpStatus.value()));
        response.setMessage(httpStatus.getReasonPhrase());
        response.setData(data);

        return this;
    }

    public ParseEntityResponse<T> parse(T data, HttpStatus httpStatus, String message) {
        response.setErrorCode(String.valueOf(httpStatus.value()));
        response.setMessage(message);
        response.setData(data);

        return this;
    }

    public ParseEntityResponse<T> parse(T data, String status, String message) {
        response.setErrorCode(status);
        response.setMessage(message);
        response.setData(data);

        return this;
    }

    public ParseEntityResponse<T> parse(T data, String message) {
        response.setMessage(message);
        response.setData(data);

        return this;
    }
}
