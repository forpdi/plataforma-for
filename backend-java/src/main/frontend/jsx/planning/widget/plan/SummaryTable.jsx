
import _ from 'underscore';
import Numeral from "numeral";
import React from "react";
import {Link} from "react-router";

import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";

import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},
	propTypes: {
		planMacro: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			tree: null
		};
	},
	componentDidMount(){
		var me = this;

		PlanStore.on("find", (store, raw, opts) => {			
			var tree = raw.map((plan, index) => {
				return {
					label: plan.name,
					expanded: false,
					expandable: true,
					key: "subplan-"+plan.id,
					model: plan,
					id: plan.id,
					onExpand: this.expandRoot,
					onShrink: this.shrinkRoot
				};
			});

			me.setState({
				subplans: raw,
				tree: tree
			});
		}, me);

		PlanMacroStore.on("recalculation-scheduled", () => {			
			me.context.toastr.addAlertSuccess("Recálculo agendado, em breve os valores estarão atualizados.");
		}, me);
		
		this.refreshPlans(this.props.planMacro.get("id"));
	},

	componentWillUnmount() {
		PlanStore.off(null, null, this);
		PlanMacroStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps) {
	    if (this.props.planMacro.get("id") != newProps.planMacro.get("id")) {
	    	this.refreshPlans(newProps.planMacro.get("id"));
	    }
	},

	refreshPlans(planId) {
		PlanStore.dispatch({
			action: PlanStore.ACTION_FIND,
			data: {
				parentId: planId
			},
			opts: {
				wait: true
			}
		});
	},

	scheduleSummaryCalculation(planId) {
		PlanMacroStore.dispatch({
			action: PlanMacroStore.ACTION_SCHEDULE_RECALCULATION,
			data: this.props.planMacro.get("id"),
			opts: {
				wait: true
			}
		});
	},

	renderYearCells(rowData) {
		var cells = [];
		for (var month = 0; month < 12; month++) {
			cells.push(<td key={"month-cell-"+month}>
				<div className="circle green">100%</div>
			</td>);
		}
		return (<table>
			<tbody>
				<tr>
					{cells}
				</tr>
			</tbody>
		</table>);
	},

	renderTableBody() {
		return (<tbody>
			{this.state.tree.map((rowSpec, index) => {
				//console.log(rowSpec.model);
				var achieved = !rowSpec.model.performance ? null:Numeral(rowSpec.model.performance);
				var color = "";
				if (!achieved)
					color = "gray";
				else if (achieved.value() < rowSpec.model.minimumAverage)
					color = "red";
				else if (achieved.value() < 100.0)
					color = "yellow";
				else if (achieved.value() < rowSpec.model.maximumAverage)
					color = "green";
				else
					color = "blue";
				return (<tr key={"data-row-"+index}>
					<td>{rowSpec.model.name}</td>
					<td className="text-center">
						{!achieved ? "-":(
							<div className={"circle "+color}>
								{achieved.format("0,0.00")}%
							</div>
						)}
					</td>
					<td className="summary-table-calendar-cell">
						{this.renderYearCells(rowSpec.model)}
					</td>
				</tr>);
			})}
		</tbody>);
	},

	render() {
		if (!this.state.tree) {
			return <div />;
		}
		return (
		<div className="summary-table">
			<div className="summary-table-header">
				<button onClick={this.scheduleSummaryCalculation} className="btn btn-primary">Agendar Recálculo</button>
				Tabela de Resumo - {this.props.planMacro.get("name")}
			}
			</div>
			<table>
				<thead>
					<tr>
						<th></th>
						<th></th>
						<th className="text-center">Desempenho por mês</th>
					</tr>
					<tr>
						<th>Nível</th>
						<th className="text-center" style={{width: '100px', maxWidth: '100px'}}>Rendimento</th>
						<td className="summary-table-calendar-cell">
							<table>
								<tbody>
									<tr>
										<td>Jan</td>
										<td>Fev</td>
										<td>Mar</td>
										<td>Abr</td>
										<td>Mai</td>
										<td>Jun</td>
										<td>Jul</td>
										<td>Ago</td>
										<td>Set</td>
										<td>Out</td>
										<td>Nov</td>
										<td>Dez</td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
				</thead>
				{this.renderTableBody()}
			</table>
		</div>);
	}
});
