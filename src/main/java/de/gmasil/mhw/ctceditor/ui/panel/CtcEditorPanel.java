package de.gmasil.mhw.ctceditor.ui.panel;

import javax.swing.JPanel;

import de.gmasil.mhw.ctceditor.ui.api.DataChangedCallback;

public class CtcEditorPanel extends JPanel implements DataChangedCallback {
	@Override
	public boolean hasDataChanged() {
		return false;
	}
}
