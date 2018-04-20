package com.bmn.sns.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Object {
	@JsonProperty("eTag")
	private String eTag;
	
	@JsonProperty("sequencer")
	private String sequencer;
	
	@JsonProperty("key")
	private String key;
	
	@JsonProperty("size")
	private String size;

	public String getETag() {
		return eTag;
	}

	public void setETag(String eTag) {
		this.eTag = eTag;
	}

	public String getSequencer() {
		return sequencer;
	}

	public void setSequencer(String sequencer) {
		this.sequencer = sequencer;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "ClassPojo [eTag = " + eTag + ", sequencer = " + sequencer + ", key = " + key + ", size = " + size + "]";
	}
}
