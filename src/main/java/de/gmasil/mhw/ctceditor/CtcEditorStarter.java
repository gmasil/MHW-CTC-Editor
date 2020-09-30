package de.gmasil.mhw.ctceditor;

import java.awt.EventQueue;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.URL;

import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.ui.CtcEditor;

public class CtcEditorStarter {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {
		LOG.info("MHW CTC Editor is starting");
		copyStarterFile();
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				LOG.info("Could not set system look and feel");
			}
			new CtcEditor(args);
		});
	}

	private static void copyStarterFile() {
		try {
			File sourceLocation = new File(CtcEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if (sourceLocation.getAbsolutePath().replace('\\', '/').endsWith("target/classes")) {
				LOG.debug("Running from IDE");
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
