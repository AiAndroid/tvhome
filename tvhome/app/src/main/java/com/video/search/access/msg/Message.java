package com.video.search.access.msg;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 描述: 报文基类
 * 创建时间:2013-12-7 16:24
 *
 * @author zzh
 */
public class Message implements Serializable {

	protected String url;
	protected String version;
	
	protected JSONObject message;

	public JSONObject getMessage() {
		return message;
	}
	
	public void setMessage(JSONObject message) {
		this.message = message;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}