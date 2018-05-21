package com.amano.springBoot.service;

import com.amano.springBoot.controller.input.DanmakuInput;
import com.amano.springBoot.controller.output.Msg;
import com.amano.springBoot.entity.Danmaku;
import com.amano.springBoot.repo.DanmakuRepo;
import com.amano.springBoot.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DanmakuService {

    @Autowired
    DanmakuRepo danmakuRepol;

    public Msg comment (DanmakuInput input) {

        Danmaku danmaku = new Danmaku();
        danmaku.setContent(input.getContent());
        danmaku.setImageDetailId((input.getImageDetailId()));
        danmaku.setUserId(input.getUserId());

        danmakuRepol.save(danmaku);
        return ResultUtil.success();
    }
}
