/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.util.msg;

import com.rh.core.base.Bean;

/**
 * @author liwei
 * 
 */
public class CommonMsg extends Bean implements Msg {

    /**
     * version uid
     */
    private static final long serialVersionUID = -4117740681743867448L;
    
    private String type = null;

    /**
     * 构造消息
     * @param type - msg type
     */
    public CommonMsg(String type) {
        this.type = type;
    }

    /**
     * 构造消息
     * @param bean - bean
     * @param type - msg type
     */
    public CommonMsg(Bean bean, String type) {
        copyFrom(bean);
        this.type = type;
    }

    @Override
    public String getName() {
        return this.type;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Bean getBody() {
        return this;
    }

}
