package org.google.home.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.google.home.config.factory.ConfigFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Email {

	private static String emailJsonTemplate = "{\r\n" + "    \"content\": {\r\n"
			+ "        \"subject\": \"%SUBJECT%\",\r\n" + "        \"body\": \"%BODY%\",\r\n"
			+ "        \"IsHtml\": \"%ISHTML%\"\r\n" + "    },\r\n" + "    \"attachment\": {\r\n"
			+ "        \"attachmentFileNames\": [\"%FILENAMES%\"],\r\n"
			+ "        \"attachmentsBytesList\": [\"%FILECONTENTINBYTES%\"]\r\n" + "    },\r\n"
			+ "    \"recipients\": {\r\n" + "        \"toAddresses\": [\"TO\"],\r\n"
			+ "        \"ccAddresses\": [\"CC\"]\r\n" + "    }\r\n" + "}";

	public static void sendEmailNotification() throws Exception {

		try {
			String emailEndpoint = ConfigFactory.getConfig().emailEndpoint();

			String reqBody = prepareEmailJsonBody();

			RequestSpecification request = RestAssured.given().headers(prepareEmailHeaders()).body(reqBody);

			Response response = request.post(emailEndpoint);

			int emailRespStatusCode = response.getStatusCode();

			if (emailRespStatusCode == 200) {
				System.out.println("Send Email Notification Response:: " + response.asPrettyString());
				System.out.println("Send Email Notification:: >>>>>SUCCESSFUL<<<<<");
			} else {
				throw new Exception("Email API responded with status code " + emailRespStatusCode + " instead of 200."
						+ "\n Email Response Status line:: " + response.getStatusLine() + " \n Email API Response:: "
						+ response.asPrettyString());
			}

		} catch (Exception e) {
			throw new Exception("Failed to send email due to Exception ocurred" + e.getMessage(), e);
		}

	}

	private static String prepareEmailJsonBody() throws Exception {
		Gson gson = new Gson();
		JsonArray jsonArray = null;

		String reqBody = emailJsonTemplate;
		List<String> fileNamesList = fileNamesToAttach();
		List<String> fileByteList = prepareEmailAttachments(fileNamesList);

		reqBody = reqBody.replace("%SUBJECT%", ConfigFactory.getConfig().emailSubject());
		reqBody = reqBody.replace("%BODY%", ConfigFactory.getConfig().emailBody());

		reqBody = reqBody.replace("%ISHTML%", ConfigFactory.getConfig().emailIsHTML());

		reqBody = reqBody.replace("[\"%FILENAMES%\"]", gson.toJsonTree(fileNamesList).getAsJsonArray().toString());

		reqBody = reqBody.replace("[\"%FILECONTENTINBYTES%\"]",
				gson.toJsonTree(fileByteList).getAsJsonArray().toString());

		if (ConfigFactory.getConfig().toEmailList() != null) {
			jsonArray = gson.toJsonTree(ConfigFactory.getConfig().toEmailList().split(",")).getAsJsonArray();
			reqBody = reqBody.replace("[\"TO\"]", jsonArray.toString());
		} else {
			throw new Exception("Environment Variable \"toemail\" is not configured: blank/empty/null");
		}

		if (ConfigFactory.getConfig().ccEmailList() != null) {
			jsonArray = gson.toJsonTree(ConfigFactory.getConfig().ccEmailList().split(",")).getAsJsonArray();
			reqBody = reqBody.replace("[\"CC\"]", jsonArray.toString());
		} else {
			reqBody = reqBody.replace("[\"CC\"]", "null");
		}

		return reqBody;

	}

	private static Map<String, String> prepareEmailHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("x-functions-key", ConfigFactory.getConfig().emailServiceKey());

		return headers;
	}

	private static List<String> prepareEmailAttachments(List<String> fileNamesList) throws IOException {
		List<String> fileInBytes = new ArrayList<String>();
		for (String fnames : fileNamesList) {
			fileInBytes.add(FileHandlers.convertFileToByte(fnames));
		}
		return fileInBytes;
	}

	private static List<String> fileNamesToAttach() throws IOException {
		List<String> fNamesList = new ArrayList<String>();

		if (isFileExists(System.getProperty("user.dir") + "\\target\\", "Automation-Test-Report.pdf")) {
			if (ConfigFactory.getConfig().pdfTestReport().equalsIgnoreCase("y")) {
				fNamesList.add("Automation-Test-Report.pdf");
			}
		}
		return fNamesList;
	}

	public static boolean isFileExists(String rootDir, String fileName) {
		Path rootPath = Paths.get(rootDir);
		try {
			return Files.walk(rootPath)
					.anyMatch(path -> Files.isRegularFile(path) && path.getFileName().toString().equals(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
