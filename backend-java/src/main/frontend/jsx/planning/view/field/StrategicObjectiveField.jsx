import React from "react";
import {Link} from 'react-router';
import _ from 'underscore';
import string from 'string';

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
		accessLevel: React.PropTypes.number.isRequired,
        accessLevels: React.PropTypes.object.isRequired,
        permissions: React.PropTypes.array.isRequired,
        roles: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {			
			
		};
	},

	componentDidMount()	{
		
	},

	componentWillUnmount() {
		
	},

	render() {
		if (this.props.fieldDef.strategicObjectives != undefined && this.props.fieldDef.strategicObjectives.length > 0) {
			return (
				<table className="budget-field-table table">	
					<thead>
						<tr>
							<th className="width30percent">Plano de metas</th>
							<th>Objetivo</th>
							<th>Perspectiva do BSC</th>
						</tr>
					</thead>
					<tbody>
						{this.props.strategicObjectivesPlansParam == (-1) ?
							this.props.fieldDef.strategicObjectives.map((model, idx) => {
								return(
									<tr key={"tableField-"+idx}>
										<td id={'tableValue'+model.id+'-'+idx}>
											{model.plan.name}
										</td>
										<td id={'tableValue'+model.id+'-'+idx}>
											<Link
												to={"/plan/"+model.plan.parent.id+"/details/subplan/level/"+model.id}
												activeClassName="active"
												title="ver mais"
												>
												{model.name}
											</Link>
										</td>
										{model.level.attributes.length > 0 ?
											model.level.attributes.map((mod, id) => {
												if (mod.bscField) {
													return(
														<td key={"tableValue-"+id} id={'tableValue'+model.id+'-'+id}>
															{mod.attributeInstances[idx] ? mod.attributeInstances[idx].value : ""}
														</td>
													);
												}
											})
										: <td/>}
									</tr>
								);
							})
							:
							this.props.fieldDef.strategicObjectives.map((model, idx) => {
							if(model.plan.id == this.props.strategicObjectivesPlansParam){
								return(
									<tr key={"tableField-"+idx}>
										<td id={'tableValue'+model.id+'-'+idx}>{model.plan.name}</td>
										<td id={'tableValue'+model.id+'-'+idx}>
											<Link
												to={"/plan/"+model.plan.parent.id+"/details/subplan/level/"+model.id}
												activeClassName="active"
												title="ver mais"
												>
												{model.name}
											</Link>
										</td>
										{model.level.attributes.length > 0 ?
											model.level.attributes.map((mod, id) => {
												if (mod.bscField) {
													return(
														<td key={"tableValue-"+id} id={'tableValue'+model.id+'-'+id}>
															{mod.attributeInstances[idx] ? mod.attributeInstances[idx].value : ""}
														</td>
													);
												}
											})
										: <td/>}
									</tr>
								);
							}
							})
						}
					</tbody>				
				</table>
			);
		} else {
			return(
				<div className='table-empty-sons'>Não possui nenhum objetivo</div>
			);
		}
	}

});