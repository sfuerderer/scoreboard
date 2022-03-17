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
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	public <T> void saveTToFile(T object, String sFilename) throws FileNotCreatedException, IOException {

		File file;
		try {
			file = loadFile(sFilename);
		} catch (FileDidNotExistException e) {
			file = createFile(sFilename);
		}

		Writer writer = Files.newBufferedWriter(file.toPath());

		gson.toJson(object, writer);

		writer.close();
	}

	public <T> T loadTFromFile(String sFilename, Class<T> type) throws IOException, FileDidNotExistException {

		File settingsFile = FileHandling.getFileHandling().loadFile(sFilename);
		Reader reader = Files.newBufferedReader(settingsFile.toPath());

		return gson.fromJson(reader, type);

	}

	public <T> T loadTFromFile(String sFilename, Type type) throws IOException, FileDidNotExistException {

		File settingsFile = FileHandling.getFileHandling().loadFile(sFilename);
		Reader reader = Files.newBufferedReader(settingsFile.toPath());

		return gson.fromJson(reader, type);

	}

	public String getExtensionOfFile(File file) {
		String fileExtension = "";
		String fileName = file.getName();

		if (fileName.contains(".") && fileName.lastIndexOf(".") != 0)
			fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

		return fileExtension;
	}

}
