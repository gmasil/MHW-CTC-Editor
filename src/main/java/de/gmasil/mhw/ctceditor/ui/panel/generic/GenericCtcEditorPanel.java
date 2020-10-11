package de.gmasil.mhw.ctceditor.ui.panel.generic;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gmasil.mhw.ctceditor.ctc.Info;
import de.gmasil.mhw.ctceditor.ctc.Readonly;
import de.gmasil.mhw.ctceditor.ctc.Unknown;
import de.gmasil.mhw.ctceditor.ui.Config;
import de.gmasil.mhw.ctceditor.ui.DecimalUtils;
import de.gmasil.mhw.ctceditor.ui.api.CtcChangedCallback;
import de.gmasil.mhw.ctceditor.ui.api.RepaintTreeCallback;

public abstract class GenericCtcEditorPanel<T extends Serializable> extends BaseCtcEditorPanel {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Class<T> clazz;
	private RepaintTreeCallback treeRepainter;
	private CtcChangedCallback ctcChangedCallback;
	private boolean dataChanged = false;
	private Map<JTextField, String> mapInputToField;
	private Map<String, JTextField> mapFieldToInput;

	public GenericCtcEditorPanel(String title, Class<T> clazz, RepaintTreeCallback treeRepainter,
			CtcChangedCallback ctcChangedCallback) {
		super(title, true);
		this.clazz = clazz;
		this.treeRepainter = treeRepainter;
		this.ctcChangedCallback = ctcChangedCallback;
	}

	protected void initFields() {
		mapInputToField = new HashMap<>();
		mapFieldToInput = new HashMap<>();
		for (Field field : clazz.getDeclaredFields()) {
			addInputField(field);
		}
	}

	protected abstract Object[] getArrayValue(Field field);

	protected abstract Object getValue(Field field);

	protected abstract boolean setArrayValue(Field field, Object arrayValue, int index)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

	protected abstract boolean setValue(Field field, Object value)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

