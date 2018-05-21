package com.amano.springBoot.entity;

//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;

import javax.persistence.*;

@Entity
//@Getter
//@Setter
//@AllArgsConstructor
@Table(name = "imageList")
public class ImageList {
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "type")
    private Integer type;

    @Column(name = "title")
    private String title;

    @Column(name = "uploadDt")
    private String uploadDt;

    @Column(name = "headImage")
    private String headImage;

    @Column(name = "height")
    private String height;

    @Column(name = "width")
    private String width;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploadDt() {
        return uploadDt;
    }

    public void setUploadDt(String uploadDt) {
        this.uploadDt = uploadDt;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public ImageList(Integer type, String title, String uploadDt, String headImage, String height, String width) {
        this.type = type;
        this.title = title;
        this.uploadDt = uploadDt;
        this.headImage = headImage;
        this.height = height;
        this.width = width;
    }

    public ImageList() {
    }
}
