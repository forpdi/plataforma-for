
import React from "react";
import {Link} from 'react-router';
import UserStore from "forpdi/jsx/core/store/User.jsx";
import string from 'string';
import EditableDataView from "forpdi/jsx/core/widget/form/EditableDataView.jsx";
import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import AccessLevels from "forpdi/jsx/core/store/AccessLevels.json";
import Logo from 'forpdi/img/foto_padrao.png';
import FileStore from "forpdi/jsx/core/store/File.jsx"
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import _ from "underscore";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';
import NotificationMessageUser from "forpdi/jsx/core/view/user/NotificationMessageUser.jsx";


//import Toastr from 'toastr';

var Validate = Validation.validate;

var VerticalForm = Form.VerticalForm;

export default React.createClass({
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		router: React.PropTypes.object,
		roles: React.PropTypes.object.isRequired,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},
	getInitialState() {
		return {
			dataUser:null,
			model: null,
			isEditUser: false,
			fields: this.getFields(),
			infoPlanGoals: false,
			infoDocument: false,
			infoGoal: false,
			user: UserSession.get("user"),
			permissions: [],
			detailed: [],
			selectedPermissions: [],
			editingPermission: false,
			passwordChange: false,
			loading: !!this.props.params.modelId,
			modelId: this.props.params.modelId,
			fieldsMessage: this.getFieldsMessage(),
			page: 1,
			totalMessages: 0,
			hideShowMoreMessages:false
			//url: this.props.location.pathname,
		};
	},

	

	componentDidMount() {
		var me = this;
		// if (!me.context.roles.ADMIN) {
		// 	me.context.router.replace("/home");
		// 	return;
		// }

		UserStore.on("retrieve-permissions", (model) => {			
			if(me.isMounted()){
				model.data.map((item, idx) => {
					if(item.granted)
						this.state.selectedPermissions.push(idx);
				});		
				if (me.isMounted()) {		
					me.setState({
						permissions: model.data
					});
				}
			}
		}, me);

		UserStore.on("retrieve", (model) => {
			if (me.isMounted()) {
				me.setState({
					model: model.attributes,
				});
			}
			this.getPermissionsList();
			
			UserSession.dispatch({
			action: UserSession.ACTION_LIST_MESSAGES,
				data: {
					userId:this.state.model.id,
					limit: 3
				}
			});

			me.updateLoadingState();
		}, me);

		UserStore.on("update-picture-user", (model) => {

			UserStore.dispatch({
				action: UserStore.ACTION_RETRIEVE,
				data: this.props.params.modelId
			});
			
			//Toastr.remove();
			//Toastr.success("Foto alterada com sucesso.");
			this.context.toastr.addAlertSuccess("Foto alterada com sucesso.");			
		}, me);

		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE,
			data: this.props.params.modelId
		});

		UserSession.on("retrieve-showMoreMessages",(model) => {
			//console.log(model);

			if ((this.state.totalMessages + model.data.length) >= model.total) {
				if (me.isMounted()) {
					me.setState({
						hideShowMoreMessages: true
					});
				}
			}
			if (me.isMounted()) {
				me.setState({
					totalMessages: model.data.length + this.state.totalMessages
				});
			}
		},me);

		
		UserSession.on("retrieve-messages",(model) => {

			if (model.data.length >= model.total || model.data.length <= 0) {
				if (me.isMounted()) {
					me.setState({
						hideShowMoreMessages: true
					});
				}
			}
			if (me.isMounted()) {
				me.setState({
					totalMessages: model.data.length
				});
			}
		},me);


		UserStore.on("editUser", (store) => {						
			if (store.data) {
				if (this.isMounted()) {
					this.setState({
						model: store.data,
					});
				}

				this.context.toastr.addAlertSuccess("Dados editados com sucesso.");
				me.updateLoadingState();
				
				if (this.isMounted()) {
					this.setState({
						isEditUser: false
					});
				}
				
			} else {
				var errorMsg = JSON.parse(store.responseText)
				this.context.toastr.addAlertError(errorMsg.message);
			}		
		}, me);


		UserSession.on("sendMessage",(store) => {
			this.context.toastr.addAlertSuccess("Mensagem enviada com sucesso.");
			this.onClear();

			UserSession.dispatch({
			action: UserSession.ACTION_LIST_MESSAGES,
				data: {
					userId:this.state.model.id,
					limit: 3
				}
			});
			
		},me);

		UserStore.on("permissions-saved", (store) => {
			if(me.isMounted()){
				me.setState({
					editingPermission: false
				});
			}
			this.context.toastr.addAlertSuccess("Permissões editadas com sucesso.");
			me.updateLoadingState();

		}, me);
	},

	getPermissionsList(){		
		UserStore.dispatch({
			action: UserStore.ACTION_LIST_PERMISSIONS,
			data: {
				userId: this.state.model.id
			}
		})
	},

	componentWillUnmount() {
		UserStore.off(null, null, this);
	},

	getFields(model) {
	 
		return [{
			name: "name",
			type: "text",
			placeholder: "",
			label: "Nome",
			maxLength:255,
			value: model  ? model.name:null,
			required: true
		},
		{
			name: "email",
			type: "email",
			placeholder: "",
			label: "E-mail",
			maxLength:255,
			value:  model ? model.email:null,
			required: true,
			disabled: this.context.roles.SYSADMIN || (model && UserSession.get("user").id == model.id) ? false : true,
			disableMsg: "Este campo só pode ser editado pelo próprio usuário."
		},

		{
			name: "cpf",
			type: "cpf",
			placeholder: "",
			label: "CPF",
			value:  model ? model.cpf:null,
			required: true,
			disabled: this.context.roles.SYSADMIN || (model && UserSession.get("user").id == model.id) ? false : true,
			disableMsg: "Este campo só pode ser editado pelo próprio usuário."
		}, 
		
		{
			name: "birthdate",
			type: "date",
			required: true,
			placeholder: "",
			label: "Data de Nascimento",
			onChange: this.onStartDateChange,
			value: 	model ? model.birthdate:null,
			required: true
		},

		{
			name: "cellphone",
			type: "tel",
			placeholder: "",
			label: "Celular",
			value:  model ? model.cellphone:null,
			required: true
		},
		{
			name: "phone",
			type: "tel",
			placeholder: "",
			label: "Telefone",
			value: 	model? model.phone:null,
		},

		{
			name: "department",
			type: "text",
			placeholder: "",
			label: "Departamento",
			maxLength:255,
			value:  model ? model.department:null,
		},
		{	
			name: 'accessLevel',
			type: 'select',
			maxLength: 255,
			placeholder: '-- Selecione um Nível de Usuário --',
			label: "Tipo de Usuário",
			required: true,
			value: model ? model.accessLevel :null,
			displayField: 'name',
			valueField: 'accessLevel',
			options: this.context.roles.SYSADMIN ? 
				AccessLevels.list.filter((level) => {
					return level.accessLevel <= this.context.accessLevel
				})
			:	
				AccessLevels.listNoSysAdm.filter((level) => {
					return level.accessLevel <= this.context.accessLevel;
				})
		},
		{
			name: "newPassword",
			type: "password",
			placeholder: "",
			label: "Nova Senha",
			value: "",
			required: false	
		}]

	},


	getFieldsMessage (model) {
		return [{
			name: "assunto",
			type: "subject",
			placeholder: "",
			label: "Assunto",
			maxLength:70,
			value: null,
			required: true
		},
		{
			name: "mensagem",
			type: "message",
			placeholder: "",
			label: "Mensagem",
			maxLength:255,
			value: null,
			required: true
		}]


	},

	onClear() {
		if (this.refs.messageUser) {
			this.refs.messageUser.refs.assunto.refs['field-assunto'].value = '';
			this.refs.messageUser.refs.mensagem.refs['field-mensagem'].value = '';
		} else {
			var fields = this.getFieldsMessage();
			if (this.isMounted()) {
				this.setState({
					fieldsMessage: fields
				});
			}
		}
	},

	showMoreMessages() {
		var newPage = this.state.page+1;
		UserSession.dispatch({
        	action: UserSession.ACTION_LIST_MESSAGES,
        	data: {
        		userId:this.state.model.id,
    			limit: 3,
    			page: newPage
    		}
    	});	
    	if (this.isMounted()) {
	    	this.setState({
	    		page: newPage
	    	})
	    }
	},

	onSubmitMenssage(data) {
		var errorField = Validate.validationSendMenssageUser(data, this.refs.messageUser)

		if (errorField) {
			//Toastr.remove();
			//Toastr.error("Erro ao salvar os dados do formulário.Corrija-os e tente salvar novamente");
			this.context.toastr.addAlertError("Erro ao enviar mensagem ao usuário. Corrija-os e tente enviar novamente");
		} else {
			UserSession.dispatch({
				action: UserSession.ACTION_SEND_MESSAGE,
					data: {
						subject:data.assunto,
						message:data.mensagem, 
						userId:this.state.model.id
					}
				});
		}

	},

	onStartDateChange(data){
		var model = this.state.model;
		if (this.isMounted()) {
			this.setState({
				model:model,
			});
		}
		this.refs.editUser.refs.birthdate.props.fieldDef.value = data.format('DD/MM/YYYY');
	},

	updateLoadingState() {
		if (this.isMounted()) {
			this.setState({
				fields: this.getFields(this.state.model),
				loading: (this.props.params.modelId && !this.state.model)		
			});
		}
	},

	validarCPF(cpf){
		if(cpf.length != 11 || cpf.replace(eval('/'+cpf.charAt(1)+'/g'),'') == '') {
		    return false;
		} else {
	   	    var d;
	   	    var c;
	        for(var n=9; n<11; n++) {
	            for(d=0, c=0; c<n; c++)
	            	d += cpf.charAt(c) * ((n + 1) - c);
	            d = ((10 * d) % 11) % 10;
	            if(cpf.charAt(c) != d)
	            	return false;
	        }
	      return true;
	   }
	},


	onSubmit(data) {
		var me = this;
		data.id = this.state.model.id;
		data.birthdate = this.refs.editUser.refs.birthdate.props.fieldDef.value;
		var msg;
		var errorField = Validate.validationProfileUser(data, this.refs.editUser);
	
		if (errorField) {
			//Toastr.remove();
			//Toastr.error("Erro ao salvar os dados do formulário.Corrija-os e tente salvar novamente");
			this.context.toastr.addAlertError("Erro ao salvar os dados do formulário. Corrija-os e tente salvar novamente");
		} else {
			
			if (me.props.params.modelId) {
				var msg = "Os dados serão atualizados. Deseja continuar essa ação?";
				Modal.confirmCustom(() => {
					Modal.hide();
					UserStore.dispatch({
						action: UserStore.ACTION_UPDATE_USER,
						data: {
							user:data,
							newPassword: data.newPassword == "" ? null : data.newPassword 
						}
					});
				},msg,this.refreshCancel);
			} else {
				var msg = "O usuário será criado. Deseja continuar essa ação?";
				Modal.confirmCustom(() => {
					Modal.hide();
					UserStore.dispatch({
						action: UserStore.ACTION_CREATE,
						data: data
					});
				},msg,this.refreshCancel);


				/*UserStore.dispatch({
					action: UserStore.ACTION_CREATE,
					data: data
				});*/
			}
		}
		
	},


	
	showPlanGoals () {
		if (this.isMounted()) {
			this.setState({
				infoPlanGoals: !this.state.infoPlanGoals		
			});
		}
	},

	showDocumen () {
		if (this.isMounted()) {
			this.setState({
				infoDocument: !this.state.infoDocument	
			});
		}
	},

	showGoal () {
		if (this.isMounted()) {
			this.setState({
				infoGoal: !this.state.infoGoal	
			});
		}
	},

	refreshCancel () {
		Modal.hide();
		//this.setState({
		//	editUser: false
		//	
		//});
	},


	refreshAccept () {
		Modal.hide();
		if (this.isMounted()) {
			this.setState({
				isEditUser: false	
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
		
		if (this.isMounted()) {
			this.setState({
				fields:deleteFieldsPassword
			})
		}

		var msg = "Você não salvou as modificações. Deseja continuar?";

		Modal.confirmCancelCustom(this.refreshAccept,msg,this.refreshCancel);

	},

	confirmEdit () {
		if (this.isMounted()) {
			this.setState({
				isEditUser: true
			});
		}
	},

	uploadFile() {
		var formatsBlocked = "(exe*)";
		var me = this;
		Modal.uploadFile(
			"Enviar Documento",
			(<div>
				<p>
					Selecione um documento.
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
					action: UserStore.ACTION_UPDATE_PICTURE_EDIT_USER,
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
		if (this.isMounted()) {
			this.setState({
				detailed: array
			});
		}
	},

	editPermissions(){
		if (this.isMounted()) {
			this.setState({
				editingPermission: !this.state.editingPermission
			});
		}
	},

	selectPermission(idx){
		var array = this.state.selectedPermissions || [];
		if(_.contains(array, idx)){
			this.state.permissions[idx].granted = false;
			array.splice(array.indexOf(idx), 1);
		} else {
			this.state.permissions[idx].granted = true;
			array.push(idx);
		}	
		if (this.isMounted()) {	
			this.setState({
				selectedPermissions: array
			});		
		}

	},

	savePermissions(){
		var list = [];
		this.state.permissions.map((permission) => {
			list.push({
				user: {
					id: this.state.model.id
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

	render() {

		if (this.state.loading) {
			return <LoadingGauge />;
		}
		var birth;
		//console.log(this.props.location.pathname);
		if (this.state.model.birthdate) {
			try {
				birth=this.state.model.birthdate.split(" ")[0];
			}
			catch(err) {
				birth=this.state.model.birthdate.getDate() + "/" + this.state.model.birthdate.getMonth+1
				+ "/" + this.state.model.birthdate.getFullYear();
			}
		}
		
		return (
  				<div className="fpdi-profile-user padding40">
				 	<h1  id = "title-profile-user"> Editar Usuário </h1>  
						<div className="marginBottom20">
							<span>
								<Link className="fpdi-breadcrumb fpdi-breadcrumbDivisor"
									to={"/users"}
									title={"Usuários"}>Usuários</Link>
								<span className="mdi mdi-chevron-right fpdi-breadcrumbDivisor"></span>
							</span>
							
							<span className="fpdi-breadcrumb fpdi-selectedOnBreadcrumb">
								{this.state.model.name}
							</span>
						</div>
						<div className="row">
						
							<div className="col-sm-3">
								{this.state.isEditUser == false ?

   	 								<div className="panel panel-default panel-default-user">
      									<div className="panel-heading">Dados do usuário
      										{(this.context.roles.ADMIN  || _.contains(this.context.permissions, 
														"org.forpdi.core.user.authz.permission.ManageUsersPermission")) ?  
      											<span className="mdi mdi-pencil cursorPointer floatRight" onClick={this.confirmEdit} title="Editar perfil do usuário"/>
      											: ""
      										}
      									</div>
      										<div className="panel-body">
      										
												<div className="fpdi-container-profile">
													<img className="fpdi-image-profile" src={this.state.model.picture == null ?(Logo):this.state.model.picture}/>
												</div>
												{(this.context.roles.ADMIN  || _.contains(this.context.permissions, 
														"org.forpdi.core.user.authz.permission.ManageUsersPermission") ? 
												<div className="fpdi-container-profile-icon">
  													<span className="mdi mdi-camera mdi-camera-attributes cursorPointer" onClick={this.uploadFile} title="Alterar imagem de perfil do usuário"/>
  												</div> : "")}
  												
  												<div>
  													<form>
  														<div className="form-group form-profile">
  															<label className = "fpdi-text-label"> NOME </label>
  																<p id = "p-profileUser"> {(this.state.model.name.length>25)?(string(this.state.model.name).trim().substr(0,25).concat("...").toString()):(this.state.model.name)} </p>

  															<label className = "fpdi-text-label"> E-MAIL </label>
    															<p id = "p-profileUser"> {this.state.model.email} </p>
    														
    														<label className = "fpdi-text-label"> CPF </label>
    															<p id = "p-profileUser"> {this.state.model.cpf} </p>

    														<label className = "fpdi-text-label"> DATA DE NASCIMENTO </label>
    															<p id = "p-profileUser"> {this.state.model.birthdate == null ? this.state.model.birthdate : this.state.model.birthdate.split(" ")[0]} </p>

    														<label className = "fpdi-text-label">CELULAR</label>
    															<p id = "p-profileUser"> {this.state.model.cellphone} </p>

															<label className = "fpdi-text-label">TELEFONE</label>
    															<p id = "p-profileUser"> {this.state.model.phone}  </p>
    																	
    														<label className = "fpdi-text-label"> DEPARTAMENTO</label>
    															<p id = "p-profileUser"> {this.state.model.department == null ? this.state.model.department: (this.state.model.department.length>25)?(string(this.state.model.department).trim().substr(0,25).concat("...").toString()):(this.state.model.department)} </p>

    														<label className = "fpdi-text-label"> TIPO DE USUÁRIO </label>
    															<p id = "p-profileUser"> {AccessLevels.mapped[this.state.model.accessLevel]} </p> 	
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
												{(this.context.roles.ADMIN  || _.contains(this.context.permissions, 
														"org.forpdi.core.user.authz.permission.ManageUsersPermission") ? 
												<div className="fpdi-container-profile-icon">
  													<span className="mdi mdi-camera mdi-camera-attributes cursorPointer" onClick={this.uploadFile} title="Alterar imagem de perfil do usuário"/>
  												</div> : "")}												
  												
  												<div className="panel-body">
   							 							<VerticalForm
				    										ref="editUser"
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
      									<div className="panel-heading"> Permissões do usuário 
      										{(this.state.editingPermission || !(this.context.roles.ADMIN  || _.contains(this.context.permissions, 
														"org.forpdi.core.user.authz.permission.ManageUsersPermission")) ? "" : 
      											<span className="mdi mdi-pencil cursorPointer floatRight" onClick={this.editPermissions} title="Editar permissões"/>)}
      									</div>
      									<div className="padding5">
											{this.state.permissions.map((item, idx) => {
												if(item.granted || this.state.model.accessLevel >= item.accessLevel || 
													(this.state.editingPermission && this.context.accessLevel >= item.accessLevel)){
													return(
														<div key={"perm-"+idx} className="user-permission-list-item">
															{(this.state.editingPermission ? 
																<input type="checkbox" defaultChecked={item.granted || this.state.model.accessLevel >= item.accessLevel}
															 		className="user-permission-checkbox" disabled={this.state.model.accessLevel >= item.accessLevel || 
															 		this.context.accessLevel < item.accessLevel} onClick={this.selectPermission.bind(this, idx)}/> : ""
															 	)
															}
															
															{item.permission}
															<span className={"mdi cursorPointer floatRight "+(_.contains(this.state.detailed, idx) ?
															 "mdi-chevron-down" : "mdi-chevron-right")} onClick={this.details.bind(this, idx)}/>
															{(_.contains(this.state.detailed, idx) ? this.showDescription(item.description) : "")}
														</div>
													);
												}
											})}
											{!!this.state.editingPermission ?
												(<div className="form-group user-permission-btn-ctn">
													<button className="btn btn-success user-permission-btn-save" onClick={this.savePermissions}>Salvar</button>
													<button className="btn btn-default" onClick={this.editPermissions}>Cancelar</button>												
												</div>)
												:
												""
											}
										</div>										
      								</div>      																
								</div>
								<div className="col-sm-6">
									<div className="panel panel-default panel-default-user">
										<div className="panel-heading"> Enviar mensagens </div>
										<div className="padding5">
											<div className="panel-body">
												<VerticalForm
				    								ref="messageUser"
													onSubmit={this.onSubmitMenssage}
													fields={this.state.fieldsMessage}
													store={UserStore}
													submitLabel="Enviar"
													cancelLabel="Limpar"
													onCancel = {this.onClear}
												/>
											</div>
										</div>
									</div>
									
									<div className = "panel panel-default panel-message">
										<div className="panel-heading"> Histórico de mensagens </div>
										<div className="padding5">
											<div className="panel-body">
												<NotificationMessageUser ref="pagination"/>
        										{this.state.hideShowMoreMessages == false ?
        											<div className="textAlignCenter">
                    									<a onClick={this.showMoreMessages}>ver mais...</a>
                									</div>
                								:""}
											</div>
										</div>
									</div> 

								</div>
							</div>
					</div>);
			
	}

});





