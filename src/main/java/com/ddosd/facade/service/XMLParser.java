package com.ddosd.facade.service;

import net.sf.json.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
	

	public static int parseXML(Document document){

		document.getDocumentElement().normalize();
		NodeList nList = document.getElementsByTagName("report");
		return nList.getLength();
		
	}
	
	private static int coutnInnerTags(Node node){
		
		Element element = (Element) node;
		int max = 0;
		int length = element.getChildNodes().getLength();
		if(length > 0){
			max = length;
			for (int i = 0; i < length; i++) {
				Node iNode = element.getChildNodes().item(i);
				Element element2 = (Element) iNode;
				if(element2.getChildNodes().getLength() > max){
					max = element2.getChildNodes().getLength();
				}
				int innerCount = coutnInnerTags(iNode);
				if(innerCount > max){
					max = innerCount;
				}
					
			}
		}		
		return max;
	}
}
