package com.ddosd.facade.service;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadForm {

	public static final String key="fileUploadForm";
	
	private MultipartFile xmlFile;

	public MultipartFile getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(MultipartFile xmlFile) {
		this.xmlFile = xmlFile;
	}	
}
