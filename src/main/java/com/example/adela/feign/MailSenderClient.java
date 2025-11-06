package com.example.adela.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "adela-mail-sender") 
public interface MailSenderClient {

    @PostMapping("/mail/send")
    void sendMail(@RequestBody EmailRequest request);
}