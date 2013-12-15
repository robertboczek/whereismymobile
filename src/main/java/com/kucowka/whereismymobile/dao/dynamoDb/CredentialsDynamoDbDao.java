package com.kucowka.whereismymobile.dao.dynamoDb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.kucowka.whereismymobile.dao.CredentialsDao;
import com.kucowka.whereismymobile.models.Credentials;

@Component
public class CredentialsDynamoDbDao extends GenericDynamoDbDao<String, Credentials> 
	implements CredentialsDao {
	
	private static final Logger logger = Logger.getLogger(CredentialsDynamoDbDao.class);
	
	@Qualifier("memcached.client")
	@Autowired()
	private MemcachedClient memcached;

	@Override
	public void save(Credentials credentials) {
		super.save(credentials);
		if (memcached != null) {
			logger.info("Caching credentials for id: " + credentials.getEmail());
			List<Credentials> credentialsList = new ArrayList<Credentials>();
			credentialsList.add(credentials);
			cacheCredentials(credentials.getEmail(), credentialsList);
		}	
		
	}

	private void cacheCredentials(String key, List<Credentials> value) {
		memcached.set(key, 3600, value);
	}

	@SuppressWarnings("unchecked")
	public List<Credentials> getById(String id) {
		Object cachedObject = memcached.get(id);
		List<Credentials> list = null;
		if (cachedObject != null && cachedObject instanceof List) {
			list = (List<Credentials>) cachedObject;
			logger.info("Using cached version of credentials");
		}
		if (list == null) {
			list = super.getById(id);
			if (list != null) {
				cacheCredentials(id, list);
			}
		}
		
		return list;
	}

}
