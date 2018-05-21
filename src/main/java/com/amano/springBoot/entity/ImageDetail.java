package com.amano.springBoot.entity;

//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;

import javax.persistence.*;

@Entity
//@Getter
//@Setter
public class ImageDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "imageListId")
    private String imageListId;

    @Column(name = "url")
    private String url;

    @Column(name = "height")
    private String height;

    @Column(name = "width")
    private String width;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageListId() {
        return imageListId;
    }

    public void setImageListId(String imageListId) {
        this.imageListId = imageListId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
