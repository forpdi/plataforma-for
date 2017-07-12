import React from "react";
import {Link} from 'react-router';

import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import _ from 'underscore';
import Messages from "forpdi/jsx/core/util/Messages.jsx";


var initialIndicatorsSelected = [];
var initialIndicatorsList = [];

var percentages = [];

export default React.createClass({

	getDefaultProps() {
		return {
			indicatorsList: [],
			calculationType: 0,
			visualization: false
		}
	},
	
	getInitialState() {				
		return {
			loading: true,
			total: 0,
			selectedIndicators: []
		};
	},

	componentDidMount() {
		var me = this;
		var total = 0;		
		this.props.indicatorsList.map((ind) => {
			total += ind.percentage;
			this.state.selectedIndicators.push({
				id: ind.aggregate.id,
				name: ind.aggregate.name,
				percentage: ind.percentage,
				deleted: ind.deleted
			});
		});		

		me.setState({
			calculationType: this.props.calculationType,
			total: total
		});

		StructureStore.on("indicatorsretrivied", (model) => {			
			model.data.map((indicator) => {
				indicator.selected = false;
			});
			model.data.map((indicator) => {
				this.state.selectedIndicators.map((selected) => {
					if(indicator.id == selected.id){
						indicator.selected = true;						
					}					
				});				
			});
			if(this.isMounted()){
				me.setState({
					indicators: model.data
				});
			}
		}, me);
	},

	componentWillReceiveProps(newProps) {
		if(this.isMounted() && newProps.visualization){
			StructureStore.dispatch({
				action: StructureStore.ACTION_GET_INDICATORS
			});
			var total = 0;
			this.state.selectedIndicators = [];
			newProps.indicatorsList.map((ind) => {
				total += ind.percentage;
				this.state.selectedIndicators.push({
					id: ind.aggregate.id,
					name: ind.aggregate.name,
					percentage: ind.percentage,
					deleted: ind.deleted
				});
			});		

			this.setState({
				calculationType: newProps.calculationType,
				total: total
			});			
		}
	},

	onChangeCalculation(evt){
		var value = this.refs['indicator-calculation-type'].value;		
		this.setState({
			calculationType: value
		});
	},

	addIndicator(indicator){
		var list = this.state.selectedIndicators || [];
		var total = this.state.total || 0;
		indicator.selected = true;
		indicator.deleted = false;
		if(_.contains(_.map(list, 'id'), indicator.id)){
			var idx = _.indexOf(_.map(list, 'id'), indicator.id);
			list[idx].deleted = false;
			total += list[idx].percentage;
		} else {
			list.push({
				id: indicator.id,
				name: indicator.name,
				percentage: 0,
				deleted: false
			});			
		}		
		this.setState({
			total: total,
			selectedIndicators: list
		});
	},

	removeIndicator(idx){		
		var list = this.state.selectedIndicators || [];
		this.state.indicators.map((indicator) => {			
			if(indicator.id == list[idx].id){
				indicator.selected = false;
				indicator.deleted = true;
			}			
		});		
		list[idx].deleted = true;
		var total = (this.state.total - list[idx].percentage >= 0 ? this.state.total - list[idx].percentage : 0);
		list[idx].percentage = 0;
						
		this.setState({
			selectedIndicators: list,
			total: total
		});
	},

	changeWeigth(idx, id){		
		var newValue = parseInt(this.refs['ind-'+idx].value);
		if(isNaN(newValue)){			
			newValue = 0;
		}
		var ind;
		this.state.selectedIndicators.map((item) => {
			if(item.id == id){
				ind = item;
			}
		});
		var total = this.state.total || 0;
		if(ind){
			total -= parseInt(ind.percentage) || 0;			
		} 

		ind.percentage = newValue;
		total += newValue;		
		this.setState({
			total: total
		});			
	},

	renderIndicatorAggregateConfig(){		
		if(this.state.indicators){		
			var calculationLabel;
			switch(this.state.calculationType){
				case 0:
					calculationLabel = Messages.get("label.arithmeticMean");
					break;	
				case 1:
					calculationLabel = Messages.get("label.weightedAverage");
					break;
				case 2:
					calculationLabel = Messages.get("label.sum");
					break;	
			}			
			return(
				<div className="fpdi-marginTop20">
					<label className="fpdi-text-label">{Messages.get("label.accumulationForm")} {this.props.visualization? "" : <span className="fpdi-required">&nbsp;</span>}</label>
					{this.props.visualization ? 
						<span className="pdi-normal-text">{calculationLabel}</span>
					:
						<select
							className="form-control"
							placeholder={Messages.get("label.selectCalculation")}
							name="indicator-calculation-type"
							id="indicator-calculation-type"
							ref="indicator-calculation-type"						
							onChange = {this.onChangeCalculation}
							defaultValue={this.state.calculationType}
							>						
								<option key='calc-opt-avg' value={0}  data-placement="right" title={Messages.get("label.arithmeticMean")}>
									{Messages.get("label.arithmeticMean")}</option>;
								<option key='calc-opt-weighted-avg' value={1}  data-placement="right" title={Messages.get("label.weightedAverage")}>
									{Messages.get("label.weightedAverage")}</option>;
								<option key='calc-opt-sum' value={2}  data-placement="right" title={Messages.get("label.sum")}>
									{Messages.get("label.sum")}</option>;
						</select>
					}
					{this.props.visualization ? ""
					:
						<div className="fpdi-indicators">
							<label className="fpdi-text-label">{Messages.get("label.indicators")}</label>
							<div className="form-control fpdi-indicators-ctn">
								{
									this.state.indicators.length > 1 ?
									(this.state.indicators.map((item, idx) => {
										if (item.id != this.props.selfId) {
											var name = item.name + " - \"" + item.plan.parent.name+"\"";
											if(name.length > 80)
												name = name.substring(0,80)+"...";
											return (
												<div key={"indicador-"+idx}>
													{item.selected ?
														<span className="mdi mdi-minus fpdi-indicator-item-selected" 
															onClick={this.removeIndicator.
																bind(this,idx)}>
															<span className="indicator-list-truncate">
																{name}
															</span>
														</span>												 
													:
														<span className="mdi mdi-plus fpdi-indicator-item" 
															onClick={this.addIndicator.bind(this,item)}> 
															<span className="indicator-list-truncate">
																{name}
															</span>
														</span>
													}												
													<br/>
												</div>
											);
										}
									})) : <span className="indicator-list-truncate">
											{Messages.get("label.haveNoSimpleIndicatorsToSelect")}
										</span>								
								}
							</div>
						</div>	
					}					
					<div className="panel panel-default">						  
					  <div className="panel-heading">{Messages.get("label.selectedIndicators")}</div>
					  <table className="table">
					    <thead>
					    	<tr>
						    	<th>Nome</th>	
						    	<th/>					    	
						    	{this.state.calculationType == 1 ? <th>Peso</th> : <th/>}
						    </tr>
					    </thead>
					    <tbody>
					    	{this.state.selectedIndicators.map((indicator,idx) => {						    		
					    		return (
					    			indicator.deleted ? <tr key={"ind-"+idx} /> : 
					    			<tr key={"ind-"+idx}>
					    				<td>{indicator.name}</td>
					    				<td/>
					    				{this.props.visualization && this.state.calculationType == 1 ?
						    				<td>
				    							{indicator.percentage || 0} %
						    				</td>
					    				: this.state.calculationType == 1 ?					    					
						    				<td className="width11percent">
				    							<input type="number" step="1.0"  className="width50" max="100" min="0" 
				    							defaultValue={indicator.percentage || 0} ref={"ind-"+idx} 
				    							onChange={this.changeWeigth.bind(this, idx, indicator.id)}  
				    							onKeyPress={this.onlyNumber} />%
						    				</td>
					    				: <td/>}
					    				{this.props.visualization ? <td/>
					    				:
						    				<td >
						    					<span className="mdi mdi-delete fpdi-indicator-remove" 
						    						onClick={this.removeIndicator.bind(this, idx)}/>
						    				</td>
						    			}
					    			</tr>
					    		);
					    	})}
					    	{!this.props.visualization && this.state.calculationType == 1 ?
						    	<tr>	
						    		<td><b>Total</b></td>
						    		<td/>
						    		<td className={this.state.total != 100 ? "fpdi-indicator-weigth-total" : <td/>}>					    			
						    			<b>{this.state.total+'%'}</b>						    			
						    		</td>						    		
						    		<td className="fpdi-indicator-weigth-total-text">
						    			{this.state.total != 100 ? Messages.get("label.totalMustBe100") : ""}
						    		</td>						    		
						    	</tr>
					    	: <tr/>}
					    </tbody>
					  </table>
					</div>
				</div>
			);
		} else {					
			StructureStore.dispatch({
				action: StructureStore.ACTION_GET_INDICATORS
			});
		}
	},

	render(){
		this.calculationValue = this.state.calculationType;
		this.selectedIndicators = this.state.selectedIndicators;
		this.total = this.state.total;
		return(
			<div>
				{this.renderIndicatorAggregateConfig()}
			</div>
		);
	}
});