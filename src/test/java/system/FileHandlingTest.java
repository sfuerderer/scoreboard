/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.google.gson.annotations.Expose;

class FileHandlingTest {

	static class Data {
		@Expose
		String sName;

		Data(String sName) {
			this.sName = sName;
		}
	}

	@TempDir
	Path tempDir;

	@Test
	void savesAndLoadsObject() throws Exception {
		String file = tempDir.resolve("sub/data.json").toString();

		FileHandling.getFileHandling().saveTToFile(new Data("first"), file);
		FileHandling.getFileHandling().saveTToFile(new Data("second"), file);

		Data loaded = FileHandling.getFileHandling().loadTFromFile(file, Data.class);
		assertEquals("second", loaded.sName);
	}

	@Test
	void leavesNoTemporaryFilesBehind() throws Exception {
		String file = tempDir.resolve("data.json").toString();

		FileHandling.getFileHandling().saveTToFile(new Data("value"), file);

		try (Stream<Path> files = Files.list(tempDir)) {
			assertEquals(1, files.count());
		}
	}

	@Test
	void invalidJsonThrowsIOException() throws Exception {
		Path file = tempDir.resolve("broken.json");
		Files.writeString(file, "{ \"sName\": ");

		assertThrows(IOException.class,
				() -> FileHandling.getFileHandling().loadTFromFile(file.toString(), Data.class));
	}

	@Test
	void emptyFileIsLoadedAsNull() throws Exception {
		Path file = tempDir.resolve("empty.json");
		Files.writeString(file, "");

		assertNull(FileHandling.getFileHandling().loadTFromFile(file.toString(), Data.class));
	}
}
