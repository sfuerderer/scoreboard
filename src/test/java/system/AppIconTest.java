/************************************************
 * Copyright (c) 2026 Sebastian Fürderer       **
 * All rights reserved                         **
 ************************************************/
package system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

class AppIconTest {

	private static BufferedImage read(String sResource) throws IOException {
		try (InputStream in = AppIcon.class.getResourceAsStream(sResource)) {
			assertNotNull(in, "missing resource " + sResource);
			return ImageIO.read(in);
		}
	}

	@Test
	void everyIconSizeExistsWithMatchingDimensions() throws IOException {
		for (int iSize : AppIcon.SIZES) {
			BufferedImage image = read(AppIcon.getResourceName(iSize));
			assertEquals(iSize, image.getWidth(), AppIcon.getResourceName(iSize));
			assertEquals(iSize, image.getHeight(), AppIcon.getResourceName(iSize));
		}
	}

	@Test
	void dockIconExists() throws IOException {
		BufferedImage image = read(AppIcon.DOCK_ICON);
		assertEquals(1024, image.getWidth());
		assertEquals(1024, image.getHeight());
	}
}
