package de.gmasil.mhw.ctceditor.ui;

import java.io.File;
import java.io.Serializable;

import com.sun.javafx.application.PlatformImpl;

import de.gmasil.mhw.ctceditor.ui.api.FileOpenedListener;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class WindowsFileChooser implements Serializable {
	private FileOpenedListener listener;
	private File lastOpenedFile = null;

	public WindowsFileChooser(FileOpenedListener listener) {
		this.listener = listener;
		lastOpenedFile = new File(Config.getLastOpenedFile());
	}

	public void showOpenDialog() {
		PlatformImpl.startup(() -> {
			FileChooser fileChooser = createFileChooser();
			File selectedFile = fileChooser.showOpenDialog(null);
			if (selectedFile != null) {
				lastOpenedFile = selectedFile;
				Config.setLastOpenedFile(lastOpenedFile.getAbsolutePath());
				Config.save();
				listener.onFileOpened(selectedFile);
			}
		});
	}

	public void showSaveDialog() {
		PlatformImpl.startup(() -> {
			FileChooser fileChooser = createFileChooser();
			File selectedFile = fileChooser.showSaveDialog(null);
			if (selectedFile != null) {
				lastOpenedFile = selectedFile;
				Config.setLastOpenedFile(lastOpenedFile.getAbsolutePath());
				Config.save();
				listener.onFileSaved(selectedFile);
			}
		});
	}

	private FileChooser createFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CTC files (*.ctc)", "*.ctc"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("All files", "*.*"));
		setInitialDirectory(fileChooser);
		return fileChooser;
	}

	private void setInitialDirectory(FileChooser fileChooser) {
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
	}
}
