package com.rh.bn.linktransfer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 百年人寿项目旧OA相关链接处理接口类
 * @author tanyh 20151208
 *
 */
public interface LinkTransfer {

    public void transferByRequest(HttpServletRequest request, HttpServletResponse response);
}
