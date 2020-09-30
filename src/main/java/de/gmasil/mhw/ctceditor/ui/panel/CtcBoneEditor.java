package de.gmasil.mhw.ctceditor.ui.panel;

import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.gmasil.mhw.ctceditor.ctc.CtcBone;

public class CtcBoneEditor extends JPanel {
	private Set<CtcBone> bones;

	public CtcBoneEditor(Set<CtcBone> bones) {
		this.bones = bones;
		add(new JLabel("CTC Bone"));
	}
}
