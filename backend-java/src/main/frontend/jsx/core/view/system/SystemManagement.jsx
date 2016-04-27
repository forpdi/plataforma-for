
import React from "react";
import { Router, Route, Link } from 'react-router';
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

import NotFound from 'forpdi/jsx/core/view/NotFound.jsx';
import Companies from 'forpdi/jsx/core/view/system/companies/Companies.jsx';

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
		return (<div className="fpdi-tabs">
			<ul className="fpdi-tabs-nav" role="tablist">
			    <li role="tab" 
			    	onClick={this.onTabClick.bind(this, "general")}
			    	className={this.props.params.tab == "general" ? "active":""}>
			   		Geral
			    </li>
			    <li role="tab"
			    	onClick={this.onTabClick.bind(this, "companies")}
			    	className={this.props.params.tab == "companies" ? "active":""}>
			    	Instituições
			    </li>
			    <li role="tab"
			    	onClick={this.onTabClick.bind(this, "domains")}
			    	className={this.props.params.tab == "domains" ? "active":""}>
			    	Domínios
			    </li>
			  </ul>
			  <div className="fpdi-tabs-content container-fluid">
					<Router>
						<Route path="/system/companies" component={Companies} />

						<Route path="*" component={NotFound} />
					</Router>
			  </div>
		</div>);
	  }
	});
