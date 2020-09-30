package de.gmasil.mhw.ctceditor.ui.panel;

import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.gmasil.mhw.ctceditor.ctc.CtcChain;

public class CtcChainEditor extends JPanel {
	private Set<CtcChain> chains;

	public CtcChainEditor(Set<CtcChain> chains) {
		this.chains = chains;
		add(new JLabel("CTC Chain"));
	}
}
