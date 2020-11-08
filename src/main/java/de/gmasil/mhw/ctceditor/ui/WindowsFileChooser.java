/**
 * MHW-CTC-Editor
 * Copyright © 2020 gmasil.de
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
import java.io.Serializable;

import com.sun.javafx.application.PlatformImpl;

import de.gmasil.mhw.ctceditor.ui.api.FileOpenedListener;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class WindowsFileChooser implements Serializable {
	private FileOpenedListener listener;

	public WindowsFileChooser(FileOpenedListener listener) {
		this.listener = listener;
	}

	public void showOpenDialog() {
		PlatformImpl.startup(() -> {
			FileChooser fileChooser = createFileChooser();
			File selectedFile = fileChooser.showOpenDialog(null);
			if (selectedFile != null) {
				Config.setLastOpenedFile(selectedFile.getAbsolutePath());
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
				Config.setLastOpenedFile(selectedFile.getAbsolutePath());
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
		if (Config.getLastOpenedFile() != null) {
			File lastOpenedFile = new File(Config.getLastOpenedFile());
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
