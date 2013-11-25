package com.kucowka.whereismymobile.dao.dynamoDb;

import org.springframework.stereotype.Component;

import com.kucowka.whereismymobile.dao.CredentialsDao;
import com.kucowka.whereismymobile.models.Credentials;

@Component
public class CredentialsDynamoDbDao extends GenericDynamoDbDao<String, Credentials> implements CredentialsDao {

}
