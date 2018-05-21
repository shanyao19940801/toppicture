package com.amano.springBoot.controller.input;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DanmakuInput {
    @NotNull(message = "userid 不能唯为空")
    private Long userId;

    @NotNull(message = "imageDetailId 不能唯为空")
    private Long imageDetailId;

    @NotNull(message = "content 不能唯为空")
    private String content;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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
}
