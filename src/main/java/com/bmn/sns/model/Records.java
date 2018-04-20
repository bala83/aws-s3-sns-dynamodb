package com.bmn.sns.model;

public class Records {
	private String eventSource;

	private UserIdentity userIdentity;

	private ResponseElements responseElements;

	private String eventName;

	private String eventVersion;

	private String eventTime;

	private S3 s3;

	private String awsRegion;

	private RequestParameters requestParameters;

	public String getEventSource() {
		return eventSource;
	}

	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}

	public UserIdentity getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(UserIdentity userIdentity) {
		this.userIdentity = userIdentity;
	}

	public ResponseElements getResponseElements() {
		return responseElements;
	}

	public void setResponseElements(ResponseElements responseElements) {
		this.responseElements = responseElements;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventVersion() {
		return eventVersion;
	}

	public void setEventVersion(String eventVersion) {
		this.eventVersion = eventVersion;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public S3 getS3() {
		return s3;
	}

	public void setS3(S3 s3) {
		this.s3 = s3;
	}

	public String getAwsRegion() {
		return awsRegion;
	}

	public void setAwsRegion(String awsRegion) {
		this.awsRegion = awsRegion;
	}

	public RequestParameters getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(RequestParameters requestParameters) {
		this.requestParameters = requestParameters;
	}

	@Override
	public String toString() {
		return "ClassPojo [eventSource = " + eventSource + ", userIdentity = " + userIdentity + ", responseElements = "
				+ responseElements + ", eventName = " + eventName + ", eventVersion = " + eventVersion
				+ ", eventTime = " + eventTime + ", s3 = " + s3 + ", awsRegion = " + awsRegion
				+ ", requestParameters = " + requestParameters + "]";
	}
}
