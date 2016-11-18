/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.util.msg.index;

import com.rh.core.base.Bean;
import com.rh.core.base.start.MsgLisLoader;
import com.rh.core.plug.search.RhIndex;
import com.rh.core.util.msg.Msg;

/**
 * index异步消息对象
 * 
 * @author liwei
 * 
 */
public class IndexMsg implements Msg {


	// index message
	private RhIndex indexMsg = null;

	/**
	 * build Index Async Message
	 * @param rhindex RhIndex
	 */
	public IndexMsg(RhIndex rhindex) {
		indexMsg = rhindex;
	}

	/**
	 * get index message
	 * 
	 * @return index msg
	 */
	public RhIndex getIndex() {
		return this.indexMsg;
	}

    @Override
    public String getName() {
        return MsgLisLoader.INDEX_MSG_TYPE;
    }

    @Override
    public String getType() {
        return MsgLisLoader.INDEX_MSG_TYPE;
    }

    @Override
    public Bean getBody() {
        return indexMsg;
    }
	
}
