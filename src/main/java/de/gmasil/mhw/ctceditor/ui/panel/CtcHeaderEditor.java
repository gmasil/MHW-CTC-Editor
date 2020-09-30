package de.gmasil.mhw.ctceditor.ui.panel;

import javax.swing.JButton;
import javax.swing.JLabel;

import de.gmasil.mhw.ctceditor.ctc.CtcHeader;

public class CtcHeaderEditor extends CtcEditorPanel {
	private CtcHeader header;
	private boolean dataChanged = false;

	public CtcHeaderEditor(CtcHeader header) {
		this.header = header;
		add(new JLabel("CTC Header"));
		JButton btnChangeData = new JButton("change");
		btnChangeData.addActionListener(e -> {
			dataChanged = true;
		});
		add(btnChangeData);
	}

	@Override
	public boolean hasDataChanged() {
		return dataChanged;
	}
}
