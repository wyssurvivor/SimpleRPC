package com.wang.wys.model;

import com.wang.wys.util.CodecUtil;

import java.io.*;

/**
 * Created by Ryan on 17/6/4.
 */
public class RPCRequest extends Message implements Serializable{
    public int getVal1() {
        return val1;
    }

    public void setVal1(int val1) {
        this.val1 = val1;
    }

    public int getVal2() {
        return val2;
    }

    public void setVal2(int val2) {
        this.val2 = val2;
    }

    int val1;
    int val2;

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteArrayOutputStream);
        stream.writeByte(getType());
        stream.writeInt(val1);
        stream.writeInt(val2);
        stream.flush();
        stream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static void main(String[] args) throws IOException {
//        RPCRequest rpcRequest = new RPCRequest();
//        rpcRequest.setValue("test content");
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream((rpcRequest.getBytes()));
//        ObjectInputStream stream = new ObjectInputStream(byteArrayInputStream);
//        System.out.println(stream.readUTF());
    }

    public byte getType() {
        return MsgType.REQUEST.getValue();
    }
}
