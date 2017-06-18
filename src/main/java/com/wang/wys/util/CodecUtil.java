package com.wang.wys.util;

import com.wang.wys.model.Constants;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Ryan on 17/6/18.
 */
public class CodecUtil {

    public static void writeMark(ObjectOutputStream stream) throws IOException {
        stream.writeByte(Constants.MARK[0]);
        stream.writeByte(Constants.MARK[1]);
        stream.writeByte(Constants.MARK[2]);
    }

    public static void writeVersion(ObjectOutputStream stream) throws IOException {
        stream.writeByte(Constants.CODEC_VERSION);
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
}
