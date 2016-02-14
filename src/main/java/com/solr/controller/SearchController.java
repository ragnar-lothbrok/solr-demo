package com.solr.controller;

import java.util.Collections;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solr.service.DataService;

@RestController
public class SearchController {

	final static Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	DataService dataService;

	@RequestMapping(value = "/search", produces = { "application/json" }, method = RequestMethod.GET)
	public List<SolrDocument> search(@RequestParam(required=false)String query) {
		try {
			return dataService.search(query);
		} catch (Exception exception) {
			logger.error("Exception occured : " + exception);
		}
		return Collections.emptyList();
	}
	
}
