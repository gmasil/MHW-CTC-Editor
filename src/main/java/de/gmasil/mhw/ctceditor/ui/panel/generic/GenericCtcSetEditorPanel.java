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
package de.gmasil.mhw.ctceditor.ui.panel.generic;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import de.gmasil.mhw.ctceditor.ui.api.CtcChangedCallback;
import de.gmasil.mhw.ctceditor.ui.api.RepaintTreeCallback;

public class GenericCtcSetEditorPanel<T extends Serializable> extends GenericCtcEditorPanel<T> {
	private Set<T> objectSet;

	public GenericCtcSetEditorPanel(String title, Class<T> clazz, Set<T> objectSet, RepaintTreeCallback treeRepainter,
			CtcChangedCallback ctcChangedCallback) {
		super(title, clazz, treeRepainter, ctcChangedCallback);
		this.objectSet = objectSet;
		initFields();
	}

	public Set<T> getObjectSet() {
		return objectSet;
	}

	@Override
	protected Object[] getArrayValue(Field field) {
		Object[] original = objectSet.toArray();
		Object[] ret = getArrayValue(field, original[0]);
		for (int i = 1; i < original.length; i++) {
			Object[] values = getArrayValue(field, original[i]);
			for (int x = 0; x < values.length; x++) {
				if (!values[x].equals(ret[x])) {
					ret[x] = null;
				}
			}
		}
		return ret;
	}

	@Override
	protected Object getValue(Field field) {
		Object[] original = objectSet.toArray();
		Object ret = getValue(field, original[0]);
		for (int i = 1; i < original.length; i++) {
			if (!getValue(field, original[i]).equals(ret)) {
				ret = null;
			}
		}
		return ret;
	}

	@Override
	protected boolean setArrayValue(Field field, Object arrayValue, int index)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object[] original = objectSet.toArray();
		boolean changed = false;
		for (int i = 0; i < original.length; i++) {
			if (setArrayValue(field, original[i], arrayValue, index)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	protected boolean setValue(Field field, Object value)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object[] original = objectSet.toArray();
		boolean changed = false;
		for (int i = 0; i < original.length; i++) {
			if (setValue(field, original[i], value)) {
				changed = true;
			}
		}
		return changed;
	}
}
