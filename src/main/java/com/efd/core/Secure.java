package com.efd.core;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Created by volodymyr on 17.06.17.
 */
public class Secure {

    public String generateToken() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return Arrays.toString(bytes);
    }

    public String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean sendEmail(String newPassword, String email) throws IOException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.properties");
        properties.load(inputStream);
        String username = properties.getProperty("mail.username");
        String password = properties.getProperty("mail.password");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("StrikeTec"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("New Password from StrikeTec");
            message.setText("New password is - " + newPassword);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            //throw new RuntimeException(e);
            return false;
        }

    }

    public String generateNewPasswor() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        return RandomStringUtils.random( 8, characters );
    }

}
