package com.phuclq.student.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class EntityResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String timeStamp;

    protected String securityVersion;

    protected String result;

    protected String message;

    protected String errorCode;

    protected T data;


}
