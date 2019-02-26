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
			contingency: null,
			newRowDisplayed: false,
			updateRowDisplayed: false,
			isLoading: true,
		}
	},

	componentDidMount() {
		RiskStore.on('contingencyListed', (response) => {
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
		RiskStore.on('contingencyCreated', (response) => {
			if (response.data) {
				this.context.toastr.addAlertSuccess("Ação de contingenciamento cadastrada com sucesso.");
				this.setState({
					isLoading: true,
				});
				RiskStore.dispatch({
					action: RiskStore.ACTION_LIST_CONTINGENCY,
					data: {
						riskId: this.props.risk.id,
					},
				});
			} else {
				this.context.toastr.addAlertError("Erro ao cadastrar a ação de contingenciamento.");
			}
		}, this);
		RiskStore.on('contingencyDeleted', (response) => {
			if (response.success) {
				this.context.toastr.addAlertSuccess("Ação de contingenciamento excluída com sucesso.");
				this.setState({
					isLoading: true,
				});
				RiskStore.dispatch({
					action: RiskStore.ACTION_LIST_CONTINGENCY,
					data: {
						riskId: this.props.risk.id,
					},
				});
			} else {
				this.context.toastr.addAlertError("Erro ao excluir a ação de contingenciamento.");
			}
		}, this);
		RiskStore.on('contingencyUpdated', (response) => {
			if (response.success) {
				this.context.toastr.addAlertSuccess("Ação de contingenciamento atualizada com sucesso.");
				this.setState({
					isLoading: true,
				});
				RiskStore.dispatch({
					action: RiskStore.ACTION_LIST_CONTINGENCY,
					data: {
						riskId: this.props.risk.id,
					},
				});
			} else {
				this.context.toastr.addAlertError("Erro ao atualizar a ação de contingenciamento.");
			}
		}, this);
		UserStore.on('retrieve-user', (response) => {
			const users = response.data;
			if (response.data) {
				this.setState({
					users: response.data,
					contingency: {
						risk: this.props.risk,
						user: users.length > 0 ? users[0] : null,
					},

				});
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar os usuários da companhia");
			}
		});
		RiskStore.dispatch({
			action: RiskStore.ACTION_LIST_CONTINGENCY,
			data: {
				riskId: this.props.risk.id,
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
			action:	<VerticalInput
				fieldDef={{
					name: "new-contingency-action",
					type: "textarea",
					rows: "3",
					onChange: this.actionChangeHandler
				}}
			/>,
			user: {
				name: <VerticalInput
					fieldDef={{
						name: "new-contingency-user",
						type: "select",
						options: _.map(this.state.users, user => user.name),
						renderDisplay: value => value,
						onChange: this.userChangeHandler
					}}
				/>
			},
			tools: <div className="row-tools-box">
				<button className="row-button-icon" onClick={this.newContingency}>
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
			contingency: {
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
		const contingency = data[idx];
		data[idx] = {
			action:	<VerticalInput
				fieldDef={{
					name: "new-contingency-action",
					type: "textarea",
					rows: "3",
					value: data[idx].action,
					onChange: this.actionChangeHandler,
				}}
			/>,
			user: {
				name: <VerticalInput
					fieldDef={{
						name: "new-contingency-user",
						type: "select",
						options: _.map(this.state.users, user => user.name),
						renderDisplay: value => value,
						value: data[idx].user.name,
						onChange: this.userChangeHandler
					}}
				/>
			},
			tools: <div className="row-tools-box">
				<button className="row-button-icon" onClick={this.updateContingency}>
					<span className="mdi mdi-check" />
				</button>
				<button
					className="row-button-icon"
					onClick={() => {
						const { data } = this.state;
						data[idx] = contingency;
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
			contingency,
			updateRowDisplayed: true,
		});
	},

	newContingency() {
		if (!this.state.contingency.user) {
			this.context.toastr.addAlertError("É necessário que seja selecionado um usuário responsável");
			return;
		}
		RiskStore.dispatch({
			action: RiskStore.ACTION_NEW_CONTINGENCY,
			data: {
				contingency: this.state.contingency,
			},
		});
	},

	deleteContingency(id) {
		RiskStore.dispatch({
			action: RiskStore.ACTION_DELETE_CONTINGENCY,
			data: {
				contingencyId: id,
			},
		});
	},

	updateContingency() {
		RiskStore.dispatch({
			action: RiskStore.ACTION_UPDATE_CONTINGENCY,
			data: {
				contingency: { ...this.state.contingency, tools: undefined },
			},
		});
	},


	actionChangeHandler(e) {
		this.setState({
			contingency: {
				...this.state.contingency,
				action: e.target.value,
			}
		});
	},

	userChangeHandler(e) {
		const idx = e.target.options.selectedIndex;
		this.setState({
			contingency: {
				...this.state.contingency,
				user: this.state.users[idx],
			}
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
					onClick={() => this.deleteContingency(id)}
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
			Header: 'Ação',
			accessor: 'action',
			minWidth: 200
		}, {
			Header: 'Responsável',
			accessor: 'user.name',
		}, {
			Header: '',
			accessor: 'tools',
			sortable: false,
			width: 100
		}];
		return (
			<div className="general-table">
				<div className='table-outter-header'>
                    AÇÕES DE CONTINGENCIAMENTO
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
							Nenhuma ação de contingenciamento cadastrada
						</div>
					}
				/>
			</div>
		)
	}
});
