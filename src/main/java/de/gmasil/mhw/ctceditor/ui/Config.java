package de.gmasil.mhw.ctceditor.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config extends Properties {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String CONFIG_FILE = "editor.properties";

	public static final String LAST_OPENED_FILE = "file.last-opened";

	public Config() {
		LOG.info("Loading config file " + CONFIG_FILE);
		load();
	}

	public void load() {
		if (new File(CONFIG_FILE).exists()) {
			try {
				InputStream in = new FileInputStream(CONFIG_FILE);
				load(in);
			} catch (IOException e) {
				LOG.warn("Error while loading configuration", e);
			}
		} else {
			reset();
		}
	}

	public void save() {
		try {
			store(new FileOutputStream(CONFIG_FILE), null);
		} catch (IOException e) {
			LOG.warn("Error while saving configuration", e);
		}
	}

	public void reset() {
		setProperty(LAST_OPENED_FILE, "");
	}
}
