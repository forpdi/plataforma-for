
import React from "react";
import { Link } from 'react-router';
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

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

	render() {
		return (<div className="fpdi-tabs">
			<ul className="fpdi-tabs-nav" role="tablist">
			    <Link role="tab" to="/system/general" activeClassName="active">
			   		Geral
			    </Link>
			    <Link role="tab" to="/system/companies" activeClassName="active">
			   		Instituições
			    </Link>
			    <Link role="tab" to="/system/domains" activeClassName="active">
			   		Domínios
			    </Link>
			  </ul>
			  <div className="fpdi-tabs-content container-fluid">
					{this.props.children}
			  </div>
		</div>);
	  }
	});
