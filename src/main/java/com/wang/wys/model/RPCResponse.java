package com.wang.wys.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Ryan on 17/6/4.
 */
public class RPCResponse extends Message implements Serializable{

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    private Object result;

    public byte getType() {
        return MsgType.RESPONSE.getValue();
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteArrayOutputStream);
        stream.writeObject(this);
        stream.flush();
        stream.close();

        return byteArrayOutputStream.toByteArray();
    }
}
