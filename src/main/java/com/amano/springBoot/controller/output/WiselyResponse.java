package com.amano.springBoot.controller.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
//@AllArgsConstructor
public class WiselyResponse {
    public WiselyResponse(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    private String type;

    private Object data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
