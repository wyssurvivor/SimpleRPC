package com.wang.wys.model;

import java.io.IOException;

/**
 * Created by wangyongshan on 17-6-19.
 */
public abstract class Message  {
    public abstract byte getType();
    public abstract byte[] getBytes() throws IOException;
}
