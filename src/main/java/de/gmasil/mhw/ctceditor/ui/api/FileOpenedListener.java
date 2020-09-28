package de.gmasil.mhw.ctceditor.ui.api;

import java.io.File;

@FunctionalInterface
public interface FileOpenedListener {
	public void onFileOpened(File file);
}
