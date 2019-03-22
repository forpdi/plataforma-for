import React from "react";
import ReactTable from 'react-table';
import "react-table/react-table.css";
import _ from 'underscore';
import { Button } from 'react-bootstrap';
import moment from 'moment'

import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import UserStore from 'forpdi/jsx/core/store/User.jsx';
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		toastr: React.PropTypes.object.isRequired,
		planRisk: React.PropTypes.object.isRequired,
	},

	getInitialState() {
		return {
			data: [],
			users: [],
			impacts: [],
			probabilities: [],
			monitor: null,
			beginDate: null,
			beginHour: null,
			newRowDisplayed: false,
			updateRowDisplayed: false,
			isLoading: true,
		}
	},

	componentDidMount() {

		RiskStore.on('monitorListed', (response) => {
			if (response !== null) {
				this.setState({
					data: _.map(response.data, (value, idx) => (
						_.assign(value, {
							tools: this.isPermissionedUser()
								? this.renderRowTools(value.id, idx)
								: null,
						})
					)),
					isLoading: false,
					newRowDisplayed: false,
					updateRowDisplayed: false,
				});
			}
		}, this);

		RiskStore.on('monitorCreated', (response) => {
			if (response.data) {
				this.context.toastr.addAlertSuccess("Monitoramento cadastrado com sucesso.");
				this.setState({
					isLoading: true,
				});
				RiskStore.dispatch({
					action: RiskStore.ACTION_LIST_MONITOR,
					data:  this.props.risk.id,
				});
			} else {
				this.context.toastr.addAlertError(response.msg);
			}
		}, this);

		RiskStore.on('monitorDeleted', (response) => {
			if (response.success) {
				this.context.toastr.addAlertSuccess("Monitoramento excluído com sucesso.");
				this.setState({
					isLoading: true,
				});
				RiskStore.dispatch({
					action: RiskStore.ACTION_LIST_MONITOR,
					data:  this.props.planRiskId,
				});
			} else {
				this.context.toastr.addAlertError(response.msg);
			}
		}, this);

		RiskStore.on('monitorUpdated', (response) => {
			if (response.success) {
				this.context.toastr.addAlertSuccess("monitoramento atualizado com sucesso.");
				this.setState({
					isLoading: true,
				});
				RiskStore.dispatch({
					action: RiskStore.ACTION_LIST_MONITOR,
					data: this.props.risk.id,
				});
			} else {
				this.context.toastr.addAlertError(response.msg);
			}
		}, this);

		UserStore.on('retrieve-user', (response) => {
			const users = response.data;
			if (response.data) {
				this.setState({
					users: response.data,
					monitor: {
						risk: this.props.risk,
						user: users.length > 0 ? users[0] : null,
					},

				});
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar os usuários da companhia");
			}
		}, this);

		this.setState({
			impacts: this.getSelectOptions(this.context.planRisk.attributes.policy.impact),
			probabilities: this.getSelectOptions(this.context.planRisk.attributes.policy.probability),
		});
		this.refreshComponent(this.props.risk.id, 1, 500);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.risk.id !== this.props.risk.id) {
			this.refreshComponent(newProps.risk.id,1, 500)
		}
	},

	isPermissionedUser() {
		return (this.context.roles.COLABORATOR ||
			_.contains(this.context.permissions, PermissionsTypes.FORRISCO_MANAGE_RISK_ITEMS_PERMISSION)
		);
	},

	refreshComponent(riskId, page, pageSize) {
		RiskStore.dispatch({
			action: RiskStore.ACTION_LIST_MONITOR,
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

	getSelectOptions(originalArray) {
		var originalArray = originalArray.match(/\[.*?\]/g);
		var options = [];

		if (originalArray != null) {
			for (var i in originalArray) {
				options.push(originalArray[i].substring(1, originalArray[i].length - 1))
			}
		}
		return options;
	},

	componentWillUnmount() {
		RiskStore.off(null, null, this);
		UserStore.off(null, null, this);
	},

	insertNewRow() {
		if (this.state.newRowDisplayed || this.state.updateRowDisplayed) {
			return;
		}
		// const showusers = _.map(this.state.users, user => user.name);
		// console.log(showusers);
        const newRow = {
			report: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-monitor-report",
					type: "textarea",
					rows: "3",
					onChange: this.reportChangeHandler
				}}
			/>,
			probability: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-monitor-probability",
					type: "select",
					options: _.map(this.state.probabilities, probability => probability),
					renderDisplay: value => value,
					onChange: this.probabilityChangeHandler
				}}
			/>,
			impact: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-monitor-impact",
					type: "select",
					options: _.map(this.state.impacts, impact => impact),
					renderDisplay: value => value,
					onChange: this.impactChangeHandler
				}}
			/>,
			user: {
				name: <VerticalInput
					fieldDef={{
						name: "new-monitor-user",
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
						name: "new-monitor-begin-date",
						type: "custom-mask",
						mask: "11/11/1111",
						onChange: this.beginDateChangeHandler
					}}
				/>
				<VerticalInput
					fieldDef={{
						name: "new-monitor-begin-hour",
						type: "custom-mask",
						mask: "11:11",
						onChange: this.beginHourChangeHandler
					}}
				/>
			</div>,
			tools: <div className="row-tools-box">
				<span
					className="mdi mdi-check btn btn-sm btn-success"
					title="Salvar"
					onClick={this.newMonitor}
				/>
				<span
					className="mdi mdi-close btn btn-sm btn-danger"
					title="Cancelar"
					onClick={() =>
						this.setState({
							data: this.state.data.slice(1),
							newRowDisplayed: false,
						})
					}
				/>
			</div>,
		}
		const { data } = this.state;
		data.unshift(newRow);
        this.setState({
			data,
			newRowDisplayed: true,
			monitor: {
				risk: this.props.risk,
				user: this.state.users.length > 0 ? this.state.users[0] : null,
				impact: this.state.impacts.length > 0 ? this.state.impacts[0] : null,
				probability: this.state.probabilities.length > 0 ? this.state.probabilities[0] : null,
			},
		});
    },

	enableUpdateMode(idx) {
		if (this.state.updateRowDisplayed || this.state.newRowDisplayed) {
			return;
		}
		const { data } = this.state;
		const monitor = {
			...data[idx],
		};
		data[idx] = {
			report: <VerticalInput
				fieldDef={{
					name: "new-monitor-report",
					type: "textarea",
					rows: "3",
					value: data[idx].report,
					onChange: this.reportChangeHandler,
				}}
			/>,
			probability: <VerticalInput
				fieldDef={{
					name: "new-monitor-probability",
					type: "select",
					options: _.map(this.state.probabilities, probability => probability),
					renderDisplay: value => value,
					value: data[idx].probability,
					onChange: this.probabilityChangeHandler,
				}}
			/>,
			impact: <VerticalInput
				fieldDef={{
					name: "new-monitor-impact",
					type: "select",
					options: _.map(this.state.impacts, impact => impact),
					renderDisplay: value => value,
					value: data[idx].impact,
					onChange: this.impactChangeHandler,
				}}
			/>,
			user: {
				name: <VerticalInput
					fieldDef={{
						name: "new-monitor-user",
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
					name: "new-monitor-begin-date",
					type: "custom-mask",
					mask: "11/11/1111",
					value: data[idx].begin.split(' ')[0],
					onChange: this.beginDateChangeHandler
				}}
				/>
				<VerticalInput
					fieldDef={{
						name: "new-monitor-begin-hour",
						type: "custom-mask",
						mask: "11:11",
						value: data[idx].begin.split(' ')[1],
						onChange: this.beginHourChangeHandler
					}}
				/>
			</div>,
			tools: <div className="row-tools-box">
				<span
					className="mdi mdi-check btn btn-sm btn-success"
					title="Salvar"
					onClick={this.updateMonitor}
				/>
				<span
					className="mdi mdi-close btn btn-sm btn-danger"
					title="Cancelar"
					onClick={() => {
						const { data } = this.state;
						data[idx] = monitor;
						this.setState({
							data,
							updateRowDisplayed: false,
						})
					}}
				/>
			</div>,
		}
		this.setState({
			data,
			beginDate: monitor.begin.split(' ')[0],
			beginHour: monitor.begin.split(' ')[1],
			monitor,
			updateRowDisplayed: true,
		});
	},

	newMonitor() {
		if (!this.state.monitor.user) {
			this.context.toastr.addAlertError("É necessário que seja selecionado um usuário responsável");
			return;
		}
		const beginDate = moment(this.state.beginDate, 'DD/MM/YYYY').toDate();
		if(moment() < beginDate) {
			this.context.toastr.addAlertError("A data do monitor não deve ser maior que a data atual");
			return;
		}
		RiskStore.dispatch({
			action: RiskStore.ACTION_NEW_MONITOR,
			data: {
				monitor: {
					...this.state.monitor,
					begin: `${this.state.beginDate} ${this.state.beginHour}:00`,
				},
			},
		});
	},

	deleteMonitor(id) {
		RiskStore.dispatch({
			action: RiskStore.ACTION_DELETE_MONITOR,
			data: {
				monitorId: id,
			},
		});
	},

	updateMonitor() {
		const beginDate = moment(this.state.beginDate, 'DD/MM/YYYY').toDate();
		if(moment() < beginDate) {
			this.context.toastr.addAlertError("A data do monitor não deve ser maior que a data atual");
			return;
		}
		RiskStore.dispatch({
			action: RiskStore.ACTION_UPDATE_MONITOR,
			data: {
				monitor: {
					...this.state.monitor,
					begin: `${this.state.beginDate} ${this.state.beginHour}:00`,
					tools: undefined,
				},
			},
		});
	},


	reportChangeHandler(e) {
		this.setState({
			monitor: {
				...this.state.monitor,
				report: e.target.value,
			}
		});
	},

	probabilityChangeHandler(e) {
		const idx = e.target.options.selectedIndex;
		this.setState({
			monitor: {
				...this.state.monitor,
				probability: this.state.probabilities[idx],
			}
		});
	},

	impactChangeHandler(e) {
		const idx = e.target.options.selectedIndex;
		this.setState({
			monitor: {
				...this.state.monitor,
				impact: this.state.impacts[idx],
			}
		});
	},

	userChangeHandler(e) {
		const idx = e.target.options.selectedIndex;
		this.setState({
			monitor: {
				...this.state.monitor,
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
					onClick={() => this.deleteMonitor(id)}
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
			Header: 'Parecer',
			accessor: 'report',
			minWidth: 350,
		}, {
			Header: 'Probabilidade',
			accessor: 'probability',
			minWidth: 150,
		}, {
			Header: 'Impacto',
			accessor: 'impact',
			minWidth: 150,
		}, {
			accessor: 'user.name',
			Header: 'Responsável',
			minWidth: 250,
		}, {
			Header: 'Data e horário',
			accessor: 'begin',
			minWidth: 150,
		}, {
			Header: '',
			accessor: 'tools',
			sortable: false,
			width: 100,
		}];
		return (
			<div className="general-table">
				<div className='table-outter-header'>
                    HISTÓRICO DE MONITORAMENTOS
					{
						this.isPermissionedUser() &&
                    	<Button bsStyle="info" onClick={this.insertNewRow} >Novo</Button>
					}
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
							Nenhum monitoramento cadastrado
						</div>
					}
				/>
			</div>
		)
	}
});
