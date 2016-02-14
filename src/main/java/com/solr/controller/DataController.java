package com.solr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.solr.model.Catalog;
import com.solr.model.Vendor;
import com.solr.service.DataService;

@RestController
public class DataController {

	final static Logger logger = LoggerFactory.getLogger(DataController.class);

	@Autowired
	DataService dataService;

	@RequestMapping(value = "/catalog", produces = { "application/json" }, method = RequestMethod.POST)
	public Boolean addToSolr(@RequestBody Catalog catalog) {
		try {
			return dataService.addCatalog(catalog);
		} catch (Exception exception) {
			logger.error("Exception occured : " + exception);
		}
		return Boolean.FALSE;
	}

	@RequestMapping(value = "/vendor", produces = { "application/json" }, method = RequestMethod.POST)
	public Boolean addToSolr(@RequestBody Vendor vendor) {
		try {
			return dataService.addVendor(vendor);
		} catch (Exception exception) {
			logger.error("Exception occured : " + exception);
		}
		return Boolean.FALSE;
	}
	

}
