
import React from "react";

import MainMenu from "forpdi/jsx_forrisco/MainMenu.jsx";

import Helmet from 'react-helmet';

export default React.createClass({
	render() {
		return (
			<div className="fpdi-app-body">
				<MainMenu {...this.props} />
				<div className="fpdi-app-content">
					<Helmet>
						<title>ForRisco</title>
						<link rel="icon" type="image/x-icon" href="favicon2.ico"></link>
					</Helmet>
					{this.props.children}
				</div>
			</div>
		);
	}
});
