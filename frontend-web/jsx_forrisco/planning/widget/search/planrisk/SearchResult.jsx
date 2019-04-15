import moment from 'moment';
import React from 'react';
import {Link} from 'react-router';
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
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
		PlanRiskStore.on("searchTerms", (response, data) => {
			if (response.data !== null) {
				if (data.page === 1) {
					this.setState({
						resultSearchMore: response.data,
						resultSearchTotal: response.total,
						page: 1
					});

				} else {
					for (var i = 0; i < response.data.length; i++) {
						this.state.resultSearchMore.push(response.data[i]);
					}
				}

				this.setState({
					resultSearchMore:this.state.resultSearchMore
				});
			}
		},this);

	},

	componentWillUnmount() {
		PlanRiskStore.off(null, null, this);
	},

	showMoreOccurencesSearches() {

		var newPage = this.state.page+1;

		if(this.props.itensSelect !== null && this.props.subitensSelect !== null){
			PlanRiskStore.dispatch({
				action: PlanRiskStore.ACTION_SEARCH_BY_KEY,
				data: {
					planRiskId: this.props.planRiskId,
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

		} else {
			PlanRiskStore.dispatch({
				action: PlanRiskStore.ACTION_SEARCH_TERMS,
				data: {
					planRiskId: this.props.planRiskId,
					terms: this.props.terms,
					limit:10,
					page: newPage
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

	getPath(model) {
		switch(model.level) {
			case 'Item':
				return `/forrisco/plan-risk/${this.props.planRiskId}/item/${model.id}`;
			case 'Subitem':
				return `/forrisco/plan-risk/${this.props.planRiskId}/item/${model.parentId}/subitem/${model.id}`;
			case 'Informações gerais':
				return `/forrisco/plan-risk/${this.props.planRiskId}/item/overview`;
			default: return null;
		}
	},

	render() {

		return (
			<div className="fpdi-search">
				<div className = "fpdi-search-view">
					<p>
						{Messages.getEditable("label.searchReturned","fpdi-nav-label")}&nbsp;
						{this.state.resultSearchTotal}&nbsp;
						{
							this.state.resultSearchTotal == 1
								? Messages.getEditable("label.result","fpdi-nav-label")
								: Messages.getEditable("label.results","fpdi-nav-label")
						}

					</p>
				</div>
				{this.state.resultSearchMore.length > 0 ?
					<div>
						{this.state.resultSearchMore.map((model, idx) => {
							return(
								<div key={"levelInstance-" + idx}>
									<div id="fpdi-result-search">
										<div id="fpdi-result-search-title">
											{model.level}
										</div>
										<Link
											to={this.getPath(model)}
											activeClassName="active"
											title={Messages.get("label.title.viewMore")}
										>
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
