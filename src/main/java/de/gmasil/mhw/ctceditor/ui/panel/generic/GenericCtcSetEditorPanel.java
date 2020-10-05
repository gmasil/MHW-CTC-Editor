package de.gmasil.mhw.ctceditor.ui.panel.generic;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

import de.gmasil.mhw.ctceditor.ui.api.RepaintTreeCallback;

public class GenericCtcSetEditorPanel<T extends Serializable> extends GenericCtcEditorPanel<T> {
	private Set<T> objectSet;

	public GenericCtcSetEditorPanel(String title, Class<T> clazz, Set<T> objectSet, RepaintTreeCallback treeRepainter) {
		super(title, clazz, treeRepainter);
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
	protected void setArrayValue(Field field, Object arrayValue, int index) {
		Object[] original = objectSet.toArray();
		for (int i = 0; i < original.length; i++) {
			setArrayValue(field, original[i], arrayValue, index);
		}
	}

	@Override
	protected void setValue(Field field, Object value) {
		Object[] original = objectSet.toArray();
		for (int i = 0; i < original.length; i++) {
			setValue(field, original[i], value);
		}
	}
}
