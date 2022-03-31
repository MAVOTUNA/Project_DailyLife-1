package com.DailyLife.service;

import com.DailyLife.Sha256;
import com.DailyLife.dto.User;
import com.DailyLife.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService{

    private final JavaMailSender mailSender;
    private final UserMapper userMapper;
    private static long sequence = 0L;

    public String emailSend(String userEmail) {
        Random rd = new Random();

        StringBuilder authKey = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            authKey.append(rd.nextInt(10));
        }

        System.out.println("userEmail = " + userEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("인증메일입니다.");
        message.setText(authKey.toString());

//        mailSender.send(message);
        return authKey.toString();
    }

    public int addUser(User user) throws NoSuchAlgorithmException {
        Sha256 encrypt = new Sha256();
        String cryptogram = encrypt.encrypt(user.getUserPasswordCheck());
        user.setUserPassword(cryptogram);
        user.setUserNum(++sequence);
        log.info("암호화 : "+cryptogram);
       return userMapper.addUser(user);
    }

    public int login(User user){
        return userMapper.login(user);
    }
    public void logout(HttpServletResponse response) throws  Exception{
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out =response.getWriter();
        out.println("<script");
        out.println("location.href=document.referrer;");
        out.println("</script");
        out.close();

    }
    public String findByEmail(String email) {

        return userMapper.findByEmail(email).orElse("fail");

    }

    public int CheckById(String userId) {

        return userMapper.CheckById(userId);

    }
}
