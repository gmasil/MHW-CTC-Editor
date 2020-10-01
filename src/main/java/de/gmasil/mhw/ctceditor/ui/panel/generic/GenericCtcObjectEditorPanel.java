package de.gmasil.mhw.ctceditor.ui.panel.generic;

import java.io.Serializable;
import java.lang.reflect.Field;

public class GenericCtcObjectEditorPanel<T extends Serializable> extends GenericCtcEditorPanel<T> {
	private T object;

	public GenericCtcObjectEditorPanel(String title, Class<T> clazz, T object) {
		super(title, clazz);
		this.object = object;
		initFields();
	}

	@Override
	protected Object[] getValueAsArray(Field field) {
		return getValueAsArray(field, object);
	}

	@Override
	protected Object getValue(Field field) {
		return getValue(field, object);
	}
}
