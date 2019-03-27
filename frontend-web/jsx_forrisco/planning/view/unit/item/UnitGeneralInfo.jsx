import React from "react";
import { Link } from "react-router";
import _ from "underscore";

import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "@/core/util/Messages";
import UnitProcess from "forpdi/jsx_forrisco/planning/view/unit/item/UnitProcess.jsx"
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import UserStore from 'forpdi/jsx/core/store/User.jsx';
import Modal from "@/core/widget/Modal";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
		permissions: React.PropTypes.array.isRequired,
	},

	getInitialState() {
		return {
			unit: null,
			showUpdateMode: false,
			users: [],
			unitToUpdate: null,
			loading: true,
		}
	},

	componentDidMount() {

		UnitStore.on('unitRetrieved', response => {
			this.setState({
				unit: response.data,
				loading: false,
			});
			//Construção da Aba Superior
			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname, this.state.unit.name);
			});
		}, this);

		UnitStore.on('unitUpdated', response => {
			if (response.success) {
				this.context.toastr.addAlertSuccess(
					this.props.isSubunit
						? "A subunidade foi alterada com sucesso."
						: "A unidade foi alterada com sucesso."
					);
				this.setState({
					unit: this.state.unitToUpdate,
					showUpdateMode: false,
				});
			} else {
				this.context.toastr.addAlertError(response.responseJSON.message);
			}
		}, this);

		UnitStore.on('unitDeleted', response => {
			if (response.success === true) {
				const hasMinTabsLength = this.context.tabPanel.state.tabs.length <= 1 ? true : false;
				this.context.tabPanel.removeTabByPath(this.props.location.pathname);
				if (hasMinTabsLength) {
					this.context.router.push(`/forrisco/plan-risk/${this.props.params.planRiskId}/unit/overview`);
				}
				this.context.toastr.addAlertSuccess(
					this.props.isSubunit
						? "A subunidade foi excluída com sucesso."
						: "A unidade foi excluída com sucesso."
				);
			} else {
				this.context.toastr.addAlertError(response.responseJSON.message);
			}
		}, this);

		UserStore.on('retrieve-user', (response) => {
			if (response.data) {
				this.setState({
					users: response.data,
				});
			} else {
				this.context.toastr.addAlertError("Erro ao recuperar os usuários da companhia");
			}
		}, this);
		this.refreshComponent(this.getUnitId(), 1, 500);
	},

	componentWillReceiveProps(newProps) {
		if (this.props.isSubunit) {
			if (this.props.params.subunitId !== newProps.params.subunitId) {
				this.refreshComponent(newProps.params.subunitId, 1, 500);
			}
		} else {
			if (this.props.params.unitId !== newProps.params.unitId) {
				this.refreshComponent(newProps.params.unitId, 1, 500);
			}
		}
	},

	componentWillUnmount() {
		UnitStore.off(null, null, this);
		UserStore.off(null, null, this);
	},

	getUnitId() {
		return this.props.isSubunit
				? this.props.params.subunitId
				: this.props.params.unitId;
	},

	refreshComponent(unitId, page, pageSize) {

		UnitStore.dispatch({
			action: UnitStore.ACTION_RETRIEVE_UNIT,
			data: { unitId },
		});

		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
			data: {
				page: page,
				pageSize: pageSize,
			},
		});
		this.setState({
			showUpdateMode: false,
			unitToUpdate: null,
			loading: true,
		});
	},

	switchUpdateMode() {
		this.setState({
			showUpdateMode: !this.state.showUpdateMode,
			unitToUpdate: this.state.unit,
		})
	},

	selectChangeHandler(e) {
		const idx = e.target.options.selectedIndex;
		this.setState({
			unitToUpdate: {
				...this.state.unitToUpdate,
				user: this.state.users[idx],
			}
		});
	},

	fieldChangeHandler(e) {
		this.setState({
			unitToUpdate: {
				...this.state.unitToUpdate,
				[e.target.name]: e.target.value,
			}
		});
	},

	updateUnit(e) {
		e.preventDefault();
		const unit = this.state.unitToUpdate;

		if (unit.name === '' || unit.user === null || unit.abbreviation === '') {
			this.context.toastr.addAlertError(Messages.get("label.error.form"));
		} else {
			UnitStore.dispatch({
				action: UnitStore.ACTION_UPDATE_UNIT,
				data: {
					unit: this.state.unitToUpdate,
				},
			});
		}
	},

	deleteUnit() {
		const msg = this.props.isSubunit
			? 'Você tem certeza que deseja excluir essa subunidade?'
			: 'Você tem certeza que deseja excluir essa unidade?';
		Modal.confirmCustom(() => {
			Modal.hide();
			UnitStore.dispatch({
				action: UnitStore.ACTION_DELETE_UNIT,
				data: {
					id: this.getUnitId(),
				},
			});
		}, msg, () => Modal.hide());
	},

	renderDropdown() {
		return(
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<Link onClick={this.switchUpdateMode}>
						<span className="mdi mdi-pencil cursorPointer" title={this.props.isSubunit ? Messages.get("label.editSubunit") : Messages.get("label.editUnit")}>
							<span id="menu-levels">{this.props.isSubunit ? Messages.get("label.editSubunit") : Messages.get("label.editUnit")}</span>
						</span>
					</Link>
				</li>
				<li>
					<Link onClick={this.deleteUnit}>
					<span className="mdi mdi-delete cursorPointer" title={this.props.isSubunit ? Messages.get("label.deleteSubunit") : Messages.get("label.deleteUnit")}>
						<span id="menu-levels">{this.props.isSubunit ? Messages.get("label.deleteSubunit") : Messages.get("label.deleteUnit")}</span>
					</span>
					</Link>
				</li>
			</ul>
		)
	},

	renderInfo() {
		const { unit } = this.state;
		return (
			<div>
				<div className="form-group form-group-sm">
					<label className="fpdi-text-label">
						SIGLA
					</label>
					<div>
						<span className="pdi-normal-text">
							{unit.abbreviation}
						</span>
					</div>
				</div>
				<div className="form-group form-group-sm">
					<label className="fpdi-text-label">
						RESPONSÁVEL
					</label>
					<div>
						<span className="pdi-normal-text">
							{unit.user.name}
						</span>
					</div>
				</div>
				<div className="form-group form-group-sm">
					<label className="fpdi-text-label">
						DESCRIÇÃO
					</label>
					<div>
						<span className="pdi-normal-text">
							{unit.description}
						</span>
					</div>
				</div>
			</div>
		);
	},

	renderUpdateMode() {
		const { unit } = this.state;
		return (
			<form onSubmit={this.updateUnit}>
				<div className="form-group form-group-sm">
					{/*<label className="fpdi-text-label" for>
						<span>NOME</span>
					</label>*/}
					<VerticalInput
						fieldDef={{
							name: "name",
							type: "text",
							value: unit.name,
							required: true,
							onChange: this.fieldChangeHandler,
							label: 'NOME'
						}}
					/>
				</div>
				<div className="form-group form-group-sm">
					<VerticalInput
						fieldDef={{
							name: "abbreviation",
							type: "text",
							value: unit.abbreviation,
							required: true,
							onChange: this.fieldChangeHandler,
							label: 'SIGLA'
						}}
					/>
				</div>
				<div className="form-group form-group-sm">
					<VerticalInput
						fieldDef={{
							name: "unit-user",
							type: "select",
							options: _.map(this.state.users, user => user.name),
							value: unit.user.name,
							required: true,
							renderDisplay: value => value,
							onChange: this.selectChangeHandler,
							label: 'RESPONSÁVEL'
						}}
					/>
				</div>
				<div className="form-group form-group-sm">
					<VerticalInput
						fieldDef={{
							name: "description",
							type: "textarea",
							rows: 4,
							value: unit.description,
							onChange: this.fieldChangeHandler,
							label: 'DESCRIÇÃO'
						}}
					/>
				</div>
				<div className="form-group">
					<button type="submit" className="btn btn-sm btn-success">{Messages.get("label.submitLabel")}</button>
					<button className="btn btn-sm btn-default" onClick={this.switchUpdateMode}>{Messages.get("label.cancel")}</button>
				</div>
			</form>
		);
	},

	render() {
		if (this.state.loading === true) {
			return <LoadingGauge/>;
		}

		const { unit } = this.state;

		return (
			<div>
				<div className="fpdi-card fpdi-card-full floatLeft">
					<h1>
						{unit.name}
						{
							(this.context.roles.MANAGER ||
								_.contains(this.context.permissions, PermissionsTypes.FORRISCO_MANAGE_UNIT_PERMISSION) ||
								_.contains(this.context.permissions, PermissionsTypes.FORRISCO_EDIT_UNIT_PERMISSION))
							&&
							<span className="dropdown">
								<a className="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="true"
								title={Messages.get("label.actions")}>

									<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
									<span className="mdi mdi-chevron-down" />

								</a>
								{this.renderDropdown()}
							</span>
						}
					</h1>

					{
						this.state.showUpdateMode
						? this.renderUpdateMode()
						: this.renderInfo()
					}

					{!this.state.showUpdateMode ?
					<UnitProcess
						planRiskId={this.props.params.planRiskId}
						unitId={unit.id}
					/>
						:""}
				</div>
			</div>
		)
	}
})
