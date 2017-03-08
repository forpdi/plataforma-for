import moment from 'moment';
import React from 'react';
import {Link} from 'react-router';
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";


export default React.createClass({
	propTypes: {
		resultSearch: React.PropTypes.array,
		
	},

	getInitialState() {
		return {
			page: 1,
			resultSearchMore:this.props.resultSearch,
			termPesquisaProps:this.props.terms,
			parentIdProps: this.props.parentId,
			subPlansSelectProps:this.props.subPlansSelect,
			levelsSelectProps:this.props.levelsSelect,
			dataInitProps:this.props.dataInit,
			dataEndProps:this.props.dataEnd,
			ordResultProps:this.props.ordResult
		};
	},

	

	componentDidMount() {
		var me = this;
		
	

		 PlanStore.on("planFind", (model) => {
		 
			if (model != null) {
				this.setState({
           			resultSearchMore:model.data
        		});		
			}
		},this);				



	},
	componentWillUnmount() {
		
		
	},
	componentWillReceiveProps(newProps) {
		
	
		
	},

	showMoreOccurencesSearches() {
		
		var newPage = this.state.page+1;
		PlanStore.dispatch({
			action: PlanStore.ACTION_FIND_TERMS,
			data: {
				parentId: this.props.parentId,
				terms: this.props.terms,
				subPlansSelect:this.props.subPlansSelect,
				levelsSelect:this.props.levelsSelect,
				dataInit: this.props.dataInit,
				dataEnd: this.props.dataEnd,
				ordResult: this.props.ordResult,
				limit: 10,
				page: newPage
			},
			opts: {
				wait: true
			}
		});
    	this.setState({
    		page: newPage
    	})
	},


	render() {
		var total = 0;
		if (this.state.resultSearchMore.length > 0) { 
			this.state.resultSearchMore.map((model, idx) => {
				/*if (model.name)
					total = total + 1;*/
				total = total + model.levelInstances.length;
			});
		}
		return (
			<div className="fpdi-search">		
				<div className = "fpdi-search-view">
					<p>Sua pesquisa retornou {total} {total == 1 ? "resultado" : "resultados"}</p>
				</div>
				{this.state.resultSearchMore.length > 0 ? 
					this.state.resultSearchMore.map((model, idx) => {
						
						return(
							<div key={"plan-"+idx}>
								
								{/*model.name ? 
									<div id = "fpdi-result-search-level">
										<div id="fpdi-result-search-title">
											Plano de Metas<br/>
										</div> 
										<Link
											to={'/plan/'+model.parent.id+'/details/subplan/'+model.id}
											activeClassName="active"
										>
										{model.name}
										</Link>
									</div> 
								: ""*/}
								
								<div>
									{model.levelInstances.length > 0 ? 
										model.levelInstances.map((mod, id) => {	
											return(
												<div key={"structure-"+id} id = "fpdi-result-search">
													<div id="fpdi-result-search-title">
														{mod.level.name}
													</div>
														<Link
											to={"/plan/"+this.props.planId+"/details/subplan/level/"+mod.id}
											activeClassName="active"
											title="ver mais"
											>
											{mod.name}
										</Link>
												</div>
											);				
										})
										
									: ""}
									{model.levelInstances.length > 0 ? 
										<div className="textAlignCenter marginBottom10">
	                    					<a onClick={this.showMoreOccurencesSearches}>ver mais...</a>
	                					</div>
	                				: ""}

								</div>
							</div>
						);
					})
				: ""}		
			</div>
		);
	}

});

