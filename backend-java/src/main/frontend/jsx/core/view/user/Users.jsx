import S from 'string';
import React from "react";
import {Link} from 'react-router';
//import Toastr from 'toastr';
import AccessLevels from "forpdi/jsx/core/store/AccessLevels.json";
import UserStore from "forpdi/jsx/core/store/User.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Pagination from "forpdi/jsx/core/widget/Pagination.jsx";
import _ from "underscore";
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx"

import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';

//import Toastr from 'toastr';

var Validate = Validation.validate;

export default React.createClass({
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},
	getInitialState() {
		return {
			loading: true,
			loadingSend: false,
			error: null,
			hideUser:false,
			hideNewUser:false,
			sortIconStatus:"",
			accessLevelSelect: null,
			models:null,
			totalUsers:null,
			usersToImport: [],
			countSelectedUsers: 0,
			importFileName: "",
			emptyUsersToImport: false
		};
	},

	componentDidMount() {
		var me = this;
		/* Comentado pois interferia no acesso ao perfil do próprio usuário por outros níveis de conta,
		 * remover o código caso não for essencial para outras funcionalidades
		 
		if (!me.context.roles.ADMIN) {
			me.context.router.replace("/home");
			return;

		}
	
		}*/
		
		//UserStore.on('find', store => {
		//	me.setState({
		//		loading: false,
		//		models: store.models
		//	});
			
		//}, me);

		me.getUsers(1,5);



		UserStore.on("retrieve-user", (model) => {
			me.setState({
				loading: false,
				models: model.data,
				totalUsers:model.total
			});
		},me)

		


		UserStore.on("remove", (data) => {
			if(data.data){
				this.context.toastr.addAlertSuccess("Usuário removido com sucesso.");
				me.getUsers(1,5);
				//me.refs["pagination"].reload();
			}else{
				this.context.toastr.addAlertError("Impossível excluir esse usuário pois ele é responsável por algum nível do plano de metas.");
				
			}

			//Toastr.remove();
			//Toastr.success("Usuário removido com sucesso.");
			
		}, me);
		UserStore.on("block", () => {
			//Toastr.remove();
			//Toastr.success("Usuário bloqueado com sucesso.");
			this.context.toastr.addAlertSuccess("Usuário bloqueado com sucesso.");
			me.getUsers(1,5);
			//me.refs["pagination"].reload();
		}, me);
		UserStore.on("unblock", () => {
			//Toastr.remove();
			//Toastr.success("Usuário desbloqueado com sucesso.");
			this.context.toastr.addAlertSuccess("Usuário desbloqueado com sucesso.");
			me.getUsers(1,5);
			//me.refs["pagination"].reload();
		}, me);
		UserStore.on("resend-invitation", () => {
			//Toastr.remove();
			//Toastr.success("Convite reenviado com sucesso.");
			me.getUsers(1,5);
			this.context.toastr.addAlertSuccess("Convite reenviado com sucesso.");
		}, me);
		UserStore.on("sync", (model) => {
			//Toastr.remove();
			this.setState({
				loadingSend: false
			});

			this.context.toastr.addAlertSuccess("Um e-mail de convite foi enviado para "  + model.attributes.email + ", no qual o usuário precisa completar o cadastro.");

			this.refs.nameUser.value = "";
			this.refs.emailUser.value = "";
			this.refs.selectAccessLevels.value = -1;
			me.getUsers(1,5);
			//console.log(me.refs["pagination"])
			//me.refs["pagination"].loadPage(1, null, 10);

		}, me);
		UserStore.on("fail", msg =>{
			UserStore.dispatch({
				action: UserStore.ACTION_RETRIEVE
			});
		},me)
		UserStore.on("users-imported", (data) =>{
			this.context.toastr.addAlertSuccess("Usuários importados com sucesso.");
			me.getUsers(1,5);
			//me.refs["pagination"].reload();
		},me)


	},
	componentWillUnmount() {
		UserStore.off(null, null, this);
	},

	closeAlert() {
		this.setState({
			error: null
		});
	},

	cancelBlockUnblock () {
		Modal.hide();
	},

	removeUser(id, event) {
		event && event.preventDefault();
		var msg = "Tem certeza que deseja remover este usuário?"
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			UserStore.dispatch({
				action: UserStore.ACTION_REMOVE,
				data: id
			});
		},msg,this.cancelBlockUnblock);
	},
	
	blockUser(id,nome, event) {
		event && event.preventDefault();
		var msg =  "O usuário " + nome + " será bloqueado.Deseja continuar ?";

		Modal.confirmCancelCustom(() => {
			Modal.hide();
			UserStore.dispatch({
				action: UserStore.ACTION_BLOCK,
				data: id
			});
		},msg,this.cancelBlockUnblock);

	},

	unblockUser(id,nome, event) {
		event && event.preventDefault();
		var msg =  "O usuário " + nome + " será desbloqueado.Deseja continuar ?";
		Modal.confirmCustom(() => {
			Modal.hide();
			UserStore.dispatch({
				action: UserStore.ACTION_UNBLOCK,
				data: id
			});
		},msg,this.cancelBlockUnblock);
	},

	resendInvitation(id, event) {
		event && event.preventDefault();
		UserStore.dispatch({
			action: UserStore.ACTION_RESEND_INVITATION,
			data: id
		});
		this.context.toastr.addAlertSuccess("O convite foi reenviado ao usuário")
	},

	hideFieldsUser() {
    	this.setState({
      		hideUser: !this.state.hideUser
   	 	})
  	},

  	hideFieldsNewUser() {
    	this.setState({
      		hideNewUser: !this.state.hideNewUser
   	 	})
  	},
	
	userExists() {
		
		
		for (var aux in this.state.models) {
			if(this.state.models[aux].email.localeCompare(this.refs.emailUser.value.trim()) == 0) {
				return true;
			}
		}
		return false;
	},

  	onSubmitConviteUser() {
		var me = this;
		var errorField = false;

		var errorField = Validate.validationConviteUser(this.refs);

		if (errorField){
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError("É necessário preencher todos os campos do formulário");
			return;
		} else {
			this.setState({
				loadingSend: true
			});

			this.setState({
				accessLevelSelect: this.refs.selectAccessLevels.value
			})
		}

		if (!me.userExists()) { 

			UserStore.dispatch({
				action: UserStore.ACTION_SIGNUP,
				data: {
					name:this.refs.nameUser.value.trim(),
					email:this.refs.emailUser.value.trim(),
					accessLevel:parseInt(this.refs.selectAccessLevels.value)
				}
			});
		} else {
			this.context.toastr.addAlertError("O convite para este email já foi enviado, utilize a opção de reenviar na tabela Usuários do Sistema");
			this.setState({
				loadingSend: false
			});
		}
	},

	quickSortByUsersName(sorting){
	  	
	  	var data = [];
	  	data = this.state.models;

	  	if(sorting == "asc"){
	  		data.sort(function(a, b) { 
	  			if (a.name < b.name) {
           			return -1;
           		}
           		if (a.name > b.name) {
           			return 1;
          		}       
          		return 0; 
       		})
     	} else if(sorting == "desc"){
       		data.sort(function(a, b) { 
          		if (a.name < b.name) {
          			return 1;
          		}
          		if (a.name > b.name) {
           			return -1;
          		}       
          		return 0; 
       		})
    	} 

     this.setState({
       	models: data,
       	sortIconStatus:sorting
       	
     }) 

   },

   pageChange(page,pageSize) {
   		this.getUsers(page, pageSize);
   },

   getUsers(page, pageSize) {
   		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
			data: {
				page: page,
				pageSize: pageSize
			}
		});


   },

	renderSameUserOptions() {
		return (
			<ul className="dropdown-menu">
				<li>
					<Link  to = {"/users/profilerUser/" +UserSession.attributes.user.id}>
						Meu Perfil
					</Link>
				</li>
			</ul>
		);
	},

	renderAnotherUser(model, cpf) {
		return (
			<ul className="dropdown-menu">
				{cpf.isEmpty() ? <li>
					<a onClick={this.resendInvitation.bind(this, model.id)}>
						Reenviar convite
					</a>
				</li>:""}
				{!cpf.isEmpty() && (this.context.roles.MANAGER  || _.contains(this.context.permissions, 
					"org.forpdi.core.user.authz.permission.ManageUsersPermission") || _.contains(this.context.permissions, 
					"org.forpdi.core.user.authz.permission.ViewUsersPermission"))? 
				<li>
					<Link to={"/users/"+model.id+"/edit"}>
						Visualizar usuário
					</Link>
				</li>:""}
				{(this.context.roles.ADMIN  || _.contains(this.context.permissions, 
					"org.forpdi.core.user.authz.permission.ManageUsersPermission") ?
					<li>
						{model.blocked ?
							<a onClick={this.unblockUser.bind(this, model.id,model.name)}>
								Desbloquear usuário
							</a>
							:
							<a onClick={this.blockUser.bind(this, model.id,model.name)}>
								Bloquear usuário
							</a>
						}
					</li> : ""
				)}
				{(this.context.roles.ADMIN  || _.contains(this.context.permissions, 
					"org.forpdi.core.user.authz.permission.ManageUsersPermission") ? 
					<li>
						<a onClick={this.removeUser.bind(this, model.id)}>
							Remover usuário
						</a>
					</li> : ""
				)}
			</ul>
		);
	},

	readCSVFile(){
		var me = this;
		Modal.readFile(
			"Importar usuários",
			(<div>
				<p>
					Faça upload de um arquivo no formato .csv. O arquivo deve conter somente as colunas "nome" e "e-mail".
				</p>
			</div>),
			".csv",
			(resp) => {
				var file = document.getElementById("file-reader-field").files[0];
				var reader = new FileReader();
				var importFileName = file.name;
		      	var ext = file.name.substring(file.name.lastIndexOf(".")).toLowerCase();
		      	if(ext == '.csv'){

					reader.readAsText(file);
			      	reader.onload = function(event){
				  		var fileArr = event.target.result.split('\n');      
				  		var msg = "Lista de usuários";
				  		function User() {
			                var name;
			                var email;
			                var validEmail;
			            }
				  		var userList = [];
				  		var csvValidation = true;
				  		for (var i=1; i<(fileArr.length); i=i+1) {
				  			 //fazer validação do arquivo csv de forma geral
				  			 var linha = fileArr[i].split(';');
				  			 if(linha.length !=2){
				  			 	csvValidation = false;
				  			 	break;
				  			 }
				  			 var user = new User();
				  			 user.name = linha[0];
				  			 user.email = linha[1];
				  			 user.validEmail = Validate.emailIsValid(user.email);
				  			 userList.push(user);
				  		}
				  		if(csvValidation){
					  		me.setState({
					      		usersToImport: userList,
					      		importFileName: importFileName,
					      		emptyUsersToImport: false
					   	 	})
					  		Modal.importUsers(
					  			"Importar Usuários",
					  			me.renderImportedUsers(userList),
					  			() => {
					  				me.selectedUsersToImport();
					  				//Modal.hide retirado para quadro não desaparecer
								}
					  		);
					  	}else{
					  		var errorTag = document.getElementById("upload-error-span");
		      				errorTag.innerHTML = "O arquivo não está dentro dos padrões.";
					  	}
			      	};
		      	}else{
		      		var errorTag = document.getElementById("upload-error-span");
		      		errorTag.innerHTML = "O arquivo deve ter extensão '.csv'";
		      	}     	
			}
		);
	},

	selectedUsersToImport(userList, importFileName){
		var i;
		var nameList = [];
		var emailList = [];
		var accessList = [];

		for(i=0; i<this.state.usersToImport.length; i++){

			if(document.getElementById("checkbox-"+i).checked && !document.getElementById("checkbox-"+i).disabled){

				var name = document.getElementById("user-name-"+i).innerHTML;
				var email = document.getElementById("user-email-"+i).innerHTML;
				var accessLevel = document.getElementById("user-accesLevel-"+i).value;
				
				nameList.push(name.trim());
				emailList.push(email.trim());
				accessList.push(accessLevel);

			}
		}

		if (nameList.length > 0 && emailList.length > 0 && accessList.length > 0) {
			Modal.hide();
			UserStore.dispatch({
				action: UserStore.ACTION_IMPORT_USERS,
				data: {
					nameList: nameList,
					emailList: emailList,
					accessList: accessList
				}
			}); 
			
		} else {
			/* A função do Modal é chamada no render() porque para renderiza assincronamente*/
			if(this.isMounted()){
				this.setState({
					emptyUsersToImport: true
				});
			}
		}
	},

	selectAll(){
		var i=0;
		var me = this;
		var countSelected = 0;

		for(i=0; i<this.state.usersToImport.length; i++){
			if(!document.getElementById("checkbox-"+i).disabled){
				document.getElementById("checkbox-"+i).checked = document.getElementById("selectAll").checked;		
				if(document.getElementById("checkbox-"+i).checked){
					countSelected++;
				}
			}
		}
		//console.log(countSelected);
		this.setState({
			countSelectedUsers: countSelected
		});
		document.getElementById("qntdSelectedUsers").innerHTML = countSelected;
	},

	onCheck(idy){
		var selectAll = true;
		var i;
		var countSelected = this.state.countSelectedUsers;
		if(document.getElementById("checkbox-"+idy).checked){
			countSelected++;
		}else{
			countSelected--;
		}
		this.setState({
			countSelectedUsers: countSelected
		});
		document.getElementById("qntdSelectedUsers").innerHTML = countSelected;
		//console.log(countSelected);

		for(i=0; i<this.state.usersToImport.length; i++){
			if(!document.getElementById("checkbox-"+i).checked){
				selectAll = false;
			}
		}
		//console.log(selectAll);
		document.getElementById("selectAll").checked = selectAll;
	},



	renderImportedUsers(users){
		var me = this;
		return (<div><table className="import-user-table table table-fixed">	      					
					<thead className="import-table-header">
						<tr>
							<th className = "column-goals-perfomance col-xs-1"> 
								<input id="selectAll" value="selectAll" type="checkbox" title="Todos" onChange={me.selectAll}/>
							</th>
							<th className = "column-goals-perfomance col-xs-4"> 
								Nome
							</th>
							<th className = "column-goals-perfomance col-xs-4"> 
								Email
							</th>
							<th className = "column-goals-perfomance col-xs-3"> 
								Tipo de conta
							</th>
						</tr>										
					</thead>
					<tbody id="importUsersData">
					{users.map((attr, idy) =>{
					return(
						!attr.validEmail ?
						
							<tr key={idy} id={"row-"+idy}>
								
								<td className = "col-xs-1 disabled">
										<span  data-placement="right" title="Email inválido."><input id={"checkbox-"+idy} type="checkbox" onChange={me.onCheck.bind(this,idy)} disabled="true"/></span>
								</td>
								<td className = "col-xs-4 disabled" id={"user-name-"+idy}><span  data-placement="right" title="Email inválido.">{attr.name}</span></td>
								<td className = "col-xs-4 disabled" id={"user-email-"+idy}><span  data-placement="right" title="Email inválido.">{attr.email}</span></td>
								<td className = "col-xs-3 disabled">
									<span  data-placement="right" title="Email inválido."><select id={"user-accesLevel-"+idy} className="form-control user-select-box disabled" disabled="true">
										{AccessLevels.list.map((attr, idy) =>{
			                							return(<option key={attr.accessLevel} value={attr.accessLevel}
			                								data-placement="right" title={attr.name}>
			                									{attr.name}</option>);
													})
										}
			                     	</select></span>
			                     </td>

			                </tr>
		                
		               	: 
		               	<tr key={idy} id={"row-"+idy}>
							<td className = "col-xs-1">
								<input id={"checkbox-"+idy} type="checkbox" onChange={me.onCheck.bind(this,idy)} />
							</td>
							<td className = "col-xs-4" id={"user-name-"+idy}>{attr.name}</td>
							<td className = "col-xs-4" id={"user-email-"+idy}>{attr.email}</td>
							<td className = "col-xs-3">
								<select id={"user-accesLevel-"+idy} className="form-control user-select-box">
									{AccessLevels.list.map((attr, idy) =>{
		                							return(<option key={attr.accessLevel} value={attr.accessLevel}
		                								data-placement="right" title={attr.name}>
		                									{attr.name}</option>);
												})
									}
		                     	</select>
		                     </td>
		                </tr>
						);
					})}
				</tbody>
			</table>
				<div>
					<strong ><span id="qntdSelectedUsers">0</span> usuários selecionados.</strong>
					<span className="floatRight">
						<strong>Arquivo: </strong>
							<span id="fileName">{this.state.importFileName}
						</span>
					</span>
				</div>
				{this.state.emptyUsersToImport ? 
					(<div className="emptyUsersToImport">
						Você não selecionou <strong>nenhum usuário</strong> para importar!
					</div>) 
					: ""
				}
			</div>
		);
	},

	renderRecords() {
		{/* verifica se nao há usuários na tabela	
		if (!this.state.models || (this.state.models.length <= 0)) {

			return (
				<div className="panel panel-default"> 
        			<div className="panel-heading">
        				<b className="budget-graphic-title"> Usuários do Sistema </b>
        				<div className="performance-strategic-btns floatRight">
                				<span  className={(this.state.hideUser)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFieldsUser}/>
        				</div>
        			</div>

        		{!this.state.hideUser ?
        			<table className="table fpdi-table">
        				<thead className="hidden-xs">
        					<tr>	
        						<th className="col-sm-3">{Messages.get("label.name")}  <span className={this.state.sortIconStatus == "desc"?"mdi mdi-sort-ascending cursorPointer":
                       				(this.state.sortIconStatus =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                       				onClick={(this.state.sortIconStatus == "" || this.state.sortIconStatus =="desc") ? this.quickSortByUsersName.bind(this,"asc") :  this.quickSortByUsersName.bind(this,"desc")} > </span></th>
								<th className="col-sm-3">{Messages.get("label.email")}</th>
								<th className="col-sm-3">{Messages.get("label.cpf")}</th>
								<th className="col-sm-3">Tipo de Conta </th>
								<th className="col-sm-3"> Situação </th>
							</tr>		 				
        				</thead>
        			
        					<tbody>
        						<tr>
        							<td></td>
        							<td></td>
        							<td>
        							<div className='table-empty-sons'> 
										Não há usuários cadastrados
				   			 		</div> </td>
				   			 		<td></td>
				   			 		<td></td>
				   			 	</tr>
        					</tbody>
        					

        			</table>
        		:""}
        	</div>);
		}	*/}
		
		var sameUser;
		return (
			<div className = "container-user">
				{(this.context.roles.ADMIN  || _.contains(this.context.permissions, 
					"org.forpdi.core.user.authz.permission.ManageUsersPermission") ?
					<div className="panel panel-default"> 
	        			<div className="panel-heading displayFlex">
	        				<b className="budget-graphic-title"> Novo usuário </b>
	        				<div className="performance-strategic-btns floatRight">
	        					<button type="button" className="btn btn-primary budget-new-btn" onClick={this.readCSVFile}> Importar usuários </button>
	                			<span  className={(this.state.hideNewUser)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFieldsNewUser}/>
	              			</div>
	        			</div>

	        			{!this.state.hideNewUser ? 
	        				this.state.loadingSend ?
	        					<LoadingGauge />
	        				:
		        				<table className="table fpdi-table">
		        					<thead className="hidden-xs">	
		        						<tr>
		        							<th className="col-sm-3">{Messages.get("label.name")}  <span className="fpdi-required"></span> </th>
		        							<th className="col-sm-3">{Messages.get("label.email")} <span className="fpdi-required"></span> </th>
		        							<th className="col-sm-3">Tipo de Conta <span className="fpdi-required"></span>  </th>
		        							<th className="col-sm-3"> </th>
		        						</tr>      				
		        					</thead>
		        					<tbody>
		        						<tr>
		        							<td className="fdpi-table-cell"> 
		        								<input maxLength="255" className="budget-field-table" ref='nameUser' type='text' defaultValue=""/>
		        								<div ref="formAlertNameUser" className="formAlertError"></div>
	        								</td>
		        							<td className="fdpi-table-cell">
		        								<input maxLength="255" className="budget-field-table" ref='emailUser' type='text' defaultValue=""/>
		        								<div ref="formAlertEmail" className="formAlertError"></div>
	        								</td>
		        							<td className="fdpi-table-cell"> 
		        								<select  className="form-control user-select-box" ref="selectAccessLevels" defaultValue={-1}>
													<option value={-1} disabled data-placement="right" title="Selecione o tipo de conta">Selecione o tipo de conta </option>
													{this.context.roles.SYSADMIN ?
														AccessLevels.list.map((attr, idy) =>{
			                    							return(<option key={attr.accessLevel} value={attr.accessLevel}
			                    								data-placement="right" title={attr.name}>
			                    									{attr.name}</option>);
														})
													:
														AccessLevels.listNoSysAdm.map((attr, idy) =>{
			                    							return(<option key={attr.accessLevel} value={attr.accessLevel}
			                    								data-placement="right" title={attr.name}>
			                    									{attr.name}</option>);
														})
													}
												}
												</select>
												<div ref="formAlertTypeAccont" className="formAlertError"></div>
											</td>
											<td className="fdpi-table-cell">
												<button type="button" className="btn btn-primary budget-new-btn" onClick={this.onSubmitConviteUser}> Enviar convite </button>
											</td>
										</tr>  
		        						
										<tr>
											<td colSpan="4" className="fdpi-table-cell">
												<div className="fpdi-indicator-weigth-total">*Favor não convidar usuários utilizando e-mails do MSN, Hotmail, Outlook e Windows Live Mail, pois os mesmos podem não receber o convite.</div>
											</td>
										</tr>
		        					</tbody>
	        				</table>

	        			:""}
	        		</div>
					 : "")}
				

			<div className="panel panel-default">     
        		<div className="panel-heading">
        			<b className="budget-graphic-title"> Usuários do Sistema </b>
        			<div className="performance-strategic-btns floatRight">
                		<span  className={(this.state.hideUser)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFieldsUser}/>
              		</div>
        		</div>
        		{!this.state.hideUser ?
        			<div>
						<table className="table fpdi-table">
							<thead className="hidden-xs">
								<tr>
									<th className="col-sm-3">{Messages.get("label.name")}  <span className={this.state.sortIconStatus == "desc"?"mdi mdi-sort-ascending cursorPointer":
                       					(this.state.sortIconStatus =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                       					onClick={(this.state.sortIconStatus == "" || this.state.sortIconStatus =="desc") ? this.quickSortByUsersName.bind(this,"asc") :  this.quickSortByUsersName.bind(this,"desc")} title="Ordenar"> </span></th>
									<th className="col-sm-3">{Messages.get("label.email")}</th>
									<th className="col-sm-3">{Messages.get("label.cpf")}</th>
									<th className="col-sm-3">Tipo de Conta </th>
									<th className="col-sm-3"> Situação </th>		
								</tr>
							</thead>
							<tbody>
								{this.state.models.map((model, idx) => {
									var cpf = S(model.cpf);
									sameUser = (cpf.isEmpty() || UserSession.attributes.user == null ? false : cpf == UserSession.attributes.user.cpf);

									return (<tr key={"user-"+idx} className={model.blocked ? "danger":""}>
											<td  className={"col-sm-3"+(cpf.isEmpty() ? " warning":"")}>											
												<span className="dropdown">
													<a
														className="dropdown-toggle"
														data-toggle="dropdown"
														aria-haspopup="true"
														aria-expanded="true"
														title="Ações"
													>
														<span className="sr-only">Ações</span>
														<span title="Ações" className="mdi mdi-chevron-down" />
													</a>
													
													{sameUser ? this.renderSameUserOptions() : this.renderAnotherUser(model,cpf)}											
												</span>
												{model.name}
											</td>
											<td className={"col-sm-3"+(cpf.isEmpty() ? " warning":"")}> <p id = "userNCadastrado"> {model.email} </p> </td> 
											<td className={"col-sm-3"+(cpf.isEmpty() ? " warning":"")}>
												<p id = "userNCadastrado"> {cpf.isEmpty() ? ".." :cpf.s} </p>
											</td>
											<td className={"col-sm-3"+(cpf.isEmpty() ? " warning":"")}>
												<p id = "userNCadastrado">{model.accessLevel ? AccessLevels.mapped[model.accessLevel] : AccessLevels.mapped[this.state.accessLevelSelect]} </p>
											</td>
											<td className={"col-sm-3"+(cpf.isEmpty() ? " warning":"")}>
												{cpf.isEmpty() ? (model.blocked ? "Bloqueado" : <p id = "userNCadastrado"> <small>O usuário ainda não concluiu o cadastro.</small> </p>) : (model.blocked ? "Bloqueado" : "Regular")}
											</td>
										</tr>);
									})}
							</tbody>
						</table>
						<TablePagination ref = "pagination"
							total={this.state.totalUsers}
							onChangePage={this.pageChange}
							tableName={"users-table"}
						/>
				</div>: ""}
			</div>
		</div>);
	},

	render() {
		/* Colocar no render devido o processo assíncrono */
		if (this.state.emptyUsersToImport){
			Modal.importUsers(
	  			"Importar Usuários",
	  			this.renderImportedUsers(this.state.usersToImport),
	  			() => {
	  				this.selectedUsersToImport();
				}
	  		);
		}

		if (this.props.children) {
			return this.props.children;
		}
		return (<div className="fpdi-profile-user padding40">
			<div className="fpdi-tabs-content container-fluid animated fadeIn">
				<h1>{Messages.get("label.users")}</h1>
				{/*
					<ul className="fpdi-action-list text-right">
						<Link to="/users/new" className="btn btn-sm btn-primary">
							<span className="mdi mdi-plus"
							/> Convidar um usuário
						</Link>
					</ul> 
				*/}
				{this.state.loading ? <LoadingGauge />:this.renderRecords()}

				<Pagination store={UserStore} ref="pagination" />
			</div>
		</div>);
	  }
	});
