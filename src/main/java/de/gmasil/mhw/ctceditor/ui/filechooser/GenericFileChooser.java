package de.gmasil.mhw.ctceditor.ui.filechooser;

import de.gmasil.mhw.ctceditor.ui.Config;
import de.gmasil.mhw.ctceditor.ui.api.FileOpenedListener;
import javafx.application.Platform;
import javafx.stage.FileChooser;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class GenericFileChooser {

    protected final FileOpenedListener listener;

    public GenericFileChooser(FileOpenedListener listener) {
        this.listener = listener;
    }

    public void showOpenDialog() {
        JFileChooser chooser = createFileChooser();
        int status = chooser.showOpenDialog(null);
        if (status == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile != null) {
                listener.onFileOpened(selectedFile);
            }
        }
    }

    public void showSaveDialog() {
        JFileChooser chooser = createFileChooser();
        int status = chooser.showSaveDialog(null);
        if (status == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile != null) {
                Config.setLastOpenedFile(selectedFile.getAbsolutePath());
                Config.save();
                listener.onFileSaved(selectedFile);
            }
        }
    }

    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter ctcFileFilter = new FileNameExtensionFilter("CTC files (*.ctc)", "ctc");
        fileChooser.addChoosableFileFilter(ctcFileFilter);
        fileChooser.setFileFilter(ctcFileFilter);
        fileChooser.setCurrentDirectory(new File(Config.getLastOpenedFile()));
        return fileChooser;
    }
}
