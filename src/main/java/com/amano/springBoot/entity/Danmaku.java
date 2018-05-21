package com.amano.springBoot.entity;

import com.amano.springBoot.common.entity.BaseEntity;
import com.amano.springBoot.controller.enumConstant.DanmakuStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

//@Getter
//@Setter
@Entity
public class Danmaku extends BaseEntity {


    private Long imageDetailId;

    private String content;

    private Long userId;

    private DanmakuStatus danmakuStatus;

    public Long getImageDetailId() {
        return imageDetailId;
    }

    public void setImageDetailId(Long imageDetailId) {
        this.imageDetailId = imageDetailId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public DanmakuStatus getDanmakuStatus() {
        return danmakuStatus;
    }

    public void setDanmakuStatus(DanmakuStatus danmakuStatus) {
        this.danmakuStatus = danmakuStatus;
    }
}
