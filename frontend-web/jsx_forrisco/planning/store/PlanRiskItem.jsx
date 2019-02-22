import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL + "planrisk/item";

var ItemModel = Fluxbone.Model.extend({
	url: URL
});

var PlanRiskItemStore = Fluxbone.Store.extend({
	ACTION_GET_ALL_ITENS: 'planRiskItem-getAllItens',
	ACTION_DETAIL_ITEM: 'planRiskItem-detailItem',
	ACTION_DETAIL_SUBITEM: 'planRiskItem-detailSubItem',
	ACTION_GET_SUB_ITENS: 'planRiskItem-getSubItens',
	ACTION_SAVE_ITENS: 'planRiskItem-saveNewItens',
	ACTION_SAVE_SUBITENS: 'planRiskItem-saveNewSubItens',


	url: URL,
	model: ItemModel,

	getAllItens(params) {
		var me = this;
		$.ajax({
			method: "GET",
			url: me.url,
			dataType: 'json',
			data: params,
			success(data, status, opts) {
				me.trigger("allItens", data)
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		})
	},

	getSubItens(data, node) {
		var me = this;
		$.ajax({
			method: "GET",
			url: me.url + "/sub-itens/" + data.id,
			dataType: 'json',
			success(data, status, opts) {
				me.trigger("allSubItens", data, node)
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		})
	},

	saveNewItens(planRiskItem) {
		var me = this;
		$.ajax({
			url: me.url + "/new",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(planRiskItem),
			success(model) {
				me.trigger("itemSaved", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("itemSaved", opts);
			}
		})
	},

	saveNewSubItens(planRiskSubItem) {
		var me = this;
		$.ajax({
			url: me.url + "/new/subitem",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(planRiskSubItem),
			success(model) {
				me.trigger("itemSaved", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("itemSaved", opts);
			}
		})
	},

	detailItem(data) {
		var me = this;
		$.ajax({
			url: me.url + "/" + data.id,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("detailItem", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("detailItem", opts);
			}
		})
	},

	detailSubItem(data) {
		var me = this;
		$.ajax({
			url: me.url + "/subitem/" + data.id,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("detailSubItem", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("detailSubItem", opts);
			}
		})
	}

});

export default new PlanRiskItemStore();
