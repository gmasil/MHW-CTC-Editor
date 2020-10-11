package de.gmasil.mhw.ctceditor.ui.panel;

import java.util.Set;

import de.gmasil.mhw.ctceditor.ctc.CtcBone;
import de.gmasil.mhw.ctceditor.ui.api.CtcChangedCallback;
import de.gmasil.mhw.ctceditor.ui.api.RepaintTreeCallback;
import de.gmasil.mhw.ctceditor.ui.panel.generic.GenericCtcSetEditorPanel;

public class CtcBoneEditorPanel extends GenericCtcSetEditorPanel<CtcBone> {
	public CtcBoneEditorPanel(Set<CtcBone> bones, RepaintTreeCallback treeRepainter,
			CtcChangedCallback ctcChangedCallback) {
		super("CTC Bone", CtcBone.class, bones, treeRepainter, ctcChangedCallback);
	}
}
