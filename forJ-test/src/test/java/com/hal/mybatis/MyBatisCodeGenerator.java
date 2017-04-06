package com.hal.mybatis;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * 
 * 
 * @warnings 生成时需本地文件夹存在
 * @author Adminstrator
 *
 */
public class MyBatisCodeGenerator {
	private final static String PROJ_PATH = "C:\\Mayi\\common\\workspace\\mayi-pos";
	private final static String PATH = PROJ_PATH + "\\mayi-pos-core\\src\\test\\resources";
	//E:\\work\\mayi\\小蚂蚁项目\\10_SRC\\trunk\\pos\\mayi-pos\\mayi-pos-core\\src\\test\\resources\\
	/**
	 * 
	* @Title: main
	* @Description: http://generator.sturgeon.mopaas.com/index.html
	* @param args
	* @throws Exception
	* @author 
	 */
	public static void main(String[] args) throws Exception{
		System.out.println("+++++++++generate begin++++++++++");
		//采购
	  	//genProcureMapper();
	  	//系统
	  	//genSystemMapper();
		//门店
//		genShopMapper();
		//商品
		genProductMapper();
		//员工权限
		//genAuthorityMapper();
		//订单
//		genOrderMapper();
		//财务
		//genFinanceMapper();
	  	System.out.println("+++++++++generate end+++++++++++");
	}
	protected static void genAuthorityMapper() throws Exception{
		String authorityReaderUrl = PATH + "/MBG_authority_reader_configuration.xml";
		String authorityWriterUrl = PATH + "/MBG_authority_writer_configuration.xml";
		genMyBatisMapper(authorityReaderUrl);
		genMyBatisMapper(authorityWriterUrl);
	}
	protected static void genProductMapper() throws Exception{
		String productReaderUrl = "D:/halSVNWork/10_SRC/trunk/pos/mayi-pos/mayi-pos-core/src/test/resources/MBG_product_reader_configuration.xml";
		String productWriterUrl = "D:/halSVNWork/10_SRC/trunk/pos/mayi-pos/mayi-pos-core/src/test/resources/MBG_product_writer_configuration.xml";
		genMyBatisMapper(productWriterUrl);
		genMyBatisMapper(productReaderUrl);
	}
	//财务
	protected static void genFinanceMapper() throws Exception{
//		String financeReaderUrl = "D:/ant_work/SVN_dir/小蚂蚁项目/10_SRC/trunk/pos/mayi-pos/mayi-pos-core/src/test/resources/MBG_finance_reader_configuration.xml";
		String financeWriterUrl = "D:/ant_work/SVN_dir/小蚂蚁项目/10_SRC/trunk/pos/mayi-pos/mayi-pos-core/src/test/resources/MBG_finance_writer_configuration.xml";
		genMyBatisMapper(financeWriterUrl);
//		genMyBatisMapper(financeReaderUrl);
	}
	//采购
	protected static void genProcureMapper() throws Exception{
		String procureWriterUrl = "D:/halSVNWork/10_SRC/trunk/pos/mayi-pos/mayi-pos-core/src/test/resources/MBG_procure_writer_configuration.xml";
		String procureReaderUrl = "D:/halSVNWork/10_SRC/trunk/pos/mayi-pos/mayi-pos-core/src/test/resources/MBG_procure_reader_configuration.xml";
		genMyBatisMapper(procureWriterUrl);
	  	genMyBatisMapper(procureReaderUrl);
	}
	// system
	protected static void genSystemMapper() throws Exception{
		String systemWriterUrl = PATH + "/MBG_system_writer_configuration.xml";
		String systemReaderUrl = PATH + "/MBG_system_reader_configuration.xml";
		genMyBatisMapper(systemWriterUrl);
	  	genMyBatisMapper(systemReaderUrl);
	}
	
	// Shop
	protected static void genShopMapper() throws Exception{
		String systemWriterUrl = PATH + "/MBG_shop_writer_configuration.xml";
		String systemReaderUrl = PATH + "/MBG_shop_reader_configuration.xml";
		genMyBatisMapper(systemWriterUrl);
	  	genMyBatisMapper(systemReaderUrl);
	}

	
	// Order
	protected static void genOrderMapper() throws Exception{
		String systemWriterUrl = PATH + "/MBG_order_writer_configuration.xml";
		String systemReaderUrl = PATH + "/MBG_order_reader_configuration.xml";
		genMyBatisMapper(systemWriterUrl);
	  	genMyBatisMapper(systemReaderUrl);
	}

	private static void genMyBatisMapper(String url)
			throws Exception {
		List<String> warnings = new ArrayList<String>();
		List<String> tableNameList = new ArrayList<String>();
	  	boolean overwrite = true;
		File configFile = new File(url);
	  	ConfigurationParser confParser = new ConfigurationParser(warnings);
	  	Configuration config = confParser.parseConfiguration(configFile);
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
	  	DefaultShellCallback callback = new DefaultShellCallback(overwrite);
	  	MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
	  	myBatisGenerator.generate(null);
	  	
	  	// parse generated file
	  	//parseGeneratedFile2Project(url, tableNameList, type);
	}
	

}
