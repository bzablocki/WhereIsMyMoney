package com.bfwg.rest;

import lombok.Getter;

public enum StyleKeyEnum {
    LEFT("left"),
    TOP("top");

    @Getter
    private String key;

    StyleKeyEnum(String key) {
        this.key = key;
    }
}
