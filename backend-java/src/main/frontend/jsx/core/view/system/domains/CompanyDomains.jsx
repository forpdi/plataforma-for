
import React from "react";
import {Link} from 'react-router';

import CompanyDomainStore from "forpdi/jsx/core/store/CompanyDomain.jsx";

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
		CompanyDomainStore.on('find', store => {
			if (me.isMounted()) {
				me.setState({
					loading: false,
					models: store.models
				});
			}
		}, me);

		CompanyDomainStore.on('destroy', store => {
			CompanyDomainStore.dispatch({
				action: CompanyDomainStore.ACTION_FIND,
				data: null
			});

			//Toastr.remove();
			//Toastr.success(Messages.get("notification.domain.delete"));
			this.context.toastr.addAlertSuccess(Messages.get("notification.domain.delete"));
		}, me);

		CompanyDomainStore.on("fail", (msg) => {
			if (me.isMounted()) {
				me.setState({
					error: msg
				});
			}
		}, this);
	},
	componentWillUnmount() {
		CompanyDomainStore.off(null, null, this);
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
		var msg = "Você tem certeza que deseja excluir " + model.get("host") + "?";
		event.preventDefault();
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			CompanyDomainStore.dispatch({
				action: CompanyDomainStore.ACTION_DESTROY,
				data: model
			});
		},msg,this.cancelBlockUnblock);
	},

	renderRecords() {
		if (!this.state.models || (this.state.models.length <= 0)) {
			return <p><i>Nenhum domínio cadastrado ainda.</i></p>;
		}
		return (<div className="row">
			{this.state.models.map((model, idx) => {
				return (<div key={"companydomain-"+idx} className="col-md-3 col-sm-4 col-xs-6">
					<div className="fpdi-card fpdi-card-full fpdi-card-company">
						<div className="row">
							<div className="fpdi-card-title col-md-8">
								<span>{model.get("host")}</span>
							</div>
							<div className="text-right col-md-4">
								<Link to={"/system/domains/edit/"+model.get("id")} className="mdi mdi-pencil-box" title="Editar" />
								<a onClick={this.deleteRecord.bind(this, model)} className="mdi mdi-delete marginRight0" title="Excluir" />
							</div>
						</div>
						<div className="fpdi-company-logo" style={{backgroundImage: 'url('+
							((model.get("company").logo!='')?(model.get("company").logo):(Logo))
							+")"}} />
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
			<h1>{Messages.get("label.domains")}</h1>
			<ul className="fpdi-action-list text-right">
				<Link to="/system/domains/new" className="btn btn-sm btn-primary">
					{/*<span className="mdi mdi-plus"
					/>*/} Adicionar domínio
				</Link>
			</ul>

			{this.state.loading ? <LoadingGauge />:this.renderRecords()}
			<Pagination store={CompanyDomainStore} />
		</div>);
	  }
	});
