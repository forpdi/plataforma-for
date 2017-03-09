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
			resultSearchMore:[],
			termsSearch:this.props.terms,
			parentIdSearch: this.props.parentId,
			subPlansSelectSearch:this.props.subPlansSelect,
			levelsSelectSearch:this.props.levelsSelect,
			dataInitSearch:this.props.dataInit,
			dataEndSearch:this.props.dataEnd,
			ordResultSearch:this.props.ordResult,
			hideShowMore: false,
			totalSearchOccurrence:null
		};
	},

	

	componentDidMount() {
		var me = this;
		
		PlanStore.on("planFind", (model, data) => {
			if (model != null && this.isMounted()) {
				if (data.page == 1) {
					this.setState({
           				resultSearchMore:model.data,
           				resultSearchTotal:model.total,
           				page: 1
        			});	
				} else {	
					var i;
					for (i = 0; i < model.data.length; i++) {
						this.state.resultSearchMore.push(model.data[i]);
					}
					
					this.setState({
						resultSearchMore:this.state.resultSearchMore
					});
				}
			}
		},this);				

	},
	componentWillUnmount() {
		PlanStore.off(null, null, this);
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
				limit:10,
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
		var total = this.state.resultSearchTotal;
		return (
			<div className="fpdi-search">		
				<div className = "fpdi-search-view">
					<p>Sua pesquisa retornou {total} {total == 1 ? "resultado" : "resultados"}</p>
				</div>
				{this.state.resultSearchMore.length > 0 ? 
					<div>
						{this.state.resultSearchMore.map((model, idx) => {
							return(
								<div key={"levelInstance-"+idx}>
									<div id="fpdi-result-search">
										<div id="fpdi-result-search-title">
											{model.level.name}
										</div>
										<Link
											to={"/plan/"+this.props.planId+"/details/subplan/level/"+model.id}
											activeClassName="active"
											title="ver mais"
											>
											{model.name}
										</Link>
									</div>
								</div>
							);
						})}
						{this.state.resultSearchMore.length < this.state.resultSearchTotal ? 
							<div className="textAlignCenter marginTop20">
	        					<a onClick={this.showMoreOccurencesSearches}>ver mais...</a>
	    					</div>
	    				: ""}
    				</div>
				: ""}		
			</div>
		);
	}

});