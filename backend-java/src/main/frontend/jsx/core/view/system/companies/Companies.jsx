
import React from "react";

import Modal from "forpdi/jsx/core/widget/Modal.jsx";

export default React.createClass({
	getInitialState() {
		return {
		};
	},
	componentDidMount() {
		var me = this;
		
	},
	componentWillUnmount() {
		
	},

	onTabClick(tab) {
		location.assign("#/system/"+tab);
	},

	render() {
		return (<div className="container-fluid animated fadeIn">
			<h1>Instituições</h1>
		</div>);
	  }
	});
