
import _ from 'underscore';
import Numeral from "numeral";
import React from "react";
import {Link} from "react-router";

import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";

import LoadingImage from 'forpdi/img/loading2.gif';
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
					indent: 0,
					key: "subplan-"+plan.id,
					plan: plan,
					performance: plan.performance,
					minimum: plan.minimumAverage,
					maximum: plan.maximumAverage,
					parents: []
				};
			});

			me.setState({
				subplans: raw,
				tree: tree
			});
		}, me);

		StructureStore.on("retrieve-level-instance-performance", (models, parent) =>{
			if (!models || (models.length <= 0)) {
				parent.expandable = false;
			} else {
				var insertionPoint = 1 + this.state.tree.findIndex((spec) => {
					return (spec.key === parent.key);
				});
	            _.each(models, (level, index) => {
					me.state.tree.splice(insertionPoint, 0, {
						label: level.name,
						expanded: false,
						expandable: true,
						indent: parent.indent+1,
						key: "level-"+level.id,
						plan: parent.plan,
						performance: level.levelValue,
						minimum: level.levelMinimum,
						maximum: level.levelMaximum,
						level: level.id,
						parentKey: parent.key,
						parentLevel: parent.level,
						parents: parent.parents.concat([parent.key])
					});
				});
				parent.expanded = true;
			}
			parent.loading = false;
			me.forceUpdate();
        }, me);

		PlanMacroStore.on("recalculation-scheduled", () => {			
			me.context.toastr.addAlertSuccess("Recálculo agendado, em breve os valores estarão atualizados.");
		}, me);
		
		this.refreshPlans(this.props.planMacro.get("id"));
	},

	componentWillUnmount() {
		PlanStore.off(null, null, this);
		PlanMacroStore.off(null, null, this);
		StructureStore.off(null, null, this);
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

	tweakExpansion(spec, event) {
		event && event.preventDefault();
		if (!spec.expanded) {
			spec.loading = true;
			this.forceUpdate();
			StructureStore.dispatch({
                action: StructureStore.ACTION_RETRIEVE_LEVEL_INSTANCE_PERFORMANCE,
                data: {
                    planId: spec.plan.id,
                    parentId: !spec.level ? 0:spec.level
                },
                opts: spec
            });
		} else {
			spec.expanded = false;
			this.setState({
				tree: this.state.tree.filter((row) => {
					return (row.parents.indexOf(spec.key) < 0);
				})
			});
		}
	},

	renderYearCells(rowData) {
		var cells = [];
		for (var month = 0; month < 12; month++) {
			/*cells.push(<td key={"month-cell-"+month}>
				<div className="circle green">100%</div>
			</td>);*/
			cells.push(<td key={"month-cell-"+month}>
				-
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
				var achieved = !rowSpec.performance ? null:Numeral(rowSpec.performance);
				var color = "";
				if (!achieved)
					color = "gray";
				else if (achieved.value() < rowSpec.minimum)
					color = "red";
				else if (achieved.value() < 100.0)
					color = "yellow";
				else if (achieved.value() < rowSpec.maximum)
					color = "green";
				else
					color = "blue";
				return (<tr key={"data-row-"+index}>
					<td style={{"paddingLeft": ""+(rowSpec.indent*20 + 5)+"px"}}>
						{rowSpec.loading ? <img src={LoadingImage} style={{"height": "12px"}} />:(rowSpec.expandable ? (
							<a className={rowSpec.expanded ? "mdi mdi-chevron-down":"mdi mdi-chevron-right"}
								onClick={this.tweakExpansion.bind(this, rowSpec)}>&nbsp;</a>
						):"")}
						<span>{rowSpec.label}</span>
					</td>
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
