package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {
    private EmailServer mockEmailServer;  // Макет EmailServer
    private EmailService emailService;     // Тестований EmailService

    @BeforeEach
    void setUp() {
        // Ініціалізація макета EmailServer та EmailService перед кожним тестом
        mockEmailServer = mock(EmailServer.class);
        emailService = new EmailService(mockEmailServer);
    }

    @Test
    void testSendEmail_Success() throws Exception {
        // Тест для перевірки успішної відправки електронного листа
        String recipient = "test@example.com";
        String subject = "Test Subject";
        String message = "This is a test message";
        
        // Виконати метод sendEmail
        emailService.sendEmail(recipient, subject, message, null);

        // Перевірити, що метод send викликано з правильними параметрами
        verify(mockEmailServer).send(recipient, subject, message);
    }

    @Test
    void testSendEmail_Failure() throws Exception {
        // Тест для перевірки обробки помилок під час відправки електронного листа
        String recipient = "test@example.com";
        String subject = "Test Subject";
        String message = "This is a test message";

        // Налаштувати макет для генерації виключення
        doThrow(new Exception("Connection error")).when(mockEmailServer).send(recipient, subject, message);

        // Перевірити, що викидається виключення при виклику sendEmail
        Exception exception = assertThrows(Exception.class, () -> {
            emailService.sendEmail(recipient, subject, message, null);
        });

        // Перевірити, що повідомлення виключення є правильним
        assertEquals("Connection error", exception.getMessage());
    }

    @Test
    void testSendEmail_EmptyMessage() {
        // Тест для перевірки, що викидається IllegalArgumentException, якщо текст повідомлення порожній
        String recipient = "test@example.com";
        String subject = "Test Subject";
        String message = ""; // Порожнє повідомлення

        // Перевірити, що викидається IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendEmail(recipient, subject, message, null);
        });

        // Перевірити, що повідомлення виключення є правильним
        assertEquals("Message cannot be empty", exception.getMessage());
    }

    @Test
    void testSendEmail_WithAdditionalHeader() throws Exception {
        // Тест для перевірки, що додатковий заголовок встановлюється і лист відправляється
        String recipient = "test@example.com";
        String subject = "Test Subject";
        String message = "This is a test message";
        String additionalHeader = "X-Custom-Header: Value";

        // Виконати метод sendEmail з додатковим заголовком
        emailService.sendEmail(recipient, subject, message, additionalHeader);

        // Перевірити, що додатковий заголовок був встановлений
        verify(mockEmailServer).addHeader(additionalHeader);
        // Перевірити, що метод send викликано з правильними параметрами
        verify(mockEmailServer).send(recipient, subject, message);
    }
}
