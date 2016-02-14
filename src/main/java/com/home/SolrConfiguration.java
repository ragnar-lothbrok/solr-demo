package com.home;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;

import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.IOUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.solr.core.DefaultQueryParser;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FilterQuery;

import com.home.solr.analyser.EnglishKeywordAnalyzer;

@Configuration
public class SolrConfiguration {

	@Autowired
	ApplicationContext applicationContext;

	@Value("${spring.data.solr.host}")
	private String solrUrl;

	@Bean
	public SolrServer solrServer() throws MalformedURLException {
		SolrServer server = new LBHttpSolrServer(solrUrl.split(","));
		return server;
	}

	@Bean
	public SolrTemplate solrTemplate() throws MalformedURLException {
		SolrTemplate solrTemplate = new SolrTemplate(solrServer());
		solrTemplate.registerQueryParser(FilterQuery.class, new DefaultQueryParser());
		return solrTemplate;
	}

	@Bean
	public EnglishKeywordAnalyzer englishAnalyzer() throws IOException {
		return new EnglishKeywordAnalyzer(getStopWordsSet());
	}

	private CharArraySet getStopWordsSet() throws IOException {
		Reader reader = null;
		try {
			Resource resource = applicationContext.getResource("classpath:stopwords.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
			return WordlistLoader.getWordSet(inputStreamReader, new CharArraySet(16, true));
		} finally {
			IOUtils.close(reader);
		}
	}

}
