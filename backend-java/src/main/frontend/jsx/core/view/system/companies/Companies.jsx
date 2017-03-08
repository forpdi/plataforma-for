
import React from "react";
import {Link} from 'react-router';
import CompanyStore from "forpdi/jsx/core/store/Company.jsx";
import string from 'string';
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Pagination from "forpdi/jsx/core/widget/Pagination.jsx";

import Messages from "forpdi/jsx/core/util/Messages.jsx";
//import Toastr from 'toastr';

import Logo from 'forpdi/img/logo.png';

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			loading: true,
			error: null
		};
	},
	componentDidMount() {
		var me = this;
		CompanyStore.on('find', store => {
			me.setState({
				loading: false,
				models: store.models
			});
		}, me);
		CompanyStore.on('destroy', store => {
			CompanyStore.dispatch({
				action: CompanyStore.ACTION_FIND,
				data: null
			});
			//Toastr.remove();
			//Toastr.success(Messages.get("notification.institution.delete"));
			this.context.toastr.addAlertSuccess(Messages.get("notification.institution.delete"));
		}, me);
		CompanyStore.on("fail", (msg) => {
			me.setState({
				error: msg
			});
		}, me);
	},
	componentWillUnmount() {
		CompanyStore.off(null, null, this);
	},

	closeAlert() {
		this.setState({
			error: null
		});
	},

	cancelBlockUnblock () {
		Modal.hide();
	},
	
	deleteRecord(model, event) {
		var msg = "Você tem certeza que deseja excluir essa instituição?";
		event.preventDefault();
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			CompanyStore.dispatch({
				action: CompanyStore.ACTION_DESTROY,
				data: model
			});
		},msg,this.cancelBlockUnblock);
	},

	renderRecords() {
		if (!this.state.models || (this.state.models.length <= 0)) {
			return <p><i>Nenhuma instituição cadastrada ainda.</i></p>;
		}
		return (<div className="row">
			{this.state.models.map((model, idx) => {
				return (<div key={"company-"+idx} className="col-md-3 col-sm-4 col-xs-6">
					<div className="fpdi-card fpdi-card-full fpdi-card-company">
						<div className="row">
							<div className="fpdi-card-title col-md-8">
								<span title = {model.get("name")} className = "cursorPointer"> {(model.get("name").length>20)?(string(model.get("name")).trim().substr(0,20).concat("...").toString()):(model.get("name"))} </span>
							</div>
							<div className="text-right col-md-4">
								<Link to={"/system/companies/edit/"+model.get("id")} className="mdi mdi-pencil-box" title="Editar" />
								<a onClick={this.deleteRecord.bind(this, model)} className="mdi mdi-delete" title="Excluir" />
							</div>
						</div>
						<div className="fpdi-company-logo" style={{backgroundImage: 'url('+
						((model.get("logo")!='')?(model.get("logo")):(Logo))+")"}} />
						
						
					</div>
				</div>);
			})}
		</div>);
	},

	render() {
		if (this.props.children) {
			return this.props.children;
		}
		return (<div className="container-fluid animated fadeIn">
			<h1>{Messages.get("label.institutions")}</h1>
			<ul className="fpdi-action-list text-right">
				<Link to="/system/companies/new" className="btn btn-sm btn-primary">
					{/*<span className="mdi mdi-plus"
					/>*/} Adicionar instituição
				</Link>
			</ul>
			{this.state.error ? (<div className="alert alert-danger animated fadeIn" role="alert">
					<button type="button" className="close" aria-label="Fechar Alerta" onClick={this.closeAlert}>
						<span aria-hidden="true">&times;</span>
					</button>
					{this.state.error}
				</div>)
			:""}

			{this.state.loading ? <LoadingGauge />:this.renderRecords()}

			<Pagination store={CompanyStore} />
		</div>);
	  }
	});
