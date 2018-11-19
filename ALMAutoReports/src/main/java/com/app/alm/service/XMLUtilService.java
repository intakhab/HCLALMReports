package com.app.alm.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.app.alm.domain.User;

/***
 * @author intakhabalam.s@hcl.com provide the input service of XML manipulation
 * @see Environment {@link Environment}
 */
@Service
public class XMLUtilService {

	private final Logger logger = LogManager.getLogger("Dog-X");
	@Autowired
	Environment env;

	/**
	 * This method will convert given object/class into XML
	 * 
	 * @param object
	 *            {@link Object}
	 * @return string {@link String}
	 */
	public String convertObjectToXML(Object object) {
		try {
			StringWriter stringWriter = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(object, stringWriter);
			return stringWriter.toString();
		} catch (JAXBException e) {
			logger.error("Error {X-1010}: marshalling xml ", e.getMessage());
		}
		return null;
	}

	/***
	 * 
	 * This method will convert XML to Object from given input file
	 * 
	 * @param clazz
	 *            {@link Class}
	 * @param file
	 *            {@link File}
	 * @return class
	 * @throws JAXBException
	 *             {@link JAXBException}
	 * @throws FileNotFoundException
	 *             {@link FileNotFoundException}
	 */
	public <T> T convertXMLToObject(Class<?> clazz, File file) throws JAXBException, FileNotFoundException {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller um = context.createUnmarshaller();
			@SuppressWarnings("unchecked")
			T unmarshal = (T) um.unmarshal(file);
			logger.info("Data unmarshal sucessfully...");
			return unmarshal;
		} catch (JAXBException je) {
			logger.error("Error {X-1011}: Interpreting XML response {} ", je.getMessage());

		}
		return null;
	}

	/****
	 * Method will edit user from attribute id
	 * 
	 * @param xmlPath
	 *            {@link File}}
	 * @param user
	 *            {@link User}
	 * @return {@link Boolean}
	 */
	public boolean editUserXMLWithAttributeId(File xmlPath, User user) {
		try {

			Document doc = parseDocument(xmlPath);
			updateUserElement(doc, user);// updating
			doc.getDocumentElement().normalize();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(xmlPath);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			logger.info("User DB updated successfully...");
			return true;
		} catch (Exception e) {

			logger.error("Error {X-1023}: {editUserXMLWithAttributeId} : " + e.getMessage());
			return false;
		}

	}

	/***
	 * This method will update xml values
	 * 
	 * @param doc
	 * @param user
	 */
	private static void updateUserElement(Document doc, User user) {
		// loop for each user
		NodeList usersList = doc.getElementsByTagName("user");
		for (int i = 0; i < usersList.getLength(); i++) {
			Element elem = (Element) usersList.item(i);
			String id = elem.getAttribute("id");
			if (id.equals(user.getId())) {
				elem.getElementsByTagName("username").item(0).getFirstChild().setNodeValue(user.getUsername());
				elem.getElementsByTagName("userpass").item(0).getFirstChild().setNodeValue(user.getUserpass());
				elem.getElementsByTagName("active").item(0).getFirstChild().setNodeValue("" + user.isActive());
			}
		}
	}

	/***
	 * 
	 * @param fileName
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document parseDocument(File fileName) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(fileName);
		doc.getDocumentElement().normalize();
		return doc;
	}

	/***
	 * 
	 * Method will convert Object to XML with class and save in disk
	 * 
	 * @param classz
	 *            {@link Object}
	 * @param fileName
	 *            {@link String}
	 * @return object {@link Object}
	 * @throws JAXBException
	 *             {@link JAXBException}
	 * @throws FileNotFoundException
	 *             {@link FileNotFoundException}
	 */
	public Object convertObjectToXML(Object classz, String fileName) throws JAXBException, FileNotFoundException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(classz.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(classz, Paths.get(fileName).toFile());
			logger.info("Data saved sucessfully...");
			return classz;
		} catch (JAXBException e) {
			logger.error("Error {X-1012}: saving object to XML {} ", e.getMessage());
		}
		return null;
	}

	/***
	 * This method will print the XML content
	 * 
	 * @param file
	 *            {@link File}
	 * @return {@link String}
	 */
	public String printXML(File file) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db;
		String content = "";
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			Writer out = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(out));
			content = out.toString();
			logger.info("\n" + content);
			out.close();

		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			logger.error("Error {X-1017}: in printing XML  {} : " + e.getMessage());
		}
		return content;

	}

	/***
	 * 
	 * @param XMLStr
	 *            {@link String}
	 * @param filename
	 *            {@link String}
	 * @return {@link Boolean}
	 */

	public boolean writeXMLString(String XMLStr, String filename) {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(XMLStr)));
			document.getDocumentElement().normalize();

			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();
			Source src = new DOMSource(document);
			Result dest = new StreamResult(Paths.get(filename).toFile());
			aTransformer.transform(src, dest);
		} catch (Exception e) {
			logger.error("Error {X-1021}: in Writing XML  {} : " + e.getMessage());
			return false;
		}
		return true;
	}

}
