package de.gmasil.mhw.ctceditor;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.ui.CtcEditor;

public class CtcEditorStarter {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static boolean isRunningFromIde = false;
	private static String version = "Dev";
	private static String revision = "0";

	public static void main(String[] args) {
		LOG.info("MHW CTC Editor is starting");
		copyStarterFile();
		if (!isRunningFromIde) {
			try {
				readManifest();
			} catch (IOException e) {
				LOG.warn("Unable to read version from manifest");
			}
			LOG.info("Running version {}", version);
		}
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				LOG.info("Could not set system look and feel");
			}
			new CtcEditor(args);
		});
	}

	public static boolean isRunningFromIde() {
		return isRunningFromIde;
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

	private static void copyStarterFile() {
		try {
			File sourceLocation = new File(CtcEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if (sourceLocation.getAbsolutePath().replace('\\', '/').endsWith("target/classes")) {
				LOG.debug("Running from IDE");
				isRunningFromIde = true;
				version = "DEV";
			} else if (sourceLocation.getAbsolutePath().endsWith(".jar")) {
				File starterFile = new File(sourceLocation.getParentFile(), "MHW-CTC-Editor.cmd");
				if (!starterFile.exists()) {
					URL url = MethodHandles.lookup().lookupClass().getResource("/MHW-CTC-Editor.cmd");
					FileUtils.copyURLToFile(url, starterFile);
				}
			}
		} catch (Exception e) {
			LOG.warn("Could not copy starter file", e);
		}
	}
}
