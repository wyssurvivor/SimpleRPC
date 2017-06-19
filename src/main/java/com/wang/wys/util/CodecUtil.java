package com.wang.wys.util;

import com.wang.wys.model.Constants;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Ryan on 17/6/18.
 */
public class CodecUtil {

    public static void writeMark(ByteBuf byteBuf) throws IOException {
        byteBuf.writeBytes(Constants.MARK);
    }

    public static void writeVersion(ByteBuf byteBuf) throws IOException {
        byteBuf.writeByte(Constants.CODEC_VERSION);
    }

    public static void writeHead(ByteBuf byteBuf, int effectiveLen) throws IOException {
        writeMark(byteBuf); //写入markword
        writeVersion(byteBuf); //写入编码格式版本
        byteBuf.writeInt(effectiveLen); //写入有效数据长度
    }

    public static boolean checkMark(byte[] bytes) {
        for(int i=0;i< Constants.MARK.length;i++) {
            if(bytes[i] != Constants.MARK[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkVersion(byte version) {
        if(version == Constants.CODEC_VERSION) {
            return true;
        }

        return false;
    }

    public static boolean preCheck(ByteBuf byteBuf) {
        int len = byteBuf.readableBytes();
        if (len < 8) { // mark 3bytes, version 1byte, data length 4bytes
            return false;
        }

        byte[] markeBytes = new byte[3];
        byteBuf.readBytes(markeBytes);
        if (!CodecUtil.checkMark(markeBytes)) {
            return false;
        }

        if (!CodecUtil.checkVersion(byteBuf.readByte())) {
            return false;
        }

        return true;
    }
}
