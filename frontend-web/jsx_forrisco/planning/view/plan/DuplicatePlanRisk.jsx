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
      oldPlanRisk: {
        planName: '',
        plocyName: '',
      },
      units: [],
			items: [],
			keepItems: 0,
			keepUnits: 0,
		};
	},

	componentDidMount() {
    PlanRiskItemStore.dispatch({
      action: PlanRiskItemStore.ACTION_GET_ALL_ITENS,
      data: { planRiskId: this.props.params.planRiskId }
    });

		PlanRiskItemStore.on('allItens', response => {
			const items = response.data;
			if (items) {
				this.setState({ items });
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar dados dos itens do plano");
			}
		});

    UnitStore.dispatch({
      action: UnitStore.ACTION_FIND_BY_PLAN,
      data: { planId: this.props.params.planRiskId },
    });

    UnitStore.on('unitbyplan', response => {
      const units = response.data;
      if (units) {
        this.setState({ units });
      } else {
				this.context.toastr.addAlertError("Erro ao recuperar dados das unidades");
			}
    });

		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
			data: this.props.planRiskId,
		});

		PlanRiskStore.on('retrivedplanrisk', response => {
      const plan = response.attributes;
      if (plan) {
        this.setState({
          oldPlanRisk: {
            planName: plan.name,
            policyName: plan.policy.name,
          },
        })
      } else {
				this.context.toastr.addAlertError("Erro ao recuperar dados do plano");
			}

			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname, plan.policy.name);
			});
		});
		this.refreshComponent(this.props.params.planRiskId);

		this.setState({
			isLoading: false
		});
	},

	// componentWillReceiveProps(newProps) {
	// 	if (this.props.params.planRiskId !== newProps.params.planRiskId) {
	// 		this.refreshComponent(newProps.params.planRiskId)
	// 	}
	// },

	refreshComponent(planRiskId) {
		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
			data: planRiskId
		})
	},

	componentWillUnmount() {
		PlanRiskItemStore.off(null, null, this);
		PlanRiskStore.off(null, null, this);
		UnitStore.off(null, null, this);
	},

	handleSubmit(event) {
		event.preventDefault();
		// const formData = new FormData(event.target);
		//
		// PlanRiskStore.dispatch({
		// 	action: PlanRiskStore.ACTION_EDIT_PLANRISK,
		// 	data: {
		// 		planRisk: {
		// 			id: this.props.params.planRiskId,	//ID do plano a ser editado
		// 			name: formData.get('name'),
		// 			description: formData.get('description'),
		// 			policy: {
		// 				id: formData.get('linkedPolicy')
		// 			}
		// 		}
		// 	}
		// });
		//
		// PlanRiskStore.on('editPlanRisk', response => {
		// 	this.context.toastr.addAlertSuccess("Plano de Risco editado com sucesso");
		// 	this.context.router.push("/forrisco/plan-risk/" + response.data.id + "/item/" + this.props.params.itemId + '/info');
		// 	PlanRiskItemStore.off('editPlanRisk');
		// })
	},

	getCheckboxes(list, groupName) {
		return _.map(list, (item, idx) => (
			<div key={idx}>
				<input style={{ "margin": "0px 5px" }} type="checkbox" name={groupName} />
				{item.name}
			</div>
		));
	},

	onRadioChange(event) {
		this.setState({ [event.target.name]: event.target.value });
	},

	getForm() {
		return (
			<form onSubmit={this.handleSubmit} ref={"planRiskEditForm"}>
				<h4 style={{ "marginTop": "30px" }}>MANTER ITENS DO PLANO</h4>
				<Radio
					name="keepItems"
					value={1}
					checked={this.state.keepItems == 1}
					onChange={this.onRadioChange}
					label="Sim"
				/>
				<Radio
					name="keepItems"
					value={0}
					checked={this.state.keepItems == 0}
					onChange={this.onRadioChange}
					label="Não"
				/>
				{(this.state.keepItems == 1) ? this.getCheckboxes(this.state.items, "keepItem") : null}

				<h4 style={{ "marginTop": "30px" }}>MANTER CONTEÚDO DO PLANO</h4>
				<Radio name="keepPlanContent" label="Sim" />
				<Radio name="keepPlanContent" label="Não" />

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
				{(this.state.keepUnits == 1) ? this.getCheckboxes(this.state.units, "keepUnit") : null}

				<h4 style={{ "marginTop": "30px" }}>MANTER CONTEÚDO DAS UNIDADES</h4>
				<Radio name="keepUnitContent" label="Sim" />
				<Radio name="keepUnitContent" label="Não" />

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
			return <LoadingGauge/>;
		}

		return (
			<div>
				<h1>Duplicar Plano</h1>
        <span>Plano a ser duplicado: <b>{this.state.oldPlanRisk.planName}</b></span><br/>
        <span>Política vinculada: <b>{this.state.oldPlanRisk.policyName}</b></span>
				{this.getForm()}
			</div>
		)
	}
})
