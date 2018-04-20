package com.bmn.sns.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnsSubscriptionConfirmationModel implements Serializable {
	

	 /* "Type" : "SubscriptionConfirmation",
	  "MessageId" : "165545c9-2a5c-472c-8df2-7ff2be2b3b1b",
	  "Token" : "2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736",
	  "TopicArn" : "arn:aws:sns:us-west-2:123456789012:MyTopic",
	  "Message" : "You have chosen to subscribe to the topic arn:aws:sns:us-west-2:123456789012:MyTopic.\nTo confirm the subscription, visit the SubscribeURL included in this message.",
	  "SubscribeURL" : "https://sns.us-west-2.amazonaws.com/?Action=ConfirmSubscription&TopicArn=arn:aws:sns:us-west-2:123456789012:MyTopic&Token=2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736",
	  "Timestamp" : "2012-04-26T20:45:04.751Z",
	  "SignatureVersion" : "1",
	  "Signature" : "EXAMPLEpH+DcEwjAPg8O9mY8dReBSwksfg2S7WKQcikcNKWLQjwu6A4VbeS0QHVCkhRS7fUQvi2egU3N858fiTDN6bkkOxYDVrY0Ad8L10Hs3zH81mtnPk5uvvolIC1CXGu43obcgFxeL3khZl8IKvO61GWB6jI9b5+gLPoBc1Q=",
	  "SigningCertURL" : "https://sns.us-west-2.amazonaws.com/SimpleNotificationService-f3ecfb7224c7233fe7bb5f59f96de52f*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8827530940145752119L;
	@JsonProperty("Type")
	  private String Type;
	@JsonProperty("MessageId")
	  private String MessageId;
	@JsonProperty("Token")
	  private String Token;
	@JsonProperty("TopicArn")
	  private String TopicArn;
	@JsonProperty("Message")
	  private String Message;
	@JsonProperty("SubscribeURL")
	  private String SubscribeURL;
	@JsonProperty("Timestamp")
	  private String Timestamp;
	@JsonProperty("SignatureVersion")
	  private String SignatureVersion;
	@JsonProperty("Signature")
	  private String Signature;
	@JsonProperty("SigningCertURL")
	  private String SigningCertURL;
	@JsonProperty("UnsubscribeURL")
	  private String UnsubscribeURL;
	@JsonProperty("Subject")
	  private String Subject;
	  
	public String getUnsubscribeURL() {
		return UnsubscribeURL;
	}
	public void setUnsubscribeURL(String unsubscribeURL) {
		UnsubscribeURL = unsubscribeURL;
	}
	public String getSubject() {
		return Subject;
	}
	public void setSubject(String subject) {
		Subject = subject;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getMessageId() {
		return MessageId;
	}
	public void setMessageId(String messageId) {
		MessageId = messageId;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public String getTopicArn() {
		return TopicArn;
	}
	public void setTopicArn(String topicArn) {
		TopicArn = topicArn;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getSubscribeURL() {
		return SubscribeURL;
	}
	public void setSubscribeURL(String subscribeURL) {
		SubscribeURL = subscribeURL;
	}
	public String getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}
	public String getSignatureVersion() {
		return SignatureVersion;
	}
	public void setSignatureVersion(String signatureVersion) {
		SignatureVersion = signatureVersion;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
	public String getSigningCertURL() {
		return SigningCertURL;
	}
	public void setSigningCertURL(String signingCertURL) {
		SigningCertURL = signingCertURL;
	}
	  

}
