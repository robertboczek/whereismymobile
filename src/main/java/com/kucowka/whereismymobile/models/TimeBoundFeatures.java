package com.kucowka.whereismymobile.models;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="TimeBoundFeatures")
public class TimeBoundFeatures extends Entity {

	private String id;
	
	private Long expireTime;
	
	private TimeBoundFeaturesType featureType;
	
	private String featureContent;
	
	private boolean valid;

	@DynamoDBHashKey(attributeName="Id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute
	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	@DynamoDBAttribute
	public TimeBoundFeaturesType getFeatureType() {
		return featureType;
	}

	public void setFeatureType(TimeBoundFeaturesType featureType) {
		this.featureType = featureType;
	}

	@DynamoDBAttribute
	public String getFeatureContent() {
		return featureContent;
	}

	public void setFeatureContent(String featureContent) {
		this.featureContent = featureContent;
	}

	@DynamoDBAttribute
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "TimeBoundFeatures [id=" + id + ", expireTime=" + expireTime
				+ ", featureType=" + featureType + ", featureContent="
				+ featureContent + ", valid=" + valid + "]";
	}
}
