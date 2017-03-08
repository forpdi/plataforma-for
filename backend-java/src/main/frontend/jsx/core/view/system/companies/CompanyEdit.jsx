
import React from "react";
import {Link} from 'react-router';

import CompanyStore from "forpdi/jsx/core/store/Company.jsx";

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
//import Toastr from 'toastr';


var VerticalForm = Form.VerticalForm;
var errorField = false;

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			loading: !!this.props.params.modelId,
			modelId: this.props.params.modelId,
			model: null,
			fields: !this.props.params.modelId ? this.getFields():null
		};
	},
	getFields(model) {
		return [{
			name: "name",
			type: "text",
			placeholder: "",
			maxLength:255,
			required:true,
			label: "Nome",
			value: model ? model.get("name"):null
		},{
			name: "logo",
			type: "text",
			maxLength:255,
			placeholder: "",
			label: "URL da Logo (Para melhor visualização insira imagem com medidas: 203x100)",
			value: model ? model.get("logo"):null,
			onChange: this.isImg
		},{
			name: 'description',
			type: 'textarea',
			maxLength:10000,
			placeholder: '',
			label: "Descrição",
			value: model ? model.get("description"):null

		},{
			name: 'localization',
			type: 'text',
			maxLength:255,
			placeholder: 'Cidade/Estado',
			required:true,
			label: "Cidade/Estado",
			value: model ? model.get("localization"):null	
		},{
			name: "showDashboard",
			type: "checkbox",
			placeholder: "",
			required:true,
			label: "Habilitar dashboard da comunidade",
			value: model ? model.get("showDashboard") : true,
			onChange: this.fieldsDashboardBoardCommunity
		},{
			name: "showMaturity",
			type: "checkbox",
			placeholder: "",
			required:true,
			label: "Exibir data de vencimento para a comunidade",
			value: model ? model.get("showMaturity") : true
		}];
	},
	componentDidMount() {
		var me = this;
		
		CompanyStore.on("sync", (model) => {
			if (EnvInfo.company) {
				EnvInfo.company.name = model.get("name");
				EnvInfo.company.logo = model.get("logo");
				EnvInfo.company.description = model.get("description");
				EnvInfo.company.showDashboard = model.get("showDashboard");
				EnvInfo.company.showMaturity = model.get("showMaturity");				
			}
			me.context.router.push("/system/companies");				
		}, me);

		CompanyStore.on("retrieve", (model) => {
			me.setState({
				loading: false,
				model: model,
				fields: me.getFields(model)
			});
		}, me);

		if (this.state.loading) {
			CompanyStore.dispatch({
				action: CompanyStore.ACTION_RETRIEVE,
				data: this.state.modelId
			});
		}
	},
	componentWillUnmount() {
		CompanyStore.off(null, null, this);
	},

	fieldsDashboardBoardCommunity () {
		if (this.refs.CompanyEditInstitution.refs.showMaturity.refs["field-showMaturity"].disabled == true) {
			this.refs.CompanyEditInstitution.refs.showMaturity.refs["field-showMaturity"].disabled = false;
		} else {
			this.refs.CompanyEditInstitution.refs.showMaturity.refs["field-showMaturity"].disabled = true;
		}
	},
	isImg() {
		var me = this;
		var file = this.refs.CompanyEditInstitution.refs.logo.refs["field-logo"].value;
		if (file.trim() != ""){
			var img = new Image();
			img.src = file;
			img.onload = function() {
				me.refs.CompanyEditInstitution.refs.logo.refs["field-logo"].className = "form-control";
				errorField = false;
			}
			img.onerror = function() {
				me.context.toastr.addAlertError("Endereço de imagem inválido.");
				errorField = true;
				me.refs.CompanyEditInstitution.refs.logo.refs["field-logo"].className += " borderError";
			}
		} else {
			me.refs.CompanyEditInstitution.refs.logo.refs["field-logo"].className = "form-control";
			errorField = false;
		}
	},
	onSubmit(data) {
		var me = this;
		console.log(data)

		var msg="";

		if ( (data.name.trim() == "") && (data.localization.trim() == "")){
			msg+= "Os campos nome e cidade/estado estão vazios!";
		} else if (data.name.trim() == "")  {
			msg+= "O campo nome está vazio!";
		} else if(data.localization.trim() == ""){
			msg+= "O campo de cidade/estado está vazio!";
		} else if (errorField) {
			msg+= " Endereço de imagem inválido!";
		}

		if(msg!= ""){
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError(msg);
			return;
		}

		if (me.props.params.modelId) {
			me.state.model.set(data);			
			CompanyStore.dispatch({
				action: CompanyStore.ACTION_UPDATE,
				data: me.state.model
			});
			msg = Messages.get("notification.institution.update");
			msg = "Instituição atualizada com sucesso. Talvez seja necessário atualizar a página para ativar as alterações!"
			//Toastr.remove();
			//Toastr.success(Messages.get("notification.institution.update"));
			this.context.toastr.addAlertSuccess(msg);
		} else {
			CompanyStore.dispatch({
				action: CompanyStore.ACTION_CREATE,
				data: data
			});
			//Toastr.remove();
			//Toastr.success(Messages.get("notification.institution.save"));
			//this.context.toastr.addAlertSuccess(Messages.get("notification.institution.save"));
			msg = "Instituição criada com sucesso. Talvez seja necessário atualizar a página para ativar as alterações!"
			this.context.toastr.addAlertSuccess(msg);
		}
	},

	render() {
		return (<div className="col-sm-offset-3 col-sm-6 animated fadeIn">
			{this.state.loading ? <LoadingGauge />:<div>
				<h1>
					{this.state.model ? "Editar instituição":"Adicionar nova instituição"}
				</h1>
				<VerticalForm
					ref = "CompanyEditInstitution"
					onSubmit={this.onSubmit}
					fields={this.state.fields}
					store={CompanyStore}
					submitLabel="Salvar"
				/>
			</div>}
		</div>);
	}
});
