/**
 * MHW-CTC-Editor
 * Copyright © 2020 Simon Oelerich (simon.oelerich@gmasil.de)
 *
 * This file is part of MHW-CTC-Editor.
 *
 * MHW-CTC-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MHW-CTC-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MHW-CTC-Editor. If not, see <https://www.gnu.org/licenses/>.
 */
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

public class Config {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String CONFIG_FILE = "mhw-ctc-editor.properties";

	private static final String LAST_OPENED_FILE = "file.last-opened";
	private static final String SHOW_CONSOLE = "console.show";
	private static final String SHOW_UNKNOWN_FIELDS = "unknown-fields.show";

	private static Properties properties = new Properties();

	static {
		load();
	}

	private Config() {
	}

	public static void load() {
		LOG.info("Loading config file " + CONFIG_FILE);
		if (new File(CONFIG_FILE).exists()) {
			try {
				InputStream in = new FileInputStream(CONFIG_FILE);
				properties.load(in);
			} catch (IOException e) {
				LOG.warn("Error while loading configuration", e);
			}
		}
	}

	public static void save() {
		try {
			properties.store(new FileOutputStream(CONFIG_FILE), null);
		} catch (IOException e) {
			LOG.warn("Error while saving configuration", e);
		}
	}

	public static String getLastOpenedFile() {
		return properties.getProperty(LAST_OPENED_FILE, "");
	}

	public static void setLastOpenedFile(String lastOpenedFile) {
		properties.setProperty(LAST_OPENED_FILE, lastOpenedFile);
	}

	public static boolean getShowConsole() {
		return properties.getProperty(SHOW_CONSOLE, "true").equals("true");
	}

	public static void setShowConsole(boolean showConsole) {
		properties.setProperty(SHOW_CONSOLE, "" + showConsole);
	}

	public static boolean getShowUnknownFields() {
		return properties.getProperty(SHOW_UNKNOWN_FIELDS, "true").equals("true");
	}

	public static void setShowUnknownFields(boolean showUnknownFields) {
		properties.setProperty(SHOW_UNKNOWN_FIELDS, "" + showUnknownFields);
	}
}
