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
