package de.gmasil.mhw.ctceditor.ui.api;

import java.io.File;
import java.io.Serializable;

public interface FileOpenedListener extends Serializable {
	public void onFileOpened(File file);

	public void onFileSaved(File file);
}
