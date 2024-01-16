/**
 * MHW-CTC-Editor
 * Copyright © 2020 gmasil.de
 * <p>
 * This file is part of MHW-CTC-Editor.
 * <p>
 * MHW-CTC-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * MHW-CTC-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with MHW-CTC-Editor. If not, see <https://www.gnu.org/licenses/>.
 */
package de.gmasil.mhw.ctceditor.ui.filechooser;

import de.gmasil.mhw.ctceditor.ui.Config;
import de.gmasil.mhw.ctceditor.ui.api.FileOpenedListener;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

public class WindowsFileChooser extends GenericFileChooser {

    public WindowsFileChooser(FileOpenedListener listener) {
        super(listener);
    }

    static {
        Platform.startup(() -> {});
    }
    
    @Override
    public void showOpenDialog() {
        Platform.runLater(() -> {
            FileChooser fileChooser = createFileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                Config.setLastOpenedFile(selectedFile.getAbsolutePath());
                Config.save();
                listener.onFileOpened(selectedFile);
            }
        });
    }

    @Override
    public void showSaveDialog() {
        Platform.runLater(() -> {
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
