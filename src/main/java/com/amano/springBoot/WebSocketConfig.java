package com.amano.springBoot;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //服务端发送消息给客户端的域,多个用逗号隔开
        config.enableSimpleBroker("/topic", "/user");
        //定义websoket前缀
        config.setApplicationDestinationPrefixes("/ws");
        //定义一对一推送的时候前缀
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个Stomp的节点（endpoint）,并指定使用SockJS协议。
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }
}