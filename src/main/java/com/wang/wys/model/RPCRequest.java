package com.wang.wys.model;

import com.wang.wys.util.CodecUtil;

import java.io.*;

/**
 * Created by Ryan on 17/6/4.
 */
public class RPCRequest implements Serializable{
    String value;
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteArrayOutputStream);
        CodecUtil.writeMark(stream);
        CodecUtil.writeVersion(stream);
        stream.flush();
        stream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static void main(String[] args) throws IOException {
        RPCRequest rpcRequest = new RPCRequest();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream((rpcRequest.getBytes()));
        ObjectInputStream stream = new ObjectInputStream(byteArrayInputStream);
        byte[] bytes = new byte[3];
        stream.read(bytes);
        System.out.println(CodecUtil.checkMark(bytes));
        System.out.println(CodecUtil.checkVersion(stream.readByte()));
    }
}
