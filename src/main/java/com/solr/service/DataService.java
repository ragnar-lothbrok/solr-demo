package com.solr.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solr.model.Catalog;
import com.solr.model.Vendor;

@Service
public class DataService {

	final static Logger logger = LoggerFactory.getLogger(DataService.class);

	@Autowired
	SolrServer solrServer;

	public Boolean addCatalog(Catalog catalog) {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			Field[] fields = catalog.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				doc.addField(field.getName(), field.get(catalog));
			}
			solrServer.add(doc);
			solrServer.commit();
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.TRUE;
	}

	public Boolean addVendor(Vendor vendor) {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			Field[] fields = vendor.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				doc.addField(field.getName(), field.get(vendor));
			}
			solrServer.add(doc);
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.TRUE;
	}

	public static HttpResponse getHttpResponse(String value)
			throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException {
		HttpClient httpClient = getHttpClient(1000);
		HttpPost post = new HttpPost("http://localhost:8983/solr/exclusively_catalog/update/json?wt=json&commit=true");
		StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(value));
		entity.setContentType("application/json");
		post.setEntity(entity);
		HttpResponse response = httpClient.execute(post);
		return response;
	}

	public static HttpClient getHttpClient(Integer timeout) {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
		CredentialsProvider provider = new BasicCredentialsProvider();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider)
				.setDefaultRequestConfig(config).build();
		return httpClient;
	}

	public List<SolrDocument> search(String queryParam) {
		List<SolrDocument> docList = new ArrayList<SolrDocument>();
		SolrQuery query = new SolrQuery();
		query.setQuery(queryParam);
		// query.addFilterQuery("cat:electronics", "store:amazon.com");
		// query.setFields("id", "price", "merchant", "cat", "store");
		query.setStart(0);
		query.set("defType", "edismax");

		try {
			QueryResponse response = solrServer.query(query);
			SolrDocumentList results = response.getResults();
			for (int i = 0; i < results.size(); ++i) {
				docList.add(results.get(i));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return docList;
	}
}
