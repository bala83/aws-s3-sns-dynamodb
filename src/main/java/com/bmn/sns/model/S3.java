package com.bmn.sns.model;

public class S3 {
	private Bucket bucket;

	private Object object;

	private String configurationId;

	private String s3SchemaVersion;

	public Bucket getBucket() {
		return bucket;
	}

	public void setBucket(Bucket bucket) {
		this.bucket = bucket;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(String configurationId) {
		this.configurationId = configurationId;
	}

	public String getS3SchemaVersion() {
		return s3SchemaVersion;
	}

	public void setS3SchemaVersion(String s3SchemaVersion) {
		this.s3SchemaVersion = s3SchemaVersion;
	}

	@Override
	public String toString() {
		return "ClassPojo [bucket = " + bucket + ", object = " + object + ", configurationId = " + configurationId
				+ ", s3SchemaVersion = " + s3SchemaVersion + "]";
	}
}
