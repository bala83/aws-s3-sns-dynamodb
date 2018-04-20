package com.bmn.sns.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnsResult
{
	
	@JsonProperty("Records")
    private Records[] Records;

    public Records[] getRecords ()
    {
        return Records;
    }

    public void setRecords (Records[] Records)
    {
        this.Records = Records;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Records = "+Records+"]";
    }
}
	
