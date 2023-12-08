package cn.com.liurz.flow.vo;

public class ResponseVo {

	private String status;

	private String message;

	private String errorDetail;

	private Object items;

	private String proccessStatus = "1";// 1表示流程进行中 0流程介素

	public String getProccessStatus() {
		return proccessStatus;
	}

	public void setProccessStatus(String proccessStatus) {
		this.proccessStatus = proccessStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}

	public Object getItems() {
		return items;
	}

	public void setItems(Object items) {
		this.items = items;
	}
}
