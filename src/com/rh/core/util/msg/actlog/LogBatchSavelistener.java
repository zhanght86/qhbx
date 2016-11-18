/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.util.msg.actlog;

import com.rh.core.util.msg.BaseBatchSaveListener;

/**
 * log batch save handler
 * @author liwei
 * 
 */
public class LogBatchSavelistener extends BaseBatchSaveListener {

    /** service id */
    private static final String SERV_ID = "SY_SERV_LOG_ACT";

    @Override
    protected String getServId() {
        return SERV_ID;
    }


}
