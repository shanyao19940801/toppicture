package com.amano.springBoot.service;

import com.amano.springBoot.controller.output.WiselyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 广播
     * @param msg
     */
    public void sendMsg(WiselyResponse msg){
        template.convertAndSend("/topic/version", msg);
    }
    /**
     * 聊天室
     * @param msg
     */
    public void chatRoom(WiselyResponse msg){
        template.convertAndSend("/topic/chatRoom", msg);
    }

    /**
     * 发送指定用户 一对一
     * @param users
     * @param msg
     */
    public void send2Users(String users, WiselyResponse msg) {
        template.convertAndSendToUser(users, "/msg", msg);
    }

    /**
     * 聊天室(多对多)
     * @param users
     * @param msg
     */
    public void sendMoreUsers(List<String> users, WiselyResponse msg) {
        users.forEach(userName -> {
            template.convertAndSendToUser(userName, "/msg", msg);
        });
    }
}
