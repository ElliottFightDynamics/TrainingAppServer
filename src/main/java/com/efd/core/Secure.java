package com.efd.core;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by volodymyr on 17.06.17.
 */
public class Secure {

    private static final Logger logger = LoggerFactory.getLogger(Secure.class);

    public String generateToken() throws Exception {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return Arrays.toString(bytes);
    }

    public String sha256(String base) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(base.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean sendEmail(String newPassword, String email) throws Exception {
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
        } catch (Exception e) {
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            return false;
        }

    }

    public String generateNewPassword() throws Exception {
        //String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return RandomStringUtils.random( 8, characters );
    }

    public void throwException(String message, HttpServletResponse httpServletResponse) {
        JSONObject resultJson = new JSONObject();
        try {
            resultJson.put("exception",message);
            resultJson.put(Constants.KEY_SUCCESS,false);
            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
