package com.crud.simplecrud.DTO;

import java.util.ArrayList;
import java.util.List;

public class ResponseDTO<T> {
    private String status;
    private List<String> messages = new ArrayList<>();
    private T entity;
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<String> getMessages() {
        return messages;
    }
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
    public T getEntity() {
        return entity;
    }
    public void setEntity(T entity) {
        this.entity = entity;
    }

    
    
}
