/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.util.msg.index;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;

import com.rh.core.plug.search.RhIndex;
import com.rh.core.plug.search.client.RhSearchClient;
import com.rh.core.util.msg.Msg;
import com.rh.core.util.msg.TypeMsgListener;

/**
 * index save handler
 * @author liwei
 * 
 */
public class IndexListener extends TypeMsgListener {

    /** log */
    private static Log log = LogFactory.getLog(IndexListener.class);

    @Override
    public void init(String conf) {
        log.info("index listener start...");
        super.init(conf);
    }

    @Override
    protected void onTypeMsg(Msg msg) {
        log.debug("index listener got one message. " + msg);
        if (!(msg instanceof IndexMsg)) {
            log.error(" the message is not index message." + msg);
            return;
        }
        IndexMsg indexMsg = (IndexMsg) msg;

        try {
            RhSearchClient rsc = new RhSearchClient();
            if (indexMsg.getIndex().getAct() == RhIndex.ACT.ADD) {
                // add index
                rsc.addIndex(indexMsg.getIndex());
            } else if (indexMsg.getIndex().getAct() == RhIndex.ACT.DELETE) {
                // delete index
                rsc.delIndex(indexMsg.getIndex());
            } else {
                // not implemented
                log.warn("not implemented yet!");
            }
        } catch (IOException ie) {
            ie.printStackTrace();
            log.error("handling index msg error.", ie);
        } catch (SolrServerException sse) {
            sse.printStackTrace();
            log.error("handling index msg error.", sse);
        }

    }

}
