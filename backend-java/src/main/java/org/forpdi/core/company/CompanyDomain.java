package org.forpdi.core.company;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.caelum.vraptor.boilerplate.SimpleEntity;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
@Entity(name = CompanyDomain.TABLE)
@Table(name = CompanyDomain.TABLE)
public class CompanyDomain extends SimpleEntity {
	public static final String TABLE = "fpdi_company_domain";
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=128, unique=true)
	private String host;

	@Column(nullable=false, length=255)
	private String baseUrl;
	
	@Column(nullable=false, length=128)
	private String theme = CompanyThemeFactory.getDefaultTheme().getId();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date creation;
	
	@ManyToOne(targetEntity=Company.class, fetch=FetchType.EAGER, optional=false)
	private Company company;
	
}
