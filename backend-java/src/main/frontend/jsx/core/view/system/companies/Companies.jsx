
import React from "react";

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

	onTabClick(tab) {
		location.assign("#/system/"+tab);
	},

	render() {
		return (<div className="container-fluid animated fadeIn">
			<h1>Instituições</h1>
			{this.state.loading ? <LoadingGauge />:
				<div>
					
				</div>
			}
			<Pagination store={CompanyStore} />
		</div>);
	  }
	});
