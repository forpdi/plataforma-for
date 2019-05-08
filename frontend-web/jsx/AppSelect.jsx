import React from "react";
import { Link } from 'react-router'

import ForpdiLogo from 'forpdi/img/logo.png';
import ForriscoLogo from 'forpdi/img/forrisco-logo.png';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

const AppSelect = () => (
	<div className="app-select">
		<div className="app-select--card">
			<img alt={Messages.get("label.forRiscoLogo")} src={ForpdiLogo} />
			<Link to="/home" className="app-select--card-bt app-select--forpdi">
				{Messages.getEditable('label.access')}
			</Link>
		</div>
		<div className="app-select--card">
			<img alt={Messages.get("label.forRiscoLogo")} src={ForriscoLogo} />
			<Link to="/forrisco/home" className="app-select--card-bt app-select--forrisco">
				{Messages.getEditable('label.access')}
			</Link>
		</div>
	</div>
);

export default AppSelect;
