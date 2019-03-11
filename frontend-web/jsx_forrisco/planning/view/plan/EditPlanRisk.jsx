import React from "react";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import Messages from "@/core/util/Messages";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import _ from "underscore";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem";

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
			planRiskFields: [],
			policyOptions: [{id: null, label: ""}],
			isLoading: true
		};
	},

	componentDidMount() {

		PolicyStore.on("unarchivedpolicylisted", response => {
			const policies = [];

			response.data.map(policy => {
				policies.push({id: policy.id, label: policy.name})
			});
			this.setState({policyOptions: policies});

			PlanRiskStore.dispatch({
				action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
				data: this.props.params.planRiskId
			});

		}, this);

		PlanRiskStore.on('retrivedplanrisk', response => {
			var fields = [];

			console.log(this.state.policyOptions.length);


			fields.push({
				name: "name",
				type: "text",
				//required: true,
				maxLength: 240,
				placeholder: "Nome do Plano de Gestão de Riscos",
				label: Messages.getEditable("label.name", "fpdi-nav-label"),
				value: response.attributes.name,
			}, {
				name: "description",
				type: "textarea",
				placeholder: "Descrição da Política",
				maxLength: 9900,
				label: Messages.getEditable("label.descriptionPolicy", "fpdi-nav-label"),
				value: response.attributes.description,
			}, {
				name: "linkedPolicy",
				type: "select",
				className: "form-control-h",
				required: true,
				displayField: 'label',
				valueField: 'id',
				label: Messages.getEditable("label.linkPlanPolicy", "fpdi-nav-label"),
				value: response.attributes.policy.name,
				options: this.state.policyOptions
			});


			// if(this.state.policyOptions.length > 1) {
				this.setState({
					planRiskFields: fields,
					isLoading: false
				});
			// }

			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname, response.attributes.policy.name);
			});
		}, this);
		this.refreshComponent(this.props.params.planRiskId);
	},

	componentWillReceiveProps(newProps) {
		if (this.props.params.planRiskId !== newProps.params.planRiskId) {
			this.refreshComponent(newProps.params.planRiskId)
		}
	},

	refreshComponent(planRiskId) {
		PolicyStore.dispatch({
			action: PolicyStore.ACTION_FIND_UNARCHIVED
		});

		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
			data: planRiskId
		});
	},

	componentWillUnmount() {
		PlanRiskStore.off(null, null, this);
		PolicyStore.off(null, null, this);
	},

	handleSubmit(event) {
		event.preventDefault();
		const formData = new FormData(event.target);

		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_EDIT_PLANRISK,
			data: {
				planRisk: {
					id: this.props.params.planRiskId,	//ID do plano a ser editado
					name: formData.get('name'),
					description: formData.get('description'),
					policy: {
						id: formData.get('linkedPolicy')
					}
				}
			}
		});

		PlanRiskStore.on('editPlanRisk', response => {
			this.context.toastr.addAlertSuccess("Plano de Risco editado com sucesso");
			this.context.router.push("/forrisco/plan-risk/" + response.data.id + "/item/" + this.props.params.itemId + '/info');
			PlanRiskStore.dispatch({
				action: PlanRiskStore.ACTION_FIND_UNARCHIVED_FOR_MENU
			});
			PlanRiskItemStore.off('editPlanRisk');
		})
	},

	render() {

		if (this.state.isLoading === true) {
			return <LoadingGauge/>;
		}

		return (
			<div>
				<h1 className="marginLeft115">Editar Plano de Risco</h1>
				<div className="fpdi-card padding40">
					<form onSubmit={this.handleSubmit} ref={"planRiskEditForm"}>

						{
							this.state.planRiskFields.map((field, index) => {
								return (
									<VerticalInput key={index} fieldDef={field}/>
								);
							})
						}

						<div className="fpdi-editable-data-input-group">
							<button type="submit" className="btn btn-success">{this.state.submitLabel}</button>
							<button type="button" className="btn btn-default"
									onClick={this.onCancel}>{this.state.cancelLabel}</button>
						</div>
					</form>
				</div>
			</div>
		)
	}
})
