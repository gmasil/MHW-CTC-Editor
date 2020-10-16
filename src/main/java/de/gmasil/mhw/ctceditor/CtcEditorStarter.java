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
package de.gmasil.mhw.ctceditor;

import java.awt.EventQueue;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.ui.AppParameters;
import de.gmasil.mhw.ctceditor.ui.CtcEditor;

public class CtcEditorStarter {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static String version = "Dev";
	private static String revision = "0";

	private CtcEditorStarter() {
	}

	public static void start(String... args) {
		LOG.info("MHW CTC Editor is starting");
		if (CtcEditorStarterWrapper.isRunningFromIde()) {
			LOG.debug("Running from IDE");
		} else {
			try {
				readManifest();
				LOG.info("Version {} (Revision: {})", version, revision);
			} catch (IOException e) {
				LOG.warn("Unable to read version from manifest");
			}
		}
		AppParameters params = new AppParameters(args);
		if (params.isBatchMode()) {
			LOG.info("Running in batch mode, disabling ansi colors");
		}
		if (params.isColorMode()) {
			LOG.info("Running in color mode, enforcing ansi colors");
		}
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				LOG.info("Could not set system look and feel");
			}
			new CtcEditor(params.getFileNames());
		});
	}

	public static String getVersion() {
		return version;
	}

	public static String getRevision() {
		return revision;
	}

	private static void readManifest() throws IOException {
		Enumeration<URL> resources = CtcEditorStarter.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
		while (resources.hasMoreElements()) {
			try {
				Manifest manifest = new Manifest(resources.nextElement().openStream());
				String ret = manifest.getMainAttributes().getValue("SCM-Revision");
				if (ret != null) {
					revision = ret;
				}
				ret = manifest.getMainAttributes().getValue("Version");
				if (ret != null) {
					version = ret;
				}
			} catch (IOException e) {
				// Nothing to do
			}
		}
	}
}
