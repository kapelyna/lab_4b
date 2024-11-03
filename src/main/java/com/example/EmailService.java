package com.example;

public class EmailService {
    private EmailServer emailServer;

    public EmailService(EmailServer emailServer) {
        this.emailServer = emailServer;
    }

    public void sendEmail(String recipient, String subject, String message, String additionalHeader) throws Exception {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        // Set additional headers if provided
        if (additionalHeader != null) {
            emailServer.addHeader(additionalHeader);
        }

        // Attempt to send email
        emailServer.send(recipient, subject, message);
    }
}