	@Override
	public void onApplyClicked() {
		boolean hasCtcDataChanged = false;
		for (Entry<JTextField, String> entry : mapInputToField.entrySet()) {
			JTextField textField = entry.getKey();
			String fieldName = entry.getValue();
			if (fieldName.contains("[")) {
				// TODO: data is marked changed in array procedure even if there is no actual
				// field changed
				String[] split = fieldName.substring(0, fieldName.length() - 1).split("\\[");
				String arrayFieldName = split[0];
				Field field = getField(arrayFieldName);
				if (field != null && !field.isAnnotationPresent(Readonly.class)
						&& textField.getText().trim().length() != 0) {
					int index = Integer.parseInt(split[1]);
					try {
						if (setArrayValue(field, textField.getText(), index)) {
							hasCtcDataChanged = true;
						}
					} catch (NumberFormatException nfe) {
						handleNumberFormatException(field, textField.getText());
					} catch (Exception e) {
						LOG.error("Unexpected error while saving field {}", field.getName(), e);
					}
				}
			} else {
				Field field = getField(fieldName);
				if (field != null && !field.isAnnotationPresent(Readonly.class)
						&& textField.getText().trim().length() != 0) {
					try {
						if (setValue(field, textField.getText())) {
							hasCtcDataChanged = true;
						}
					} catch (NumberFormatException nfe) {
						handleNumberFormatException(field, textField.getText());
					} catch (Exception e) {
						LOG.error("Unexpected error while saving field {}", field.getName(), e);
					}
				}
			}
		}
		dataChanged = false;
		treeRepainter.repaintTree();
		if (hasCtcDataChanged) {
			ctcChangedCallback.setCtcChanged(true);
		}
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
				valueToSet = getArrayValue(field)[index];
			} else {
				valueToSet = getValue(getField(fieldName));
			}
			setTextFieldValue(textField, valueToSet);
		}
		dataChanged = false;
	}

	protected void addInputField(Field field) {
		if (field.getType() == List.class) {
			return;
		}
		if (!Config.getShowUnknownFields() && isFieldUnknown(field)) {
			return;
		}
		boolean readonly = field.isAnnotationPresent(Readonly.class);
		if (field.getType().isArray()) {
			Object[] values = getArrayValue(field);
			String info = "";
			Info annotation = field.getAnnotation(Info.class);
			if (annotation != null) {
				info = annotation.value();
			}
			for (int i = 0; i < values.length; i++) {
				addInputField(field.getName() + "[" + i + "]", values[i], info, readonly);
				info = "";
			}
		} else {
			String info = "";
			Info annotation = field.getAnnotation(Info.class);
			if (annotation != null) {
				info = annotation.value();
			}
			addInputField(field.getName(), getValue(field), info, readonly);
		}
	}

	protected boolean isFieldUnknown(Field field) {
		return field.getName().startsWith("unknown") || field.isAnnotationPresent(Unknown.class);
	}

	private void addInputField(String name, Object value, String info, boolean readonly) {
		if (info == null) {
			info = "";
		}
		getMainPanel().add(new JLabel(name));
		JTextField textField = new JTextField("");
		textField.setEditable(!readonly);
		setTextFieldValue(textField, value);
		addChangeListener(textField, e -> dataChanged = true);
		getMainPanel().add(textField, "w 120::");
		getMainPanel().add(new JLabel(info));
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

	protected Object[] getArrayValue(Field field, Object object) {
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

	protected boolean setArrayValue(Field field, Object object, Object arrayValue, int index)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String setter = getSetter(field.getName());
		Method method = clazz.getMethod(setter, field.getType());
		Object[] currentArray = getArrayValue(field, object);
		if (field.getType().isInstance(new int[0])) {
			return handleIntArrayValue(object, arrayValue, index, method, currentArray);
		} else if (field.getType().isInstance(new float[0])) {
			return handleFloatArrayValue(object, arrayValue, index, method, currentArray);
		} else if (field.getType().isInstance(new byte[0])) {
			return handleByteArrayValue(object, arrayValue, index, method, currentArray);
		} else {
			LOG.error("Unknown argument array type {} during invocation of {}", field.getType(), setter);
		}
		return false;
	}

	private boolean handleIntArrayValue(Object object, Object arrayValue, int index, Method method,
			Object[] currentArray) throws IllegalAccessException, InvocationTargetException {
		int[] currentIntArray = convertToIntArray(currentArray);
		int intArrayValue = Integer.parseInt((String) arrayValue);
		if (currentIntArray[index] != intArrayValue) {
			currentIntArray[index] = intArrayValue;
			method.invoke(object, currentIntArray);
			return true;
		}
		return false;
	}

	private boolean handleFloatArrayValue(Object object, Object arrayValue, int index, Method method,
			Object[] currentArray) throws IllegalAccessException, InvocationTargetException {
		float[] currentFloatArray = convertToFloatArray(currentArray);
		float floatArrayValue = Float.parseFloat((String) arrayValue);
		if (currentFloatArray[index] != floatArrayValue) {
			currentFloatArray[index] = floatArrayValue;
			method.invoke(object, currentFloatArray);
			return true;
		}
		return false;
	}

	private boolean handleByteArrayValue(Object object, Object arrayValue, int index, Method method,
			Object[] currentArray) throws IllegalAccessException, InvocationTargetException {
		byte[] currentByteArray = convertToByteArray(currentArray);
		byte byteArrayValue = Byte.parseByte((String) arrayValue);
		if (currentByteArray[index] != byteArrayValue) {
			currentByteArray[index] = byteArrayValue;
			method.invoke(object, currentByteArray);
			return true;
		}
		return false;
	}

	protected boolean setValue(Field field, Object object, Object value)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String setter = getSetter(field.getName());
		Method method = clazz.getMethod(setter, field.getType());
		if (field.getType() == int.class) {
			return handleIntValue(field, object, value, method);
		} else if (field.getType() == float.class) {
			return handleFloatValue(field, object, value, method);
		} else if (field.getType() == byte.class) {
			return handleByteValue(field, object, value, method);
		} else {
			LOG.error("Unknown argument type {} during invocation of {}", field.getType(), setter);
		}
		return false;
	}

	private boolean handleIntValue(Field field, Object object, Object value, Method method)
			throws IllegalAccessException, InvocationTargetException {
		int oldValue = (int) getValue(field, object);
		int newValue = Integer.parseInt((String) value);
		if (oldValue != newValue) {
			method.invoke(object, newValue);
			return true;
		}
		return false;
	}

	private boolean handleFloatValue(Field field, Object object, Object value, Method method)
			throws IllegalAccessException, InvocationTargetException {
		float oldValue = (float) getValue(field, object);
		float newValue = Float.parseFloat((String) value);
		if (oldValue != newValue) {
			method.invoke(object, newValue);
			return true;
		}
		return false;
	}

	private boolean handleByteValue(Field field, Object object, Object value, Method method)
			throws IllegalAccessException, InvocationTargetException {
		byte oldValue = (byte) getValue(field, object);
		byte newValue = Byte.parseByte((String) value);
		if (oldValue != newValue) {
			method.invoke(object, newValue);
			return true;
		}
		return false;
	}

	private void handleNumberFormatException(Field field, Object value) {
		LOG.error("Cannot assign value '{}' to field {} of type {}", value, field.getName(),
				field.getType().getSimpleName());
	}

	private int[] convertToIntArray(Object object) {
		Object[] array = (Object[]) object;
		int[] ints = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			if (array[i].getClass() == Integer.class) {
				ints[i] = (int) array[i];
			} else {
				ints[i] = Integer.parseInt((String) array[i]);
			}
		}
		return ints;
	}

	private float[] convertToFloatArray(Object object) {
		Object[] array = (Object[]) object;
		float[] floats = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			if (array[i].getClass() == Float.class) {
				floats[i] = (float) array[i];
			} else {
				floats[i] = Float.parseFloat((String) array[i]);
			}
		}
		return floats;
	}

	private byte[] convertToByteArray(Object object) {
		Object[] array = (Object[]) object;
		byte[] bytes = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			if (array[i].getClass() == Byte.class) {
				bytes[i] = (byte) array[i];
			} else {
				bytes[i] = Byte.parseByte((String) array[i]);
			}
		}
		return bytes;
	}

	private Field getField(String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			LOG.error("Error while getting declared field {}", fieldName, e);
			return null;
		}
	}

	private String getSetter(String fieldName) {
		return getFieldFunction(fieldName, "set");
	}

	private String getGetter(String fieldName) {
		return getFieldFunction(fieldName, "get");
	}

	private String getFieldFunction(String fieldName, String prefix) {
		StringBuilder getter = new StringBuilder();
		getter.append(prefix);
		// first letter in upper case
		getter.append(("" + fieldName.charAt(0)).toUpperCase());
		getter.append(fieldName.substring(1));
		return getter.toString();
	}

	@Override
	public boolean hasDataChanged() {
		return dataChanged;
	}

	public static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
		Objects.requireNonNull(text);
		Objects.requireNonNull(changeListener);
		DocumentListener dl = new DocumentListener() {
			private int lastChange = 0;
			private int lastNotifiedChange = 0;

			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				lastChange++;
				if (lastNotifiedChange != lastChange) {
					lastNotifiedChange = lastChange;
					changeListener.stateChanged(new ChangeEvent(text));
				}
			}
		};
		text.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
			Document d1 = (Document) e.getOldValue();
			Document d2 = (Document) e.getNewValue();
			if (d1 != null) {
				d1.removeDocumentListener(dl);
			}
			if (d2 != null) {
				d2.addDocumentListener(dl);
			}
			dl.changedUpdate(null);
		});
		Document d = text.getDocument();
		if (d != null) {
			d.addDocumentListener(dl);
		}
	}
}
