import React from "react";
import string from 'string';
import _ from "underscore";

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import UserStore from "forpdi/jsx/core/store/User.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import FileStore from "forpdi/jsx/core/store/File.jsx"
import AccessLevels from "forpdi/jsx/core/store/AccessLevels.json";
import NotificationUser from "forpdi/jsx/core/view/user/NotificationUser.jsx";
import Logo from 'forpdi/img/foto_padrao.png';
import Validation from 'forpdi/jsx/core/util/Validation.jsx';
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import permissionTypesByApp from "forpdi/jsx/planning/enum/PermissionTypesByApp.js";

var Validate = Validation.validate;

var VerticalForm = Form.VerticalForm;

export default React.createClass({


	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {
			loading: !!this.props.params.modelId,
			modelId: this.props.params.modelId,
			model: null,
			fields: this.getFields(),
			user: UserSession.get("user"),
			editUser: false,
			url: null,
			passwordChange: false,
			permissions: [],
			selectedForpdiPermissions: true,
			detailed: [],
			page: 1,
			hideShowMore: false,
			total: 0
		}

	},

	componentDidMount() {
		var me = this;
		UserStore.on("retrieve-user-profile", (model) => {
			me.setState({
				model: model.data
			});
			me.updateLoadingState();
		}, me);


		UserStore.on("update-picture", (model) => {
			this.setState({
				model: model.data,
			});
			//Toastr.remove();
			//Toastr.success("Foto alterada com sucesso.");
			this.context.toastr.addAlertSuccess(Messages.get("notification.photoChangedSuccessfully"));
		}, me);

		this.setState({
			passwordChange: false
		});

		UserStore.on("useredit", (model) => {
			if (model.data) {
				this.setState({
					model: model.data,
				});
				this.context.toastr.addAlertSuccess(Messages.get("notification.dataEditedSuccessfully"));
				this.setState({
					editUser: false,
				});

				// Requisição descenessária
				// UserStore.dispatch({
				// 	action: UserStore.ACTION_USER_PROFILE,
				// 	data: this.state.modelId
				// });

			} else {
				var errorMsg = JSON.parse(model.responseText)
				this.context.toastr.addAlertError(errorMsg.message);
			}
		}, me);

		UserStore.on("retrieve-permissions", (model) => {
			me.setState({
				permissions: model.data
			});
		}, me);

		if (EnvInfo.company != null) {
			UserStore.dispatch({
				action: UserStore.ACTION_LIST_PERMISSIONS,
				data: {
					userId: this.state.modelId
				}
			});
			UserSession.dispatch({
            	action: UserSession.ACTION_LIST_NOTIFICATIONS,
            	data: {
        			limit: 10
        		}
        	});
		}

		UserSession.on("retrieve-notifications", (model) => {
            if (model.data.length >= model.total) {
				me.setState({
					hideShowMore: true
				});
			}
			me.setState({
				total: model.data.length
			});
        }, me);

		UserSession.on("retrieve-showMoreNotifications", (model) => {
			if ((this.state.total + model.data.length) >= model.total) {
				me.setState({
					hideShowMore: true
				});
			}
			me.setState({
				total: model.data.length + this.state.total
			});
        }, me);

	    UserStore.on("notifications-settings-updated", (model) => {
	    	this.context.toastr.addAlertSuccess(Messages.get("notification.settings.savedSuccessfully"));
	    },me);
	    this.refreshComponent(this.state.modelId);
	},

	refreshComponent(modelId) {
		UserStore.dispatch({
			action: UserStore.ACTION_USER_PROFILE,
			data: modelId
		});

	},

	componentWillUnmount() {
		UserStore.off(null, null, this);
		UserSession.off(null, null, this);
	},

	getFields(model) {
		return [{
			name: "name",
			type: "text",
			placeholder: "",
			label: Messages.getEditable("label.name","fpdi-nav-label"),
			maxLength:255,
			value: model? model.name:null,
			required: true
		},
		{
			name: "email",
			type: "email",
			placeholder: "",
			label: Messages.getEditable("label.email","fpdi-nav-label"),
			maxLength:255,
			value:  model? model.email:null,
			required: true
		},

		{
			name: "cpf",
			type: "cpf",
			placeholder: "",
			label: Messages.getEditable("label.cpf","fpdi-nav-label"),
			value:  model? model.cpf:null,
			required: true
		},

		{
			name: "birthdate",
			type: "date",
			required: true,
			placeholder: "",
			label: Messages.getEditable("label.birthdate","fpdi-nav-label"),
			onChange:this.onStartDateChange,
			value:  model? model.birthdate:null,
		},

		{
			name: "cellphone",
			type: "tel",
			placeholder: "",
			label: Messages.getEditable("label.cellphone","fpdi-nav-label"),
			value:  model? model.cellphone:null,
			required: true
		},
		{
			name: "phone",
			type: "tel",
			placeholder: "",
			label: Messages.getEditable("label.phone","fpdi-nav-label"),
			value:  model? model.phone:null,
		},

		{
			name: "department",
			type: "text",
			placeholder: "",
			label: Messages.getEditable("label.department","fpdi-nav-label"),
			maxLength:255,
			value:  model? model.department:null,
		},

		{
			name: "senha",
			type: "changePassword",
			placeholder: Messages.getEditable("label.changePassword","fpdi-nav-label"),
			onClick: this.changePassword
		}]

	},


	onStartDateChange(data){
		var model = this.state.model;
		this.setState({
			model:model,
		});

		if (data != null) {
			this.refs.profileEditUser.refs.birthdate.props.fieldDef.value = data.format('DD/MM/YYYY')
		}

	},

	updateLoadingState() {
		this.setState({
			fields: this.getFields(this.state.model),
			loading: (this.props.params.modelId && !this.state.model)
		});
	},

	uploadFile() {
		var me = this;
		var formatsBlocked = "(exe*)";
		Modal.uploadFile(
			Messages.get("label.sendPicture"),
			(<div>
				<p>
					{Messages.get("label.selectFile")}
				</p>
			</div>),
			FileStore.url+"/upload",
			"image/*",
			formatsBlocked,
			(resp) => {
				me.setState({url: resp.message});
				Modal.hide();
				UserStore.dispatch({
					action: UserStore.ACTION_UPDATE_PICTURE,
					data: {
						user: this.state.model,
						uri: this.state.url.replace("https", "http")
					}
				});
			},
			(resp) => {
				Modal.hide();
				me.setState({error: resp.message});
			},
			"jpg, jpeg, gif, png, svg."
		);
	},

	confirmEdit () {
		this.setState({
			editUser: true,
			passwordChange: false
		});
	},


	onCancel () {
		var deleteFieldsPassword;
		var tamDeleteFieldsPassword;

		deleteFieldsPassword = this.state.fields;
		tamDeleteFieldsPassword = deleteFieldsPassword.length - 1;

		if (deleteFieldsPassword[tamDeleteFieldsPassword].name == "newPasswordTwo") {
				deleteFieldsPassword.pop();
				deleteFieldsPassword.pop();
				deleteFieldsPassword.pop();
				deleteFieldsPassword.push({
					name: "senha",
					type: "changePassword",
					placeholder: Messages.get("label.changePassword"),
					onClick: this.changePassword
				})
		}

		this.setState({
			fields:deleteFieldsPassword
		})

		var msg = Messages.get("label.msgEdit");

		Modal.confirmCancelCustom(this.refreshAccept,msg,this.refreshCancel);

		this.context.router.push("/users/profilerUser/"+(this.state.model.id));
	},

	refreshCancel () {
		Modal.hide();
	},


	refreshAccept () {
		Modal.hide();
		this.context.router.push("/users/profilerUser/"+(this.state.model.id));
		this.setState({
			editUser: false
		});
	},

	changePassword () {
		var fieldsForm;
		var fieldsPassword;

		fieldsPassword = this.state.fields;

		fieldsPassword.pop();

		fieldsPassword.push({
			name: "currentPassword",
			type: "password",
			placeholder: "",
			label: Messages.getEditable("label.currentPassword","fpdi-nav-label"),
			value: "",
			required: true
		})
		fieldsPassword.push({
			name: "newPassword",
			type: "password",
			placeholder: "",
			label: Messages.getEditable("label.newPassword","fpdi-nav-label"),
			value: "",
			required: true
		})

		fieldsPassword.push({
			name: "newPasswordTwo",
			type: "password",
			placeholder: "",
			label: Messages.getEditable("label.newPasswordTwo","fpdi-nav-label"),
			value: "",
			required: true
		})

		this.setState({
			fields: fieldsPassword
		});
	},

	onSubmit(data) {
		data.id = this.state.user.id;
		data.birthdate = this.refs.profileEditUser.refs.birthdate.props.fieldDef.value;

		var errorField = Validate.validationProfileUser(data, this.refs.profileEditUser);

		if (errorField) {
			//Toastr.remove();
			//Toastr.error("Erro ao salvar os dados do formulário.Corrija-os e tente salvar novamente");
			this.context.toastr.addAlertError(Messages.get("label.errorSavingdata"));
		} else {
			var msg = Messages.get("label.msgUpdate");
			Modal.confirmCustom(() => {
				Modal.hide();
				UserStore.dispatch({
					action: UserStore.ACTION_UPDATE,
					data: {
						user:data,
						currentPassword: data.currentPassword != undefined ? data.currentPassword : null,
						newPassword: data.newPassword != undefined ? data.newPassword : null,
						newPasswordTwo: data.newPasswordTwo != undefined ? data.newPasswordTwo : null
					}
				});
			},msg,this.refreshCancel);
			 errorField = false
		}
	},

	showDescription(description){
		var items = description.split(",");
		return(
			<div className="user-permission-detail">
				<ul>
					{items.map((item, idx) => {
						return(
							<li key={"item-"+idx}>{item}</li>
						)
					})}
				</ul>
			</div>
		);
	},

	details(idx){
		var array = this.state.detailed || [];
		if(_.contains(array, idx)){
			array.splice(array.indexOf(idx), 1);
		} else {
			array.push(idx);
		}
		this.setState({
			detailed: array
		});
	},

	showMoreNotifications() {
		var newPage = this.state.page+1;
		UserSession.dispatch({
        	action: UserSession.ACTION_LIST_NOTIFICATIONS,
        	data: {
    			limit: 10,
    			page: newPage
    		}
    	});
    	this.setState({
    		page: newPage
    	})
	},

	setNotificationConfig(){
		var me = this;
		var notificationSettingOption;
		var update = false;

		if (document.getElementById('notificationSetting1').checked && this.state.model.notificationSettings != 1) {
		  notificationSettingOption = document.getElementById('notificationSetting1').value;
		  update = true;
		}else if (document.getElementById('notificationSetting2').checked && this.state.model.notificationSettings != 2) {
		  notificationSettingOption = document.getElementById('notificationSetting2').value;
		  update = true;
		}else if (document.getElementById('notificationSetting3').checked && this.state.model.notificationSettings != 3) {
		  notificationSettingOption = document.getElementById('notificationSetting3').value;
		  update = true;
		}
		if(update) {
			UserStore.dispatch({
				action: UserStore.ACTION_UPDATE_NOTIFICATION_SETTINGS,
				data: {
					id: me.props.params.modelId,
					notificationSetting: notificationSettingOption
				}
			});
			var model = this.state.model;
			model.notificationSettings = notificationSettingOption;

			this.setState({
				model: model
			})
		}

	},

	getNotificationConfig(){
		var me = this;
		var notificationSettingOption;
		if (me.state.model.notificationSettings == 1) {
		  notificationSettingOption = document.getElementById('notificationSetting1');
		}else if (me.state.model.notificationSettings == 2) {
		  notificationSettingOption = document.getElementById('notificationSetting2');
		}else if (me.state.model.notificationSettings == 3) {
		  notificationSettingOption = document.getElementById('notificationSetting3');
		}

		notificationSettingOption.checked = true;
	},

	getCurrentPermissions() {
		if (this.state.selectedForpdiPermissions) {
			return _.filter(this.state.permissions, permission =>
				permissionTypesByApp.forpdi.includes(permission.type)
			);
		}
		return _.filter(this.state.permissions, permission =>
			permissionTypesByApp.forrisco.includes(permission.type)
		);
	},

	render() {

		if (this.state.loading) {
			return <LoadingGauge />;
		}

		return (
			<div className="fpdi-profile-user padding40">
			<h1 id = "title-profile-user">{Messages.getEditable("label.myProfile","fpdi-nav-label")}</h1>

				<div className="row">

					<div className="col-sm-3">
						{this.state.editUser == false ?

 								<div className="panel panel-default panel-default-user">
									<div className="panel-heading">{Messages.getEditable("label.userData","fpdi-nav-label")} <span className="mdi mdi-pencil cursorPointer floatRight" title={Messages.get("label.editProfile")} onClick={this.confirmEdit}></span> </div>
										<div className="panel-body">

										<div className="fpdi-container-profile">
											<img className="fpdi-image-profile" src={this.state.model.picture == null ?(Logo):this.state.model.picture} />
										</div>
										<div className="fpdi-container-profile-icon">
											<span className="mdi mdi-camera mdi-camera-attributes cursorPointer" onClick={this.uploadFile} title={Messages.get("label.title.betterImagePreview")}></span>
											</div>

											<div>
												<form>
													<div className="form-group form-profile">
														<label className = "fpdi-text-label"> {Messages.getEditable("label.name","fpdi-nav-label")} </label>
															<p id = "p-profileUser" title = {this.state.model.name}> {(this.state.model.name.length>25)?(string(this.state.model.name).trim().substr(0,25).concat("...").toString()):(this.state.model.name)} </p>

														<label className = "fpdi-text-label"> {Messages.getEditable("label.email","fpdi-nav-label")} </label>
															<p id = "p-profileUser"> {this.state.model.email} </p>

														<label className = "fpdi-text-label"> {Messages.getEditable("label.cpf","fpdi-nav-label")} </label>
															<p id = "p-profileUser"> {this.state.model.cpf} </p>

														<label className = "fpdi-text-label"> {Messages.getEditable("label.birthdate","fpdi-nav-label")} </label>
															<p id = "p-profileUser"> {this.state.model.birthdate == null ? this.state.model.birthdate : this.state.model.birthdate.split(" ")[0]} </p>

														<label className = "fpdi-text-label">{Messages.getEditable("label.cellphone","fpdi-nav-label")}</label>
															<p id = "p-profileUser"> {this.state.model.cellphone} </p>

														<label className = "fpdi-text-label">{Messages.getEditable("label.phone","fpdi-nav-label")}</label>
															<p id = "p-profileUser"> {this.state.model.phone} </p>

														<label className = "fpdi-text-label"> {Messages.getEditable("label.department","fpdi-nav-label")}</label>
															<p id = "p-profileUser" title = {this.state.model.department}> {this.state.model.department == null ? this.state.model.department: (this.state.model.department.length>25)?(string(this.state.model.department).trim().substr(0,25).concat("...").toString()):(this.state.model.department)}</p>
													</div>
												</form>
											</div>

										</div>
						 			</div>

						 		:

						 		<div className="panel panel-default panel-default-user">
									<div className="panel-heading">{Messages.getEditable("label.userData","fpdi-nav-label")} </div>
										<div className="panel-body">
											<div className="fpdi-container-profile">
											<img className="fpdi-image-profile" src={this.state.model.picture == null ?(Logo):this.state.model.picture}/>
										</div>
										<div className="fpdi-container-profile-icon">
												<span className="mdi mdi-camera mdi-camera-attributes cursorPointer" onClick={this.uploadFile} title={Messages.get("label.title.changeProfilePhoto")}></span>
											</div>


											<div className="panel-body">
						 							<VerticalForm
			    										ref="profileEditUser"
														onSubmit={this.onSubmit}
														fields={this.state.fields}
														store={UserStore}
														submitLabel={Messages.get("label.submitLabel")}
														onCancel = {this.onCancel}
														/>

										</div>

										</div>


						 		</div> }

					</div>

					<div className="col-sm-4">
						<div className="panel panel-default panel-default-user">
							<div className="panel-heading">
								{Messages.getEditable("label.userPermissions","fpdi-nav-label")}
								<span className="floatRight">
									<span
										className={`system-switcher ${this.state.selectedForpdiPermissions ? 'system-switcher-selected' : ''}`}
										onClick={() => this.setState({ selectedForpdiPermissions: true })}
									>
										ForPdi
									</span>
									<span
										className={`system-switcher ${!this.state.selectedForpdiPermissions ? 'system-switcher-selected' : ''}`}
										onClick={() => this.setState({ selectedForpdiPermissions: false })}
									>
										{Messages.get("label.forRiscoLogo")}
									</span>
								</span>
							</div>
							<div className="padding5">
								{this.getCurrentPermissions().map((item, idx) => {
									if(item.granted || this.context.accessLevel >= item.accessLevel){
										return(
											<div key={"perm-"+idx} className="user-permission-list-item">
												{item.permission}
												<span className={"mdi cursorPointer floatRight "+(_.contains(this.state.detailed, idx) ?
												"mdi-chevron-down" : "mdi-chevron-right")} onClick={this.details.bind(this, idx)}/>
												{(_.contains(this.state.detailed, idx) ? this.showDescription(item.description) : "")}
											</div>
										);
									}
								})}
							</div>
						</div>
					</div>

					<div className="col-sm-5">
						{/*<NotificationUser/>*/}
						<div className="panel panel-default panel-default-user">
							<div className="panel-heading"> {Messages.getEditable("label.notification","fpdi-nav-label")}
								{this.context.accessLevel != AccessLevels.enum.SYSADMIN ?
									<div className="dropdown floatRight">
										<a
											id="notifications-settings-menu"
											className="dropdown-toggle"
											data-toggle="dropdown"
											aria-haspopup="true"
											aria-expanded="true"
											title={Messages.get("label.settings")}
											>
												<span className="mdi mdi-settings cursorPointer floatRight"/>
										</a>
										<div className="dropdown-menu dropdown-menu-right width250" aria-labelledby="notifications-settings-menu">
											<p>{Messages.getEditable("notification.receiving","fpdi-nav-label")}</p>
											<div className="radio" id="notificationSettingRadio" onClick={this.setNotificationConfig}>
												<div key={'field-opt-1'}><label><input
													type="radio"
													name="notificationSetting"
													id="notificationSetting1"
													defaultChecked={this.state.model.notificationSettings == 1 ? true : false}
													value="1"
													/>{Messages.getEditable("notification.standard","fpdi-nav-label")}
													<span className="fpdi-required">&nbsp;</span>
												</label></div>
												<div key={'field-opt-2'}><label><input
													type="radio"
													name="notificationSetting"
													id="notificationSetting2"
													defaultChecked={this.state.model.notificationSettings == 2 ? true : false}
													value="2"
													/>{Messages.getEditable("notification.emailType","fpdi-nav-label")}
												</label></div>
												<div key={'field-opt-3'}><label><input
													type="radio"
													name="notificationSetting"
													id="notificationSetting3"
													defaultChecked={this.state.model.notificationSettings == 3 ? true : false}
													value="3"
													/>{Messages.getEditable("notification.noEmailType","fpdi-nav-label")}
												</label></div>
											</div>
											<p className="notificationSettingsDropdown"><span className="fpdi-required"></span> {Messages.getEditable("notification.typeInformation","fpdi-nav-label")} </p>
										</div>
									</div>
								:""}
							</div>
								{/*<div className="panel-body">Em construção</div>*/}
								<div>
									<NotificationUser ref={"userNotification"}/>
									{this.state.hideShowMore == false ?
										<div className="textAlignCenter marginBottom10">
	                    					<a onClick={this.showMoreNotifications}>{Messages.getEditable("label.viewMore","fpdi-nav-label")}</a>
	                					</div>
	                				: ""}
								</div>
						</div>
					</div>
				</div>
			</div>
		);
	}

});
