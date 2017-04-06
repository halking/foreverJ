package com.hal.mybatis;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MybatisGenetatorMain {
    
	private static String RESOURCE_PATH = "\\src\\test\\resources\\";
	private static String XML_PATH = "mybatis_generator.xml";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("-----------------------statring code----------------------");
//		generatorCode(ROOT_PATH+XML_PATH);
//		String path1 = MybatisGenetatorMain.class.getResource("/").toString();
		String root = System.getProperty("user.dir");
		System.out.println("path1:"+root);
		System.out.println(root+RESOURCE_PATH+XML_PATH);
//		generatorCode(root+RESOURCE_PATH+XML_PATH);
		System.out.println("-----------------------ending code----------------------");
	}
	
	public static void generatorCode(String url) throws Exception{
		List<String> warning = new ArrayList<String>();
		List<String> tableNameList = new ArrayList<String>();
 		boolean overWrite = true;
		File configFile = new File(url);
		ConfigurationParser factory = new ConfigurationParser(warning);
		Configuration config = factory.parseConfiguration(configFile);
	  	Document document = config.toDocument();
	  	XmlElement xmlElement = document.getRootElement();
	  	List<Element> elementList = xmlElement.getElements();
	  	for (Element element : elementList) {
	  		XmlElement xml = (XmlElement) element;
	  		List<Element> contextElementList = xml.getElements();
	  		for (Element contextElement : contextElementList) {
	  			XmlElement contextXmlElement = (XmlElement) contextElement;
	  			if ("table".equalsIgnoreCase(contextXmlElement.getName())) {
	  				List<Attribute> attributes = contextXmlElement.getAttributes();
	  				for (Attribute attribute : attributes) {
	  					if ("tableName".equalsIgnoreCase(attribute.getName())) {
	  						tableNameList.add(attribute.getValue());
	  					}
	  				}
	  			}
	  		}
		}
		DefaultShellCallback callback = new DefaultShellCallback(overWrite);
		MyBatisGenerator genetator = new MyBatisGenerator(config,callback,warning);
		genetator.generate(null);
	}
}
