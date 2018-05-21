package com.amano.springBoot.entity;

import com.amano.springBoot.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Love extends BaseEntity {


    private Long userId;

    private String imageListId;

    private Boolean isLove;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImageListId() {
        return imageListId;
    }

    public void setImageListId(String imageListId) {
        this.imageListId = imageListId;
    }

    public Boolean getLove() {
        return isLove;
    }

    public void setLove(Boolean love) {
        isLove = love;
    }
}
