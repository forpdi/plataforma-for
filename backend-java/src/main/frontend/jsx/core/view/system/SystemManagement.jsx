
import React from "react";
import { Link } from 'react-router';
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

import Messages from "forpdi/jsx/core/util/Messages.jsx";

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
			   		{Messages.get("label.general")}
			    </Link>
			    <Link role="tab" to="/system/companies" activeClassName="active">
			   		{Messages.get("label.institutions")}
			    </Link>
			    <Link role="tab" to="/system/domains" activeClassName="active">
			   		{Messages.get("label.domains")}
			    </Link>
			  </ul>
			  <div className="fpdi-tabs-content container-fluid">
					{this.props.children}
			  </div>
		</div>);
	  }
	});
