package com.phuclq.student.component;


import com.phuclq.student.response.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class RestEntityResponse<T> {

    @Autowired
    private ParseEntityResponse<T> parseEntityResponse;

    private EntityResponse<T> reponse;

    private HttpHeaders httpHeaders;

    private HttpStatus httpStatus;

    public ResponseEntity<EntityResponse<T>> getResponse() {
        return new ResponseEntity<EntityResponse<T>>(getDataResponse(), getHttpHeaders(), getHttpStatus());
    }

    public ResponseEntity<Void> getResNoContent() {
        return ResponseEntity.noContent().headers(getHttpHeaders()).build();
    }

    public ResponseEntity<EntityResponse<T>> getResWrapOrNotFound() {
        return new ResponseEntity<EntityResponse<T>>(getDataResponse(), getHttpStatus());
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public RestEntityResponse<T> setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;

        return this;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public RestEntityResponse<T> setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;

        return this;
    }

    public EntityResponse<T> getDataResponse() {
        return reponse;
    }

    public RestEntityResponse<T> setDataResponse(T data) {

        this.reponse = parseEntityResponse.parse(data, getHttpStatus()).get();

        return this;
    }

    public RestEntityResponse<T> setDataResponse(T reponse, String message) {
        this.reponse = parseEntityResponse.parse(reponse, message).get();

        return this;
    }

    public RestEntityResponse<T> setDataResponse(T data, HttpStatus httpStatus, String message) {
        this.reponse = parseEntityResponse.parse(data, httpStatus, message).get();

        return this;
    }

    public RestEntityResponse<T> setDataResponse(T data, String status, String message) {
        this.reponse = parseEntityResponse.parse(data, status, message).get();

        return this;
    }

    public RestEntityResponse<T> setDataResponse(T data, String status, String message, boolean check) {
        this.reponse = parseEntityResponse.parse(data, status, message).get();

        return this;
    }

}
