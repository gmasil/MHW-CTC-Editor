package de.gmasil.mhw.ctceditor.ui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalUtils {
	private DecimalUtils() {
	}

	public static String toHumanString(float value) {
		return toHumanString((Object) value);
	}

	public static String toHumanString(double value) {
		return toHumanString((Object) value);
	}

	private static String toHumanString(Object value) {
		String ret = value.toString();
		if (ret.contains("E")) {
			String[] split = ret.split("E");
			DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
			df.setMaximumFractionDigits(Math.abs(Integer.parseInt(split[1])));
			ret = df.format(value);
		}
		return ret;
	}
}
