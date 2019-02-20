
import React from "react";

import MainMenu from "forpdi/jsx_forrisco/MainMenu.jsx";

export default React.createClass({
	render() {
		return (
			<div className="fpdi-app-body">
				<MainMenu {...this.props} />
				<div className="fpdi-app-content">
					{this.props.children}
				</div>
			</div>
		);
	}
});
