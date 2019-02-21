import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL + "unit";

var ItemModel = Fluxbone.Model.extend({
	url: URL
});

var UnitItemStore = Fluxbone.Store.extend({
	ACTION_GET_ALL_ITENS: 'unitItem-getAllItens',
	url: URL,
	model: ItemModel,

	getAllItens(params) {
		var me = this;
		$.ajax({
			method: "GET",
			url: me.url+"?planId="+params.planRiskId,
			dataType: 'json',
			data: params,
			success(data, status, opts) {
				me.trigger("allitensunit", data)
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		})
	}
});

export default new UnitItemStore();
