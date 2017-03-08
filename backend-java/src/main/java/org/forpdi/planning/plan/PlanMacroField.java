package org.forpdi.planning.plan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.caelum.vraptor.boilerplate.SimpleEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
@Entity(name = PlanMacroField.TABLE)
@Table(
	name = PlanMacroField.TABLE
)
public class PlanMacroField extends SimpleEntity {
	public static final String TABLE = "fpdi_plan_macro_field";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length=255)
	private String label;

	@Column(nullable = true, length=10000)
	private String value;

	@Column(nullable = false, length=32)
	private String type;

	@SkipSerialization
	@ManyToOne(targetEntity=PlanMacro.class, optional=false, fetch=FetchType.EAGER)
	private PlanMacro macro;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PlanMacro getMacro() {
		return macro;
	}

	public void setMacro(PlanMacro macro) {
		this.macro = macro;
	}

}
