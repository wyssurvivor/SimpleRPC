package com.wang.wys.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Ryan on 17/6/4.
 */
public class RPCResponse extends Message implements Serializable{

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    int result;

    public byte getType() {
        return MsgType.RESPONSE.getValue();
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteArrayOutputStream);
        stream.writeByte(getType());
        stream.writeInt(result);
        stream.flush();
        stream.close();

        return byteArrayOutputStream.toByteArray();
    }
}
