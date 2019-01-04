package org.forrisco.risk;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;

import org.forrisco.core.item.Item;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.process.ProcessUnit;
import org.forrisco.core.unit.Unit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;


/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class RiskBS extends HibernateBusiness {
	


	/**
	 * Salva no banco de dados um novo risco
	 * 
	 * @param Risk,
	 *            instância de um risco a ser salvo
	 */
	public void saveRisk(Risk risk) {
		risk.setDeleted(false);
		this.persist(risk);	
	}
	
	/**
	 * Salva no banco de dados graus de risco
	 * 
	 * @param policy,
	 *            instância da política a ser salva
	 */
	public void saveRiskLevel(Policy policy) {
		
		String[][] str =  policy.getRisk_level();
		
		for(int i = 0; i<str[0].length;i++) {
			if(str[1][i] !=null) {
				RiskLevel rk= new RiskLevel();		
				rk.setId(null);
				rk.setColor(Integer.parseInt(str[1][i]));
				rk.setLevel(str[0][i]);
				rk.setPolicy(policy);
				this.persist(rk);	
			}
		}
	}
	
	


	/**
	 * Salva no banco de dados um nova ação preventiva
	 * 
	 * @param PreventiveAction,
	 *            instância de uma ação preventiva a ser salva
	 */
	public void saveAction(PreventiveAction action) {
		action.setDeleted(false);
		this.persist(action);
	}
	
	/**
	 * Retorna uma lista de grau de risco a partir da política
	 * política não salva no banco (acho que da para usar a outra função)
	 * @param policy
	 * 
	 */
	public PaginatedList<RiskLevel> listRiskLevel(Policy policy) {
		List<RiskLevel> array = new  ArrayList<RiskLevel>();
		PaginatedList<RiskLevel> list = new  PaginatedList<RiskLevel>();
		String[][] str =  policy.getRisk_level();
		
		for(int i = 0; i<str[0].length;i++) {
			if(str[1][i] !=null) {
				RiskLevel rk= new RiskLevel();		
				rk.setId(null);
				rk.setColor(Integer.parseInt(str[1][i]));
				rk.setLevel(str[0][i]);
				rk.setPolicy(policy);
				array.add(rk);
			}
		}
		
		list.setList(array);
		list.setTotal(Long.valueOf(array.size()));
		return list;
	}
	
	
	/**
	 * Salva no banco de dados graus de risco
	 * 
	 * @param RiskLevel,
	 *            instância de um grau de risco a ser salvo
	 */
	public void saveRiskLevel(RiskLevel risklevel) {
		risklevel.setDeleted(false);
		this.persist(risklevel);	
	}
	
		
	/**
	 *Deleta do banco de dados  graus de risco
	 * 
	 * @param Item,
	 *			instância do item a ser deletado
	 */
	public void delete(RiskLevel risklevel) {
		risklevel.setDeleted(true);
		this.persist(risklevel);
	}


/**
 * Retorna os graus de risco a partir da política
 * 
 * @param policy
 * 			instância da política
 * @return
 */
	public PaginatedList<RiskLevel> listRiskLevelbyPolicy(Policy policy) {
		PaginatedList<RiskLevel> results = new PaginatedList<RiskLevel>();
		
		Criteria criteria = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy));

		Criteria count = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskLevel.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	
	
	
	/**
	 * Recuperar riscos de uma unidade
	 * 
	 * @param Unit,
	 *            instância da unidade
	 *            
	 */
	public PaginatedList<Risk> listRiskbyUnit(Unit unit) {
		PaginatedList<Risk> results = new PaginatedList<Risk>();
		List<Risk> risks= new ArrayList<Risk>();
		
		Criteria criteria = this.dao.newCriteria(ProcessUnit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit));
		
		for( ProcessUnit pu : this.dao.findByCriteria(criteria, ProcessUnit.class)){
			risks.addAll(this.listRiskbyProcessUnit(pu));
		}
		
		results.setList(risks);
		results.setTotal((long) risks.size());
		
		return results;
	}


	/**
	 * Recuperar lista de riscos a partir de processunit
	 * 
	 * @param ProcessUnit,
	 *            instância processunit
	 * 
	 * @return List<Risk>
	 */
	private List<Risk> listRiskbyProcessUnit(ProcessUnit pu) {
		Criteria criteria = this.dao.newCriteria(Risk.class)
		.add(Restrictions.eq("deleted", false))
		.add(Restrictions.eq("pu", pu));
		
		return this.dao.findByCriteria(criteria, Risk.class);
	}

	//recuperar o risklevel do risco
	//risco->unidade -> plano->politica -> risco level
	/**
	 * Recuperar o grau de risco a partir da probabilidade e impacto
	 * 
	 * @param ProcessUnit,
	 *            instância processunit
	 * 
	 * @return List<Risk>
	 */
	public RiskLevel getRiskLevelbyRisk(Risk risk) {
		
		Unit unit = this.exists(risk.getUnit().getId(), Unit.class);
		PlanRisk planRisk = this.exists(unit.getPlan().getId(), PlanRisk.class);
		Policy policy = this.exists(planRisk.getPolicy().getId(), Policy.class);

		
		int total=policy.getNline()*policy.getNcolumn()+policy.getNline()+policy.getNcolumn();
		String[] aux=policy.getMatrix().split(";");
		String[][] matrix = new String[total][3];
		
		for(int i=0; i< aux.length;i++){
			matrix[i][0]=aux[i].split("\\[.*\\]")[1];
			Pattern pattern = Pattern.compile("\\[.*\\]");
			Matcher matcher = pattern.matcher(aux[i]);
			if(matcher.find()){
				matrix[i][1]=matcher.group(0).split(",")[0].split("\\[")[1];
				matrix[i][2]=matcher.group(0).split(",")[1].split("\\]")[0];
			}
		}
		
		int linha=0;
		int coluna=0;
		for(int i=0;i<policy.getNline();i++){
			if(i%policy.getNline() ==0){
				if(risk.getProbability().equals(matrix[i][0])){
					linha=i;
					i=policy.getNline();
				}
			}
		}
		
		for(int i=total-policy.getNcolumn();i<total;i++){
			coluna+=1;
			if(risk.getImpact().equals(matrix[i][0])){
				//coluna=i-total+policy.getNcolumn()+1;
				i=total;
			}
		}
		
		String result=matrix[linha*policy.getNcolumn()+coluna][0];
		
		Criteria criteria = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy))
				.add(Restrictions.eq("level", result));
		
		return (RiskLevel)criteria.uniqueResult();
		
	}

	
	/**
	 * Recuperar as ações preventivas a partir de um risco
	 * 
	 * @param Risk
	 *            instância de um risco
	 * 
	 * @return PaginatedList<PreventiveAction>
	 */
	public PaginatedList<PreventiveAction> listActionbyRisk(Risk risk) {
		
		PaginatedList<PreventiveAction> results = new PaginatedList<PreventiveAction>();
		
		Criteria criteria = this.dao.newCriteria(PreventiveAction.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(PreventiveAction.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, PreventiveAction.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	public void delete(PreventiveAction action) {
		action.setDeleted(true);
		this.persist(action);
	}

	public void save(PreventiveAction oldaction) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Salva no banco de dados uma nova ação preventiva
	 * 
	 * @param PreventiveAction,
	 *            instância de uma ação preventiva
	 */
	public void saveAction(Risk risk) {
		risk.setDeleted(false);
		this.persist(risk);	
	}

}