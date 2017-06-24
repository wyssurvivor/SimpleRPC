package com.wang.wys.model;

import com.wang.wys.util.CodecUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 17/6/4.
 */
public class RPCRequest extends Message implements Serializable{
    Class ifaceClass;
    String funcName;
    Class[] paramTypes ;
    Object[] params ;

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class getIfaceClass() {
        return ifaceClass;
    }

    public void setIfaceClass(Class ifaceClass) {
        this.ifaceClass = ifaceClass;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }


    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteArrayOutputStream);
        stream.writeObject(this);
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
