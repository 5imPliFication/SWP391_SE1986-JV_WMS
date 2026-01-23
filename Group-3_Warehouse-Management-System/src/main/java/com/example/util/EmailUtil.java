package com.example.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {
    // Tài khoản gửi mail
    private static final String EMAIL_FROM = "nguyentungtpat@gmail.com";
    private static final String APP_PASSWORD = "mglx ueca oslh dqxo";

    public static void sendEmail(String emailTo, String newPassword) {
        // 1. Cấu hình SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // 2. Xác thực
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, APP_PASSWORD);
            }
        };

        // 3. Tạo session
        Session session = Session.getInstance(props, authenticator);

        try {
            // 4. Tạo mail
            MimeMessage message = new MimeMessage(session);

            message.setFrom(EMAIL_FROM);
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailTo)
            );
            message.setSubject("Reset mật khẩu", "UTF-8");

            String content = """
                    <h3>Xin chào,</h3>
                    <p>Mật khẩu mới:</p>
                    <b>%s</b>
                    <p>Vui lòng đăng nhập và đổi mật khẩu ngay.</p>
                    """.formatted(newPassword);

            message.setContent(content, "text/html; charset=UTF-8");

            // 5. Gửi mail
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sent message failed....");
        }
    }
}
