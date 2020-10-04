package de.gmasil.mhw.ctceditor.ui.panel.generic;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

public class GenericCtcSetEditorPanel<T extends Serializable> extends GenericCtcEditorPanel<T> {
	private Set<T> objectSet;

	public GenericCtcSetEditorPanel(String title, Class<T> clazz, Set<T> objectSet) {
		super(title, clazz);
		this.objectSet = objectSet;
		initFields();
	}

	public Set<T> getObjectSet() {
		return objectSet;
	}

	@Override
	protected Object[] getValueAsArray(Field field) {
		Object[] original = objectSet.toArray();
		Object[] ret = getValueAsArray(field, original[0]);
		for (int i = 1; i < original.length; i++) {
			Object[] values = getValueAsArray(field, original[i]);
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
}
