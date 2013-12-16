package com.kucowka.whereismymobile.dao.dynamoDb;

import org.springframework.stereotype.Repository;

import com.kucowka.whereismymobile.dao.TimeBoundFeaturesDao;
import com.kucowka.whereismymobile.models.TimeBoundFeatures;

@Repository
public class TimeBoundFeaturesDynamoDbDao  extends GenericDynamoDbDao<String, TimeBoundFeatures> 
	implements TimeBoundFeaturesDao {

}
