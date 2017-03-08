package org.forpdi.planning.attribute;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.planning.attribute.types.enums.FormatValue;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.caelum.vraptor.boilerplate.HibernateDAO;

/**
 * Classe com implementações de métodos para auxílios nas classes de negócio
 * e para as jobs assíncronas. Contempla regras relacionadas aos atributos.
 * 
 * @author Renato R. R. de Oliveira
 *
 */
@RequestScoped
public class AttributeHelper {

	private final HibernateDAO dao;
	
	/** @deprecated CDI-eyes only */
	protected AttributeHelper() {
		this(null);
	}
	
	@Inject
	public AttributeHelper(HibernateDAO dao) {
		this.dao = dao;
	}
	

	/**
	 * Buscar a instância de um atributo.
	 * 
	 */
	public AttributeInstance retrieveAttributeInstance(StructureLevelInstance levelInstance, Attribute attribute) {
		Criteria criteria = this.dao.newCriteria(AttributeInstance.class);
		criteria.add(Restrictions.eq("levelInstance", levelInstance));
		criteria.add(Restrictions.eq("attribute", attribute));
		AttributeInstance attributeInstance = (AttributeInstance) criteria.uniqueResult();
		if ((attribute.isExpectedField() || attribute.isMaximumField() || attribute.isMinimumField()
				|| attribute.isReachedField()) && attributeInstance != null) {
			AttributeInstance attr = this.retrieveFormatAttributeInstance(levelInstance.getParent());
			FormatValue formatValue = FormatValue.forAttributeInstance(attr);
			attributeInstance.setFormattedValue(attributeInstance.getValueAsNumber() != null
					? formatValue.format(attributeInstance.getValueAsNumber().toString()) : "");
		}
		return attributeInstance;
	}
	
	/** Busca o atributo que define a formatação do valor de um nível. */
	public AttributeInstance retrieveFormatAttributeInstance(Long levelInstanceId) {
		Criteria criteria = this.dao.newCriteria(AttributeInstance.class)
				.createAlias("attribute", "attribute", JoinType.INNER_JOIN)
				.add(Restrictions.eq("attribute.formatField", true))
				.add(Restrictions.eq("levelInstance.id", levelInstanceId));
		return (AttributeInstance) criteria.uniqueResult();
	}
	public AttributeInstance retrieveFormatAttributeInstance(StructureLevelInstance levelInstance) {
		return this.retrieveFormatAttributeInstance(levelInstance.getId());
	}
	

	/** Busca o atributo que define a polaridade do valor de um nível. */
	public AttributeInstance retrievePolarityAttributeInstance(Long levelInstanceId) {
		Criteria criteria = this.dao.newCriteria(AttributeInstance.class);
		criteria.createAlias("attribute", "attribute", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("levelInstance.id", levelInstanceId));
		criteria.add(Restrictions.eq("attribute.polarityField", true));
		AttributeInstance attributeInstance = (AttributeInstance) criteria.uniqueResult();
		return attributeInstance;
	}
	public AttributeInstance retrievePolarityAttributeInstance(StructureLevelInstance levelInstance) {
		return this.retrievePolarityAttributeInstance(levelInstance.getId());
	}
	

	/** Recupera os campos especiais de meta de uma instância de nível.
	 * @param levelInstance Instância de nível que deseja recuperar o campo especial.
	 * @param boolField O nome do campo booleano da tabela Attribute que indica qual campo especial aquele campo é.
	 * @return Instância do atributo especial de meta dessa instância de nível.
	 */
	protected AttributeInstance retrieveSpecialAttribute(StructureLevelInstance levelInstance, String boolField) {
		Criteria criteria = this.dao.newCriteria(AttributeInstance.class);
		criteria
			.createAlias("attribute", "attribute", JoinType.INNER_JOIN)
			.add(Restrictions.eq("levelInstance", levelInstance))
			.add(Restrictions.eq("attribute."+boolField, true))
		;
		return (AttributeInstance) criteria.uniqueResult();
	}
	public AttributeInstance retrieveExpectedFieldAttribute(StructureLevelInstance levelInstance) {
		return this.retrieveSpecialAttribute(levelInstance, "expectedField");
	}
	public AttributeInstance retrieveMinimumFieldAttribute(StructureLevelInstance levelInstance) {
		return this.retrieveSpecialAttribute(levelInstance, "minimumField");
	}
	public AttributeInstance retrieveMaximumFieldAttribute(StructureLevelInstance levelInstance) {
		return this.retrieveSpecialAttribute(levelInstance, "maximumField");
	}
	public AttributeInstance retrieveReachedFieldAttribute(StructureLevelInstance levelInstance) {
		return this.retrieveSpecialAttribute(levelInstance, "reachedField");
	}
	


}
