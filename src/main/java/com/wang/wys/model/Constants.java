package com.wang.wys.model;

/**
 * Created by Ryan on 17/6/17.
 */
public class Constants {
    public static byte[] MARK ;
    public static final byte CODEC_VERSION = 1;
    static {
        MARK = new byte[3];
        MARK[0] = 0x57;
        MARK[1] = 0x58;
        MARK[2] = 0x53;
    }
}
