package de.gmasil.mhw.ctceditor.ui.panel.generic;

import java.awt.Component;
import java.awt.Dimension;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.ctc.Info;
import de.gmasil.mhw.ctceditor.ctc.Unknown;
import de.gmasil.mhw.ctceditor.ui.Config;
import de.gmasil.mhw.ctceditor.ui.DecimalUtils;

public abstract class GenericCtcEditorPanel<T extends Serializable> extends BaseCtcEditorPanel {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Class<T> clazz;
	private boolean dataChanged = false;
	private Map<JTextField, String> mapInputToField;
	private Map<String, JTextField> mapFieldToInput;

	public GenericCtcEditorPanel(String title, Class<T> clazz) {
		super(title, true);
		this.clazz = clazz;
	}

	protected void initFields() {
		mapInputToField = new HashMap<>();
		mapFieldToInput = new HashMap<>();
		for (Field field : clazz.getDeclaredFields()) {
			addInputField(field);
		}
	}

	protected abstract Object[] getValueAsArray(Field field);

	protected abstract Object getValue(Field field);

	@Override
	public void onApplyClicked() {
		// TODO: apply field values to object
	}

	@Override
	public void onResetClicked() {
		for (Entry<JTextField, String> entry : mapInputToField.entrySet()) {
			JTextField textField = entry.getKey();
			String fieldName = entry.getValue();
			Object valueToSet;
			if (fieldName.contains("[")) {
				String[] split = fieldName.substring(0, fieldName.length() - 1).split("\\[");
				String arrayFieldName = split[0];
				int index = Integer.parseInt(split[1]);
				Field field = getField(arrayFieldName);
				valueToSet = getValueAsArray(field)[index];
			} else {
				valueToSet = getValue(getField(fieldName));
			}
			setTextFieldValue(textField, valueToSet);
		}
	}

	protected void addInputField(Field field) {
		if (field.getType() == List.class) {
			return;
		}
		if (!Config.getShowUnknownFields() && isFieldUnknown(field)) {
			return;
		}
		if (field.getType().isArray()) {
			Object[] values = getValueAsArray(field);
			String info = "";
			Info annotation = field.getAnnotation(Info.class);
			if (annotation != null) {
				info = annotation.value();
			}
			for (int i = 0; i < values.length; i++) {
				addInputField(field.getName() + "[" + i + "]", values[i], info);
				info = "";
			}
		} else {
			String info = "";
			Info annotation = field.getAnnotation(Info.class);
			if (annotation != null) {
				info = annotation.value();
			}
			addInputField(field.getName(), getValue(field), info);
		}
	}

	protected boolean isFieldUnknown(Field field) {
		return field.getName().startsWith("unknown") || field.isAnnotationPresent(Unknown.class);
	}

	private void addInputField(String name, Object value, String info) {
		if (info == null) {
			info = "";
		}
		getMainPanel().add(prepare(new JLabel(name)));
		JTextField textField = new JTextField("");
		setTextFieldValue(textField, value);
		getMainPanel().add(prepare(textField));
		getMainPanel().add(prepare(new JLabel(info)));
		mapInputToField.put(textField, name);
		mapFieldToInput.put(name, textField);
	}

	private void setTextFieldValue(JTextField textField, Object value) {
		String stringValue;
		if (value == null) {
			stringValue = "";
		} else if (value instanceof Float) {
			stringValue = DecimalUtils.toHumanString((float) value);
		} else {
			stringValue = value.toString();
		}
		textField.setText(stringValue);
	}

	protected Object[] getValueAsArray(Field field, Object object) {
		if (field.getType().isInstance(new int[0])) {
			int[] array = (int[]) getValue(field, object);
			Object[] objects = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				objects[i] = Integer.valueOf(array[i]);
			}
			return objects;
		} else if (field.getType().isInstance(new float[0])) {
			float[] array = (float[]) getValue(field, object);
			Object[] objects = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				objects[i] = Float.valueOf(array[i]);
			}
			return objects;
		} else if (field.getType().isInstance(new byte[0])) {
			byte[] array = (byte[]) getValue(field, object);
			Object[] objects = new Object[array.length];
			for (int i = 0; i < array.length; i++) {
				objects[i] = Byte.valueOf(array[i]);
			}
			return objects;
		} else {
			LOG.warn("Cannot show field {}, unknown array type {}", field.getName(), field.getType().getName());
			return new Object[0];
		}
	}

	protected Object getValue(Field field, Object object) {
		String getter = getGetter(field.getName());
		try {
			Method method = clazz.getMethod(getter);
			return method.invoke(object);
		} catch (Exception e) {
			LOG.error("Error while invoking method {}", getter, e);
			return null;
		}
	}

	private Field getField(String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			LOG.error("Error while getting declared field {}", fieldName, e);
			return null;
		}
	}

	private String getGetter(String fieldName) {
		StringBuilder getter = new StringBuilder();
		getter.append("get");
		// first letter in upper case
		getter.append(("" + fieldName.charAt(0)).toUpperCase());
		getter.append(fieldName.substring(1));
		return getter.toString();
	}

	private Component prepare(Component component) {
		component.setMinimumSize(new Dimension(0, 20));
		component.setMaximumSize(new Dimension(999, 20));
		component.setPreferredSize(new Dimension(50, 20));
		return component;
	}

	@Override
	public boolean hasDataChanged() {
		return dataChanged;
	}
}
