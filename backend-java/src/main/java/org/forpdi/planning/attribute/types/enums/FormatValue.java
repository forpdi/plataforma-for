package org.forpdi.planning.attribute.types.enums;

import java.text.NumberFormat;
import java.util.Locale;

import org.forpdi.planning.attribute.AttributeInstance;

import com.ibm.icu.text.DecimalFormat;

public enum FormatValue {

	PERCENTAGE(1, "porcentagem"), NUMERIC(2, "numérico"), MONETARY(3, "monetário"), HOURS(4, "horas"), DAYS(5,
			"dias"), MOTHS(6, "meses");

	public static FormatValue forAttributeInstance(AttributeInstance attr) {
		if (attr == null) {
			return FormatValue.NUMERIC;
		} else {
			switch (attr.getValue()) {
			case "Porcentagem":
				return FormatValue.PERCENTAGE;
			case "Numérico":
				return FormatValue.NUMERIC;
			case "Monetário(R$)":
				return FormatValue.MONETARY;
			case "Horas":
				return FormatValue.HOURS;
			case "Dias":
				return FormatValue.DAYS;
			case "Meses":
				return FormatValue.MOTHS;
			default:
				return FormatValue.NUMERIC;
			}
		}
	}

	private int value;
	private String name;
	Locale ptBr = new Locale("pt", "BR");
	NumberFormat moedaFormat = NumberFormat.getCurrencyInstance(ptBr);
	DecimalFormat decimalFormatDbl = new DecimalFormat("#,##0.00");
	DecimalFormat decimalFormatInt = new DecimalFormat("#,##0");

	private FormatValue(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String format(String value) {
		if (value != null) {
			switch (this.value) {
			case 1:
				return decimalFormatDbl.format(Double.valueOf(value)) + "%";
			case 2:
				return decimalFormatDbl.format(Double.valueOf(value));
			case 3:
				return moedaFormat.format(Double.valueOf(value));
			case 4:
				return decimalFormatInt.format(Double.valueOf(value)) + " hrs";
			case 5:
				if (Double.valueOf(value) > 1) {
					return decimalFormatInt.format(Double.valueOf(value)) + " dias";
				} else {
					return decimalFormatInt.format(Double.valueOf(value)) + " dia";
				}
			case 6:
				return decimalFormatInt.format(Double.valueOf(value)) + " meses";
			default:
				return "valor inválido";
			}
		} else {
			return new String("");
		}
	}
}
