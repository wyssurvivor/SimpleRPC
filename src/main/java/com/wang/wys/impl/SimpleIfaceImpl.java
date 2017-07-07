package com.wang.wys.impl;

import com.wang.wys.interfaces.SimpleIface;

/**
 * Created by Ryan on 17/6/24.
 */
public class SimpleIfaceImpl implements SimpleIface {
    public int add(int val1, int val2) throws Exception {
        Thread.currentThread().sleep(5000);
        return val1 + val2;
    }

    public void close() {

    }
}
