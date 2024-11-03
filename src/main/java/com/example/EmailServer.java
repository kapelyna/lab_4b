package com.example;

public interface EmailServer {
    void send(String recipient, String subject, String message) throws Exception;
    void addHeader(String header);
}
