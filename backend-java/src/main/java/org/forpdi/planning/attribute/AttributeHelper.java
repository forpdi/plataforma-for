package org.forpdi.planning.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.planning.attribute.types.enums.FormatValue;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.caelum.vraptor.boilerplate.HibernateDAO;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * Classe com implementações de métodos para auxílios nas classes de negócio e
 * para as jobs assíncronas. Contempla regras relacionadas aos atributos.
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
		// .setProjection(Projections.property("id"));
		criteria.add(Restrictions.eq("levelInstance", levelInstance));
		criteria.add(Restrictions.eq("attribute", attribute));
		criteria.setMaxResults(1);
		// Logger.getLogger(this.getClass()).error("stop "+criteria.list().toString());


		AttributeInstance attributeInstance = (AttributeInstance) criteria.uniqueResult();
		if ((attribute.isExpectedField() || attribute.isMaximumField() || attribute.isMinimumField()
				|| attribute.isReachedField()) && attributeInstance != null) {
			AttributeInstance attr = this.retrieveFormatAttributeInstance(levelInstance.getParent());
			FormatValue formatValue = FormatValue.forAttributeInstance(attr);
			attributeInstance.setFormattedValue(attributeInstance.getValueAsNumber() != null
					? formatValue.format(attributeInstance.getValueAsNumber().toString())
					: "");
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
	
	/**
	 * Busca as instancias de atributos referentes a polaridade relacionados com os ids de StructureLevelInstance passados
	 * 
	 * @param levelInstanceIds lista de ids de StructureLevelInstance
	 * @return lista de atributos que se refere a polaridade
	 */
	public List<AttributeInstance> retrievePolaritiesByLevelInstanceIds(List<Long> levelInstanceIds) {
		if (GeneralUtils.isEmpty(levelInstanceIds)) {
			return Collections.emptyList();
		}
		Criteria criteria = this.dao.newCriteria(AttributeInstance.class)
			.createAlias("attribute", "attribute", JoinType.INNER_JOIN)
			.add(Restrictions.in("levelInstance.id", levelInstanceIds))
			.add(Restrictions.eq("attribute.polarityField", true));
		return this.dao.findByCriteria(criteria, AttributeInstance.class);
	}

	/**
	 * Gera um Map com o id de uma StructureLevelInstance que representa uma meta (goal) e um 
	 * AttributeInstance que representa uma polaridade (polarity) 
	 * 
	 * @param goals lista de metas
	 * @return mapa id da meta / polaridade
	 */
	public Map<Long, AttributeInstance> generatePolarityMap(List<StructureLevelInstance> goals) {
		// cria uma lista com as instancias pai de goals (istancias metas) de onde eh possivel recuperar a polaridade
		// cria um map com os ids de goals e dos pais para facilitar o aceeso posterior 
		List<Long> goalParentIds = new ArrayList<>(goals.size());
		Map<Long, Long> idParentMap = new HashMap<>();
		for (StructureLevelInstance goal : goals) {
			if (goal.getParent() != null) {
				goalParentIds.add(goal.getParent());
				idParentMap.put(goal.getParent(), goal.getId());
			}
		}
		// recupera todas AttributeInstance em que levelInstance possui o campo de polaridade
		 List<AttributeInstance> polarities = this.retrievePolaritiesByLevelInstanceIds(goalParentIds);
		// cria um map para acessar a polaridade atraves do id do goal (meta)
		Map<Long, AttributeInstance> polarityMap = new HashMap<>();
		for (AttributeInstance polarity : polarities) {
			long structureLevelInstanceId = idParentMap.get(polarity.getLevelInstance().getId());
			polarityMap.put(structureLevelInstanceId, polarity);
		}
		return polarityMap;
	}
	
	
	/**
	 * Recupera os campos especiais de meta de uma instância de nível.
	 * 
	 * @param levelInstance
	 *            Instância de nível que deseja recuperar o campo especial.
	 * @param boolField
	 *            O nome do campo booleano da tabela Attribute que indica qual campo
	 *            especial aquele campo é.
	 * @return Instância do atributo especial de meta dessa instância de nível.
	 */
	protected AttributeInstance retrieveSpecialAttribute(StructureLevelInstance levelInstance, String boolField) {
		Criteria criteria = this.dao.newCriteria(AttributeInstance.class);
		criteria.createAlias("attribute", "attribute", JoinType.INNER_JOIN)
				.add(Restrictions.eq("levelInstance", levelInstance))
				.add(Restrictions.eq("attribute." + boolField, true));
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

	public AttributeInstance retrieveFinishDateFieldAttribute(StructureLevelInstance levelInstance) {
		return this.retrieveSpecialAttribute(levelInstance, "finishDate");
	}

}
