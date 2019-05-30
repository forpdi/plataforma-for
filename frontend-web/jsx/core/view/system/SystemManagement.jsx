import React from "react";
import { Link } from 'react-router';
import _ from 'underscore';

import Messages from "forpdi/jsx/core/util/Messages.jsx";
import CompanyStore from "forpdi/jsx/core/store/Company.jsx";

export default React.createClass({
	getInitialState() {
		return {
			hasCompany: false,
		};
	},

	componentDidMount() {
		var me = this;
		CompanyStore.on('companies-listed', (store) => {
			this.setState({
				hasCompany: !!store.data.length,
			});
		}, me);
		CompanyStore.dispatch({
			action: CompanyStore.ACTION_LIST_COMPANIES,
		 	data: {
				page: 1,
		 	},
	  	});
		CompanyStore.dispatch({
			action: CompanyStore.ACTION_FIND_THEMES,
			data: null
		});

	},

	componentWillUnmount() {
		CompanyStore.off(this, this, null);
	},

	render() {
		return (
			<div className="fpdi-tabs">
				{
					<div>
						<ul className="fpdi-tabs-nav" role="tablist">
							<Link role="tab" to="/system/companies" activeClassName="active">
								{Messages.getEditable("label.institutions", "fpdi-nav-label")}
							</Link>
							{
								!this.state.hasCompany
								?
								<div title={Messages.get("label.noCompanyRegistered")}>
									<span className="cursorDefault">
										<Link role="tab" to={this.props.location.pathname} activeClassName="tabItemDisable">
											{Messages.getEditable("label.domains", "fpdi-nav-label")}
										</Link>
									</span>
								</div>
								:
								<Link role="tab" to="/system/domains" activeClassName="active">
									{Messages.getEditable("label.domains", "fpdi-nav-label")}
								</Link>
							}
						</ul>
						<div className="fpdi-tabs-content container-fluid">
							{this.props.children}
						</div>
					</div>
				}
			</div>
		);
	}
});
