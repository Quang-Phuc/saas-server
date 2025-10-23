package com.phuclq.student.exception;

public class ExceptionResponse {

    private String code;
    private Messages messages;

    public ExceptionResponse(String code, Messages messages) {
        this.code = code;
        this.messages = messages;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }
}
