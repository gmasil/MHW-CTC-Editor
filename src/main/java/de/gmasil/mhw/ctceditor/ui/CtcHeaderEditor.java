package de.gmasil.mhw.ctceditor.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.gmasil.mhw.ctceditor.ctc.CtcHeader;

public class CtcHeaderEditor extends JPanel {
	private CtcHeader header;

	public CtcHeaderEditor(CtcHeader header) {
		this.header = header;
		add(new JLabel("CTC Header"));
	}

}
