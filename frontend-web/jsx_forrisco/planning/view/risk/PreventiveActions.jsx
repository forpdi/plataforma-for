import React from "react";
import ReactTable from 'react-table';
import "react-table/react-table.css";
import _ from 'underscore';
import { Button } from 'react-bootstrap';

import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import UserStore from 'forpdi/jsx/core/store/User.jsx';
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx";
import { MED_PAGE_SIZE } from "forpdi/jsx/core/util/const.js";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

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
			action: null,
			newRowDisplayed: false,
			updateRowDisplayed: false,
			isLoading: true,
		}
	},

	componentDidMount() {
		RiskStore.on('preventiveActionsListed', (response) => {
			if (response !== null) {
				this.setState({
					data: _.map(response.data, (value, idx) => (
						_.assign(value, {
							tools: this.isPermissionedUser() ? this.renderRowTools(value.id, idx) : null,
						})
					)),
					dataTotal: response.total,
					isLoading: false,
					newRowDisplayed: false,
					updateRowDisplayed: false,
				});
				this.changeAccomplishmentData();
			}
		}, this);

		RiskStore.on('preventiveActionCreated', (response) => {
			if (response.data) {
				this.context.toastr.addAlertSuccess("Ação de prevenção cadastrada com sucesso.");
				this.setState({
					isLoading: true,
				});
				this.getData(this.props.risk.id);
			} else {
				this.context.toastr.addAlertError("Erro ao cadastrar ação de prevenção.");
			}
		}, this);

		RiskStore.on('preventiveActionDeleted', (response) => {
			if (response.success) {
				this.context.toastr.addAlertSuccess("Ação de prevenção excluída com sucesso.");
				this.setState({
					isLoading: true,
				});
				this.getData(this.props.risk.id);
			} else {
				this.context.toastr.addAlertError("Erro ao excluir ação de prevenção.");
			}
		}, this);

		RiskStore.on('preventiveActionUpdated', (response) => {
			if (response.success) {
				this.context.toastr.addAlertSuccess("Ação de prevenção atualizada com sucesso.");
				this.setState({
					isLoading: true,
				});
				this.getData(this.props.risk.id);
			} else {
				this.context.toastr.addAlertError("Erro ao atualizar ação de prevenção.");
			}
		}, this);

		UserStore.on('retrieve-user', (response) => {
			const users = response.data;
			if (response.data) {
				this.setState({
					users: response.data,
					action: {
						risk: this.props.risk,
						user: users.length > 0 ? users[0] : null,
					},

				});
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar os usuários da companhia");
			}
		}, this);
		this.refresh(this.props.risk.id)
	},

	componentWillReceiveProps(newProps) {
		if (this.props.risk.id !== newProps.risk.id) {
			this.refresh(newProps.risk.id)
		}
	},

	componentWillUnmount() {
		RiskStore.off(null, null, this);
		UserStore.off(null, null, this);
	},

	isPermissionedUser() {
		return (this.context.roles.MANAGER ||
			_.contains(this.context.permissions, PermissionsTypes.FORRISCO_MANAGE_RISK_PERMISSION)
		);
	},

	refresh(riskId){
		this.getData(riskId);
		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
			data: {
				page: 1,
				pageSize: 500,
			},
		});
	},

	getData(riskId, page = 1, pageSize = MED_PAGE_SIZE) {
		RiskStore.dispatch({
			action: RiskStore.ACTION_LIST_PREVENTIVE_ACTIONS,
			data: {
				riskId,
				page,
				pageSize,
			},
		});
		this.refs['preventive-actions-pagination'] && this.refs['preventive-actions-pagination'].setState({
			page,
			pageSize,
		});
	},

	pageChange(page, pageSize) {
		this.getData(this.props.risk.id, page, pageSize);
	},

	changeAccomplishmentData() {
		this.setState({
			data: _.map(this.state.data, (action, idx) => (
				_.assign(action, {
					accomplished: this.renderAccomplishmentOnList(action.accomplished, action.id, idx),
				})
			)),
		});
	},

	insertNewRow() {
		if (this.state.newRowDisplayed || this.state.updateRowDisplayed) {
			return;
		}
        const newRow = {
			action: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-preventive-action-action",
					type: "textarea",
					rows: "3",
					onChange: this.actionChangeHandler
				}}
			/>,
			user: {
				name: <VerticalInput
					fieldDef={{
						name: "new-preventive-action-user",
						type: "select",
						options: _.map(this.state.users, user => user.name),
						renderDisplay: value => value,
						onChange: this.userChangeHandler
					}}
				/>
			},
			accomplished: <VerticalInput
				className="padding7 accomplishment-radio"
				fieldDef={{
					name: "new-preventive-action-accomplishment",
					type: "radio",
					options: [{ label: "Sim", value: true }, { label: "Não", value: false }],
					valueField: 'value',
					value: false,
					renderDisplay: value => value.label,
					onClick: this.accomplishmentChangeHandler
				}}
			/>,
			tools: <div className="row-tools-box">
				<span
					className="mdi mdi-check btn btn-sm btn-success"
					title="Salvar"
					onClick={this.newPreventiveAction}
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
			action: {
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
		const action = {
			...data[idx],
		};
		data[idx] = {
			action: <VerticalInput
				fieldDef={{
					name: "new-preventive-action-action",
					type: "textarea",
					rows: "3",
					value: data[idx].action,
					onChange: this.actionChangeHandler,
				}}
			/>,
			user: {
				name: <VerticalInput
					fieldDef={{
						name: "new-preventive-action-user",
						type: "select",
						options: _.map(this.state.users, user => user.name),
						renderDisplay: value => value,
						value: data[idx].user.name,
						onChange: this.userChangeHandler
					}}
				/>
			},
			accomplished: <VerticalInput
				className="padding7 accomplishment-radio"
				fieldDef={{
					name: "new-preventive-action-accomplishment",
					type: "radio",
					options: [{ label: "Sim", value: true }, { label: "Não", value: false }],
					valueField: 'value',
					value: data[idx].accomplished,
					renderDisplay: value => value.label,
					onClick: this.accomplishmentChangeHandler
				}}
			/>,
			tools: <div className="row-tools-box">
				<span
					className="mdi mdi-check btn btn-sm btn-success"
					title="Salvar"
					onClick={() => {
						this.updatePreventiveAction({
							...this.state.action,
							accomplished: this.state.action.accomplished.props.fieldDef.value,
							tools: undefined,
						})
					}}
				/>
				<span
					className="mdi mdi-close btn btn-sm btn-danger"
					title="Cancelar"
					onClick={() => {
						const { data } = this.state;
						data[idx] = action;
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
			action,
			updateRowDisplayed: true,
		});
	},

	newPreventiveAction() {
		if (!this.state.action.user) {
			this.context.toastr.addAlertError("É necessário que seja selecionado um usuário responsável");
			return;
		}
		RiskStore.dispatch({
			action: RiskStore.ACTION_NEW_PREVENTIVE_ACTION,
			data: {
				action: {
					...this.state.action,
				},
			},
		});
	},

	deletePreventiveAction(id) {
		var msg = "Você tem certeza que deseja excluir esta Ação Preventiva?"
		Modal.confirmCustom(() => {
			Modal.hide();
			RiskStore.dispatch({
				action: RiskStore.ACTION_DELETE_PREVENTIVE_ACTION,
				data: {
					actionId: id,
				},
			});
		}, msg, () => {
			Modal.hide()
		});
	},
	
	updatePreventiveAction(action) {
		RiskStore.dispatch({
			action: RiskStore.ACTION_UPDATE_PREVENTIVE_ACTION,
			data: {
				action,
			},
		});
	},


	actionChangeHandler(e) {
		this.setState({
			action: {
				...this.state.action,
				action: e.target.value,
			}
		});
	},

	userChangeHandler(e) {
		const idx = e.target.options.selectedIndex;
		this.setState({
			action: {
				...this.state.action,
				user: this.state.users[idx],
			}
		});
	},

	accomplishmentChangeHandler(e) {
		this.setState({
			action: {
				...this.state.action,
				accomplished: e.target.value,
			}
		});
	},

	updateOnAccomplishmentChange(e, idx) {
		const { data } = this.state;
		this.updatePreventiveAction({
			...data[idx],
			accomplished: e.target.value,
		});

	},

	renderAccomplishmentOnList(accomplished, id, idx) {
		return (
			this.isPermissionedUser()
				? <VerticalInput
					className="accomplishment-radio"
					fieldDef={{
						name: `new-preventive-action-accomplishment${idx}`,
						type: "radio",
						options: [{ label: "Sim", value: true }, { label: "Não", value: false }],
						valueField: 'value',
						value: accomplished,
						renderDisplay: value => value.label,
						onClick: e => {this.updateOnAccomplishmentChange(e, idx)},
					}}
				/>
				: <span>{accomplished ? 'Sim' : 'Não'}</span>
		);
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
					onClick={() => this.deletePreventiveAction(id)}
				>
					<span className="mdi mdi-delete" />
				</button>
			</div>
		);
	},

	render() {
		if(!this.props.visualization){
			return <div></div>
		}

		const columns = [{
			Header: 'Ação',
			accessor: 'action',
			minWidth: 400,
		},{
			accessor: 'user.name',
			Header: 'Responsável',
			minWidth: 300,
		}, {
			Header: 'Ação realizada?',
			accessor: 'accomplished',
			minWidth: 200,
		}, {
			Header: '',
			accessor: 'tools',
			sortable: false,
			width: 100,
		}];
		return (
			<div className="general-table">
				<div className='table-outter-header'>
					AÇÕES DE PREVENÇÃO
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
							Nenhuma ação de prevenção cadastrada
						</div>
					}
				/>
				<TablePagination
					ref='preventive-actions-pagination'
					defaultPageSize={MED_PAGE_SIZE}
					total={this.state.dataTotal}
					onChangePage={this.pageChange}
					tableName={"preventive-actions-table"}
				/>
			</div>
		)
	}
});
