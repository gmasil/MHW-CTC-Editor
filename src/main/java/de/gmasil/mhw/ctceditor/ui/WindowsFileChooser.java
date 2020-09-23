package de.gmasil.mhw.ctceditor.ui;

import java.io.File;

import com.sun.javafx.application.PlatformImpl;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class WindowsFileChooser {
	private Config config;
	private FileOpenedListener listener;
	private File lastOpenedFile = null;

	public WindowsFileChooser(Config config, FileOpenedListener listener) {
		this.config = config;
		this.listener = listener;
		lastOpenedFile = new File(config.getProperty(Config.LAST_OPENED_FILE));
	}

	public void openDialog() {
		PlatformImpl.startup(() -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("CTC files (*.ctc)", "*.ctc"));
			fileChooser.getExtensionFilters().add(new ExtensionFilter("All files", "*.*"));
			if (lastOpenedFile != null) {
				File initialDirectory;
				if (lastOpenedFile.isFile()) {
					initialDirectory = lastOpenedFile.getParentFile();
				} else {
					initialDirectory = lastOpenedFile;
				}
				if (initialDirectory.exists()) {
					fileChooser.setInitialDirectory(initialDirectory);
				}
			}
			File selectedFile = fileChooser.showOpenDialog(null);
			if (selectedFile != null) {
				lastOpenedFile = selectedFile;
				config.setProperty(Config.LAST_OPENED_FILE, lastOpenedFile.getAbsolutePath());
				config.save();
				listener.onFileOpened(selectedFile);
			}
		});
	}
}
