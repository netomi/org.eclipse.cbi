/*******************************************************************************
 * Copyright (c) 2015 Eclipse Foundation and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Mikaël Barbero - initial implementation
 *******************************************************************************/
package org.eclipse.cbi.webservice.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.eclipse.cbi.util.PropertiesReader;
import org.eclipse.cbi.webservice.server.EmbeddedServerProperties;
import org.junit.Test;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

public class EmbeddedServerPropertiesTest {

	@Test(expected=IllegalStateException.class)
	public void testEmptyPropertiesGetAccessLog() throws IOException {
		try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
			EmbeddedServerProperties propertiesReader = new EmbeddedServerProperties(new PropertiesReader(new Properties(), fs));
			propertiesReader.getAccessLogFile();
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void testEmptyPropertiesGetServicePathSpec() throws IOException {
		try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
			EmbeddedServerProperties propertiesReader = new EmbeddedServerProperties(new PropertiesReader(new Properties(), fs));
			propertiesReader.getServicePathSpec();
		}
	}
	
	@Test
	public void testEmptyPropertiesGetTempFolder() throws IOException {
		try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
			EmbeddedServerProperties propertiesReader = new EmbeddedServerProperties(new PropertiesReader(new Properties(), fs));
			assertEquals(fs.getPath(System.getProperty("java.io.tmpdir")), propertiesReader.getTempFolder());
		}
	}
	
	@Test
	public void testEmptyPropertiesGetServerPort() throws IOException {
		try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
			EmbeddedServerProperties propertiesReader = new EmbeddedServerProperties(new PropertiesReader(new Properties(), fs));
			assertEquals(8080, propertiesReader.getServerPort());
		}
	}
	
	@Test
	public void testGetAccessLog() throws IOException {
		try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
			EmbeddedServerProperties propertiesReader = new EmbeddedServerProperties(new PropertiesReader(createTestProperties(), fs));
			Path accessLog = fs.getPath("/var/log/access.log");
			assertEquals(accessLog, propertiesReader.getAccessLogFile());
			assertTrue(Files.exists(accessLog.getParent()));
		}
	}
	
	@Test
	public void testGetServicePathSpec() throws IOException {
		try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
			EmbeddedServerProperties propertiesReader = new EmbeddedServerProperties(new PropertiesReader(createTestProperties(), fs));
			assertEquals("service/serve", propertiesReader.getServicePathSpec());
		}
	}
	
	@Test
	public void testGetTempFolder() throws IOException {
		try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
			EmbeddedServerProperties propertiesReader = new EmbeddedServerProperties(new PropertiesReader(createTestProperties(), fs));
			Path tmpFolder = fs.getPath("/tmp/X");
			assertEquals(tmpFolder, propertiesReader.getTempFolder());
			assertTrue(Files.exists(tmpFolder));
		}
	}
	
	@Test
	public void testGetServerPort() throws IOException {
		try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
			EmbeddedServerProperties propertiesReader = new EmbeddedServerProperties(new PropertiesReader(createTestProperties(), fs));
			assertEquals(1025, propertiesReader.getServerPort());
		}
	}
	
	private static Properties createTestProperties() {
		Properties properties = new Properties();
		properties.setProperty("server.access.log", "/var/log/access.log   ");
		properties.setProperty("server.temp.folder", "/tmp/X");
		properties.setProperty("server.port", "1025");
		properties.setProperty("server.service.pathspec", "service/serve");
		return properties;
	}
}
