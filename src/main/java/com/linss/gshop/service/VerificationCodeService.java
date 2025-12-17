package com.linss.gshop.service;

import com.linss.gshop.entity.VerificationCode;
import com.linss.gshop.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final int CODE_LENGTH = 5;
    private static final long EXPIRATION_TIME_MINUTES = 10;
    private static final long SEND_INTERVAL_MINUTES = 1;

    public String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (random.nextBoolean()) {
                // 添加字母
                char c = (char) (random.nextInt(26) + 'A');
                sb.append(c);
            } else {
                // 添加数字
                sb.append(random.nextInt(10));
            }
        }
        return sb.toString();
    }

    public void sendVerificationCode(String email) {
        Optional<VerificationCode> latestCode = verificationCodeRepository.findTopByEmailOrderByCreatedAtDesc(email);
        if (latestCode.isPresent() && new Date().getTime() - latestCode.get().getCreatedAt().getTime() < TimeUnit.MINUTES.toMillis(SEND_INTERVAL_MINUTES)) {
            throw new RuntimeException("验证码发送过于频繁，请稍后再试");
        }

        String code = generateCode();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setCreatedAt(new Date());
        verificationCode.setExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(EXPIRATION_TIME_MINUTES)));
        verificationCodeRepository.save(verificationCode);

        sendEmail(email, code);
    }

    public boolean verifyCode(String email, String code) {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByEmailAndCode(email, code);
        if (verificationCodeOptional.isPresent()) {
            VerificationCode verificationCode = verificationCodeOptional.get();
            if (new Date().getTime() <= verificationCode.getExpiresAt().getTime()) {
                verificationCodeRepository.delete(verificationCode);
                return true;
            } else {
                verificationCodeRepository.delete(verificationCode);
                throw new RuntimeException("验证码已过期");
            }
        } else {
            throw new RuntimeException("验证码错误");
        }
    }

    private void sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(fromEmail); // 设置发件人地址
        message.setSubject("G-shop：验证码");
        message.setText("您的验证码是: " + code + "，请在10分钟内使用。");
        mailSender.send(message);
    }
}
