package vn.picore.playtwitter.dto;

import java.io.Serializable;

public class ResponseData<T> implements Serializable {

	private static final long serialVersionUID = 3159038893123905511L;

	private int status;
	private String msg;
	private T data;

	public ResponseData() {

	}

	public ResponseData(int status, T data, String msg) {
		this.setStatus(status);
		this.setMsg(msg);
		this.setData(data);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
