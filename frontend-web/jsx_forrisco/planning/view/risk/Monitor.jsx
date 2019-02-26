import React from "react";
import ReactTable from 'react-table';
import "react-table/react-table.css";
import _ from 'underscore';
import { Button } from 'react-bootstrap';

import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import UserStore from 'forpdi/jsx/core/store/User.jsx';
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
	},

	getInitialState() {
		return {
			data: [],
			users: [],
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
						_.assign(value, { tools: this.renderRowTools(value.id, idx) })
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
					data: {
						planId: this.props.planRiskId,
					},
				});
			} else {
				this.context.toastr.addAlertError("Erro ao cadastrar monitoramento.");
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
					data: {
						planId: this.props.planRiskId,
					},
				});
			} else {
				this.context.toastr.addAlertError("Erro ao excluir monitoramento.");
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
					data: {
						planId: this.props.planRiskId,
					},
				});
			} else {
				this.context.toastr.addAlertError("Erro ao atualizar monitoramento.");
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
		});
		RiskStore.dispatch({
			action: RiskStore.ACTION_LIST_MONITOR,
			data: {
				planId: this.props.planRiskId,
			},
		});
		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
			data: {
				page: 1,
				pageSize: 500,
			},
		});
	},

	componentWillUnmount() {
		RiskStore.off(null, null, this);
		UserStore.off(null, null, this);
	},

	insertNewRow() {
		if (this.state.newRowDisplayed || this.state.updateRowDisplayed) {
			return;
		}
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
					type: "textarea",
					rows: "1",
					onChange: this.probabilityChangeHandler
				}}
			/>,
			impact: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-monitor-impact",
					type: "textarea",
					rows: "1",
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
				<button className="row-button-icon" onClick={this.newMonitor}>
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
		}
		const { data } = this.state;
		data.unshift(newRow);
        this.setState({
			data,
			newRowDisplayed: true,
			monitor: {
				risk: this.props.risk,
				user: this.state.users.length > 0 ? this.state.users[0] : null,
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
		console.log("print data");
		console.log(data);
		console.log(idx);
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
					type: "textarea",
					rows: "1",
					value: data[idx].probability,
					onChange: this.probabilityChangeHandler,
				}}
			/>,
			impact: <VerticalInput
				fieldDef={{
					name: "new-monitor-impact",
					type: "textarea",
					rows: "1",
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
				<button className="row-button-icon" onClick={this.updateMonitor}>
					<span className="mdi mdi-check" />
				</button>
				<button
					className="row-button-icon"
					onClick={() => {
						const { data } = this.state;
						data[idx] = monitor;
						this.setState({
							data,
							updateRowDisplayed: false,
						})
					}}>
					<span className="mdi mdi-close" />
				</button>
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
		console.log(this.state.monitor);
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
		this.setState({
			monitor: {
				...this.state.monitor,
				probability: e.target.value,
			}
		});
	},

	impactChangeHandler(e) {
		this.setState({
			monitor: {
				...this.state.monitor,
				impact: e.target.value,
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
					onClick={() => {console.log("editing"); this.enableUpdateMode(idx)}}
				>
					<span className="mdi mdi-pencil" />
				</button>
				<button
					className="row-button-icon"
					onClick={() => {console.log("deleting"); this.deleteMonitor(id)}}
				>
					<span className="mdi mdi-delete" />
				</button>
			</div>
		);
	},

	render() {
		if (this.state.isLoading === true) {
			console.log('carregando esse paranaue');
			return <LoadingGauge/>;
		}
		console.log('carregou');
		const columns = [{
			Header: 'Parecer',
			accessor: 'report',
			minWidth: 200,
		}, {
			Header: 'Probabilidade',
			accessor: 'probability',
			minWidth: 100,
		}, {
			Header: 'Impacto',
			accessor: 'impact',
			minWidth: 80,
		}, {
			Header: 'Responsável',
			accessor: 'user.name',
			minWidth: 150,
		}, {
			Header: 'Data e horário',
			accessor: 'begin',
		}, {
			Header: '',
			accessor: 'tools',
			sortable: false,
			width: 100
		}];
		return (
			<div className="general-table">
				<div className='table-outter-header'>
                    HISTÓRICO DE MONITORAMENTOS
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
						<div className="marginLeft10">
							Nenhum monitoramento cadastrado
						</div>
					}
				/>
			</div>
		)
	}
});
