package de.gmasil.mhw.ctceditor.ui.panel;

import javax.swing.JLabel;

import de.gmasil.mhw.ctceditor.ctc.CtcHeader;

public class CtcHeaderEditor extends CtcEditorPanel {
	private CtcHeader header;
	private boolean dataChanged = false;

	public CtcHeaderEditor(CtcHeader header) {
		super("CTC Header", true);
		this.header = header;
		for (int i = 0; i < 40; i++) {
			getMainPanel().add(new JLabel("test"));
		}
	}

	@Override
	public boolean hasDataChanged() {
		return dataChanged;
	}
}
