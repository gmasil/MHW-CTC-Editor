/**
 * MHW-CTC-Editor
 * Copyright © 2020 gmasil.de
 *
 * This file is part of MHW-CTC-Editor.
 *
 * MHW-CTC-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MHW-CTC-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MHW-CTC-Editor. If not, see <https://www.gnu.org/licenses/>.
 */
package de.gmasil.mhw.ctceditor.ui.panel;

import de.gmasil.mhw.ctceditor.ctc.CtcHeader;
import de.gmasil.mhw.ctceditor.ui.api.CtcChangedCallback;
import de.gmasil.mhw.ctceditor.ui.api.RepaintTreeCallback;
import de.gmasil.mhw.ctceditor.ui.panel.generic.GenericCtcObjectEditorPanel;

public class CtcHeaderEditorPanel extends GenericCtcObjectEditorPanel<CtcHeader> {
	public CtcHeaderEditorPanel(CtcHeader header, RepaintTreeCallback treeRepainter,
			CtcChangedCallback ctcChangedCallback) {
		super("CTC Header", CtcHeader.class, header, treeRepainter, ctcChangedCallback);
	}
}
