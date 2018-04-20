package com.bmn.sns.model;

public class RequestParameters {
	private String sourceIPAddress;

	public String getSourceIPAddress() {
		return sourceIPAddress;
	}

	public void setSourceIPAddress(String sourceIPAddress) {
		this.sourceIPAddress = sourceIPAddress;
	}

	@Override
	public String toString() {
		return "ClassPojo [sourceIPAddress = " + sourceIPAddress + "]";
	}
}
