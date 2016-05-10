
import React from "react";
import {Link} from 'react-router';

import UserStore from "forpdi/jsx/core/store/User.jsx";

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Pagination from "forpdi/jsx/core/widget/Pagination.jsx";

import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
	getInitialState() {
		return {
			loading: true,
			error: null
		};
	},
	componentDidMount() {
		var me = this;
		UserStore.on('sync', store => {
			me.setState({
				loading: false,
				models: store.models
			});
		}, me);
		UserStore.on("fail", (msg) => {
			me.setState({
				error: msg
			});
		}, me);
	},
	componentWillUnmount() {
		UserStore.off(null, null, this);
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
			UserStore.dispatch({
				action: UserStore.ACTION_DESTROY,
				data: model
			});
		});
	},

	renderRecords() {
		if (!this.state.models || (this.state.models.length <= 0)) {
			return <p><i>Nenhum usuário cadastrado ainda.</i></p>;
		}
		return (<div className="fpdi-card">
			<div className="row hidden-xs">
					<div className="col-sm-4"><b>{Messages.get("label.name")}</b></div>
					<div className="col-sm-3"><b>{Messages.get("label.email")}</b></div>
					<div className="col-sm-3"><b>{Messages.get("label.cpf")}</b></div>
					<div className="col-sm-2"></div>
			</div>
			{this.state.models.map((model, idx) => {
				return (<div key={"user-"+idx} className="row">
					<div className="col-sm-4">{model.get("name")}</div>
					<div className="col-sm-3">{model.get("email")}</div>
					<div className="col-sm-3">{model.get("cpf")}</div>
					<div className="col-sm-2"></div>
				</div>);
			})}
		</div>);
	},

	render() {
		if (this.props.children) {
			return this.props.children;
		}
		return (<div className="container-fluid animated fadeIn">
			<h1>{Messages.get("label.users")}</h1>
			<ul className="fpdi-action-list text-right">
				<Link to="/users/new" className="btn btn-sm btn-primary">
					<span className="mdi mdi-plus"
					/> Adicionar usuário
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

			<Pagination store={UserStore} />
		</div>);
	  }
	});
