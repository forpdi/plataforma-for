import React from "react";
import ReactTable from 'react-table';
import "react-table/react-table.css";
import _ from 'underscore';
import { Button } from 'react-bootstrap';

import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import UserStore from 'forpdi/jsx/core/store/User.jsx';
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx";
import { MED_PAGE_SIZE } from "forpdi/jsx/core/util/const.js";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		toastr: React.PropTypes.object.isRequired,
	},

	getInitialState() {
		return {
			data: [],
			dataTotal: null,
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
						_.assign(value, {
							tools: this.isPermissionedUser()
								? this.renderRowTools(value.id, idx)
								: null,
						})
					)),
					dataTotal: response.total,
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
				this.getData(this.props.risk.id);
			} else {
				this.context.toastr.addAlertError(response.msg);
			}
		}, this);

		RiskStore.on('contingencyDeleted', (response) => {
			if (response.success) {
				this.context.toastr.addAlertSuccess("Ação de contingenciamento excluída com sucesso.");
				this.setState({
					isLoading: true,
				});
				this.getData(this.props.risk.id);
			} else {
				this.context.toastr.addAlertError(response.msg);
			}
		}, this);

		RiskStore.on('contingencyUpdated', (response) => {
			if (response.success) {
				this.context.toastr.addAlertSuccess("Ação de contingenciamento atualizada com sucesso.");
				this.setState({
					isLoading: true,
				});
				this.getData(this.props.risk.id);
			} else {
				this.context.toastr.addAlertError(response.msg);
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
		}, this);
		this.refreshComponent(this.props.risk.id);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.risk.id !== this.props.risk.id) {
			this.refreshComponent(newProps.risk.id);
		}
	},

	componentWillUnmount() {
		RiskStore.off(null, null, this);
		UserStore.off(null, null, this);
	},

	isPermissionedUser() {
		return (this.context.roles.COLABORATOR ||
			_.contains(this.context.permissions, PermissionsTypes.FORRISCO_MANAGE_RISK_ITEMS_PERMISSION)
		);
	},

	getData(riskId, page = 1, pageSize = MED_PAGE_SIZE) {
		RiskStore.dispatch({
			action: RiskStore.ACTION_LIST_CONTINGENCY,
			data: {
				riskId,
				page,
				pageSize,
			},
		});
	},

	pageChange(page, pageSize) {
		this.getData(this.props.risk.id, page, pageSize);
	},

	refreshComponent(riskId) {
		this.getData(riskId);
		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
			data: {
				page: 1,
				pageSize: 500,
			},
		});
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
				<span
					className="mdi mdi-check btn btn-sm btn-success"
					title="Salvar"
					onClick={this.newContingency}
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
				<span
					className="mdi mdi-check btn btn-sm btn-success"
					title="Salvar"
					onClick={this.updateContingency}
				/>
				<span
					className="mdi mdi-close btn btn-sm btn-danger"
					title="Cancelar"
					onClick={() => {
						const { data } = this.state;
						data[idx] = contingency;
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
			minWidth: 770
		}, {
			Header: 'Responsável',
			accessor: 'user.name',
			minWidth: 280
		}, {
			Header: '',
			accessor: 'tools',
			sortable: false,
			width: 100,
		}];
		return (
			<div className="general-table">
				<div className='table-outter-header'>
                    AÇÕES DE CONTINGENCIAMENTO
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
							Nenhuma ação de contingenciamento cadastrada
						</div>
					}
				/>
				<TablePagination
					defaultPageSize={MED_PAGE_SIZE}
					total={this.state.dataTotal}
					onChangePage={this.pageChange}
					tableName={"contingency-table"}
				/>
			</div>
		)
	}
});
