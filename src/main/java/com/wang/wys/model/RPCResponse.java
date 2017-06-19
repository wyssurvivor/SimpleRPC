package com.wang.wys.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Ryan on 17/6/4.
 */
public class RPCResponse extends Message implements Serializable{

    String value;
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public byte getType() {
        return MsgType.RESPONSE.getValue();
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
}
