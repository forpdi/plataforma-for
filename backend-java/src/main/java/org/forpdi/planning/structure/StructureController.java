package org.forpdi.planning.structure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.company.CompanyUser;
import org.forpdi.core.event.Current;
import org.forpdi.core.notification.NotificationBS;
import org.forpdi.core.notification.NotificationSetting;
import org.forpdi.core.notification.NotificationType;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.planning.attribute.AggregateIndicator;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.attribute.AttributeHelper;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.attribute.AttributeTypeFactory;
import org.forpdi.planning.attribute.types.DateField;
import org.forpdi.planning.attribute.types.ResponsibleField;
import org.forpdi.planning.fields.FieldsBS;
import org.forpdi.planning.fields.budget.Budget;
import org.forpdi.planning.fields.budget.BudgetBS;
import org.forpdi.planning.fields.budget.BudgetElement;
import org.forpdi.planning.permissions.ManagePlanPermission;
import org.forpdi.planning.permissions.ManageStructurePermission;
import org.forpdi.planning.permissions.UpdateGoalPermission;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanBS;
import org.forpdi.planning.plan.PlanMacro;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.boilerplate.Cached;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;
import br.com.caelum.vraptor.observer.upload.UploadedFile;

/**
 * @author Renato R. R. de Oliveira
 */
@Controller
public class StructureController extends AbstractController {

	@Inject
	@Current
	private CompanyDomain domain;
	@Inject
	private StructureBS bs;
	@Inject
	private StructureHelper helper;
	@Inject
	private AttributeHelper attrHelper;
	@Inject
	private PlanBS planBS;
	@Inject
	private NotificationBS notificationBS;
	@Inject
	private UserBS userBS;
	@Inject
	private BudgetBS budgetBS;
	@Inject
	private FieldsBS fieldBs;

