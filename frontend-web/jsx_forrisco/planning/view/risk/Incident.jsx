import React from "react";
import ReactTable from 'react-table';
import "react-table/react-table.css";
import _ from 'underscore';
import { Button } from 'react-bootstrap';

import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import UserStore from 'forpdi/jsx/core/store/User.jsx';
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import moment from 'moment'

const incidentTypes = {
	values: [
		{ id: 1, label: 'Ameaça' },
		{ id: 2, label: 'Oportunidade' },
	],
	getById: (id) => _.find(incidentTypes.values, value => value.id === id),
	getByLabel: (label) => _.find(incidentTypes.values, value => value.label === label),
};

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
	},

	getInitialState() {
		return {
			data: [],
			users: [],
			incident: null,
			beginDate: null,
			beginHour: null,
			newRowDisplayed: false,
			updateRowDisplayed: false,
			isLoading: true,
		}
	},

	componentDidMount() {

		RiskStore.on('incidentListed', (response) => {
			if (response !== null) {
				this.setState({
					data: _.map(response.data, (value, idx) => ({
						..._.assign(value, { tools: this.renderRowTools(value.id, idx) }),
						type: incidentTypes.getById(value.type).label,
					})),
					isLoading: false,
					newRowDisplayed: false,
					updateRowDisplayed: false,
				});
			}
		}, this);

		RiskStore.on('incidentCreated', (response) => {
			if (response.data) {
				this.context.toastr.addAlertSuccess("Incidente cadastrado com sucesso.");
				this.setState({
					isLoading: true,
				});
				RiskStore.dispatch({
					action: RiskStore.ACTION_LIST_INCIDENT,
					data: this.props.risk.id,

				});
			} else {
				this.context.toastr.addAlertError("Erro ao cadastrar incidente.");
			}
		}, this);

		RiskStore.on('incidentDeleted', (response) => {
			if (response.success) {
				this.context.toastr.addAlertSuccess("Incidente excluído com sucesso.");
				this.setState({
					isLoading: true,
				});
				RiskStore.dispatch({
					action: RiskStore.ACTION_LIST_INCIDENT,
					data: this.props.risk.id,
				});
			} else {
				this.context.toastr.addAlertError("Erro ao excluir incidente.");
			}
		}, this);

		RiskStore.on('incidentUpdated', (response) => {
			if (response.success) {
				this.context.toastr.addAlertSuccess("Incidente atualizado com sucesso.");
				this.setState({
					isLoading: true,
				});
				RiskStore.dispatch({
					action: RiskStore.ACTION_LIST_INCIDENT,
					data: this.props.risk.id,
				});
			} else {
				this.context.toastr.addAlertError("Erro ao atualizar incidente.");
			}
		}, this);

		UserStore.on('retrieve-user', (response) => {
			const users = response.data;
			if (response.data) {
				this.setState({
					users: response.data,
					incident: {
						risk: this.props.risk,
						user: users.length > 0 ? users[0] : null,
					},

				});
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar os usuários da companhia");
			}
		}, this);
		this.refreshComponent(this.props.risk.id, 1, 500);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.risk.id !== this.props.risk.id) {
			this.refreshComponent(newProps.risk.id, 1, 500)
		}
	},

	componentWillUnmount() {
		RiskStore.off(null, null, this);
		UserStore.off(null, null, this);
	},

	refreshComponent(riskId, page, pageSize) {
		RiskStore.dispatch({
			action: RiskStore.ACTION_LIST_INCIDENT,
			data: riskId,
		});

		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
			data: {
				page: page,
				pageSize: pageSize,
			},
		});
	},

	insertNewRow() {
		if (this.state.newRowDisplayed || this.state.updateRowDisplayed) {
			return;
		}
        const newRow = {
			description: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-incident-description",
					type: "textarea",
					rows: "3",
					onChange: this.descriptionChangeHandler
				}}
			/>,
			action: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-incident-action",
					type: "textarea",
					rows: "3",
					onChange: this.actionChangeHandler
				}}
			/>,
			type: <VerticalInput
				fieldDef={{
					name: "new-incident-type",
					type: "select",
					options: _.map(incidentTypes.values, value => value.label),
					renderDisplay: value => value,
					onChange: this.typeChangeHandler
				}}
			/>,
			user: {
				name: <VerticalInput
					fieldDef={{
						name: "new-incident-user",
						type: "select",
						options: _.map(this.state.users, user => user.name),
						renderDisplay: value => value,
						onChange: this.userChangeHandler
					}}
				/>
			},
			begin: <div className="double-input-cell">
				<VerticalInput
					fieldDef={{
						name: "new-incident-begin-date",
						type: "custom-mask",
						mask: "11/11/1111",
						onChange: this.beginDateChangeHandler
					}}
				/>
				<VerticalInput
					fieldDef={{
						name: "new-incident-begin-hour",
						type: "custom-mask",
						mask: "11:11",
						onChange: this.beginHourChangeHandler
					}}
				/>
			</div>,
			tools: <div className="row-tools-box">
				<button className="row-button-icon" onClick={this.newIncident}>
					<span className="mdi mdi-check" />
				</button>
				<button
					className="row-button-icon"
					onClick={() =>
						this.setState({
							data: this.state.data.slice(1),
							newRowDisplayed: false,
						})
					}>
					<span className="mdi mdi-close" />
				</button>
			</div>,
		};
		const { data } = this.state;
		data.unshift(newRow);
        this.setState({
			data,
			newRowDisplayed: true,
			incident: {
				risk: this.props.risk,
				user: this.state.users.length > 0 ? this.state.users[0] : null,
				type: incidentTypes.values[0].id,
			},
		});
    },

	enableUpdateMode(idx) {
		if (this.state.updateRowDisplayed || this.state.newRowDisplayed) {
			return;
		}
		const { data } = this.state;
		const incident = {
			...data[idx],
			type: incidentTypes.getByLabel(data[idx].type).id,
		};
		data[idx] = {
			description: <VerticalInput
				fieldDef={{
					name: "new-incident-description",
					type: "textarea",
					rows: "3",
					value: data[idx].description,
					onChange: this.descriptionChangeHandler,
				}}
			/>,
			action: <VerticalInput
				fieldDef={{
					name: "new-incident-description",
					type: "textarea",
					rows: "3",
					value: data[idx].action,
					onChange: this.actionChangeHandler,
				}}
			/>,
			type: <VerticalInput
				fieldDef={{
					name: "new-incident-user",
					type: "select",
					options: _.map(incidentTypes.values, value => value.label),
					renderDisplay: value => value,
					value: data[idx].type,
					onChange: this.typeChangeHandler
				}}
			/>,
			user: {
				name: <VerticalInput
					fieldDef={{
						name: "new-incident-user",
						type: "select",
						options: _.map(this.state.users, user => user.name),
						renderDisplay: value => value,
						value: data[idx].user.name,
						onChange: this.userChangeHandler
					}}
				/>
			},
			begin: <div className="double-input-cell">
			<VerticalInput
				fieldDef={{
					name: "new-incident-begin-date",
					type: "custom-mask",
					mask: "11/11/1111",
					value: data[idx].begin.split(' ')[0],
					onChange: this.beginDateChangeHandler
				}}
				/>
				<VerticalInput
					fieldDef={{
						name: "new-incident-begin-hour",
						type: "custom-mask",
						mask: "11:11",
						value: data[idx].begin.split(' ')[1],
						onChange: this.beginHourChangeHandler
					}}
				/>
			</div>,
			tools: <div className="row-tools-box">
				<button className="row-button-icon" onClick={this.updateIncident}>
					<span className="mdi mdi-check" />
				</button>
				<button
					className="row-button-icon"
					onClick={() => {
						const { data } = this.state;
						data[idx] = incident;
						this.setState({
							data,
							updateRowDisplayed: false,
						})
					}}>
					<span className="mdi mdi-close" />
				</button>
			</div>,
		};
		this.setState({
			data,
			beginDate: incident.begin.split(' ')[0],
			beginHour: incident.begin.split(' ')[1],
			incident,
			updateRowDisplayed: true,
		});
	},

	newIncident() {
		var beginDate = moment(this.state.beginDate, 'DD/MM/YYYY').toDate();

		if (!this.state.incident.user) {
			this.context.toastr.addAlertError("É necessário que seja selecionado um usuário responsável");
			return;
		}

		if(moment() < beginDate) {
			this.context.toastr.addAlertError("A data do incidente não deve ser maior que a data atual");
			return;
		}

		RiskStore.dispatch({
			action: RiskStore.ACTION_NEW_INCIDENT,
			data: {
				incident: {
					...this.state.incident,
					begin: `${this.state.beginDate} ${this.state.beginHour}:00`,
				},
			},
		});
	},

	deleteIncident(id) {
		RiskStore.dispatch({
			action: RiskStore.ACTION_DELETE_INCIDENT,
			data: {
				incidentId: id,
			},
		});
	},

	updateIncident() {
		RiskStore.dispatch({
			action: RiskStore.ACTION_UPDATE_INCIDENT,
			data: {
				incident: {
					...this.state.incident,
					begin: `${this.state.beginDate} ${this.state.beginHour}:00`,
					tools: undefined,
				},
			},
		});
	},


	descriptionChangeHandler(e) {
		this.setState({
			incident: {
				...this.state.incident,
				description: e.target.value,
			}
		});
	},

	actionChangeHandler(e) {
		this.setState({
			incident: {
				...this.state.incident,
				action: e.target.value,
			}
		});
	},

	typeChangeHandler(e) {
		this.setState({
			incident: {
				...this.state.incident,
				type: incidentTypes.getByLabel(e.target.value).id,
			}
		});
	},

	userChangeHandler(e) {
		const idx = e.target.options.selectedIndex;
		this.setState({
			incident: {
				...this.state.incident,
				user: this.state.users[idx],
			}
		});
	},

	beginDateChangeHandler(e) {
		this.setState({
			beginDate: e.target.value,
		});
	},

	beginHourChangeHandler(e) {
		this.setState({
			beginHour: e.target.value,
		});
	},

	renderRowTools(id, idx) {
		return (
			<div className="row-tools-box">
				<button
					className="row-button-icon"
					onClick={() => this.enableUpdateMode(idx)}
				>
					<span className="mdi mdi-pencil" />
				</button>
				<button
					className="row-button-icon"
					onClick={() => this.deleteIncident(id)}
				>
					<span className="mdi mdi-delete" />
				</button>
			</div>
		);
	},

	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge/>;
		}
		const columns = [{
			Header: 'Descrição',
			accessor: 'description',
			minWidth: 250,
		}, {
			Header: 'Ações corretivas',
			accessor: 'action',
			minWidth: 250,
		}, {
			Header: 'Tipo',
			accessor: 'type',
			minWidth: 150
		}, {
			Header: 'Responsável',
			accessor: 'user.name',
			minWidth: 250
		}, {
			Header: 'Data e horário',
			accessor: 'begin',
			minWidth: 150
		}, {
			Header: '',
			accessor: 'tools',
			sortable: false,
		}];
		return (
			<div className="general-table">
				<div className='table-outter-header'>
                    HISTÓRICO DE INCIDENTES
                    <Button bsStyle="info" onClick={this.insertNewRow} >Novo</Button>
                </div>
				<ReactTable
					data={this.state.data}
					columns={columns}
					showPagination={false}
					loading={false}
					resizable={true}
					pageSize={this.state.data.length}
					NoDataComponent={() =>
						<div className="rt-td">
							Nenhum incidente cadastrado
						</div>
					}
				/>
			</div>
		)
	}
});
