import moment from 'moment';
import React from 'react';
import {Link} from 'react-router';
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
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

		PolicyStore.on("findTerms", (model, data) => {
			if (model.data != null){

				var i;
				for (i = 0; i < model.data.length; i++) {
					if(model.data[i].item != null){
						model.data[i].level="Subitem"
					}else{
						model.data[i].level="Item"
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
		PolicyStore.off(null, null, this);
	},

	showMoreOccurencesSearches() {

		var newPage = this.state.page+1;

		if(this.props.itensSelect !=null && this.props.subitensSelect != null){
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_FIND_TERMS,
				data: {
					policyId: this.props.policyId,
					terms: this.props.terms,
					itensSelect:this.props.itensSelect,
					subitensSelect:this.props.subitensSelect,
					ordResult: this.props.ordResult,
					limit:10,
					page: newPage
				},
				opts: {
					wait: true
				}
			});
		}else{
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_FINDALL_TERMS,
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


		}

    	this.setState({
    		page: newPage
    	})
	},


	render() {

		return (
			<div className="fpdi-search">
				<div className = "fpdi-search-view">
					<p>{Messages.getEditable("label.searchReturned","fpdi-nav-label")} {this.state.resultSearchTotal} { this.state.resultSearchTotal == 1 ? Messages.getEditable("label.result","fpdi-nav-label") : Messages.getEditable("label.results","fpdi-nav-label")}</p>
				</div>
				{this.state.resultSearchMore.length > 0 ?
					<div>
						{this.state.resultSearchMore.map((model, idx) => {

							console.log(model);
							return(
								<div key={"levelInstance-"+idx}>
									<div id="fpdi-result-search">
										<div id="fpdi-result-search-title">
											{model.level}
										</div>
										<Link
											to={"/forrisco/policy/" + this.props.policyId + "/item/" + (model.item ? model.item.id + "/subitem/" : "") + model.id}
											activeClassName="active"
											title={Messages.get("label.title.viewMore")}>
											{model.name}
										</Link>
										{model.description ? (model.description != "" ? " "+model.description : "") :""}
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
