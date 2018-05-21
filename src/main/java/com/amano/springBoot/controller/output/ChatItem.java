package com.amano.springBoot.controller.output;


public class ChatItem {
    private String name;
    private String content;
    private String timestamp;

    public ChatItem(String name, String content, String timestamp) {
        this.name = name;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
