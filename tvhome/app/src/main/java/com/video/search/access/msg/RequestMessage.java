package com.video.search.access.msg;

import org.json.JSONObject;

/**
 * 描述: 请求报文
 * 创建时间:2013-12-7 下午5:54
 *
 * @author zzh
 */
public class RequestMessage extends Message
{

	public RequestMessage() {
		message = new JSONObject();
	}

	public RequestMessage(String url) {
		this.message = new JSONObject();
		this.url = url;
	}

}
