import React from "react";
import queryString from 'query-string';

/*import ForPDIChart from "forpdi/jsx/core/widget/ForPDIChart.jsx"
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import string from 'string';
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
*/

var numeral = require('numeral');

export default React.createClass({

	getInitialState() {
		return {


		}
	},
render(){
  let params = queryString.parse(this.props.location.search);
  console.log("//TODO listar indicentes",params);

	return <div>

	</div>
	}
});
