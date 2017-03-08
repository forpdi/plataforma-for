import React from "react";
import {Link} from "react-router";
import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import UserStore from "forpdi/jsx/core/store/User.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import UserEdit from "forpdi/jsx/core/view/user/UserEdit.jsx";
import FileStore from "forpdi/jsx/core/store/File.jsx"
import _ from "underscore";
import AccessLevels from "forpdi/jsx/core/store/AccessLevels.json";
import NotificationUser from "forpdi/jsx/core/view/user/NotificationUser.jsx";
import string from 'string';
import Logo from 'forpdi/img/foto_padrao.png';
import Validation from 'forpdi/jsx/core/util/Validation.jsx';

//import Toastr from 'toastr';

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
			detailed: [],
			page: 1,
			hideShowMore: false,
			total: 0
		}

	},

	componentDidMount() {
		var me = this;
		UserStore.on("retrieve-user-profile", (model) => {
			if (me.isMounted()) {
				me.setState({
					model: model.data
				});
			}
			me.updateLoadingState();	
		}, me);

		
		UserStore.on("update-picture", (model) => {
			if (this.isMounted()) {
				this.setState({
					model: model.data,
				});
			}
			
			//Toastr.remove();
			//Toastr.success("Foto alterada com sucesso.");
			this.context.toastr.addAlertSuccess("Foto alterada com sucesso.");
			
		}, me);


		UserStore.dispatch({
			action: UserStore.ACTION_USER_PROFILE,
			data: this.state.modelId
			
		});

		if (this.isMounted()) {
			this.setState({
				passwordChange: false
			})
		}

		UserStore.on("useredit", (model) => {
			if (model.data) {
				if (this.isMounted()) {
					this.setState({
						model: model.data,
					});
				}
				//Toastr.remove();
				//Toastr.success("Dados editados com sucesso.");
				this.context.toastr.addAlertSuccess("Dados editados com sucesso.");

				me.context.router.push("/users/profilerUser/"+(model.data.id));
				
				if (this.isMounted()) {
					this.setState({
						editUser: false
				
					});
				}

				UserStore.dispatch({
					action: UserStore.ACTION_USER_PROFILE,
					data: this.state.modelId
			
				});

			} else {
				var errorMsg = JSON.parse(model.responseText)
				this.context.toastr.addAlertError(errorMsg.message);
			}
			
		}, me);

		UserStore.on("retrieve-permissions", (model) => {			
			if(me.isMounted()){
				me.setState({
					permissions: model.data
				});
			}
		}, me);

	
		if (EnvInfo.company != null) {
			this.getPermissionsList();
			UserSession.dispatch({
            	action: UserSession.ACTION_LIST_NOTIFICATIONS,
            	data: {
        			limit: 10
        		}
        	});		
		}

		UserSession.on("retrieve-notifications", (model) => {
            if (model.data.length >= model.total) {
            	if (me.isMounted()) {
					me.setState({
						hideShowMore: true
					});
				}
			}
			if (me.isMounted()) {
				me.setState({
					total: model.data.length
				});
			}
        }, me);

		UserSession.on("retrieve-showMoreNotifications", (model) => {
			if ((this.state.total + model.data.length) >= model.total) {
				if (me.isMounted()) {
					me.setState({
						hideShowMore: true
					});
				}
			}
			if (me.isMounted()) {
				me.setState({
					total: model.data.length + this.state.total
				});
			}
        }, me);

	    UserStore.on("notifications-settings-updated", (model) => {
	    	this.context.toastr.addAlertSuccess("Configurações de notificação salvas com sucesso.");
	    },me);


	},


	componentWillUnmount() {
		UserStore.off(null, null, this);
	},

	getPermissionsList(){
		UserStore.dispatch({
			action: UserStore.ACTION_LIST_PERMISSIONS,
			data: {
				userId: this.state.modelId
			}
		})
	},

	getFields(model) {
		return [{
			name: "name",
			type: "text",
			placeholder: "",
			label: "Nome",
			maxLength:255,
			value: model? model.name:null,
			required: true
		},
		{
			name: "email",
			type: "email",
			placeholder: "",
			label: "E-mail",
			maxLength:255,
			value:  model? model.email:null,
			required: true
		},

		{
			name: "cpf",
			type: "cpf",
			placeholder: "",
			label: "CPF",
			value:  model? model.cpf:null,
			required: true
		}, 
		
		{
			name: "birthdate",
			type: "date",
			required: true,
			placeholder: "",
			label: "Data de Nascimento",
			onChange:this.onStartDateChange,
			value:  model? model.birthdate:null,
			required: true
		},

		{
			name: "cellphone",
			type: "tel",
			placeholder: "",
			label: "Celular",
			value:  model? model.cellphone:null,
			required: true
		},
		{
			name: "phone",
			type: "tel",
			placeholder: "",
			label: "Telefone",
			value:  model? model.phone:null,
		},

		{
			name: "department",
			type: "text",
			placeholder: "",
			label: "Departamento",
			maxLength:255,
			value:  model? model.department:null,
		},

		{
			name: "senha",
			type: "changePassword",
			placeholder: "Alterar Senha",
			onClick: this.changePassword
		}]

	},


	onStartDateChange(data){
		var model = this.state.model;
		if (this.isMounted()) {
			this.setState({
				model:model,
			});
		}
	
		if (data != null) {
			this.refs.profileEditUser.refs.birthdate.props.fieldDef.value = data.format('DD/MM/YYYY')
		} 
		
	},

	updateLoadingState() {
		if (this.isMounted()) {
			this.setState({
				fields: this.getFields(this.state.model),
				loading: (this.props.params.modelId && !this.state.model)
			});
		}
	},

	uploadFile() {
		var me = this;
		var formatsBlocked = "(exe*)";
		Modal.uploadFile(
			"Enviar Foto",
			(<div>
				<p>
					Selecione um arquivo.
				</p>
			</div>),
			FileStore.url+"/upload",
			"image/*",
			formatsBlocked,
			(resp) => {
				if (me.isMounted()) {
					me.setState({url: resp.message});
				}
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
				if (me.isMounted()) {
					me.setState({error: resp.message});
				}
				
			},
			"jpg, jpeg, gif, png, svg."
		);
	},


	confirmEdit () {
		if (this.isMounted()) {
			this.setState({
				editUser: true,
				passwordChange: false
			});
		}

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
					placeholder: "Alterar Senha",
					onClick: this.changePassword
				})
		}
			
		this.setState({
			fields:deleteFieldsPassword
		})

		var msg = "Você não salvou as modificações. Deseja continuar?";

		Modal.confirmCancelCustom(this.refreshAccept,msg,this.refreshCancel);

		this.context.router.push("/users/profilerUser/"+(this.state.model.id));
		/*this.setState({
			editUser: false
			
		});*/
	},

	refreshCancel () {


		Modal.hide();
		//this.context.router.push("/users/profilerUser/"+(this.state.model.id));
		//this.setState({
		//	editUser: false
		//	
		//});
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
			label: "Senha Atual",
			value: "",
			required: true
		})
		fieldsPassword.push({
			name: "newPassword",
			type: "password",
			placeholder: "",
			label: "Nova Senha",
			value: "",
			required: true		
		})

		fieldsPassword.push({
			name: "newPasswordTwo",
			type: "password",
			placeholder: "",
			label: "Confirme a nova senha",
			value: "",
			required: true
		})

		this.setState({
			fields: fieldsPassword
		})
		

	},


	onSubmit(data) {
		data.id = this.state.user.id;
		data.birthdate = this.refs.profileEditUser.refs.birthdate.props.fieldDef.value;

		var errorField = Validate.validationProfileUser(data, this.refs.profileEditUser);

		if (errorField) {
			//Toastr.remove();
			//Toastr.error("Erro ao salvar os dados do formulário.Corrija-os e tente salvar novamente");
			this.context.toastr.addAlertError("Erro ao salvar os dados do formulário. Corrija-os e tente salvar novamente");

		} else {
			
			var msg = "Os dados serão atualizados. Deseja continuar essa ação?";
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

	render() {		

		if (this.state.loading) {
			return <LoadingGauge />;
		}

		return (
			<div className="fpdi-profile-user padding40">
			<h1 id = "title-profile-user">Meu perfil</h1> 

				<div className="row">
				
					<div className="col-sm-3">
						{this.state.editUser == false ?

 								<div className="panel panel-default panel-default-user">
									<div className="panel-heading">Dados do usuário <span className="mdi mdi-pencil cursorPointer floatRight" title="Editar perfil" onClick={this.confirmEdit}></span> </div>
										<div className="panel-body">
										
										<div className="fpdi-container-profile">
											<img className="fpdi-image-profile" src={this.state.model.picture == null ?(Logo):this.state.model.picture} />
										</div>
										<div className="fpdi-container-profile-icon">
											<span className="mdi mdi-camera mdi-camera-attributes cursorPointer" onClick={this.uploadFile} title="Para melhor visualização da imagem insira uma foto com a mesma largura e altura"></span>
											</div>
											
											<div>
												<form>
													<div className="form-group form-profile">
														<label className = "fpdi-text-label"> NOME </label>
															<p id = "p-profileUser" title = {this.state.model.name}> {(this.state.model.name.length>25)?(string(this.state.model.name).trim().substr(0,25).concat("...").toString()):(this.state.model.name)} </p>

														<label className = "fpdi-text-label"> E-MAIL </label>
															<p id = "p-profileUser"> {this.state.model.email} </p>
														
														<label className = "fpdi-text-label"> CPF </label>
															<p id = "p-profileUser"> {this.state.model.cpf} </p>
														
														<label className = "fpdi-text-label"> DATA DE NASCIMENTO </label>
															<p id = "p-profileUser"> {this.state.model.birthdate == null ? this.state.model.birthdate : this.state.model.birthdate.split(" ")[0]} </p>
														
														<label className = "fpdi-text-label">CELULAR</label>
															<p id = "p-profileUser"> {this.state.model.cellphone} </p>
														
														<label className = "fpdi-text-label">TELEFONE</label>
															<p id = "p-profileUser"> {this.state.model.phone} </p>
																
														<label className = "fpdi-text-label"> DEPARTAMENTO</label>
															<p id = "p-profileUser" title = {this.state.model.department}> {this.state.model.department == null ? this.state.model.department: (this.state.model.department.length>25)?(string(this.state.model.department).trim().substr(0,25).concat("...").toString()):(this.state.model.department)}</p>
													</div>
												</form>
											</div>
											
										</div>
						 			</div> 

						 		: 

						 		<div className="panel panel-default panel-default-user">
									<div className="panel-heading">Dados do usuário </div>
										<div className="panel-body">
											<div className="fpdi-container-profile">
											<img className="fpdi-image-profile" src={this.state.model.picture == null ?(Logo):this.state.model.picture}/>
										</div>
										<div className="fpdi-container-profile-icon">
												<span className="mdi mdi-camera mdi-camera-attributes cursorPointer" onClick={this.uploadFile} title="Alterar foto de perfil"></span>
											</div>
										
											
											<div className="panel-body">
						 							<VerticalForm
			    										ref="profileEditUser"
														onSubmit={this.onSubmit}
														fields={this.state.fields}
														store={UserStore}
														submitLabel="Salvar"
														onCancel = {this.onCancel}
														/>
												
										</div>
											
										</div>
						 					
						 		
						 		</div> }	
							
					</div>

					<div className="col-sm-3">
						<div className="panel panel-default panel-default-user">
							<div className="panel-heading"> 
								Permissões do usuário
							</div>					
							{this.state.permissions.map((item, idx) => {
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
					
					<div className="col-sm-6">
						{/*<NotificationUser/>*/}
						<div className="panel panel-default panel-default-user">
							<div className="panel-heading"> Notificações 
								{this.context.accessLevel != AccessLevels.enum.SYSADMIN ? 
									<div className="dropdown floatRight">
										<a
											id="notifications-settings-menu"
											className="dropdown-toggle"
											data-toggle="dropdown"
											aria-haspopup="true"
											aria-expanded="true"
											title="Configurações"
											>
												<span className="mdi mdi-settings cursorPointer floatRight"/>
										</a>
										<div className="dropdown-menu dropdown-menu-right width250" aria-labelledby="notifications-settings-menu">
											<p>Recebimento de notificações:</p>
											<div className="radio" id="notificationSettingRadio" onClick={this.setNotificationConfig}>
												<div key={'field-opt-1'}><label><input
													type="radio"
													name="notificationSetting"
													id="notificationSetting1"
													defaultChecked={this.state.model.notificationSettings == 1 ? true : false}
													value="1"
													/>Manter recebimento padrão
													<span className="fpdi-required">&nbsp;</span>
												</label></div>
												<div key={'field-opt-2'}><label><input
													type="radio"
													name="notificationSetting"
													id="notificationSetting2"
													defaultChecked={this.state.model.notificationSettings == 2 ? true : false}
													value="2"
													/>Receber todas por e-mail
												</label></div>
												<div key={'field-opt-3'}><label><input
													type="radio"
													name="notificationSetting"
													id="notificationSetting3"
													defaultChecked={this.state.model.notificationSettings == 3 ? true : false}
													value="3"
													/>Não receber e-mail
												</label></div>
											</div>
											<p className="notificationSettingsDropdown"><span className="fpdi-required"></span> No recebimento padrão, as notificações de planos criados, meta concluída, vencimento de planos
											e conclusão do plano de ações do indicador são exibidas apenas no sistema.</p>
										</div>
									</div>
									:""}
							</div>
								{/*<div className="panel-body">Em construção</div>*/}
								<div>
									<NotificationUser ref={"userNotification"}/>
									{this.state.hideShowMore == false ?
										<div className="textAlignCenter marginBottom10">
	                    					<a onClick={this.showMoreNotifications}>ver mais...</a>
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