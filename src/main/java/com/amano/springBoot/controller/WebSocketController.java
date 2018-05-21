package com.amano.springBoot.controller;

import com.amano.springBoot.controller.output.ChatItem;
import com.amano.springBoot.controller.output.WiselyResponse;
import com.amano.springBoot.service.WebSocketService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;

@Controller
public class WebSocketController {

    private static final Log logger = LogFactory.getLog(WebSocketController.class);

    @Resource
    WebSocketService webSocketService;

    @MessageMapping("/chat")//@MessageMapping和@RequestMapping功能类似，用于设置URL映射地址，浏览器向服务器发起请求，需要通过该地址。
    @SendTo("/topic/chatRoom")//如果服务器接受到了消息，就会对订阅了@SendTo括号中的地址传送消息。
    public WiselyResponse say(ChatItem message) throws Exception {
        String content = message.getContent();
        String name = message.getName();
//        List<String> users = Lists.newArrayList();
//        users.add(name);
//        webSocketService.sendMoreUsers(users, new WiselyResponse("123"));
//        webSocketService.send2Users(name, new WiselyResponse(content));
        return new WiselyResponse("chat",new ChatItem(name, content, System.currentTimeMillis()+"") );
    }
    @MessageMapping("/single")
    public WiselyResponse chat(ChatItem message) {
        String content = message.getContent();
        String name = message.getName();
        logger.info("name is " + name  + " content is " + content);
        webSocketService.send2Users(name, new WiselyResponse("single", message));
        return null;
    }
}
