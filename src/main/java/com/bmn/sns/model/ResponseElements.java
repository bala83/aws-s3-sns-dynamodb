package com.bmn.sns.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseElements {
	
	@JsonProperty("x-amz-request-id")
	private String x_amz_request_id;
	@JsonProperty("x-amz-id-2")
	private String x_amz_id_2;

	public String getX_amz_request_id() {
		return x_amz_request_id;
	}

	public void setX_amz_request_id(String x_amz_request_id) {
		this.x_amz_request_id = x_amz_request_id;
	}

	public String getX_amz_id_2() {
		return x_amz_id_2;
	}

	public void setX_amz_id_2(String x_amz_id_2) {
		this.x_amz_id_2 = x_amz_id_2;
	}

	@Override
	public String toString() {
		return "ClassPojo [x-amz-request-id = " + x_amz_request_id + ", x-amz-id-2 = " + x_amz_id_2 + "]";
	}
}
