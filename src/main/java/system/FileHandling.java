/************************************************
 * Copyright (c) 2020 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import exceptions.FileDidNotExistException;
import exceptions.FileNotCreatedException;

public class FileHandling {

	private static FileHandling fileHandling;
	private Gson gson;

	private FileHandling() {
		gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
	}

	public static FileHandling getFileHandling() {
		if (fileHandling == null)
			fileHandling = new FileHandling();
		return fileHandling;
	}

	public File loadFile(String filename) throws FileDidNotExistException {
		File file = new File(filename);

		if (!file.exists()) {
			throw new FileDidNotExistException();
		}

		return file;
	}
	
	public File loadFileFromResource(String sFolderName, String sFilename, String sFileExtension) {

		return new File(FileHandling.class.getResource("/"+sFolderName+"/"+sFilename+"."+sFileExtension).toExternalForm());
	}

	public File createFile(String filename) throws FileNotCreatedException, IOException {
		File file = new File(filename);
		if (!file.getParentFile().mkdirs() && !file.getParentFile().exists())
			throw new FileNotCreatedException();
		if (!file.createNewFile())
			throw new FileNotCreatedException();
		return file;
	}

	/**
	 * Saves the object as JSON. The data is first written to a temporary file in
	 * the same folder and then moved over the target, so a crash while writing
	 * never leaves a half written file behind.
	 */
	public <T> void saveTToFile(T object, String sFilename) throws FileNotCreatedException, IOException {

		File file;
		try {
			file = loadFile(sFilename);
		} catch (FileDidNotExistException e) {
			file = createFile(sFilename);
		}

		Path target = file.toPath();
		Path tempFile = Files.createTempFile(target.toAbsolutePath().getParent(), file.getName(), ".tmp");
		try {
			try (Writer writer = Files.newBufferedWriter(tempFile)) {
				gson.toJson(object, writer);
			}
			try {
				Files.move(tempFile, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			} catch (AtomicMoveNotSupportedException e) {
				Files.move(tempFile, target, StandardCopyOption.REPLACE_EXISTING);
			}
		} finally {
			Files.deleteIfExists(tempFile);
		}
	}

	public <T> T loadTFromFile(String sFilename, Class<T> type) throws IOException, FileDidNotExistException {
		return loadTFromFile(sFilename, (Type) type);
	}

	public <T> T loadTFromFile(String sFilename, Type type) throws IOException, FileDidNotExistException {

		File settingsFile = FileHandling.getFileHandling().loadFile(sFilename);
		try (Reader reader = Files.newBufferedReader(settingsFile.toPath())) {
			return gson.fromJson(reader, type);
		} catch (JsonParseException e) {
			throw new IOException("Invalid JSON in " + sFilename, e);
		}

	}

	public String getExtensionOfFile(File file) {
		String fileExtension = "";
		String fileName = file.getName();

		if (fileName.contains(".") && fileName.lastIndexOf(".") != 0)
			fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

		return fileExtension;
	}

}
