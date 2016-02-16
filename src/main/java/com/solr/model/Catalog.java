package com.solr.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class Catalog {

	private String title;
	private String sku;
	private String itemName;
	private String category;
	private String vendor;
	private int vendorInventory;
	private float vendorPrice;
	private Long popularity;
	
	public static void main(String[] args) throws JsonProcessingException {
		
		Catalog catalog = new Catalog();
		catalog.setSku("12345ABC!@!@");
		catalog.setTitle(catalog.getSku());
		catalog.setItemName("Levis Jeans Denim");
		catalog.setCategory("Jeans Denim");
		catalog.setVendor("Batists");
		
		System.out.println(new ObjectMapper().writeValueAsString(catalog));
	}

}
