
import React from "react";
import {Link} from 'react-router';

import CompanyStore from "forpdi/jsx/core/store/Company.jsx";

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Pagination from "forpdi/jsx/core/widget/Pagination.jsx";

export default React.createClass({
	getInitialState() {
		return {
			loading: true
		};
	},
	componentDidMount() {
		var me = this;
		CompanyStore.on('sync', store => {
			this.setState({loading: false});
		}, me);
	},
	componentWillUnmount() {
		CompanyStore.off(null, null, this);
	},

	render() {
		if (this.props.children) {
			return this.props.children;
		}
		return (<div className="container-fluid animated fadeIn">
			<h1>Instituições</h1>
			<div className="text-right">
				<Link to="/system/companies/new" className="btn btn-sm btn-primary">
					<span className="mdi mdi-plus"
					/> Adicionar instituição
				</Link>
			</div>
			{this.state.loading ? <LoadingGauge />:
				<div>
					
				</div>
			}
			<Pagination store={CompanyStore} />
		</div>);
	  }
	});
