
import React from 'react';
import Marked from "marked";
import {Link} from 'react-router';
import _ from 'underscore';
import moment from 'moment';
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import SummaryTable from "forpdi/jsx/planning/widget/plan/SummaryTable.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";


export default React.createClass({
	contextTypes: {
		permissions: React.PropTypes.array.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object.isRequired,
		planMacro: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object.isRequired,
		toastr: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			tabPath: this.props.location.pathname,
			archived: null,
			loading: !!this.props.params.id,
			modelId: this.props.params.id,
			model: null,
			undeletable: false
		};
	},
	updateLoadingState(showName) {
		if (this.isMounted()) {
			this.setState({
				loading: (this.props.params.id && !this.state.model)
			});
		}
	},

	componentDidMount() {
		var me = this;
		//this.context.tabPanel.addTab(this.state.tabPath, this.context.planMacro.get("name"));
		PlanMacroStore.on("planmacroarchived", (model) => {			
			if(model.status == undefined || model.status == 200){
				if (this.isMounted()) {
					this.setState({
						archived: true
					});
				}
				this.context.toastr.addAlertSuccess("Plano arquivado com sucesso");
				me.context.router.push("/plan/"+model.data.id+"/details/");
				PlanMacroStore.dispatch({
            		action: PlanMacroStore.ACTION_FIND_UNARCHIVED
        		});
        		PlanMacroStore.dispatch({
           			action: PlanMacroStore.ACTION_FIND_ARCHIVED
        		});
			} else {
				//Toastr.error(model.responseJSON.message);
				this.context.toastr.addAlertError(model.responseJSON.message);
			}
		}, me);

		PlanMacroStore.on("planmacrounarchived", (model) => {			
			if(model.status == undefined || model.status == 200){
				if (this.isMounted()) {
					this.setState({
						archived: false
					});
				}
				this.context.toastr.addAlertSuccess("Plano desarquivado com sucesso");
				me.context.router.push("/plan/"+model.data.id+"/details/");
				PlanMacroStore.dispatch({
            		action: PlanMacroStore.ACTION_FIND_UNARCHIVED
        		});

        		PlanMacroStore.dispatch({
           			action: PlanMacroStore.ACTION_FIND_ARCHIVED
        		});
			} else {
				//Toastr.error(model.responseJSON.message);
				this.context.toastr.addAlertError(model.responseJSON.message);
			}
		}, me);
		
		PlanMacroStore.on("retrieve", (model) => {
			if(this.isMounted()){
				me.setState({
					model: model,
					archived: model.attributes.archived,
					undeletable: model.attributes.haveSons
				});
			}
			me.updateLoadingState(false);
		}, me);

		PlanMacroStore.on('plan-deleted', (response, data) => {
			me.context.toastr.addAlertSuccess(data.attributes.name + " excluído com sucesso.");
			me.context.router.push("/home");
			PlanMacroStore.dispatch({
                action: PlanMacroStore.ACTION_FIND
            });
		}, me);

		if (this.props.params.id) {
			//_.defer(() => {me.context.tabPanel.addTab(me.props.location.pathname,"Editar plano macro");});
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_RETRIEVE,
				data: this.state.modelId
			});
		}
		this.context.tabPanel.addTab(this.props.location.pathname, this.context.planMacro.get("name"));
	},
	componentWillReceiveProps(newProps) {
		if (newProps.location.pathname != this.state.tabPath) {
			this.setState({
				tabPath: newProps.location.pathname
			});
			this.context.tabPanel.addTab(newProps.location.pathname, this.context.planMacro.get("name"));
		}
	},

	cancelBlockUnblock () {
		Modal.hide();
	},

	deletePlan(event){
		event && event.preventDefault();
		var me = this;
		var msg = "Você tem certeza que deseja excluir "+me.state.model.attributes.name+"?";
		
		if (me.state.model != null) {
			Modal.confirmCancelCustom(() => {				
				Modal.hide();
				PlanMacroStore.dispatch({
					action: PlanMacroStore.ACTION_DELETE,
					data: me.state.model
				});		
			},msg,me.cancelBlockUnblock);
		}
		me.forceUpdate();
	},

	archivePlan(event) {
		event && event.preventDefault();
		var msg = "Você tem certeza que deseja arquivar esse plano?";
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_ARCHIVE,
				data: {
					id: this.context.planMacro.get("id")
				},
				wait: true
			});	

		}, msg, this.cancelBlockUnblock);
	},

	unarchivePlan(event) {
		event && event.preventDefault();
		var me = this;
		var msg = "Você tem certeza que deseja desarquivar esse plano?";
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_UNARCHIVE,
				data: {
					id: this.context.planMacro.get("id")
				},
				wait: true
			});		
		}, msg, this.cancelBlockUnblock);
	},

	onSubmit(data) {
		var me = this;
		var msg = "Existem erros no formulário";
		var dataError = false;
		var boolMsg = false;
		var difference = 0;
		var valDateBegin,valDateFinal;

		var begin = this.refs.planMacroEditForm.refs["begin"].props.fieldDef.value.split(" ");
		begin = moment(begin,"DD/MM/YYYY").toDate();
		if(begin== null){
			this.refs.planMacroEditForm.refs.begin.refs.formAlertError.innerHTML = "Você não pode deixar esse campo em branco!";
			this.refs.planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className += " borderError";
			dataError = true;
			boolMsg = true;
		}else{
			data.begin = begin.getDate()+"/"+(begin.getMonth()+1)+"/"+begin.getFullYear();
			if(this.refs.planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className && this.refs.planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className.indexOf('borderError')){
				this.refs.planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className = "form-control";
				this.refs.planMacroEditForm.refs.begin.refs.formAlertError.innerHTML = "";
			}
			
		}

		var end = this.refs.planMacroEditForm.refs["end"].props.fieldDef.value.split(" ");
		end = moment(end,"DD/MM/YYYY").toDate();
		if(end== null){
			this.refs.planMacroEditForm.refs.end.refs.formAlertError.innerHTML = "Você não pode deixar esse campo em branco!";
			this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			dataError = true;
			boolMsg = true;
		}else{
			if(this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className && this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className.indexOf('borderError')){
				this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className = "form-control";
				this.refs.planMacroEditForm.refs.end.refs.formAlertError.innerHTML = "";
			}
			data.end = end.getDate()+"/"+(end.getMonth()+1)+"/"+end.getFullYear();
			
		}
		
		if (!dataError) {
			valDateBegin = begin.getTime();
			valDateFinal = end.getTime();
			difference = valDateFinal - valDateBegin;
		}
		if (!dataError && begin.getTime() == end.getTime()) {
			this.refs.planMacroEditForm.refs.end.refs.formAlertError.innerHTML = "Data de término deve ser posterior à data de início";
			this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			boolMsg = true;
		} else if (!dataError && difference < 86400000) {
			this.refs.planMacroEditForm.refs.end.refs.formAlertError.innerHTML = "Data de término deve ser posterior à data de início";
			this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			boolMsg = true;
		}
		if(data.name == "" ||  !!data.name.match(/^(\s)+$/) ){
			boolMsg = true;
			this.refs.planMacroEditForm.refs.name.refs.formAlertError.innerHTML = "Você não pode deixar esse campo em branco!";
			this.refs.planMacroEditForm.refs.name.refs["field-name"].className += " borderError";
		}else{
			if(this.refs.planMacroEditForm.refs.name.refs["field-name"].className && this.refs.planMacroEditForm.refs.name.refs["field-name"].className.indexOf('borderError')){
				this.refs.planMacroEditForm.refs.name.refs["field-name"].className = "form-control";
				this.refs.planMacroEditForm.refs.name.refs.formAlertError.innerHTML = "";
			}
		} 

		if(boolMsg){
			this.context.toastr.addAlertError(msg);
			return;
		}

		if (me.props.params.id) {
			me.state.model.set(data);
			msg = "Os dados serão atualizados. Deseja continuar essa ação?";
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_UPDATE,
				data: me.state.model
			});
		} else {
			msg = "O Plano Macro será criado. Deseja continuar essa ação?";
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_NEWPLAN,
				data: data,
				opts: {
					wait: true
				}
			});
		}
	},

	onChangeDate(field, data){
		var model = this.state.model;
		this.setState({
			model:model,
		});
		this.refs['planMacroEditForm'].refs[field].props.fieldDef.value = data.format('DD/MM/YYYY');
	},

	renderUnarchivePlan() {
		return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<a
						data-placement="bottom"
						onClick={this.unarchivePlan}>
						<span className="mdi mdi-folder-upload mdi-18" title="Desarquivar plano" > 
							<span id = "menu-levels">
								Desarquivar plano
							</span>
						</span>
					</a>
		         </li>
			</ul>
		);
	},

	renderArchivePlan() {
		return (
			<ul id="level-menu" className="dropdown-menu">
				<li> 
					<Link to={"/plan/"+this.context.planMacro.get("id")+"/edit"} data-placement="bottom">
						<span className="mdi mdi-pencil mdi-18" title="Editar plano"> 
							<span id="menu-levels"> Editar plano</span>
						</span>
					</Link>
				</li>
				<li> 
					<Link
						to={"/plan/"+this.context.planMacro.get("id")+"/details/duplicate"}
						onClick={this.changeVizualization}
						data-placement="bottom">
						<span className="mdi mdi-content-copy mdi-18" title="Duplicar plano"> 
							<span id="menu-levels"> Duplicar plano</span>
						</span>
					</Link>
				</li>
				<li> 
					<a onClick={this.archivePlan} data-placement="bottom">
						<span className="mdi mdi-folder-download mdi-18 deleteIcon" title="Arquivar plano"> 
							<span id="menu-levels"> Arquivar plano </span>
						</span>
					</a>
				</li>
				{this.state.undeletable ? 
					<li> 
						<a data-placement="bottom">
							<span className="mdi mdi-delete disabledIcon mdi-18" title="Não pode ser excluído, pois possui níveis filhos"> 
								<span id="menu-levels"> Excluir plano </span>
							</span>
						</a>
					</li>
					:
					<li> 
						<a onClick={this.deletePlan} data-placement="bottom">
							<span className="mdi mdi-delete mdi-18 deleteIcon" title="Excluir plano"> 
								<span id="menu-levels"> Excluir plano </span>
							</span>
						</a>
					</li>
				}
				

			</ul>	
		);	

	},

	render() {
		return <div>
			<div className="media-list">
				<div className="media-header">
					<h1>{this.context.planMacro.get("name")}&nbsp;{
						(this.context.roles.ADMIN || _.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_MACRO_PERMISSION)) ? 
						(<span className="dropdown">
							<a
								className="dropdown-toggle"
								data-toggle="dropdown"
								aria-haspopup="true"
								aria-expanded="true"
								title="Ações"
							>
								<span className="sr-only">Ações</span>
								<span className="mdi mdi-chevron-down" />
							</a>
							
							{this.state.archived ? this.renderUnarchivePlan() : this.renderArchivePlan()}
							
						</span>	

						):""}
					</h1>
				</div>

				<div className="media-body">
					{this.state.archived ? <span className="fpdi-archived-label">Plano arquivado</span> : ""}
					<div className="row h4">
						<div className="col-sm-6 col-md-4">
							<small>DATA DE INÍCIO</small><br />{this.context.planMacro.get("begin").substr(0,10)}
						</div>
						<div className="col-sm-6 col-md-4">
							<small>DATA DE TÉRMINO</small><br />{this.context.planMacro.get("end").substr(0,10)}
						</div>
					</div>
					<div className="markdown-container" dangerouslySetInnerHTML={{__html: Marked(this.context.planMacro.get("description"))}} />
				</div>
			</div>

			<SummaryTable planMacro={this.context.planMacro} />
		</div>;
	}
});