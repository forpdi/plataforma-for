import React from "react";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

import BudgetStore from "forpdi/jsx/planning/store/Budget.jsx";

export default React.createClass({

	getInitialState() {
		return {
			value: this.props.defaultValue || "",
		};
	},

	onChange(){
		var id = this.refs['subaction-select'].value;
		const value = id >= 0 ? id : '';
		this.setState({
			value,
		});
	},

	render(){
		return(
			<div className={this.props.className}>
				{this.props.budgets && this.props.budgets.length > 0 ?
					<select ref="subaction-select" onChange={this.onChange} className="subAction-select-box" defaultValue={this.state.value}>
						<option value={-1} key="opt-0" data-placement="right" title={Messages.get("label.select")}>{Messages.get("label.select")}</option>
						{this.props.budgets.map( (budget,idx) => {
							return(
								<option value={budget.id} key={"opt-"+idx} data-placement="right" title={budget.subAction}>
									{budget.subAction}
								</option>
							);
						})}
					</select>
				: "Nenhum elemento orçamentário cadastrado."}
			</div>
		);
	}
});
