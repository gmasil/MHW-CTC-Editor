package de.gmasil.mhw.ctceditor.ui.panel.generic;

import java.io.Serializable;
import java.lang.reflect.Field;

import de.gmasil.mhw.ctceditor.ui.api.RepaintTreeCallback;

public class GenericCtcObjectEditorPanel<T extends Serializable> extends GenericCtcEditorPanel<T> {
	private T object;

	public GenericCtcObjectEditorPanel(String title, Class<T> clazz, T object, RepaintTreeCallback treeRepainter) {
		super(title, clazz, treeRepainter);
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
	protected void setArrayValue(Field field, Object arrayValue, int index) {
		setArrayValue(field, object, arrayValue, index);
	}

	@Override
	protected void setValue(Field field, Object value) {
		setValue(field, object, value);
	}
}
