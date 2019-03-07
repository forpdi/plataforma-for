import React from "react";
import _ from 'underscore';
import Messages from "@/core/util/Messages";
// import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import UserStore from "forpdi/jsx/core/store/User.jsx";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import Router from "react-router";
import UserSession from "@/core/store/UserSession";
import StructureStore from "@/planning/store/Structure";

export default React.createClass({

	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
		router: React.PropTypes.object,
	},

	getInitialState() {
		return {
			// planRiskModel: null,
			submitLabel: "Salvar",
			cancelLabel: "Cancelar",
			// policyId: null,
			plansLength: null,
			users: [],
			unit: {
				name: '',
				abbreviation: '',
				user: null,
				description: '',
				planRisk: { id: this.props.params.planRiskId },
			},
		};
	},

	componentDidMount() {
		// var policiData = [];
		// var resultSelect = PolicyStore.on("unarchivedpolicylisted", (response) => {
		//
		// 	if (response.status !== true) {
		// 		this.setState({domainError: true});
		// 	}
		//
		// 	if (response.success === true) {
		// 		response.data.map((attr) => {
		// 			policiData.push({
		// 				id: attr.id,
		// 				label: attr.name
		// 			});
		// 		});
		//
		// 		this.setState({
		// 			policies: policiData, domainError: false,
		// 		});
		// 	}
		// 	resultSelect.off("unarchivedpolicylisted");
		// });

		// UnitStore.on("listedunarchivedplanrisk", (response) => {
		// 	this.setState({
		// 		plansLength: response.total
		// 	});
		// });

		UserStore.on('retrieve-user', (response) => {
			let arrUsers = []
			if (response.data) {
				response.data.map(users => {
					arrUsers.push(users)
				});
				_.map(arrUsers, user => { return user });
				this.setState({
					users: arrUsers
				});
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar os usuários da companhia");
			}
		});

		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
			data: {
				page: 1,
				pageSize: 500,
			},
		});

		UnitStore.on("unitcreated", (response) => {
			if (response.data) {
				this.context.toastr.addAlertSuccess(Messages.get("notification.unit.save"));
				this.context.router.push("forrisco/plan-risk/" + response.data + "/");
			} else {
				this.context.toastr.addAlertError("Erro ao criar Unidade");
			}
		});

		_.defer(() => {
			this.context.tabPanel.addTab(this.props.location.pathname, 'Nova Unidade');
		});
	},

	// componentWillMount() {
	// 	PolicyStore.dispatch({
	// 		action: PolicyStore.ACTION_FIND_UNARCHIVED,
	// 	});
	//
	// 	PolicyStore.dispatch({
	// 		action: UnitStore.ACTION_FIND_UNARCHIVED,
	// 	});
	// },

	componentWillUnmount() {
		// PolicyStore.off(null, null, this);
		UnitStore.off(null, null, this);
	},

	fieldChangeHandler(e) {
		this.setState({
			unit: {
				...this.state.unit,
				[e.target.name]: e.target.value,
			}
		});
	},

	selectChangeHandler(e) {
		const idx = e.target.options.selectedIndex - 1;
		this.setState({
			unit: {
				...this.state.unit,
				user: this.state.users[idx],
			}
		});
	},

	getFields() {
		var fields = [];
		fields.push({
			name: "name",
			type: "text",
			required: true,
			maxLength: 240,
			placeholder: "Novo Plano de Gestão de Riscos",
			label: Messages.getEditable("label.name", "fpdi-nav-label"),
			onChange: this.fieldChangeHandler,
		}, {
			name: "abbreviation",
			type: "text",
			required: true,
			placeholder: "Descrição da Política",
			maxLength: 240,
			label: Messages.getEditable("label.abbreviation", "fpdi-nav-label"),
			onChange: this.fieldChangeHandler,
		}, {
			name: "user",
			type: "select",
			options: _.map(this.state.users, user => user.name),
			renderDisplay: value => value,
			className: "form-control-h",
			required: true,
			displayField: 'label',
			placeholder: "Selecone o Responsável",
			label: Messages.getEditable("label.responsible", "fpdi-nav-label"),
			onChange: this.selectChangeHandler,
		}, {
			name: "description",
			type: "textarea",
			placeholder: "Descrição da Política",
			maxLength: 9900,
			label: Messages.getEditable("label.descriptionPolicy", "fpdi-nav-label"),
			onChange: this.fieldChangeHandler,
		});

		return fields;
	},

	handleSubmit(event) {
		event.preventDefault();
		const { unit } = this.state;

		if (unit.name === '' || unit.user === null) {
			this.context.toastr.addAlertError(Messages.get("label.error.form"));
			return false;
		}

		console.log(unit);

		UnitStore.dispatch({
			action: UnitStore.ACTION_NEWUNIT,
			data: unit,
		});
	},

	onCancel() {
		if (this.state.plansLength > 0 || this.state.policies.length === 0) {
			this.context.router.push("/forrisco/home/");
		}

		if (this.state.policies.length && this.state.policies.length === 1) {
			this.context.router.push("/forrisco/policy/" + this.state.policies[0].id + "/")
		}
	},

	render() {
		return (
			<div>
				<h1 className="marginLeft115">Nova Unidade</h1>
				<div className="fpdi-card padding40">
					<form onSubmit={this.handleSubmit}>
						{
							this.getFields().map((field, index) => {
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
});
