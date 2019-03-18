import React from "react";
import Paginator from "forpdi/jsx_forrisco/core/widget/incidents/Paginator.jsx";

export default React.createClass({

	getInitialState() {
		return {
			unitList: this.props.unitList,
			totalofIncidents: this.props.incidents.length,
			//Paginator
				page: 1,
				pageSize: 5
		};
	},


	render () {
		return (
			<div>
				<div className="modal-dialog modal-md">
					<div className="modal-content">
						<div className="modal-header fpdi-modal-header">
							<div>
								<button type="button" className="close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<span> <h1 className="modal-title"> Incidentes </h1> </span>
							</div>

							<div>
								{this.props.threats? <span> Tipo: Ameaça </span> : <span> Tipo: Oportunidade </span> }
							</div>
						</div>

						<hr className="divider"/>

						<div className="modal-body fpdi-modal-body">
							{
								this.props.incidents.map((incident, index) => {
									//console.log(incident);
									//if(index < 5) {
										return (
											<div className="row" key={index}>
												<div className="col-sm-8 center">
													<label className="paddingTop5"> {incident.description} </label>
												</div>
												<div className="col-sm-4 center paddingTop5">
													{/*http://localhost:8081/#/forrisco/plan-risk/2/unit/2/risk/2?_k=feei9q*/}
													<a //href={"/#/forrisco/plan-risk/" + callback.risk.unit.planRisk.id + "/unit/" + incident.unitId + "/risk/" + callback.risk.id}
														className="btn btn-sm btn-primary center"
														onClick={this.onRedirect}>
														Visualizar
													</a>
												</div>
											</div>
										)
									//}
								})
							}
						</div>
						<Paginator ref = "pagination" onChangePage={this.pageChange} totalofIncidents={this.state.totalofIncidents} />
					</div>
				</div>
			</div>
		)
	}

})
