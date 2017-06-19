package com.wang.wys.model;

import com.wang.wys.util.CodecUtil;

import java.io.*;

/**
 * Created by Ryan on 17/6/4.
 */
public class RPCRequest extends Message implements Serializable{
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
        stream.writeByte(getType());
        stream.writeUTF(getValue());
        stream.flush();
        stream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static void main(String[] args) throws IOException {
        RPCRequest rpcRequest = new RPCRequest();
        rpcRequest.setValue("test content");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream((rpcRequest.getBytes()));
        ObjectInputStream stream = new ObjectInputStream(byteArrayInputStream);
        System.out.println(stream.readUTF());
    }

    public byte getType() {
        return MsgType.REQUEST.getValue();
    }
}
