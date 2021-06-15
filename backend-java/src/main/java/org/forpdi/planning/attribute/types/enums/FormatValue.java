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
			Double valor = value.length() == 0 ? 0.0d:Double.valueOf(value);
			switch (this.value) {
			case 1:
				return decimalFormatDbl.format( valor) + "%";
			case 2:
				return decimalFormatDbl.format(valor);
			case 3:
				return moedaFormat.format(valor);
			case 4:
				return decimalFormatInt.format(valor) + " hrs";
			case 5:
				if (valor > 1) {
					return decimalFormatInt.format(valor) + " dias";
				} else {
					return decimalFormatInt.format(valor) + " dia";
				}
			case 6:
				return decimalFormatInt.format(valor) + " meses";
			default:
				return "valor inválido";
			}
		} else {
			return new String("");
		}
	}
}
