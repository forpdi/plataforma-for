import React from "react";
import _ from "underscore";
import Messages from "@/core/util/Messages";

import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge";

import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem"
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit"
import Radio from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskRadio";

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
		roles: React.PropTypes.object.isRequired,
		planRisk: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {
			submitLabel: "Salvar",
			cancelLabel: "Cancelar",
			// planRiskFields: [],
			isLoading: true,
			oldPlanRiskModel: null,
			units: [],
			itens: [],
			keepItens: 0,
			keepUnits: 0,
		};
	},

	componentDidMount() {

		PlanRiskItemStore.on('allItens', response => {
			const itens = response.data;
			if (response.success) {
				this.setState({ itens });
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar dados dos itens do plano");
			}
		},this);

		UnitStore.on('unitbyplan', response => {
			const units = response.data;
			if (response.data != null) {
				this.setState({ units });
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar dados das unidades");
			}
		},this);

		PlanRiskStore.on('retrivedplanrisk', response => {
			if (response != null) {
				this.setState({
					oldPlanRiskModel: response.attributes,
					isLoading: false
				})
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar dados do plano");
			}

			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname, response.attributes.policy.name);
			});
		},this);

		PlanRiskItemStore.on("itensDuplicated",response => { console.log("itensDuplicated")},this);
		UnitStore.on("duplicatedUnits",response => { console.log("duplicatedUnits")},this);

		PlanRiskStore.on('plariskcreated', response => {

			if (response.success != null) {

				var itens=[];
				var units=[];

				if(this.state.keepItens == 1){
					for(var i in this.state.itens){
						if(this.refs['keepItens-'+i].checked){
							itens.push(this.state.itens[i])
						}
					}
				}
				if(this.state.keepUnits == 1){
					for(var i in this.state.units){
						if(this.refs['keepUnits-'+i].checked){
							units.push(this.state.units[i])
						}
					}
				}

				PlanRiskItemStore.dispatch({
					action: PlanRiskItemStore.ACTION_SAVE_ITENS_DUPLICATE,
					data: {	itens:itens,
							planRisk: {id: response.data.id}
						}
				});

				UnitStore.dispatch({
					action: UnitStore.ACTION_DUPLICATE,
					data: {	units: units,
							planRisk: {id: response.data.id}
						}
				});


			this.context.tabPanel.removeTabByPath(this.props.location.pathname);
			this.context.router.push("/forrisco/plan-risk/"+response.data.id+"/item/overview")

			}else{
				var msg = model.msg ? "Erro ao duplicar Plano: "+model.msg.message : "Erro ao duplicar Plano"
				this.context.toastr.addAlertError(msg);
			}

		},this);

		this.refreshComponent(this.props.params.planRiskId);
	},

	refreshComponent(planRiskId) {
		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
			data: planRiskId,
		});

		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_GET_ALL_ITENS,
			data: planRiskId
		});

		UnitStore.dispatch({
			action: UnitStore.ACTION_FIND_BY_PLAN,
			data:  planRiskId,
		});
	},

	componentWillUnmount() {
		PlanRiskItemStore.off(null, null, this);
		PlanRiskStore.off(null, null, this);
		UnitStore.off(null, null, this);
	},

	onCancel(){
		this.context.tabPanel.removeTabByPath(this.props.location.pathname);
		this.context.router.push("/forrisco/plan-risk/"+this.props.params.planRiskId+"/item/overview")
	},

	onSubmit(event) {
		event.preventDefault();

		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_NEWPLANRISK,
			data: {	policy: this.state.oldPlanRiskModel.policy,
					name: this.refs.planRiskEditForm["field-name"].value,
					description: this.refs.planRiskEditForm["field-description"].value,
				}
		});
	},

	getCheckboxes(list, groupName) {
		return _.map(list, (item, idx) => (
			<div key={idx}>
				<input style={{ "margin": "0px 5px" }} type="checkbox" ref={groupName+"-"+(idx)} name={groupName} />
				{item.name}
			</div>
		));
	},

	onRadioChange(event) {
		this.setState({ [event.target.name]: event.target.value });
	},

	getForm() {
		return (
			<form onSubmit={this.onSubmit} ref={"planRiskEditForm"}>
				<h4 style={{ "marginTop": "30px" }}>MANTER ITENS DO PLANO</h4>
				<Radio
					name="keepItens"
					value={1}
					checked={this.state.keepItens == 1}
					onChange={this.onRadioChange}
					label="Sim"
				/>
				<Radio
					name="keepItens"
					value={0}
					checked={this.state.keepItens == 0}
					onChange={this.onRadioChange}
					label="Não"
				/>
				{(this.state.keepItens == 1) ? this.getCheckboxes(this.state.itens, "keepItens") : null}

				{/*<h4 style={{ "marginTop": "30px" }}>MANTER CONTEÚDO DO PLANO</h4>
				<Radio name="keepPlanContent" label="Sim" />
				<Radio name="keepPlanContent" label="Não" />
				*/}

				<h4 style={{ "marginTop": "30px" }}>MANTER UNIDADES</h4>
				<Radio
					name="keepUnits"
					value={1}
					checked={this.state.keepUnits == 1}
					onChange={this.onRadioChange}
					label="Sim"
				/>
				<Radio
					name="keepUnits"
					value={0}
					checked={this.state.keepUnits == 0}
					onChange={this.onRadioChange}
					label="Não"
				/>
				{(this.state.keepUnits == 1) ? this.getCheckboxes(this.state.units, "keepUnits") : null}

				{/*<h4 style={{ "marginTop": "30px" }}>MANTER CONTEÚDO DAS UNIDADES</h4>
				<Radio name="keepUnitContent" label="Sim" />
				<Radio name="keepUnitContent" label="Não" />
				*/}

				<h4 style={{ "marginTop": "30px" }}>NOME DO NOVO PLANO A SER DUPLICADO</h4>
				<VerticalInput
					fieldDef={{
						type: "text",
						name: "name",
						placeholder: "Nome do Plano",
						maxLength: 240,
					}}
				/>

				<h4 style={{ "marginTop": "30px" }}>DESCRIÇÃO DO NOVO PLANO A SER DUPLICADO</h4>
				<VerticalInput
					fieldDef={{
						type: "textarea",
						name: "description",
						placeholder: "Descrição do Plano",
						maxLength: 9900,
					}}
				/>

				<div className="fpdi-editable-data-input-group">
					<button type="submit" className="btn btn-success">
						{this.state.submitLabel}
					</button>
					<button type="button" className="btn btn-default" onClick={this.onCancel}>
						{this.state.cancelLabel}
					</button>
				</div>
			</form>
		);
	},

	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge />;
		}

		return (
			<div>
				<h1>Duplicar Plano</h1>
				<span>Plano a ser duplicado: <b>{this.state.oldPlanRiskModel.name}</b></span><br />
				<span>Política vinculada: <b>{this.state.oldPlanRiskModel.policy.name}</b></span>
				{this.getForm()}
			</div>
		)
	}
})
