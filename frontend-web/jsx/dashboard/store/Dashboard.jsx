import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL+"dashboard";

var DashboardModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];
		if (!attrs.name || (attrs.name == '')) {
			errors.push("O nome da instituição é obrigatório.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var DashboardStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'dashboard-create',
	ACTION_DESTROY: 'dashboard-destroy',
	ACTION_FIND: 'dashboard-find',
	ACTION_RETRIEVE: 'dashboard-retrieve',
	ACTION_UPDATE: 'dashboard-update',
	ACTION_GET_GOALS_INFO_ADM: 'dashboard-getGoalsInfoAdmin',
	ACTION_GET_GOALS_INFO_MAN: 'dashboard-getGoalsInfoMan',
	ACTION_GET_PLAN_DETAILS: 'dashboard-getPlanDetails',
	ACTION_GET_PLAN_DETAILS_COMMUNITY: 'dashboard-getPlanDetailsCommunity',
	ACTION_PERFORMANCE_STRATEGICAXIS: 'dashboard-performanceStrategicAxis',
	ACTION_GET_THEMATIC_AXIS_INFORMATION: 'dashboard-getThematicAxis',
	ACTION_GET_GENERAL_BUDGETS: 'dashboard-getGeneralBudgets',
	ACTION_GET_INDICATORS_INFORMATION: 'dashboard-getIndicatorsInformation',
	ACTION_GET_GOALS_INFORMATION: 'dashboard-getGoalsInformation',
	ACTION_GET_GOALS_INFO_COL: 'dashboard-getGoalsInfoCol',
	ACTION_GET_GRAPH_FOR_INDICATOR: 'dashboard-getGraphForIndicator',
	ACTION_GET_GOALS_INFO_TABLE: 'dashboard-getGoalsInfoTable',
	ACTION_GET_OBJECTIVES_INFORMATION: 'dashboard-getObjectivesInformation',
	ACTION_GET_INDICATORS_HISTORY: 'dashboard-getIndicatorsHistory',
	ACTION_GET_LEVELSONS_GRAPH: 'dashboard-getLevelSonsGraph',
	ACTION_GET_COMMUNITY_INFO_TABLE: 'dashboard-getCommunityInfoTable',
	dispatchAcceptRegex: /^dashboard-[a-zA-Z0-9]+$/,

	url: URL,
	model: DashboardModel,

	getGoalsInfoTable(data){
		var me = this;
		$.ajax({
			url: me.url+"/manager/goalsInfoTable",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("goalsinfotableretrivied", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getGoalsInfoAdmin(data){
		var me = this;
		$.ajax({
			url: me.url+"/admin/goals",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("goalsinfoadminretrivied", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getGoalsInfoMan(data){
		var me = this;
		$.ajax({
			url: me.url+"/admin/goals",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("goalsinfomanretrivied", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getPlanDetails(data){
		var me = this;
		$.ajax({
			url: me.url+"/admin/planDetails",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("planDetailsRetrieved", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("planDetailsRetrieved", opts.responseJSON);
			}
		});
	},

	getPlanDetailsCommunity(data){
		var me = this;
		$.ajax({
			url: me.url+"/community/planDetails",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("planDetailsRetrieved", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},


	getThematicAxis(data) {
		var me = this;
		$.ajax({
			url: me.url+"/admin/objectivesByThematicAxis",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("objectivesByThematicAxisRetrived", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	performanceStrategicAxis(data){
		var me = this;
		$.ajax({
			url: me.url+"/admin/performanceStrategicAxis",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("performanceStrategicAxisRetrived", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getGeneralBudgets(data){
		var me = this;
		$.ajax({
			url: me.url+"/admin/budget",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("generalbudgetsretrivied", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getIndicatorsInformation(data) {
		var me = this;
		$.ajax({
			url: me.url+"/admin/indicatorsInformation",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("generalIndicatorsInformation", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getGoalsInformation(data) {
		var me = this;
		$.ajax({
			url: me.url+"/colaborator/goalsInformation",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("generalGoalsInformation", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getGoalsInfoCol(data){
		var me = this;
		$.ajax({
			url: me.url+"/colaborator/goalsInformation",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("goalsInformationColaborator", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}

		});
	},

	getGraphForIndicator(data){
		var me = this;
		$.ajax({
			url: me.url+"/graphforindicator",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("graphForIndicatorRetrived", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}

		});
	},

	getObjectivesInformation(data){
		var me = this;
		$.ajax({
			url: me.url+"/admin/objectivesInformation",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("generalObjectivesInformation", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getIndicatorsHistory(data){
		var me = this;
		$.ajax({
			url: me.url+"/manager/indicatorsHistory",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("indicatorsHistoryRetrivied", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getLevelSonsGraph(data){
		var me = this;
		$.ajax({
			url: me.url+"/community/levelsonsgraph",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("levelSonsGraphRetrivied", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getCommunityInfoTable(data){
		var me = this;
		$.ajax({
			url: me.url+"/community/communityinfotable",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("communityinfotableretrivied", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	}

});

export default new DashboardStore();
