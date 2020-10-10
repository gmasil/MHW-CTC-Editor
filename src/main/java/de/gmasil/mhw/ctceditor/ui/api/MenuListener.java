package de.gmasil.mhw.ctceditor.ui.api;

public interface MenuListener {
	public void menuOpen();

	public void menuSave();

	public void menuSaveAs();

	public void menuClose();

	public void menuExit();

	public void menuCopy();

	public void menuPaste();

	public void menuDelete();

	public boolean menuToggleConsole();

	public boolean menuToggleUnknownFields();
}
