package com.wang.wys.model;

/**
 * Created by wangyongshan on 17-6-19.
 */
public enum MsgType {
    REQUEST((byte)1),
    RESPONSE((byte)2);

    byte value;
    MsgType(byte value) {
        this.value = value;
    }
    public byte getValue() {
        return value;
    }

}
