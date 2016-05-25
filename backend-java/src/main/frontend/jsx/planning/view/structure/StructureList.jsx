
import _ from 'underscore';
import React from "react";
import {Link} from 'react-router';

import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";

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
		StructureStore.on('sync', store => {
			me.setState({
				loading: false,
				models: store.models
			});
		}, me);
		StructureStore.on("fail", (msg) => {
			me.setState({
				error: msg
			});
		}, this);
	},
	componentWillUnmount() {
		StructureStore.off(null, null, this);
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
			StructureStore.dispatch({
				action: StructureStore.ACTION_DESTROY,
				data: model
			});
		});
	},

	importStructure(evt) {
		var me = this;
		evt.preventDefault();
		Modal.uploadFile(
			"Importar uma estrtutura",
			<p>Faça o upload de um arquivo XML contendo uma estrutura de PDI.</p>,
			StructureStore.url+"/import",
			(response) => {
				console.log(response);
				me.refs['paginator'].load(1);
				Modal.hide();
			}
		);
	},

	renderRecords() {
		if (!this.state.models || (this.state.models.length <= 0)) {
			return <p><i>Nenhuma estrutura cadastrada ainda.</i></p>;
		}
		return (<div className="row">
			{this.state.models.map((model, idx) => {
				return (<div key={"company-"+idx} className="col-md-3 col-sm-4 col-xs-6">
					<div className="fpdi-card">
						<div className="row">
							<div className="fpdi-card-title col-md-8">
								<span>{model.get("name")}</span>
							</div>
							<div className="text-right col-md-4">
								<Link
									to={"/structures/preview/"+model.get("id")}
									className="mdi mdi-eye"
									title="Visualizar"
									data-toggle="tooltip"
									data-placement="top"
								/>
								<Link
									to={"/structures/edit/"+model.get("id")}
									className="mdi mdi-pencil-box"
									title="Editar"
									data-toggle="tooltip"
									data-placement="top"
								/>
								<a
									onClick={this.deleteRecord.bind(this, model)}
									className="mdi mdi-delete"
									title="Excluir"
									data-toggle="tooltip"
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
		_.defer(() => {
			$("[data-toggle=tooltip]").tooltip();
		});
		return (<div className="container-fluid animated fadeIn">
			<h1>{Messages.get("label.structures")}</h1>
			<ul className="fpdi-action-list text-right">
				<a className="btn btn-sm btn-primary" onClick={this.importStructure}>
					<span className="mdi mdi-import"
					/> Importar estrutura
				</a>
				<Link to="/structures/new" className="btn btn-sm btn-primary">
					<span className="mdi mdi-plus"
					/> Adicionar estrutura
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

			<Pagination store={StructureStore} ref="paginator" />
		</div>);
	  }
	});
