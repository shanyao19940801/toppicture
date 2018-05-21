package com.amano.springBoot.util;

import com.amano.springBoot.controller.output.ChatItem;
import com.amano.springBoot.controller.output.WiselyResponse;
import com.amano.springBoot.service.WebSocketService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

@Component
public class PushUtil {

    private static final Log logger = LogFactory.getLog(PushUtil.class);
    @Resource
    WebSocketService webSocketService;

    @Scheduled(cron = "${Scheduled.pushUtil.time}")
    public void send(){
        HashMap map = new HashMap();
        map.put("date",System.currentTimeMillis());
        String date = String.valueOf(System.currentTimeMillis());
        logger.info("job运行中");
        webSocketService.chatRoom(new WiselyResponse("chat",new ChatItem("job","每分钟一次",date)));
    }

}
