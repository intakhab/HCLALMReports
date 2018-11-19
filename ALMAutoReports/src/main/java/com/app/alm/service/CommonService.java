package com.app.alm.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.app.alm.common.HTMLTemplate;
import com.app.alm.component.DataLoaderComponent;
import com.app.alm.config.PropertiesConfig;
import com.app.alm.dto.StatusInfoDto;
import com.app.alm.dto.UserDto;

/***
 * Common class for HCLALM
 * @author intakhabalam.s@hcl.com
 * @see Service
 * @see EmailService {@link EmailService}
 * @see XMLUtilService {@link XMLUtilService}
 * @see DataLoaderComponent {@link DataLoaderComponent}
 * @see PropertiesConfig {@link PropertiesConfig}
 * @see Environment {@link Environment}
 *
 */
@Service
public class CommonService {

	private final Logger logger = LogManager.getLogger("Dog-S");

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");

	@Autowired
	Environment env;
	@Autowired
	EmailService emailService;

	@Autowired
	PropertiesConfig propertiesConfig;

	@Autowired
	XMLUtilService xmlUtilService;

	/**
	 * This method will give YYYY-DD-MM and Hrs
	 * 
	 * @return time
	 */
	public String currentTime() {
		return dateFormat.format(new Date());
	}


	/***
	 * Will print time
	 * @param fileTime {@link String}
	 * @return time {@link String}
	 */
	public String printFileTime(FileTime fileTime) {
		try {
			return dateFormat.format(fileTime.toMillis());
		} catch (Exception e) {
			return "" + System.currentTimeMillis();
		}
	}


	/***
	 * Write start html path at runtime
	 * @param hostname {@link String}
	 */
	public void writeStartFile(String hostname) {
		String newData = HTMLTemplate.getStartTemplate(hostname, env.getProperty("server.port"));
		File htmlFile = Paths.get("start.html").toFile();
		if (!htmlFile.exists()) {
			writeFile(newData, htmlFile.getPath());
		}
	}


