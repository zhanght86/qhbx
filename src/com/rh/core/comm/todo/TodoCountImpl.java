package com.rh.core.comm.todo;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import com.rh.core.base.Bean;
import com.rh.core.util.JsonUtils;

/**
 * 通过HttpClient获取桌面图标角标信息
 * @author chensheng
 */
public class TodoCountImpl implements ITodoCount {
    
    /** log */
    private static Log log = LogFactory.getLog(TodoCountImpl.class);

    @Override
    public Bean getTodo(Bean param) {
        return this.doPost(param.getStr("URL"));
    }

    /**
     * 获取待办条数
     * @param url 请求地址
     * @return 返回
     */
    private Bean doPost(String url) {
        Bean outBean = new Bean();
        HttpClient client = new DefaultHttpClient();
        // 请求超时1秒
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 1000);
        // 等待返回超时2秒
        HttpConnectionParams.setSoTimeout(client.getParams(), 2000);
        HttpPost post = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(post);
            String resStr = EntityUtils.toString(response.getEntity());
            outBean.set("TODO_COUNT", JsonUtils.toBean(resStr).getInt("TODO_COUNT"));
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
            outBean.set("TODO_COUNT", 0);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            outBean.set("TODO_COUNT", 0);
        }
        return outBean;
    }

}
