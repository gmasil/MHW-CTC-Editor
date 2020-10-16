/**
 * MHW-CTC-Editor
 * Copyright © 2020 Simon Oelerich (simon.oelerich@gmasil.de)
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
package de.gmasil.mhw.ctceditor.ui.api;

public interface MenuListener {
	public void menuOpen();

	public void menuSave();

	public void menuSaveAs();

	public void menuClose();

	public void menuExit();

	public void menuCopy();

	public void menuPaste();

	public void menuDelete();

	public void menuFindBoneFunctionID();

	public void menuFindDuplicateBoneFunctionIDs();

	public boolean menuToggleConsole();

	public boolean menuToggleUnknownFields();
}
