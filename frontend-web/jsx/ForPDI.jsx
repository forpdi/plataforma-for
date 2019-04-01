
import React from "react";

import MainMenu from "forpdi/jsx/MainMenu.jsx";

import Helmet from 'react-helmet';

export default React.createClass({
	render() {
		return (
			<div className="fpdi-app-body">

			{!this.props.location.pathname.includes("/users")
			&& !this.props.location.pathname.includes("/system")
			&& !this.props.location.pathname.includes("/structures")
			?
				<MainMenu {...this.props} />
			: ""}
				<div className="fpdi-app-content">
					<Helmet>
						<title>ForPDI</title>
						<link rel="icon" type="image/x-icon" href="favicon.ico"></link>
					</Helmet>
					{this.props.children}
				</div>
			</div>
		);
	}
});

