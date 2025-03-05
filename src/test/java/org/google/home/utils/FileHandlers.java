package org.google.home.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

public class FileHandlers {

	public static String convertFileToByte(String fName) throws IOException {

		Path startDir = Paths.get(System.getProperty("user.dir") + "/target/");
		Optional<Path> filePath = Optional.empty();
		String absoluteFPath = "";
		try (Stream<Path> stream = Files.walk(startDir)) {
			filePath = stream.filter(p -> p.getFileName().toString().equals(fName)).findFirst();
			if (filePath.isPresent()) {
				System.out.println("File found: " + filePath.get().toAbsolutePath());
				absoluteFPath = filePath.get().toAbsolutePath().toString();
			} else {
				System.out.println("File not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] encoded = Base64.encodeBase64((FileUtils.readFileToByteArray(new File(absoluteFPath))));
		return new String(encoded, StandardCharsets.US_ASCII);
	}
}
