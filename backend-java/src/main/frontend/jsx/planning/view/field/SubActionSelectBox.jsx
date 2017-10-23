import React from "react";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

import BudgetStore from "forpdi/jsx/planning/store/Budget.jsx";

export default React.createClass({

	getInitialState() {
		return {
			value: this.props.defaultValue || "",
			budgets	: []
		};
	},

	componentDidMount(){
		this.setState({
			value: this.props.defaultValue || ""
		});

		BudgetStore.on("budgetElementRetrivied", (model) => {	
			if (this.isMounted()) {		
			    this.setState({
			    	budgets: model.data
			    });	 
		    } 	
		  });
		  
		
	  	BudgetStore.dispatch({
			action: BudgetStore.ACTION_GET_BUDGET_ELEMENT,
			data: {
				companyId: EnvInfo.company.id 
			}      
      	});
	},

	componentWillReceiveProps(newProps){		
		BudgetStore.dispatch({
			action: BudgetStore.ACTION_GET_BUDGET_ELEMENT,
			data: {
				companyId: EnvInfo.company.id 
			}      
		  });
	},

	componentWillUnmount() {
		BudgetStore.off(null, null, this);
	},

	onChange(){
		var id = this.refs['subaction-select'].value;
		if(id >= 0){
			this.state.value = id;		
		} else {
			this.state.value = "";
		}
	},

	render(){
		return(
			<div className={this.props.className}>
				{this.state.budgets && this.state.budgets.length > 0 ?
					<select ref="subaction-select" onChange={this.onChange} className="subAction-select-box" defaultValue={this.state.value}>
						<option value={-1} key="opt-0" data-placement="right" title={Messages.get("label.select")}>{Messages.get("label.select")}</option>
						{this.state.budgets.map( (budget,idx) => {
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