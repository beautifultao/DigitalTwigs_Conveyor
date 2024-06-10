package com.cumt.data.entity;

public enum MessageCode {
    GET_POINT((byte) 0),
    GET_DATA((byte) 1),
    GET_PACKETS((byte) 2);

    private final byte code;

    MessageCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }
}