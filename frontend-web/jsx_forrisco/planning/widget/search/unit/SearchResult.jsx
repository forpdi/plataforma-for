import moment from 'moment';
import React from 'react';
import {Link} from 'react-router';
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";


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
			totalSearchOccurrence:null,
			resultSearchTotal:0
		};
	},



	componentDidMount() {
		var me = this;

		UnitStore.on("findTerms", (model, data) => {
			console.log("findTerms", model, data )
			if (model.data != null){
				for (var i = 0; i < model.data.length; i++) {

					if(model.data[i].riskSearchId !== undefined){
						model.data[i].level="Risco"
						model.data[i].link="/forrisco/plan-risk/"+this.props.planRiskId+"/unit/"+model.data[i].id+"/risk/"+model.data[i].riskSearchId+"/info"
					}else if(model.data[i].parent !== undefined) {
						model.data[i].level="Subunidade"
						model.data[i].link="/forrisco/plan-risk/"+this.props.planRiskId+"/unit/"+ model.data[i].parent.id +"/subunit/"+model.data[i].id+"/info"
					}else{
						model.data[i].level="Unidade"
						model.data[i].link="/forrisco/plan-risk/"+this.props.planRiskId+"/unit/"+model.data[i].id+"/info"
					}
				}

				if (data.page == 1) {
					this.setState({
						resultSearchMore:model.data,
           				resultSearchTotal:model.total,
           				page: 1
					});
				}else{
					var i;
					for (i = 0; i < model.data.length; i++) {
						this.state.resultSearchMore.push(model.data[i]);
					}
				}

				this.setState({
					resultSearchMore:this.state.resultSearchMore
				});
			}
		},this);

	},

	componentWillUnmount() {
		UnitStore.off(null, null, this);
	},

	showMoreOccurencesSearches() {

		var newPage = this.state.page + 1;

		UnitStore.dispatch({
				action: UnitStore.ACTION_FINDALL_TERMS,
				data: {
					policyId: this.props.policyId,
					terms: this.props.terms,
					limit:10,
					page: newPage
					//ordResult: this.props.ordResult,
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
		//console.log(this.props)
		return (
			<div className="fpdi-search">
				<div className = "fpdi-search-view">
					<p>{Messages.getEditable("label.searchReturned","fpdi-nav-label")} {this.state.resultSearchTotal} { this.state.resultSearchTotal == 1 ? Messages.getEditable("label.result","fpdi-nav-label") : Messages.getEditable("label.results","fpdi-nav-label")}</p>
				</div>
				{this.state.resultSearchMore.length > 0 ?
					<div>
						{this.state.resultSearchMore.map((model, idx) => {
							console.log("resultSearchMore",model.link)
							return(
								<div key={"levelInstance-"+idx}>
									<div id="fpdi-result-search">
										<div id="fpdi-result-search-title">
											{model.level}
										</div>
										&nbsp;
										<Link
											to={model.link}
											activeClassName="active"
											title={Messages.get("label.title.viewMore")}>
											{model.name}
										</Link>
										&nbsp;
										{model.description ? (model.description !== "" ? "" + model.description : "") : ""}
									</div>
								</div>
							);
						})}
						{this.state.resultSearchMore.length < this.state.resultSearchTotal ?
							<div className="textAlignCenter marginTop20">
	        					<a onClick={this.showMoreOccurencesSearches}>{Messages.getEditable("label.viewMore","fpdi-nav-label")}</a>
	    					</div>
	    				: ""}
    				</div>
				: ""}
			</div>
		);
	}

});
