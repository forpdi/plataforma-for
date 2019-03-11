import React from 'react';
import _ from 'underscore';

import UserStore from "forpdi/jsx/core/store/User.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";


export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
		router: React.PropTypes.object,
	},

	getInitialState() {
		return {
			users: [],
			plansLength: null,
			unit: {
				name: '',
				abbreviation: '',
				user: null,
				description: '',
				planRisk: { id: this.props.params.planRiskId },
				parent: { id: this.props.params.unitId }
			},
		};
	},

	componentDidMount() {
		UserStore.on('retrieve-user', (response) => {
			if (response.data) {
				this.setState({
					users: _.map(response.data, user => user)
				});
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar os usuários da companhia");
			}
		}, this);

		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
			data: {
				page: 1,
				pageSize: 500,
			},
		});

		UnitStore.on("subunitCreated", (response) => {
			if (response.data) {
				this.context.toastr.addAlertSuccess(Messages.get("notification.subunit.save"));
				this.context.router.push(`forrisco/plan-risk/${this.props.params.planRiskId}/unit/${this.props.params.unitId}/subunit/${response.data.id}`);
			} else {
				this.context.toastr.addAlertError("Erro ao criar Unidade");
			}
		});

		_.defer(() => {
			this.context.tabPanel.addTab(this.props.location.pathname, 'Nova Subunidade');
		}, this);
	},

	componentWillUnmount() {
		UserStore.off(null, null, this);
		UnitStore.off(null, null, this);
	},

	getFields() {
		/*Título do Item*/
		const formFields = [{
			name: 'description',
			type: AttributeTypes.TEXT_FIELD,
			placeholder: "Título do Item",
			maxLength: 100,
			label: "Título",
			required: true,
		}];

		this.setState({
			formFields: formFields
		});
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

	handleSubmit(event) {
		event.preventDefault();
		const { unit } = this.state;

		if (unit.name === '' || unit.user === null) {
			this.context.toastr.addAlertError(Messages.get("label.error.form"));
			return false;
		}

		UnitStore.dispatch({
			action: UnitStore.ACTION_NEW_SUBUNIT,
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
			placeholder: "Descrição da Política",
			required: true,
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

	render() {
		return (
			<div>
				<h1 className="marginLeft115">Nova Subunidade</h1>
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
							<button
								type="submit"
								className="btn btn-success"
							>
								{Messages.get('label.save')}
							</button>
							<button
								type="button"
								className="btn btn-default"
								onClick={this.onCancel}
							>
								{Messages.get('label.cancel')}
							</button>
						</div>
					</form>
				</div>
			</div>
		)
	}
})