	/**
	 * Listar os tipos de atributos existentes.
	 * 
	 * @param void
	 * 
	 * @return void
	 */
	@Get(BASEPATH + "/structure/attributetypes")
	@Cached(Cached.ONE_WEEK)
	public void listAttributeTypes() {
		try {
			this.response.setCharacterEncoding("UTF-8");
			this.response.setContentType("application/json");
			OutputStreamWriter writer = new OutputStreamWriter(this.response.getOutputStream(),
					Charset.forName("UTF-8"));
			writer.write(AttributeTypeFactory.getInstance().toJSON());
			writer.flush();
			writer.close();
			this.result.nothing();
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Importar o xml da estrutura dos planos.
	 * 
	 * @param file
	 *            Arquivo xml.
	 * @return structure Estrutura importada.
	 */
	@Post(BASEPATH + "/structure/import")
	@NoCache
	@Permissioned(value = AccessLevels.SYSTEM_ADMIN, permissions = { ManageStructurePermission.class })
	public void importStructure(UploadedFile file) {
		try {
			if (file == null) {
				this.fail("Nenhum arquivo enviado.");
			} else {
				String extension = file.getFileName().substring(file.getFileName().lastIndexOf("."),
						file.getFileName().length());
				if (!extension.equals(".xml")) {
					this.fail("Formato de arquivo inválido. Por favor, importe um arquivo no formato XML");
				} else {
					File temp = File.createTempFile("fpdi", "readedStructure-import");
					FileOutputStream copyStream = new FileOutputStream(temp);
					GeneralUtils.streamingPipe(file.getFile(), copyStream);
					copyStream.close();
					Structure structure = this.bs.importStructure(temp);
					temp.delete();
					structure.setCompany(this.domain.getCompany());
					this.bs.persist(structure);
					this.success(structure);
				}
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Salvar a estrutura dos planos no banco de dados.
	 * 
	 * @param structure
	 *            Estrutura a se salva.
	 * @return structure Estrutura salva.
	 */
	@Post(BASEPATH + "/structure")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.SYSTEM_ADMIN, permissions = { ManageStructurePermission.class })
	public void saveStructure(@NotNull @Valid Structure structure) {
		try {
			structure.setId(null);
			structure.setCompany(this.domain.getCompany());
			this.bs.persist(structure);
			this.success(structure);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Atualizar a estrutura no banco de dados.
	 * 
	 * @param structure
	 *            Estrutura a ser atualizada.
	 * @return structure Estrutura atualizada
	 */
	@Put(BASEPATH + "/structure")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.SYSTEM_ADMIN, permissions = { ManageStructurePermission.class })
	public void updateStructure(@NotNull @Valid Structure structure) {
		try {
			Structure existent = this.bs.exists(structure.getId(), Structure.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			existent.setDescription(structure.getDescription());
			existent.setName(structure.getName());
			this.bs.persist(existent);
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Buscar a estrutura pelo id.
	 * 
	 * @param id
	 *            Id da estrutura a ser buscada.
	 * @return structure Estrutura encontrada.
	 */
	@Get(BASEPATH + "/structure/{id}")
	@NoCache
	@Permissioned
	public void retrieveStructure(Long id) {
		try {
			Structure structure = this.bs.exists(id, Structure.class);
			if (structure == null) {
				this.fail("A empresa solicitada não foi encontrada.");
			} else {
				structure.setLevels(this.bs.listLevels(structure).getList());
				for (StructureLevel level : structure.getLevels()) {
					level.setAttributes(this.bs.listAttributes(level).getList());
				}
				this.success(structure);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Listar as estruturas por página.
	 * 
	 * @param page
	 *            Número da página a ser listada.
	 * @return structures Lista de estruturas.
	 */
	@Get(BASEPATH + "/structure")
	@NoCache
	@Permissioned
	public void listStructures() {
		try {
			PaginatedList<Structure> structures = this.bs.list();
			for (Structure structure : structures.getList()) {
				structure.setLevels(this.bs.listLevels(structure).getList());
				for (StructureLevel level : structure.getLevels()) {
					level.setAttributes(this.bs.listAttributes(level).getList());
				}
			}
			this.success(structures);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Deletar a estrutura pelo id.
	 * 
	 * @param id
	 *            Id da estrutura a ser deletada.
	 * @return void
	 */
	@Delete(BASEPATH + "/structure/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.SYSTEM_ADMIN, permissions = { ManageStructurePermission.class })
	public void delete(@NotNull Long id) {
		try {
			Structure existent = this.bs.exists(id, Structure.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}

			existent.setDeleted(true);
			this.bs.persist(existent);
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Listar as instâncias de um level.
	 * 
	 * @param levelId
	 *            Id do level.
	 * @param planId
	 *            Id do plano de metas.
	 * @param parentId
	 *            Id do level pai.
	 * @return list Lista de instâncias de um level.
	 */
	@Get(BASEPATH + "/structure/levelInstancelist")
	@Consumes
	@NoCache
	@Permissioned
	public void listLevelInstance(Long levelId, Long planId, Long parentId) {
		try {
			StructureLevel level = this.bs.retrieveById(levelId);
			PaginatedList<Attribute> attributeList = this.bs.listAttributes(level);

			Plan plan = this.planBS.retrieveById(planId);
			if (parentId == 0)
				parentId = null;
			boolean haveBudget = false;
			PaginatedList<StructureLevelInstance> list = this.bs.listLevelsInstance(level, plan, parentId);
			for (Attribute atr : attributeList.getList()) {
				if (atr.getType().matches("org.forpdi.planning.attribute.types.BudgetField")) {
					haveBudget = true;
				}

			}
			List<StructureLevelInstance> structureList = list.getList();
			for (int count = 0; count < structureList.size(); count++) {
				structureList.get(count).setHaveBudget(haveBudget);
			}
			list.setList(structureList);
			if (list.getTotal() > 0) {
				this.success(list);
			} else
				this.success(level);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/** Listar instâncias de níveis para exibição de performance. */
	@Get(BASEPATH + "/structure/levelinstance/performance")
	@Consumes
	@NoCache
	@Permissioned
	public void listLevelInstancePerformance(Long planId, Long parentId) {
		try {
			Plan plan = this.planBS.retrieveById(planId);
			if (parentId == 0)
				parentId = null;
			PaginatedList<StructureLevelInstance> list = this.bs.listLevelsInstance(plan, parentId);
			List<StructureLevelInstanceDetailed> levelInstanceDetailedList = new ArrayList<StructureLevelInstanceDetailed>();
			for (StructureLevelInstance levelInstance : list.getList()) {
				levelInstanceDetailedList = this.bs.listLevelInstanceDetailed(levelInstance);
				levelInstance.setLevelInstanceDetailedList(new ArrayList<StructureLevelInstanceDetailed>());
				for (int i = 0; i < 12; i++) {
					StructureLevelInstanceDetailed levelInstDetailed = null;
					for (StructureLevelInstanceDetailed levelInstanceDetailed : levelInstanceDetailedList) {
						levelInstanceDetailed.setLevelInstance(null);
						if (levelInstanceDetailed.getMonth() == i + 1) {
							levelInstDetailed = levelInstanceDetailed;
						}
					}
					levelInstance.getLevelInstanceDetailedList().add(levelInstDetailed);
				}
			}
			this.success(list);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Criar uma instância de um level.
	 * 
	 * @param planId
	 *            Id do plano.
	 * @param levelId
	 *            Id do level.
	 * @param instanceName
	 *            Nome do levelInstance.
	 * @param parentId
	 *            Id do level pai.
	 * @return levelInstance Retorna a instância criada.
	 */
	@Post(BASEPATH + "/structure/levelinstance")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanPermission.class })
	public void createLevelInstance(Long planId, Long levelId, String instanceName, Long parentId) {
		try {
			Plan plan = this.planBS.retrieveById(planId);
			StructureLevel level = this.bs.retrieveById(levelId);
			if (parentId != null) {
				StructureLevelInstance parent = this.bs.retrieveLevelInstance(parentId);
				if (parent.isAggregate()) {
					this.fail("Indicadores agregados não podem ter metas");
					return;
				}
			}

			if (plan == null || level == null) {
				this.fail("Plano ou estrutura incorretos!");
			} else {
				StructureLevelInstance levelInstance = new StructureLevelInstance();
				levelInstance.setName(instanceName);
				levelInstance.setDeleted(false);
				levelInstance.setPlan(plan);
				levelInstance.setLevel(level);
				levelInstance.setCreation(new Date());
				levelInstance.setParent(parentId);
				this.bs.persist(levelInstance);
				this.bs.updateLevelValues(levelInstance);
				AttributeInstance attrInst = this.attrHelper
						.retrievePolarityAttributeInstance(levelInstance.getParent());
				if (attrInst != null)
					levelInstance.setPolarity(attrInst.getValue());
				this.success(levelInstance);
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Buscar uma instância de um level pelo id
	 * 
	 * @param levelInstanceId
	 *            Id da instância.
	 * @return levelInstance Instância encontrada.
	 */
	@Get(BASEPATH + "/structure/levelinstance")
	@Consumes
	@NoCache
	@Permissioned
	public void retrieveLevelInstance(Long levelInstanceId) {
		try {
			StructureLevelInstance levelInstance = this.bs.retrieveLevelInstance(levelInstanceId);
			this.success(levelInstance);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Listar os atributos de um level.
	 * 
	 * @param id
	 *            Id da instância de um level.
	 * @return levelInstance Instância de um level com a lista de atributos.
	 */
	@Get(BASEPATH + "/structure/levelattributes")
	@Consumes
	@NoCache
	@Permissioned
	public void retrieveLevelAttributes(Long id) {
		try {
			StructureLevelInstance levelInstance = this.bs.retrieveLevelInstanceNoDeleted(id);

			if (levelInstance == null) {
				this.fail("Estrutura incorreta!");
			} else {
				List<Attribute> attributeList = this.bs.retrieveLevelAttributes(levelInstance.getLevel());
				attributeList = this.bs.setAttributesInstances(levelInstance, attributeList);
				levelInstance.getLevel().setAttributes(attributeList);

				AttributeInstance attributeInstance = null;
				if (levelInstance.getLevel().isIndicator())
					attributeInstance = this.attrHelper.retrievePolarityAttributeInstance(levelInstance);
				else
					attributeInstance = this.attrHelper.retrievePolarityAttributeInstance(levelInstance.getParent());
				if (attributeInstance != null)
					levelInstance.setPolarity(attributeInstance.getValue());

				levelInstance.setParents(this.bs.setParents(levelInstance));

				PaginatedList<FavoriteLevelInstance> favorites = this.bs
						.listFavoriteLevelInstances(levelInstance.getPlan().getParent());
				boolean favoriteExistent = false;
				for (int i = 0; i < favorites.getList().size(); i++) {
					if (favorites.getList().get(i).getLevelInstance().getId() == levelInstance.getId())
						favoriteExistent = true;
				}
				levelInstance.setFavoriteExistent(favoriteExistent);
				levelInstance.setFavoriteTotal(favorites.getList().size());
				levelInstance.setIndicatorList(this.bs.listAggregateIndicatorsByAggregate(levelInstance));

				this.success(levelInstance);
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Salvar as instâncias dos atributos de um level.
	 * 
	 * @param levelInstance
	 *            Instância de um level com a lista de atributos e seus valores.
	 * @return existentLevelInstance Instância com a lista de atributos e seus
	 *         valores salvos.
	 */
	@Post(BASEPATH + "/structure/levelattributes")
	@Consumes
	@NoCache
	// @Permissioned(value = AccessLevels.MANAGER, permissions = {
	// ManagePlanPermission.class })
	@Permissioned(value = AccessLevels.COLABORATOR)
	public void saveLevelAttributesInstance(StructureLevelInstance levelInstance, String url) {
		try {
			boolean changeResponsible = false;
			boolean changeDate = false;
			String userId = "";
			String urlAux = "";

			String mainUrl[] = url.split("\\?"); // remoção de parâmetro na url
			if (mainUrl.length > 0) {
				url = mainUrl[0];
			}

			StructureLevelInstance existentLevelInstance = this.bs.retrieveLevelInstance(levelInstance.getId());
			if (existentLevelInstance == null) {
				this.fail("Estrutura incorreta!");
			} else if (existentLevelInstance.isClosed()) {
				this.fail("Esta meta já foi concluída!");
			} else {
				if (!existentLevelInstance.getName().equals(levelInstance.getName())) {
					existentLevelInstance.setName(levelInstance.getName());
					this.bs.persist(existentLevelInstance);
				}
				List<Attribute> attributes = new ArrayList<>();
				for (int i = 0; i < levelInstance.getLevel().getAttributes().size(); i++) {
					Attribute attribute = this.bs
							.retrieveAttribute(levelInstance.getLevel().getAttributes().get(i).getId());
					if (attribute == null) {
						this.fail("Atributo incorreto!");
					} else {
						AttributeInstance attInst = this.bs.valueStringToValueType(attribute,
								levelInstance.getLevel().getAttributes().get(i).getAttributeInstance().getValue());
						AttributeInstance attributeInstance = this.attrHelper
								.retrieveAttributeInstance(existentLevelInstance, attribute);
						if (attributeInstance == null)
							attributeInstance = new AttributeInstance();
						if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
							userId = attInst.getValue();
							if (attributeInstance.getValue() == null
									|| !attributeInstance.getValue().equals(attInst.getValue())) {
								changeResponsible = true;
							}
						}
						if (attribute.getType().equals(DateField.class.getCanonicalName())) {
							if (attributeInstance.getValue() != null) {
								if (!attributeInstance.getValue().equals(attInst.getValue())) {
									changeDate = true;
								}
							}
						}
						attribute.setUsers(this.userBS.listUsersByCompany().getList());
						attributeInstance.setDeleted(false);
						attributeInstance.setCreation(new Date());
						attributeInstance.setLevelInstance(existentLevelInstance);
						attributeInstance.setAttribute(attribute);
						attributeInstance.setValue(attInst.getValue());
						attributeInstance.setValueAsNumber(attInst.getValueAsNumber());
						attributeInstance.setValueAsDate(attInst.getValueAsDate());
						this.bs.persist(attributeInstance);
						attribute.setAttributeInstance(attributeInstance);
						attributes.add(attribute);
					}
				}
				existentLevelInstance.setAggregate(levelInstance.isAggregate());
				existentLevelInstance.setCalculation(levelInstance.getCalculation());
				if (existentLevelInstance.isAggregate()) {
					StructureLevelInstance indicator = this.bs.isAggregating(existentLevelInstance);
					if (indicator != null) {
						this.fail("Esse indicador já está agregado ao indicador " + indicator.getName() + ".");
						return;
					}
				}
				this.bs.persist(existentLevelInstance);
				attributes = this.bs.setAttributesInstances(existentLevelInstance, attributes);
				existentLevelInstance.getLevel().setAttributes(attributes);

				if (existentLevelInstance.isAggregate()) {
					this.bs.saveIndicators(levelInstance.getIndicatorList());
				}
				existentLevelInstance = this.bs.retrieveLevelInstance(existentLevelInstance.getId());
				this.bs.updateLevelValues(existentLevelInstance);
				existentLevelInstance.setParents(this.bs.setParents(existentLevelInstance));
				StructureLevel nextLevel = this.bs.retrieveNextLevel(existentLevelInstance.getLevel().getStructure(),
						existentLevelInstance.getLevel().getSequence() + 1);
				existentLevelInstance.setLevelSon(nextLevel);
				if (changeResponsible) {
					urlAux = domain.getBaseUrl() + "/" + url;
					User responsible = this.userBS.existsByUser(Long.parseLong(userId));
					CompanyUser companyUser = this.userBS.retrieveCompanyUser(responsible, this.domain.getCompany());
					if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting() || companyUser
							.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL.getSetting()) {
						this.notificationBS.sendNotification(NotificationType.ATTRIBUTED_RESPONSIBLE,
								existentLevelInstance.getName(), existentLevelInstance.getLevel().getName(),
								Long.parseLong(userId), urlAux);
						this.notificationBS.sendNotificationEmail(NotificationType.ATTRIBUTED_RESPONSIBLE,
								existentLevelInstance.getName(), existentLevelInstance.getLevel().getName(),
								responsible, urlAux);
					} else if (companyUser.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL
							.getSetting()) {
						this.notificationBS.sendNotification(NotificationType.ATTRIBUTED_RESPONSIBLE,
								existentLevelInstance.getName(), existentLevelInstance.getLevel().getName(),
								responsible.getId(), urlAux);
					}
				}

				if (changeDate) {
					urlAux = domain.getBaseUrl() + "/" + url;
					User responsible = this.userBS.existsByUser(Long.parseLong(userId));
					CompanyUser companyUser = this.userBS.retrieveCompanyUser(responsible, this.domain.getCompany());
					if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting() || companyUser
							.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL.getSetting()) {
						this.notificationBS.sendNotification(NotificationType.DATE_ATTRIBUTE_UPDATED,
								existentLevelInstance.getName(), existentLevelInstance.getLevel().getName(),
								Long.parseLong(userId), urlAux);
						this.notificationBS.sendNotificationEmail(NotificationType.DATE_ATTRIBUTE_UPDATED,
								existentLevelInstance.getName(), existentLevelInstance.getLevel().getName(),
								responsible, urlAux);
					} else if (companyUser.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL
							.getSetting()) {
						this.notificationBS.sendNotification(NotificationType.DATE_ATTRIBUTE_UPDATED,
								existentLevelInstance.getName(), existentLevelInstance.getLevel().getName(),
								responsible.getId(), urlAux);
					}
				}

				AttributeInstance attrInst = null;
				if (existentLevelInstance.getLevel().isIndicator())
					attrInst = this.attrHelper.retrievePolarityAttributeInstance(existentLevelInstance);
				else
					attrInst = this.attrHelper.retrievePolarityAttributeInstance(existentLevelInstance.getParent());
				if (attrInst != null)
					existentLevelInstance.setPolarity(attrInst.getValue());

				List<Attribute> attributeList = this.bs.retrieveLevelAttributes(existentLevelInstance.getLevel());
				attributeList = this.bs.setAttributesInstances(existentLevelInstance, attributeList);
				existentLevelInstance.getLevel().setAttributes(attributeList);
				this.success(existentLevelInstance);
			}
		} catch (

		Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Deletar uma instância de um level.
	 * 
	 * @param levelInstance
	 *            Instância a ser deletada.
	 * @return existentLevelInstance Instância que foi deletada.
	 */
	@Post(BASEPATH + "/structure/levelinstance/delete")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanPermission.class })
	public void deleteLevelAttributesInstance(StructureLevelInstance levelInstance) {
		try {
			StructureLevelInstance existentLevelInstance = this.bs.retrieveLevelInstance(levelInstance.getId());
			if (existentLevelInstance == null) {
				this.fail("Estrutura incorreta!");
			} else {
				StructureLevelInstance indicator = this.bs.isAggregating(existentLevelInstance);
				if (indicator != null) {
					this.fail("Não pode ser excluído, está agregado ao indicador " + indicator.getName() + ".");
					return;
				}
				if(this.bs.checkHaveBudgetByLevel(existentLevelInstance.getLevel())){
					List<Budget> budgetList = this.budgetBS.listBudgetByLevelInstance(existentLevelInstance);
					for (Budget budget : budgetList) {
						BudgetElement budgetElement = this.budgetBS.budgetElementExistsById(budget.getBudgetElement().getId());
						if(budgetElement != null){
							double balanceAvailable = budgetElement.getBalanceAvailable();
							balanceAvailable += budget.getCommitted();
							budgetElement.setBalanceAvailable(balanceAvailable);
							budgetElement.setLinkedObjects(budgetElement.getLinkedObjects() - 1);
							this.budgetBS.update(budgetElement);
							this.fieldBs.deleteBudget(budget);
						}
					}				
				}
				existentLevelInstance.setDeleted(true);
				this.bs.persist(existentLevelInstance);
				this.bs.updateLevelValues(existentLevelInstance);

				PaginatedList<FavoriteLevelInstance> favorites = this.bs
						.listFavoriteByLevelInstance(existentLevelInstance);
				for (FavoriteLevelInstance favorite : favorites.getList()) {
					favorite.setDeleted(true);
					this.bs.persist(favorite);
				}
			}
			this.success(existentLevelInstance);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Listar instâncias dos leveis filhos do level especificado.
	 * 
	 * @param parentId
	 *            Id do level pai.
	 * @return levelInstance Instância do level pai com uma lista de instâncias
	 *         dos leveis filhos.
	 */
	@Get(BASEPATH + "/structure/levelinstance/levelsons")
	@Consumes
	@NoCache
	@Permissioned
	public void levelInstanceSons(Long parentId, Integer page, Integer pageSize) {
		try {
			PaginatedList<StructureLevelInstance> levelInstances = this.bs.retrieveLevelInstanceSons(parentId, page,
					pageSize);
			boolean goal = false;
			for (int i = 0; i < levelInstances.getList().size(); i++) {
				goal = levelInstances.getList().get(i).getLevel().isGoal();
				levelInstances.getList().get(i).getLevel()
						.setAttributes(this.bs.retrieveLevelSonsAttributes(levelInstances.getList().get(i)));
			}
			if (goal) {
				AttributeInstance attributeInstance = this.attrHelper.retrievePolarityAttributeInstance(parentId);
				String polarity = null;
				if (attributeInstance != null)
					polarity = attributeInstance.getValue();
				this.bs.setGoalStatus(levelInstances.getList(), polarity);
			}
			StructureLevelInstance levelInstance = new StructureLevelInstance();
			levelInstance = this.bs.retrieveLevelInstance(parentId);
			levelInstance.setSons(levelInstances);

			BigInteger structureId = this.bs.retrieveStructureIdByLevel(levelInstance.getLevel());
			Structure structure = new Structure();
			structure.setId(structureId.longValue());
			StructureLevel nextLevel = this.bs.retrieveNextLevel(structure, levelInstance.getLevel().getSequence() + 1);
			levelInstance.setLevelSon(nextLevel);

			this.success(levelInstance);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Fechar ou reabrir a instância de um level meta.
	 * 
	 * @param id
	 *            Id da instância de um level meta.
	 * @param openCloseGoal
	 *            Valor para fechar ou reabrir a meta.
	 * @return levelInstance Instância fechada ou reaberta.
	 */
	@Post(BASEPATH + "/structure/levelinstance/closegoal")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COLABORATOR, permissions = { ManagePlanPermission.class })
	public void closeLevelGoal(Long id, Boolean openCloseGoal, String url) {
		try {
			String userId = "";
			StructureLevelInstance levelInstance = this.bs.retrieveLevelInstance(id);
			levelInstance.setClosed(openCloseGoal);
			levelInstance.setClosedDate(new Date());
			this.bs.persist(levelInstance);

			if (openCloseGoal) {
				StructureLevelInstance levelInstanceParent = this.bs.retrieveLevelInstance(levelInstance.getParent());
				PaginatedList<Attribute> attributes = this.bs.listAttributes(levelInstanceParent.getLevel());
				for (Attribute attr : attributes.getList()) {
					if (attr.getType().equals(ResponsibleField.class.getCanonicalName())) {
						AttributeInstance attrInstance = this.attrHelper.retrieveAttributeInstance(levelInstanceParent,
								attr);
						if (attrInstance != null)
							userId = attrInstance.getValue();
					}
				}
				if (!userId.equals("")) {
					String aux = url.substring(0, url.lastIndexOf('/') + 1) + id;
					url = domain.getBaseUrl() + "/" + aux;
					CompanyUser companyUser = this.userBS.retrieveCompanyUser(
							this.userBS.existsByUser(Long.parseLong(userId)), this.domain.getCompany());
					if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting() || companyUser
							.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL.getSetting()) {
						this.notificationBS.sendNotification(NotificationType.GOAL_CLOSED, levelInstance.getName(),
								null, Long.parseLong(userId), url);
					} else if (companyUser.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL
							.getSetting()) {
						this.notificationBS.sendNotification(NotificationType.GOAL_CLOSED, levelInstance.getName(),
								null, Long.parseLong(userId), url);
						this.notificationBS.sendNotificationEmail(NotificationType.GOAL_CLOSED, levelInstance.getName(),
								"", companyUser.getUser(), url);
					}
				}
			} else {
				StructureLevelInstance levelInstanceParent = this.bs.retrieveLevelInstance(levelInstance.getParent());
				PaginatedList<Attribute> attributes = this.bs.listAttributes(levelInstanceParent.getLevel());
				for (Attribute attr : attributes.getList()) {
					if (attr.getType().equals(ResponsibleField.class.getCanonicalName())) {
						AttributeInstance attrInstance = this.attrHelper.retrieveAttributeInstance(levelInstanceParent,
								attr);
						if (attrInstance != null)
							userId = attrInstance.getValue();
					}
				}
				if (!userId.equals("")) {
					String aux = url.substring(0, url.lastIndexOf('/') + 1) + id;
					url = domain.getBaseUrl() + "/" + aux;
					CompanyUser companyUser = this.userBS.retrieveCompanyUser(
							this.userBS.existsByUser(Long.parseLong(userId)), this.domain.getCompany());
					if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting() || companyUser
							.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL.getSetting()) {
						this.notificationBS.sendNotification(NotificationType.GOAL_OPENED, levelInstance.getName(),
								null, Long.parseLong(userId), url);
					} else if (companyUser.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL
							.getSetting()) {
						this.notificationBS.sendNotification(NotificationType.GOAL_OPENED, levelInstance.getName(),
								null, Long.parseLong(userId), url);
						this.notificationBS.sendNotificationEmail(NotificationType.GOAL_OPENED, levelInstance.getName(),
								"", companyUser.getUser(), url);
					}
				}
			}
			this.success(levelInstance);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Preencher os atributos "alcançado" e "justificativa" de uma instância de
	 * um level meta.
	 * 
	 * @param id,
	 *            justification Id da instância de um level meta.
	 * @param reached
	 *            Valor do campo justificativa, valor do campo alcançado.
	 * @return structure Instância com os atributos preenchidos.
	 */
	@Post(BASEPATH + "/structure/levelinstance/updategoal")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COLABORATOR, permissions = { UpdateGoalPermission.class })
	public void updateLevelGoal(Long id, String reached) {
		// String justification
		boolean sucess = false;
		// AttributeInstance justificativa = null;
		AttributeInstance alcancado = null;
		List<AttributeInstance> atributosInstance = new ArrayList<AttributeInstance>();
		List<Attribute> attr = new ArrayList<Attribute>();
		List<StructureLevelInstance> instances = new ArrayList<StructureLevelInstance>();
		Attribute atribute = new Attribute();
		// atribute = new Attribute();
		atribute.setId(1l);
		attr.add(atribute);
		List<Attribute> attributeList = new ArrayList<Attribute>();
		List<AttributeInstance> reacheadField = new ArrayList<AttributeInstance>();

		try {

			StructureLevelInstance levelInstance = this.bs.retrieveLevelInstance(id);

			if (levelInstance == null) {
				this.fail("Estrutura incorreta!");
			} else if (levelInstance.isClosed()) {
				this.fail("Esta meta já foi concluída!");
			} else {
				attributeList = this.bs.retrieveLevelAttributes(levelInstance.getLevel());

				for (int i = 0; i < attributeList.size(); i++) {
					/*
					 * if (attributeList.get(i).isJustificationField()) {
					 * 
					 * justificativa =
					 * this.attrHelper.retrieveAttributeInstance(levelInstance,
					 * attributeList.get(i));
					 * 
					 * if (justificativa == null) { AttributeInstance
					 * justificativaEdit = new AttributeInstance();
					 * justificativaEdit.setAttribute(attributeList.get(i));
					 * justificativaEdit.setLevelInstance(levelInstance);
					 * justificativaEdit.setValue(justification);
					 * this.bs.persist(justificativaEdit);
					 * atributosInstance.add(justificativaEdit); sucess = true;
					 * } else {
					 * justificativa.setAttribute(attributeList.get(i));
					 * justificativa.setLevelInstance(levelInstance);
					 * justificativa.setValue(justification);
					 * attributeList.get(i).setAttributeInstance(justificativa);
					 * this.bs.persist(justificativa); sucess = true; }
					 * 
					 * }
					 */

					if (attributeList.get(i).isReachedField()) {
						alcancado = this.attrHelper.retrieveAttributeInstance(levelInstance, attributeList.get(i));
						if (alcancado == null) {
							AttributeInstance alcancadoEdit = new AttributeInstance();
							alcancadoEdit.setAttribute(attributeList.get(i));
							alcancadoEdit.setLevelInstance(levelInstance);
							alcancadoEdit.setValue(reached);
							if (!reached.isEmpty()) {
								alcancadoEdit.setValueAsNumber(Double.parseDouble(reached));
							}
							attributeList.get(i).setAttributeInstance(alcancadoEdit);
							reacheadField.add(alcancadoEdit);
							this.bs.persist(alcancadoEdit);
							atributosInstance.add(alcancadoEdit);
							sucess = true;

						} else {
							alcancado.setAttribute(attributeList.get(i));
							alcancado.setLevelInstance(levelInstance);
							alcancado.setValue(reached);
							if (reached.isEmpty())
								alcancado.setValueAsNumber(null);
							else
								alcancado.setValueAsNumber(Double.parseDouble(reached));
							reacheadField.add(alcancado);
							this.bs.persist(alcancado);
							sucess = true;
						}

					}
				}

				StructureLevelInstance levelInstanceStatus = this.bs.retrieveLevelInstance(id);
				levelInstanceStatus.getLevel()
						.setAttributes(this.bs.retrieveLevelAttributes(levelInstanceStatus.getLevel()));
				for (int i = 0; i < levelInstanceStatus.getLevel().getAttributes().size(); i++) {
					List<AttributeInstance> attrInstance = new ArrayList<AttributeInstance>();
					attrInstance
							.add(this.attrHelper.retrieveAttributeInstance(levelInstanceStatus, attributeList.get(i)));
					if (attributeList.get(i).isReachedField()) {
						levelInstanceStatus.getLevel().getAttributes().get(i).setAttributeInstances(reacheadField);

					} else {
						levelInstanceStatus.getLevel().getAttributes().get(i).setAttributeInstances(attrInstance);
					}
				}

				instances.add(levelInstanceStatus);
				AttributeInstance polarityAttr = this.attrHelper
						.retrievePolarityAttributeInstance(levelInstance.getParent());
				if (polarityAttr != null)
					levelInstance.setPolarity(polarityAttr.getValue());
				else
					levelInstance.setPolarity("Maior-melhor");
				this.bs.setGoalStatus(instances, levelInstance.getPolarity());

				levelInstance.setProgressStatus(instances.get(0).getProgressStatus());
				// levelInstance.getLevel().setAttributes(attr);
				// levelInstance.getLevel().getAttributes().get(0).setAttributeInstances(atributosInstance);
				List<Attribute> attributes = this.bs.retrieveLevelAttributes(levelInstance.getLevel());
				attributes = this.bs.setAttributesInstances(levelInstance, attributes);
				levelInstance.getLevel().setAttributes(attributes);
				if (sucess) {
					this.bs.updateLevelValues(levelInstance); // erro do
																// arredondamento
																// aquig

					this.success(levelInstance);
				}
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Listar as instâncias do level indicador.
	 * 
	 * @param void
	 * 
	 * @return list Lista de instâncias do level indicador.
	 */
	@Get(BASEPATH + "/structure/indicators")
	@NoCache
	@Permissioned
	public void listIndicators() {
		try {
			PaginatedList<StructureLevelInstance> list = this.bs.listIndicators();
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Listar instâncias do level objetivo pelo plano macro e/ou plano de metas.
	 * 
	 * @param macroId
	 *            Id do plano macro.
	 * @param planId
	 *            Id do plano de metas.
	 * @return list Lista de instâncias do level objetivo.
	 */
	@Get(BASEPATH + "/structure/objectives")
	@NoCache
	@Permissioned
	public void listObjective(Long macroId, Long planId) {
		PlanMacro planMacro = null;
		Plan plan2 = null;
		try {
			planMacro = this.planBS.retrievePlanMacroById(macroId);
			plan2 = this.planBS.retrieveById(planId);
			PaginatedList<StructureLevelInstance> list = this.bs.listObjective(planMacro, plan2);
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Gerar instâncias de um level meta.
	 * 
	 * @param indicatorId
	 *            Id da instância de um level indicador.
	 * @param name
	 *            Nome.
	 * @param responsible
	 *            Responsável.
	 * @param description
	 *            Descrição.
	 * @param expected
	 *            Valor esperado.
	 * @param minimun
	 *            Valor mínimo.
	 * @param maximum
	 *            Valor máximo.
	 * @return returnList Plano de metas com as instâncias geradas.
	 */
	@Post(BASEPATH + "/structure/levelinstance/goalsgenerate")
	@Consumes
	@NoCache
	@Permissioned
	public void goalsGenerate(Long indicatorId, String name, String responsible, String description, double expected,
			double minimum, double maximum) {
		try {
			Plan returnList = new Plan();
			String periodicity = null;
			String begin = null;
			String end = null;
			Date beginDate = null;
			Date endDate = null;
			StructureLevelInstance indicator = this.bs.retrieveLevelInstance(indicatorId);
			indicator.getLevel().setAttributes(this.bs.retrieveLevelAttributes(indicator.getLevel()));
			this.bs.setAttributesInstances(indicator, indicator.getLevel().getAttributes());
			for (Attribute attribute : indicator.getLevel().getAttributes()) {
				if (attribute.getAttributeInstance() != null) {
					if (attribute.isPeriodicityField()) {
						periodicity = attribute.getAttributeInstance().getValue();
					} else if (attribute.isBeginField()) {
						begin = attribute.getAttributeInstance().getValue();
					} else if (attribute.isEndField()) {
						end = attribute.getAttributeInstance().getValue();
					}
				}
			}
			String msgError = "";

			if (begin != null && end != null) {
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
				beginDate = (Date) formatter.parse(begin + " 00:00:00");
				endDate = (Date) formatter.parse(end + " 00:00:00");
			} else {
				if (begin == null) {
					msgError = "Por favor, edite o indicador e insira os dados obrigatórios";
				}
				if (end == null) {
					msgError = "Por favor, edite o indicador e insira os dados obrigatórios";
				}
				this.fail(msgError);
			}

			if (periodicity != null && beginDate != null && endDate != null && name != null) {
				Long parent = indicator.getId();
				Plan plan = indicator.getPlan();
				BigInteger structureId = this.bs.retrieveStructureIdByLevel(indicator.getLevel());
				Structure structure = new Structure();
				structure.setId(structureId.longValue());
				StructureLevel level = this.bs.retrieveNextLevel(structure, indicator.getLevel().getSequence() + 1);
				level.setAttributes(this.bs.retrieveLevelAttributes(level));
				this.bs.goalsGenerateByPeriodicity(parent, plan, level, name, responsible, description, expected,
						minimum, maximum, periodicity, beginDate, endDate);
			} else {
				this.fail("Dados invalidos.");
			}
			this.success(returnList);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Listar instâncias de um level indicador pelo plano macro e/ou plano de
	 * metas.
	 * 
	 * @param macroId
	 *            Id do plano macro.
	 * @param planId
	 *            Id do plano de metas.
	 * @return list Lista de instâncias de um level indicador.
	 */
	@Get(BASEPATH + "/structure/indicatorsByMacroAndPlan")
	@NoCache
	@Permissioned
	public void listIndicatorsByMacroAndPlan(Long macroId, Long planId) {
		PlanMacro planMacro = null;
		Plan plan2 = null;
		try {
			planMacro = this.planBS.retrievePlanMacroById(macroId);
			plan2 = this.planBS.retrieveById(planId);
			PaginatedList<StructureLevelInstance> list = this.bs.listIndicators(planMacro, plan2);
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Listar instâncias dos leveis filhos de um level.
	 * 
	 * @param planId
	 *            Id do plano de metas.
	 * @param parent
	 *            Id da instância de um level pai.
	 * @return list Lista de instâncias dos leveis filhos.
	 */
	@Get(BASEPATH + "/structure/levelsonsfilter")
	@NoCache
	public void listLevelSonsForFilter(Long planId, Long parent) {
		Plan plan = null;
		try {
			if (planId != null)
				plan = this.planBS.retrieveById(planId);
			PaginatedList<StructureLevelInstance> list = this.bs.retrieveLevelSonsForFilter(plan, parent);

			if (list.getTotal() > 0) {
				for (int i = 0; i < list.getList().size(); i++) {
					this.helper.fillIndicators(list.getList().get(i));
				}
			}
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Excluir uma lista de metas
	 * 
	 * @param list,
	 *            lista dos IDs das metas.
	 */
	@Post(BASEPATH + "/structure/deleteGoals")
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanPermission.class })
	@Consumes
	public void deleteManyGoals(PaginatedList<Double> list) {

		try {
			for (int i = 0; i < list.getTotal(); i++) {
				Long id = list.getList().get(i).longValue();
				StructureLevelInstance goal = this.bs.retrieveLevelInstance(id);
				if (goal == null) {
					this.fail("Meta com id " + id + " inválido.");
					return;
				}
				goal.setDeleted(true);
				this.bs.persist(goal);

				PaginatedList<FavoriteLevelInstance> favorites = this.bs.listFavoriteByLevelInstance(goal);
				for (FavoriteLevelInstance favorite : favorites.getList()) {
					favorite.setDeleted(true);
					this.bs.persist(favorite);
				}
			}
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Adicionar um level aos favoritos
	 * 
	 * @param levelInstanceId,
	 *            Id de uma instância de um level.
	 */
	@Post(BASEPATH + "/structure/savefavorite")
	@NoCache
	@Consumes
	public void saveFavoriteLevelInstance(Long levelInstanceId) {
		try {
			StructureLevelInstance levelInstance = this.bs.retrieveLevelInstance(levelInstanceId);
			PaginatedList<FavoriteLevelInstance> favorites = this.bs
					.listFavoriteLevelInstances(levelInstance.getPlan().getParent());
			if (favorites.getList().size() < 10) {
				FavoriteLevelInstance favorite = this.bs.retrieveFavoriteLevelInstance(levelInstance);
				if (favorite == null) {
					favorite = this.bs.saveFavoriteLevelInstance(levelInstance);
				} else {
					favorite.setDeleted(false);
					this.bs.persist(favorite);
				}
				this.success(favorite);
			} else {
				this.fail("Você já possui o número máximo de favoritos");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Remover um level dos favoritos
	 * 
	 * @param levelInstanceId,
	 *            Id de uma instância de um level.
	 */
	@Post(BASEPATH + "/structure/removefavorite")
	@NoCache
	@Consumes
	public void removeFavoriteLevelInstance(Long levelInstanceId) {
		try {
			StructureLevelInstance levelInstance = this.bs.retrieveLevelInstance(levelInstanceId);
			FavoriteLevelInstance favorite = this.bs.retrieveFavoriteLevelInstance(levelInstance);
			if (favorite != null) {
				favorite.setDeleted(true);
				this.bs.persist(favorite);
			}
			this.success(favorite);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Listar os leveis favoritos por usuário e empresa
	 * 
	 * @param levelInstanceId,
	 *            Id de uma instância de um level.
	 */
	@Get(BASEPATH + "/structure/listfavorites")
	@NoCache
	@Consumes
	public void listFavoriteLevelInstance(Long macroId) {
		try {
			PlanMacro macro = this.planBS.retrievePlanMacroById(macroId);
			PaginatedList<FavoriteLevelInstance> favorites = this.bs.listFavoriteLevelInstances(macro);
			this.success(favorites);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Listar o leveis indicadores que estão agregados a um level indicador
	 * agregado.
	 * 
	 * @param aggregate
	 *            Instância de um level indicador agregado.
	 * 
	 */
	@Get(BASEPATH + "/structure/listaggregates")
	@NoCache
	@Consumes
	public void listAggregates(Long id) {
		try {
			StructureLevelInstance indicator = this.helper.retrieveLevelInstance(id);
			if (indicator.isAggregate()) {
				PaginatedList<AggregateIndicator> list = new PaginatedList<AggregateIndicator>();
				list.setList(this.bs.listAggregateIndicatorsByAggregate(indicator));
				list.setTotal(new Long(this.bs.listAggregateIndicatorsByAggregate(indicator).size()));
				this.success(list);
			} else {
				this.fail("Indicador não agregado");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

}