	/**
	 * This method will copy and replace file
	 * @param src {@link String}
	 * @param destSrc {@link String}
	 */
	public void copyReplaceFile(String src, String destSrc) {
		Path sourcePath = Paths.get(src);
		Path destinationPath = Paths.get(destSrc);
		try {
			Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (FileAlreadyExistsException e) {
			// throw new FileException();
			logger.error("Error: {Com-0006} Destination file already exists {} " + e);
			// destination file already exists
		} catch (Exception e) {
			// something else went wrong
			logger.error("Error:  {Com-0007} - " + e);
		}

	}

	

	/***
	 * 
	 * @param src {@link String}
	 * @param what {@link String}
	 * @return {@link Boolean}
	 */
	public boolean containsIgnoreCaseRegexp(String src, String what) {
		return Pattern.compile(Pattern.quote(what), Pattern.CASE_INSENSITIVE).matcher(src).find();
	}

	/***
	 * @param d {@link Double}
	 * @return {@link String}
	 */
	public String getDecimalNumber(double d) {
		DecimalFormat df2 = new DecimalFormat("#.##");
		return df2.format(d);
	}

	/***
	 * Backup of config file
	 * @param fileName {@link String}
	 */
	public void backupConfigFile(String fileName) {
		String newFile = env.getProperty("backup.dir") + "/" + "config_" + currentTime() + ".db";
		File file = Paths.get(fileName).toFile();
		if (file.exists()) {
			copyReplaceFile(fileName, newFile);

		}
		logger.info("Config DB backup done sucessfully...");

	}

	/***
	 * Backup backup User File
	 */
	public void backupUserFile() {
		String newFile = env.getProperty("backup.dir") + "/" + "users_" + currentTime() + ".db";
		File file = Paths.get(env.getProperty("db.user")).toFile();
		if (file.exists()) {
			copyReplaceFile(file.getPath(), newFile);

		}
		logger.info("User DB backup done sucessfully...");

	}

	/**
	 * This method will reload Config file once user will click button on to UI
	 */
	public void reloaUserbackup() {
		String dbLocation = env.getProperty("backup.dir");
		Path parentFolder = Paths.get(dbLocation);
		// if you're only interested in files...
		Optional<File> mostRecentFile = Arrays.stream(parentFolder.toFile().listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("users");
			}
		})).filter(f -> f.isFile()).max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

		if (mostRecentFile.isPresent()) {
			File mostRecent = mostRecentFile.get();
			logger.info("Backup folder most recent file for backup {} " + mostRecent.getName());
			String dbPath = env.getProperty("db.user");
			copyReplaceFile(mostRecent.getPath(), dbPath);
		} else {
			logger.info("User backup folder Empty {} ");
		}
	}

	/**
	 * This method will reload Config file once user will click button on to UI
	 */
	public void reloaConfigBackup() {
		String dbLocation = env.getProperty("backup.dir");
		Path parentFolder = Paths.get(dbLocation);

		Optional<File> mostRecentFile = Arrays.stream(parentFolder.toFile().listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("config");
			}
		})).filter(f -> f.isFile()).max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

		if (mostRecentFile.isPresent()) {
			File mostRecent = mostRecentFile.get();
			logger.info("Backup folder most recent file for backup {} " + mostRecent.getName());
			String dbPath = env.getProperty("db.location");
			copyReplaceFile(mostRecent.getPath(), dbPath);
		} else {
			logger.info("Backup folder Empty {} ");
		}

	}



	/***
	 * This method will register user with userdto
	 * @param userDto {@link UserDto}
	 */
	public void createRegisterUsers(UserDto userDto) {
		userDto.setId("" + System.currentTimeMillis());
		createUsers(userDto);
	}

	/****
	 * After taking input this method will create users which will save in db and
	 * shown onto UI
	 * @param userDto {@link UserDto}
	 */
	public void createUsers(UserDto userDto) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			final String userPath = env.getProperty("db.user");
			File file = Paths.get(userPath).toFile(); // XML file to read
			if (!file.exists()) {
				createDummyUser(userPath);
			}
			Document document = builder.parse(file);
			Element users = document.getDocumentElement();
			Element user = document.createElement("user");
			user.setAttribute("id", userDto.getId());

			Element email = document.createElement("email");
			Text emailText = document.createTextNode(userDto.getEmail().trim());
			email.appendChild(emailText);
			user.appendChild(email);
			//
			Element username = document.createElement("username");
			Text usernameText = document.createTextNode(userDto.getUsername().trim());
			username.appendChild(usernameText);
			user.appendChild(username);
			//
			Element userpass = document.createElement("userpass");
			Text userpassText = document.createTextNode(userDto.getUserpass().trim());
			userpass.appendChild(userpassText);
			user.appendChild(userpass);

			Element status = document.createElement("active");
			Text statusText = document.createTextNode("" + userDto.isActive());
			status.appendChild(statusText);
			user.appendChild(status);

			Element lastlogin = document.createElement("createdate");
			Text lastloginText = document.createTextNode(userDto.getCreateDate());
			lastlogin.appendChild(lastloginText);
			user.appendChild(lastlogin);

			users.appendChild(user);// Parent
			transformDoc(document, file);
			logger.info("User saved successfully...");

			//
		} catch (Exception e) {
			logger.error("Error: {Com-0014} users creation problem {} ", e);

		}
	}

	/***
	 * 
	 * @param document {@link Document}
	 * @param file {@link File}
	 * @throws TransformerException
	 */
	private void transformDoc(Document document, File file) throws TransformerException {
		TransformerFactory tfact = TransformerFactory.newInstance();
		Transformer tform = tfact.newTransformer();
		tform.setOutputProperty(OutputKeys.INDENT, "yes");
		tform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
		tform.transform(new DOMSource(document), new StreamResult(file));
	}

	/****
	 * This will create a dummy reports at first time, if reports not available
	 * @param filePath {@link String}
	 */
	private void createDummyUser(String filePath) {

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("users");
			doc.appendChild(rootElement);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(Paths.get(filePath).toFile());
			transformer.transform(source, result);
			logger.info("Dummy users created successfully...");

		} catch (Exception e) {
			logger.error("Error: {Com-00015} dummy user creation problem {} ", e);
		}
	}


	
	/***
	 * This method will give application information
	 * 
	 * @return list {@link List}
	 */
	public List<StatusInfoDto> getServerStatus() {
		List<StatusInfoDto> dogList = new ArrayList<>();

		try {
			InetAddress ipAddr = InetAddress.getLocalHost();
			StatusInfoDto dog = new StatusInfoDto();
			dog.setServerStatus("Server Status");
			dog.setHostAddress("Host Address");
			dog.setHostName("Host Name");
			dog.setCononicalHostName("Canonical Host Name");
			dog.setUserName("User Name");
			dog.setPid("Process ID");
			dog.setVersionId("Version");
			dog.setStartedTime("Started Time");

			dogList.add(dog);

			StatusInfoDto dog1 = new StatusInfoDto();
			dog1.setServerStatus("Running");
			dog1.setHostAddress(ipAddr.getHostAddress());
			dog1.setHostName(ipAddr.getHostName());
			dog1.setCononicalHostName(ipAddr.getCanonicalHostName());
			String username = System.getProperty("user.name");
			if (username != null) {
				dog1.setUserName(username);
			} else {
				dog1.setUserName("");

			}
			propertiesConfig.ipAddress=" { " + username + " } " + ipAddr.getCanonicalHostName();
			
			String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
			dog1.setPid(pid);
			dog1.setVersionId(propertiesConfig.versionId);
			dog1.setPort(env.getProperty("server.port"));
			dog1.setStartedTime(STARTED_TIME);

			dogList.add(dog1);

			writeStatus(dog1);
			

		} catch (Exception e) {
			return null;
		}

		return dogList;
	}

	/***
	 * Status XML will generate for see the running status of application
	 * 
	 * @param dogInfo  {@link StatusInfoDto}
	 */
	private void writeStatus(StatusInfoDto dogInfo) {

		try {
			File file = Paths.get("status.db").toFile();
			if (!file.exists()) {
				xmlUtilService.convertObjectToXML(dogInfo, "status.db");
			}

		} catch (FileNotFoundException | JAXBException e) {
		}
	}

	/***
	 * delete status
	 */
	public void deleteStatus() {

		try {
			File file = Paths.get("status.db").toFile();
			if (file.exists()) {
				FileUtils.deleteQuietly(file);
			}
		} catch (Exception e) {
		}
	}
	/***
	 * @param hostname {@link String}
	 */
	public void writeStartFileForcely(String hostname) {
		String newData = HTMLTemplate.getStartTemplate(hostname, env.getProperty("server.port"));
		File htmlFile = Paths.get("start.html").toFile();
		if (htmlFile.exists()) {
			FileUtils.deleteQuietly(htmlFile);

		}
		writeFile(newData, htmlFile.getPath());
	}
	/**
	 * 
	 * @param str {@link String}
	 * @param splitType {@link String}
	 * @return splitstr
	 */
	public List<String> split(String str, String splitType) {
		return Stream.of(str.split(splitType)).map(elem -> new String(elem)).collect(Collectors.toList());
	}



	/***
	 * Write file
	 * @param data {@link String}
	 * @param filePath {@link String}
	 */
	public void writeFile(String data, String filePath) {
		Path filepath = Paths.get(filePath);
		byte[] bytes = data.getBytes();
		try (OutputStream out = Files.newOutputStream(filepath)) {
			out.write(bytes);
		} catch (Exception e) {
			logger.error("Error: {Com-0015} during writing file {}  ", e);

		}
	}

    /**
     * 
     * @param commonArgs {@link String}
     * @return {@link String}
     */
	public String getMins(String commonArgs) {
		long l = (MINS * Long.parseLong(commonArgs)) / 60;
		return String.valueOf(l);
	}

	/***********************************************/

	public final String CLOSE_BANNER = "\n\r ____  ____     ______    _____             _         _____      ____    ____         ______    _____        ___      ______    _____   ____  _____     ______   \r\n" + 
			"|_   ||   _|  .' ___  |  |_   _|           / \\       |_   _|    |_   \\  /   _|      .' ___  |  |_   _|     .'   `.  .' ____ \\  |_   _| |_   \\|_   _|  .' ___  |  \r\n" + 
			"  | |__| |   / .'   \\_|    | |            / _ \\        | |        |   \\/   |       / .'   \\_|    | |      /  .-.  \\ | (___ \\_|   | |     |   \\ | |   / .'   \\_|  \r\n" + 
			"  |  __  |   | |           | |   _       / ___ \\       | |   _    | |\\  /| |       | |           | |   _  | |   | |  _.____`.    | |     | |\\ \\| |   | |   ____  \r\n" + 
			" _| |  | |_  \\ `.___.'\\   _| |__/ |    _/ /   \\ \\_    _| |__/ |  _| |_\\/_| |_      \\ `.___.'\\   _| |__/ | \\  `-'  / | \\____) |  _| |_   _| |_\\   |_  \\ `.___]  | \r\n" + 
			"|____||____|  `.____ .'  |________|   |____| |____|  |________| |_____||_____|      `.____ .'  |________|  `.___.'   \\______.' |_____| |_____|\\____|  `._____.'  \r\n" + 
			"                                                                                                                                                                 \r\n" + 
			"";

	public final String OPEN_BANNER = "\n\r ____  ____     ______    _____             _         _____      ____    ____        ______    _________        _        _______      _________   _____   ____  _____     ______   \r\n" + 
			"|_   ||   _|  .' ___  |  |_   _|           / \\       |_   _|    |_   \\  /   _|     .' ____ \\  |  _   _  |      / \\      |_   __ \\    |  _   _  | |_   _| |_   \\|_   _|  .' ___  |  \r\n" + 
			"  | |__| |   / .'   \\_|    | |            / _ \\        | |        |   \\/   |       | (___ \\_| |_/ | | \\_|     / _ \\       | |__) |   |_/ | | \\_|   | |     |   \\ | |   / .'   \\_|  \r\n" + 
			"  |  __  |   | |           | |   _       / ___ \\       | |   _    | |\\  /| |        _.____`.      | |        / ___ \\      |  __ /        | |       | |     | |\\ \\| |   | |   ____  \r\n" + 
			" _| |  | |_  \\ `.___.'\\   _| |__/ |    _/ /   \\ \\_    _| |__/ |  _| |_\\/_| |_      | \\____) |    _| |_     _/ /   \\ \\_   _| |  \\ \\_     _| |_     _| |_   _| |_\\   |_  \\ `.___]  | \r\n" + 
			"|____||____|  `.____ .'  |________|   |____| |____|  |________| |_____||_____|      \\______.'   |_____|   |____| |____| |____| |___|   |_____|   |_____| |_____|\\____|  `._____.'  \r\n" + 
			"                                                                                                                                                                                   \r\n" + 
			"";

	public final Long MINS = (long) (1000 * 60 * 60);
	public String STARTED_TIME = "";

}
