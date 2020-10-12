package de.gmasil.mhw.ctceditor.ui.api;

import java.io.Serializable;

public interface CtcChangedCallback extends Serializable {
	public boolean isCtcChanged();

	public void setCtcChanged(boolean changed);
}
