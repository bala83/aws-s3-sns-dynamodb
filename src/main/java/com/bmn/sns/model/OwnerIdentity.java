package com.bmn.sns.model;

public class OwnerIdentity {
	private String principalId;

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	@Override
	public String toString() {
		return "ClassPojo [principalId = " + principalId + "]";
	}
}
