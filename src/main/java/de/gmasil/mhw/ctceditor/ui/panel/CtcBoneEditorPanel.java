package de.gmasil.mhw.ctceditor.ui.panel;

import java.util.Set;

import de.gmasil.mhw.ctceditor.ctc.CtcBone;
import de.gmasil.mhw.ctceditor.ui.panel.generic.GenericCtcSetEditorPanel;

public class CtcBoneEditorPanel extends GenericCtcSetEditorPanel<CtcBone> {
	public CtcBoneEditorPanel(Set<CtcBone> bones) {
		super("CTC Bone", CtcBone.class, bones);
	}
}