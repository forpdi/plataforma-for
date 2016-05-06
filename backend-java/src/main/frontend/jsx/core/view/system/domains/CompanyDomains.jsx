
import React from "react";
import {Link} from 'react-router';

import CompanyDomainStore from "forpdi/jsx/core/store/CompanyDomain.jsx";

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Pagination from "forpdi/jsx/core/widget/Pagination.jsx";

export default React.createClass({
	getInitialState() {
		return {
			loading: true,
			error: null
		};
	},
	componentDidMount() {
		var me = this;
		CompanyDomainStore.on('sync', store => {
			me.setState({
				loading: false,
				models: store.models
			});
		}, me);
		CompanyDomainStore.on("fail", (msg) => {
			me.setState({
				error: msg
			});
		}, this);
	},
	componentWillUnmount() {
		CompanyDomainStore.off(null, null, this);
	},

	closeAlert() {
		this.setState({
			error: null
		});
	},
	deleteRecord(model, event) {
		event.preventDefault();
		Modal.deleteConfirm(() => {
			Modal.hide();
			CompanyDomainStore.dispatch({
				action: CompanyDomainStore.ACTION_DESTROY,
				data: model
			});
		});
	},

	renderRecords() {
		if (!this.state.models || (this.state.models.length <= 0)) {
			return <p><i>Nenhum domínio cadastrada ainda.</i></p>;
		}
		return (<div className="row">
			{this.state.models.map((model, idx) => {
				return (<div key={"companydomain-"+idx} className="col-md-3 col-sm-4 col-xs-6">
					<div className="fpdi-card fpdi-card-company">
						<div className="row">
							<div className="fpdi-card-title col-md-8">
								<span>{model.get("host")}</span>
							</div>
							<div className="text-right col-md-4">
								<Link to={"/system/domains/edit/"+model.get("id")} className="mdi mdi-pencil-box" title="Editar" />
								<a onClick={this.deleteRecord.bind(this, model)} className="mdi mdi-delete" title="Excluir" />
							</div>
						</div>
						<div className="fpdi-company-logo" style={{backgroundImage: 'url('+model.get("company").logo+")"}} />
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
			<h1>Domínios</h1>
			<ul className="fpdi-action-list text-right">
				<Link to="/system/domains/new" className="btn btn-sm btn-primary">
					<span className="mdi mdi-plus"
					/> Adicionar domínio
				</Link>
			</ul>
			{this.state.error ?
				(<div className="alert alert-danger animated fadeIn" role="alert">
					<button type="button" className="close" aria-label="Fechar Alerta" onClick={this.closeAlert}>
						<span aria-hidden="true">&times;</span>
					</button>
					{this.state.error}
				</div>)
			:""}

			{this.state.loading ? <LoadingGauge />:this.renderRecords()}

			<Pagination store={CompanyDomainStore} />
		</div>);
	  }
	});
