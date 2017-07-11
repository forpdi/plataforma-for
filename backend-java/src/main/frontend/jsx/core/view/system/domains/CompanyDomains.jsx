
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
			error: null,
			page: 1,
			total: null
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

		CompanyDomainStore.on('remove', store => {
			me.findDomains(1);
			this.context.toastr.addAlertSuccess(Messages.get("notification.domain.delete"));
		}, me);

		CompanyDomainStore.on('domains-listed', (store, data) => {
			var list;
			if (data.page == 1) {
				list = store.data;
			} else {
				list = this.state.models;
				for (var i=0; i<store.data.length; i++) {
					list.push(store.data[i]);
				}
			}
			if (me.isMounted()) {
				me.setState({
					loading: false,
					models: list,
					page: this.state.page+1,
					total: store.total
				});
			}
			me.forceUpdate();
		}, me);

		CompanyDomainStore.on('sync', model => {
			me.findDomains(1);
			if (me.isMounted()) {
				me.setState({
					page: 1
				})
			}
		}, me);

		CompanyDomainStore.on("fail", (msg) => {
			if (me.isMounted()) {
				me.setState({
					error: msg
				});
			}
		}, this);

		me.findDomains(1);
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
		var msg = Messages.get("label.deleteConfirmation") + model.host + "?";
		event.preventDefault();
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			CompanyDomainStore.dispatch({
				action: CompanyDomainStore.ACTION_REMOVE_DOMAIN,
				data: model.id
			});
		},msg,this.cancelBlockUnblock);
	},

	findDomains(page) {
		CompanyDomainStore.dispatch({
   			action: CompanyDomainStore.ACTION_LIST_DOMAINS,
			data: {
				page: page
			}
 		});
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
								<span>{model.host}</span>
							</div>
							<div className="text-right col-md-4">
								<Link to={"/system/domains/edit/"+model.id} className="mdi mdi-pencil-box" title="Editar" />
								<a onClick={this.deleteRecord.bind(this, model)} className="mdi mdi-delete marginRight0" title="Excluir" />
							</div>
						</div>
						<div className="fpdi-company-logo" style={{backgroundImage: 'url('+
							((model.company && model.company.logo!='')?(model.company.logo):(Logo))
							+")"}} />
					</div>
				</div>);
			})}
			<br /><br /><br />
			{this.state.total && this.state.models && this.state.models.length < this.state.total ?
				<div className="showMore"><a onClick={this.findDomains.bind(this, this.state.page)}>Ver mais</a></div>
			: ""}
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
			{/*<Pagination store={CompanyDomainStore} />*/}
		</div>);
	  }
	});
