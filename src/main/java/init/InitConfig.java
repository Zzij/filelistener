package init;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class InitConfig {
	
	private static Logger logger = Logger.getLogger(InitConfig.class.getName());
	
	public static String ADDRESS;
	
	
	public static boolean init() throws ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Element theContext = null, root = null;
		
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder db = factory.newDocumentBuilder();
	
		String sp = System.getProperty("file.separator");
		String filePath = "conf" + sp + "conf.xml";
		Document xmldoc = null;
		File file = null;
		try{
			file = new File(filePath);
			xmldoc = db.parse(file);
		}catch (Exception e) {
			logger.error(" Server Default Config File  Not Found !");
			return false;
		}
		
		root = xmldoc.getDocumentElement();
		
		theContext = (Element) selectSingleNode(
				"/config/variable[@name='address']",root);
		if(theContext != null)
			ADDRESS = theContext.getTextContent().trim();
		logger.info("Address is " + ADDRESS);
		
		logger.info("Config File Init Complete !");
		return true;
	}
	
	public static Node selectSingleNode(String express, Object source) {
		Node result = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			result = (Node) xpath
					.evaluate(express, source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public static void main(String[] args){
		System.out.println(System.currentTimeMillis());
	}
}
