import React from "react";
import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import Paginator from "forpdi/jsx_forrisco/core/widget/risk/Paginator.jsx";

export default React.createClass({

	getInitialState() {
		return {
			unitId: null,
			riskTitle: null,
			riskType: null,
			riskProbability: null,
			riskImpact: null,
			listOfRisks: [],
			totalOfRisks: null,

			//Paginator
				page: 1,
				pageSize: 10
		};
	},

	componentDidMount() {
		RiskStore.on("riskByPI", response => {
			var listOfRisks = [];

			response.data.map(risk => {
				listOfRisks.push({risk})
			});

			if(listOfRisks.length !== 0) {
				this.setState({
					unitId: response.data[0].unit.id,
					riskTitle: response.data[0].riskLevel.level,
					riskType: response.data[0].type,
					riskProbability: response.data[0].probability,
					riskImpact: response.data[0].impact,
					listOfRisks: listOfRisks,
					totalOfRisks: response.total
				});
			} else {
				this.setState({
					unitId: null
				});
			}
		}, this);
	},

	pageChange(page, pageSize) {
		this.refreshComponent(this.props.probability, this.props.impact,page, pageSize);
	},

	componentWillReceiveProps(newPorps) {
		this.refreshComponent(newPorps.probability, newPorps.impact, newPorps.page, newPorps.pageSize)
	},

	componentWillMount() {
		this.refreshComponent(this.props.probability, this.props.impact, this.state.page, this.state.pageSize)
	},

	componentWillUnmount() {
		RiskStore.off(null, null, this)
	},

	refreshComponent(probability, impact, page, pageSize) {
		RiskStore.dispatch({
			action: RiskStore.ACTION_LIST_RISKS_BY_PI,
			data: {
				probability: probability,
				impact: impact,
				page: page,
				limit: pageSize
			}
		});
		this.forceUpdate();
	},

	onRedirect() {
		this.props.redirect(this);
	},

	render() {
		return (
			<div>
				{
					this.state.unitId !== null ?
						<div className="modal-dialog modal-md">
							<div className="modal-content">
								<div className="modal-header fpdi-modal-header">
									<div>
										<button type="button" className="close" data-dismiss="modal" aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<span> <h1 className="modal-title"> {"Risco " + this.state.riskTitle} </h1> </span>
									</div>

									<div>
									<span>
										{"Tipo: " + this.state.riskType} |
										{"Probabilidade: " + this.state.riskProbability} |
										{"Impacto: " + this.state.riskImpact}
									</span>
									</div>
								</div>

								<hr className="divider"/>

								<div className="modal-body fpdi-modal-body">
									{
										this.state.listOfRisks.map((callback, index) => {
											return (
												<div className="row" key={index}>
													<div className="col-sm-8 center">
														<label className="paddingTop5"> {callback.risk.name} </label>
													</div>

													<div className="col-sm-4 center paddingTop5">
														<a href={"/#/forrisco/plan-risk/" + callback.risk.unit.planRisk.id + "/unit/" + this.state.unitId + "/risk/" + callback.risk.id}
														   className="btn btn-sm btn-primary center"
														   onClick={this.onRedirect}>
															Visualizar
														</a>
													</div>
												</div>
											)
										})
									}
								</div>
								<Paginator ref = "pagination" onChangePage={this.pageChange} totalOfRisks={this.state.totalOfRisks}/>
							</div>
						</div>
						:
						<div className="modal-dialog modal-md">
							<div className="modal-content">
								<div className="modal-header fpdi-modal-header">
									<div>
										<button type="button" className="close" data-dismiss="modal" aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<span> <h1
											className="modal-title"> Não existem riscos nesse quadrante </h1> </span>
									</div>
								</div>
							</div>
						</div>
				}
			</div>
		)
	}
});
