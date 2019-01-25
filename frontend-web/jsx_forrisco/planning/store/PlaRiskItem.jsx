import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL + "planrisk/item";

var ItemModel = Fluxbone.Model.extend({
	url: URL
});

var PlanRiskItemStore = Fluxbone.Store.extend({
	ACTION_GET_ALL_ITENS: 'planRiskItem-getAllItens',
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
				me.trigger("allitens", data)
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		})
	}
});

export default new PlanRiskItemStore();
