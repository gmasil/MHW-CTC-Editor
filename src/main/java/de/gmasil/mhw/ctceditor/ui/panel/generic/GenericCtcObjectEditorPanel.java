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
package de.gmasil.mhw.ctceditor.ui.panel.generic;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import de.gmasil.mhw.ctceditor.ui.api.CtcChangedCallback;
import de.gmasil.mhw.ctceditor.ui.api.RepaintTreeCallback;

public class GenericCtcObjectEditorPanel<T extends Serializable> extends GenericCtcEditorPanel<T> {
	private T object;

	public GenericCtcObjectEditorPanel(String title, Class<T> clazz, T object, RepaintTreeCallback treeRepainter,
			CtcChangedCallback ctcChangedCallback) {
		super(title, clazz, treeRepainter, ctcChangedCallback);
		this.object = object;
		initFields();
	}

	public T getObject() {
		return object;
	}

	@Override
	protected Object[] getArrayValue(Field field) {
		return getArrayValue(field, object);
	}

	@Override
	protected Object getValue(Field field) {
		return getValue(field, object);
	}

	@Override
	protected boolean setArrayValue(Field field, Object arrayValue, int index)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return setArrayValue(field, object, arrayValue, index);
	}

	@Override
	protected boolean setValue(Field field, Object value)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return setValue(field, object, value);
	}
}
