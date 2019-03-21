import React from "react";
import _ from "underscore";

import permissionTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import permissionTypesByApp from "forpdi/jsx/planning/enum/PermissionTypesByApp.js";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import UserStore from "forpdi/jsx/core/store/User.jsx";

export default React.createClass({
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		roles: React.PropTypes.object.isRequired,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},
	getInitialState() {
		return {
			forpdiPermissions: [],
			forriscoPermissions: [],
			permissionsToUpdate: [],
			selectedForpdiPermissions: !this.editForpdiUserPermissioned() && this.editForriscoUserPermissioned()
				? false
				: true,
			selectedPermissions: [],
			editingPermission: false,
			detailed: [],
		};
	},

	componentDidMount() {
		var me = this;
		UserStore.on("retrieve-permissions", (model) => {
			me.setState({
				forpdiPermissions: _.filter(model.data, permission =>
					permissionTypesByApp.forpdi.includes(permission.type)
				),
				forriscoPermissions: _.filter(model.data, permission =>
					permissionTypesByApp.forrisco.includes(permission.type)
				),
			});
		}, me);
		UserStore.on("permissions-saved", (store) => {
			me.setState({
				editingPermission: false,
				[this.state.selectedForpdiPermissions ? 'forpdiPermissions' : 'forriscoPermissions']: this.state.permissionsToUpdate,
			});
			this.context.toastr.addAlertSuccess(Messages.get("label.success.editedSuccessfully"));
		}, me);
		UserStore.dispatch({
			action: UserStore.ACTION_LIST_PERMISSIONS,
			data: {
				userId: this.props.user.id,
			}
		});
	},

	componentWillUnmount() {
		UserStore.off(null, null, this);
	},

	editPermissions() {
		let otherParams = {};
		if (!this.state.editingPermission) {
			const permissions = this.getCurrentPermissions();
			otherParams = {
				permissionsToUpdate: _.map(permissions, value => _.assign({}, value)),
				selectedPermissions: _.map(permissions, (value, idx) => value.granted ? idx : undefined)
			};
		}
		this.setState({
			editingPermission: !this.state.editingPermission,
			permissionsToUpdate: _.map(this.getCurrentPermissions(), value => _.assign({}, value)),
			...otherParams,
		});
	},

	switchPermissionsApp(value) {
		this.setState({
			selectedForpdiPermissions: value,
		});
	},

	getCurrentPermissions() {
		return this.state.selectedForpdiPermissions
			? this.state.forpdiPermissions
			: this.state.forriscoPermissions;
	},

	selectPermission(idx) {
		var array = this.state.selectedPermissions || [];
		const permissions = this.state.permissionsToUpdate;
		if (_.contains(array, idx)) {
			permissions[idx].granted = false;
			array.splice(array.indexOf(idx), 1);
		} else {
			permissions[idx].granted = true;
			array.push(idx);
		}
		this.setState({
			selectedPermissions: array
		});
	},

	savePermissions() {
		const list = [];
		_.map(this.state.permissionsToUpdate, permission => {
			list.push({
				user: {
					id: this.props.user.id,
				},
				permission: permission.type,
				revoked: !permission.granted
			});
		});
		UserStore.dispatch({
			action: UserStore.ACTION_SAVE_PERMISSIONS,
			data: {
				list: list,
				total: list.length
			}
		})
	},

	showDescription(description) {
		var items = description.split(",");
		return (
			<div className="user-permission-detail">
				<ul>
					{items.map((item, idx) => {
						return (
							<li key={"item-" + idx}>{item}</li>
						)
					})}
				</ul>
			</div>
		);
	},

	details(idx) {
		var array = this.state.detailed || [];
		if (_.contains(array, idx)) {
			array.splice(array.indexOf(idx), 1);
		} else {
			array.push(idx);
		}
		this.setState({
			detailed: array
		});
	},

	editForpdiUserPermissioned() {
		return this.context.roles.ADMIN ||
			_.contains(this.context.permissions, permissionTypes.MANAGE_USERS_PERMISSION);
	},

	editForriscoUserPermissioned() {
		return this.context.roles.ADMIN ||
			_.contains(this.context.permissions, permissionTypes.FORRISCO_MANAGE_USERS_PERMISSION);
	},

	render() {
		const permissions = this.getCurrentPermissions();
		const editForpdiUserPermissioned = this.editForpdiUserPermissioned();
		const editForriscoUserPermissioned = this.editForriscoUserPermissioned();
		return (
			<div className="col-sm-4">
				<div className="panel panel-default panel-default-user">
					<div className="panel-heading"> {Messages.getEditable("label.userPermissions", "fpdi-nav-label")}
						{
							!this.state.editingPermission && (editForpdiUserPermissioned || editForriscoUserPermissioned) &&
							<span className="floatRight">
								<span
									className="mdi mdi-pencil cursorPointer"
									onClick={this.editPermissions}
									title={Messages.get("label.title.editPermissions")}
								/>
								{
									editForpdiUserPermissioned &&
									<span
										className={`system-switcher ${this.state.selectedForpdiPermissions ? 'system-switcher-selected' : ''}`}
										onClick={() => this.switchPermissionsApp(true)}
									>
										ForPdi
									</span>
								}
								{
									editForriscoUserPermissioned &&
									<span
										className={`system-switcher ${!this.state.selectedForpdiPermissions ? 'system-switcher-selected' : ''}`}
										onClick={() => this.switchPermissionsApp(false)}
									>
										{Messages.get("label.forRiscoLogo")}
									</span>
								}
							</span>
						}
					</div>
					<div className="padding5">
						{
							_.map(permissions, (item, idx) => {
								if (item.granted || this.props.user.accessLevel >= item.accessLevel ||
										(this.state.editingPermission && this.context.accessLevel >= item.accessLevel)) {
									return (
										<div key={"perm-" + idx} className="user-permission-list-item">
											{
												this.state.editingPermission ?
													<input type="checkbox" defaultChecked={item.granted || this.props.user.accessLevel >= item.accessLevel}
														className="user-permission-checkbox" disabled={this.props.user.accessLevel >= item.accessLevel ||
													this.context.accessLevel < item.accessLevel} onClick={this.selectPermission.bind(this, idx)}
												/>
												: ""
											}

											{item.permission}
											<span className={"mdi cursorPointer floatRight " + (_.contains(this.state.detailed, idx) ?
												"mdi-chevron-down" : "mdi-chevron-right")} onClick={this.details.bind(this, idx)} />
											{(_.contains(this.state.detailed, idx) ? this.showDescription(item.description) : "")}
										</div>
									);
								}
							})
						}
						{!!this.state.editingPermission ?
							(<div className="form-group user-permission-btn-ctn">
								<button className="btn btn-success user-permission-btn-save" onClick={this.savePermissions}>{Messages.getEditable("label.submitLabel", "fpdi-nav-label")}</button>
								<button className="btn btn-default" onClick={this.editPermissions}>{Messages.getEditable("label.cancel", "fpdi-nav-label")}</button>
							</div>)
							:
							""
						}
					</div>
				</div>
			</div>
		);
	}
});
