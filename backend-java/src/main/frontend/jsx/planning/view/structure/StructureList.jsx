
import _ from 'underscore';
import React from "react";
import {Link} from 'react-router';

import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Pagination from "forpdi/jsx/core/widget/Pagination.jsx";
import CompanyStore from "forpdi/jsx/core/store/Company.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
//import Toastr from 'toastr';

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			loading: true,
			error: null,
			companies: []
		};
	},
	componentDidMount() {
		var me = this;

		if (EnvInfo.company) {
			StructureStore.on('find', store => {
				if (me.isMounted()) {
					me.setState({
						loading: false,
						models: store.models
					});
				}
			}, me);
			StructureStore.on("fail", (msg) => {
				if (me.isMounted()) {
					me.setState({
						error: msg
					});
				}
			}, this);		
			StructureStore.on('destroy', store => {
				me.refs['paginator'].load(0);
				this.context.toastr.addAlertSuccess(Messages.get("notification.structure.delete"));
			}, me);

			CompanyStore.on("find", (store) => {
				if (me.isMounted()) {
					me.setState({
						companies: store.models
					});
				}
			}, me);

			CompanyStore.dispatch({
				action: CompanyStore.ACTION_FIND,
				data: null
			});
		}
	},
	componentWillUnmount() {
		StructureStore.off(null, null, this);
	},

	closeAlert() {
		if (this.isMounted()) {
			this.setState({
				error: null
			});
		}
	},

	cancelBlockUnblock () {
		Modal.hide();
	},
	
	deleteRecord(model, event) {
		var msg = Messages.get("label.deleteConfirmation") + " " + model.get("name") + "?";
		event.preventDefault();
		
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			StructureStore.dispatch({
				action: StructureStore.ACTION_DESTROY,
				data: model
			});
		},msg,this.cancelBlockUnblock);
	},

	importStructure(evt) {
		var me = this;
		evt.preventDefault();
		var formatsBlocked = "(exe*)";


		Modal.uploadFile(
			Messages.get("label.importEstructure"),
			<p>{Messages.get("label.uploadXml")}</p>,
			StructureStore.url+"/import",
			"xml/*",
			formatsBlocked,
			(response) => {				
				me.refs['paginator'].load(0);
				Modal.hide();
				//Toastr.remove();
				//Toastr.success("Estrutura " +response.data.name + " importada com sucesso.");
				this.context.toastr.addAlertSuccess( Messages.get("label.title.structure") + " " +response.data.name + " " + Messages.get("label.success.Importing"));
			},
			(response) => {			
				Modal.hide();
				this.context.toastr.addAlertError(response.message);
			},
			"xml."
		);

	},

	renderRecords() {
		
		if (!this.state.models || (this.state.models.length <= 0)) {
			return <p><i>{Messages.getEditable("label.noStructureRegistred","fpdi-nav-label")}</i></p>;
		}
		return (<div className="row">
			{this.state.models.map((model, idx) => {
				return (<div key={"company-"+idx} className="col-md-4 col-sm-6">
					<div className="fpdi-card fpdi-card-full">
						<div className="row">
							<div className="fpdi-card-title col-md-6 col-sm-7 col-xs-8">
								<span>{model.get("name")}</span>
							</div>
							<div className="text-right col-md-6 col-sm-5 col-xs-4">
								<Link
									to={"/structures/preview/"+model.get("id")}
									className="mdi mdi-eye"
									title={Messages.get("label.view")}
									data-placement="top"
								/>
								<a
									onClick={this.deleteRecord.bind(this, model)}
									className="mdi mdi-delete marginRight0"
									title={Messages.get("label.delete")}
									data-placement="top"
								/>
							</div>
						</div>
						<p>{model.get("description")}</p>
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
			<h1>{Messages.get("label.structures")}</h1>
			<ul className="fpdi-action-list text-right">
				<a className="btn btn-sm btn-primary" onClick= {this.state.companies.length == 0 ? "" : this.importStructure} disabled = {this.state.companies.length == 0 ? true : false}  
				title= {(this.state.companies.length) == 0 ? Messages.get("label.createStructure") : ""} >
					{/*<span className="mdi mdi-import"
					/>*/}{Messages.getEditable("label.importEstructurePdi","fpdi-nav-label")}
				</a>
			</ul>
			{this.state.error ? (<div className="alert alert-danger animated fadeIn" role="alert">
					<button type="button" className="close" aria-label="Fechar Alerta" onClick={this.closeAlert}>
						<span aria-hidden="true">&times;</span>
					</button>
					{this.state.error}
				</div>)
			:""}


			{EnvInfo.company ? (this.state.loading ? <LoadingGauge />:this.renderRecords())
			: <p><i>{Messages.getEditable("label.createStructure","fpdi-nav-label")}</i></p>}

			<Pagination store={StructureStore} ref="paginator" />
		</div>);
	  }
	});
