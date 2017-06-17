package com.wang.wys.model;

import java.io.Serializable;

/**
 * Created by Ryan on 17/6/4.
 */
public class RPCResponse implements Serializable{
    String value;
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
