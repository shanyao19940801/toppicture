package com.amano.springBoot.controller.enumConstant;

import java.util.ArrayList;
import java.util.List;

public enum DanmakuStatus {

    STYLE1("样式1"),STYLE2("样式2");

    String desc;

    DanmakuStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static List<DanmakuStatus> getAllList(){
        ArrayList<DanmakuStatus> list = new ArrayList<>();
        list.add(STYLE1);
        list.add(STYLE2);
        return list;
    }
}
