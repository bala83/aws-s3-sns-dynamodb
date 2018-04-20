package com.bmn.sns.model;

public class Bucket {
	private String name;

	private String arn;

	private OwnerIdentity ownerIdentity;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArn() {
		return arn;
	}

	public void setArn(String arn) {
		this.arn = arn;
	}

	public OwnerIdentity getOwnerIdentity() {
		return ownerIdentity;
	}

	public void setOwnerIdentity(OwnerIdentity ownerIdentity) {
		this.ownerIdentity = ownerIdentity;
	}

	@Override
	public String toString() {
		return "ClassPojo [name = " + name + ", arn = " + arn + ", ownerIdentity = " + ownerIdentity + "]";
	}
}
