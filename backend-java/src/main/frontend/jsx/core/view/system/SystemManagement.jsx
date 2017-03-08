import React from "react";
import { Link } from 'react-router';
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import _ from 'underscore';

import Messages from "forpdi/jsx/core/util/Messages.jsx";

import Modal from "forpdi/jsx/core/widget/Modal.jsx";

import CompanyStore from "forpdi/jsx/core/store/Company.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";

export default React.createClass({
	getInitialState() {
		return {
			companies: null,
			loading: true
		};
	},
	componentDidMount() {
		var me = this;
		CompanyStore.on("find", (store) => {
			if (me.isMounted()) {
				me.setState({
					companies: store.models
				});
			}
			me.updateLoadingState();
		}, me);
		CompanyStore.dispatch({
			action: CompanyStore.ACTION_FIND,
			data: null
		});
		CompanyStore.dispatch({
			action: CompanyStore.ACTION_FIND_THEMES,
			data: null
		});
		
	},
	componentWillUnmount() {
		
	},
	updateLoadingState() {
		if (this.isMounted()) {
			this.setState({
				loading:
					!this.state.companies
			});
		}
	},

	render() {
		return (<div className="fpdi-tabs">
			{this.state.loading ? <LoadingGauge />:<div>
			<ul className="fpdi-tabs-nav" role="tablist">
			    {(false)?(
				 <Link role="tab" to="/system/general" activeClassName="active"> 
			   		{Messages.get("label.general")} 
			   	</Link>):("")
			    }


			    <Link role="tab" to="/system/companies" activeClassName="active">
			   		{Messages.get("label.institutions")}
			    </Link>
			    {this.state.companies.length <= 0 ? 
				    <div title="Você não tem nenhuma instituição cadastrada"><span className="cursorDefault" >
				    	<Link role="tab" to={this.props.location.pathname} activeClassName="tabItemDisable"  >
				   		{	Messages.get("label.domains")}
				    	</Link></span></div>
				    :
				    <Link role="tab" to="/system/domains" activeClassName="active">
				   		{Messages.get("label.domains")}
				    </Link>
				}
			  </ul>
			  <div className="fpdi-tabs-content container-fluid">
					{this.props.children}
			  </div>
			</div>}
		</div>);
	  }
	});
