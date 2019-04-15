import React from "react";
import Paginator from "forpdi/jsx_forrisco/core/widget/incidents/Paginator.jsx";
import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

export default React.createClass({

	getInitialState() {
		return {
			//State
				isLoading: true,
			//Paginator
				page: 1,
				pageSize: 5,

			//Incidente
				incidentType: null,
				incidentList: [],
				totalOfIncidents: null,
		};
	},

	componentDidMount() {
		RiskStore.on("paginatedIncidents", response => {
			const incidentList = [];
			if (response.data) {
				response.data.map(incident => {incidentList.push({incident});});

				this.setState({
					incidentType: response.data[0].type,
					incidentList: incidentList,
					totalOfIncidents: response.total,
					isLoading: false
				});
			} else {
				this.setState({ isLoading: null });
			}
		}, this);
		this.refreshComponent(this.props.incidents, this.state.page, this.state.pageSize);
	},

	componentWillReceiveProps(newProps) {
		this.refreshComponent(newProps.incidents, newProps.page, newProps.pageSize);
	},

	refreshComponent(params, page, pageSize) {
		const incidentIdList = [];
		params.map(incident => {incidentIdList.push(incident.id)});
		RiskStore.dispatch({
			action: RiskStore.ACTION_PAGINATE_INCIDENTS,
			data: {
				incidentsId: incidentIdList,
				page: page,
				pageSize: pageSize
			}
		});
	},

	componentWillUnmount() {
		RiskStore.off(null, null, this);
	},

	pageChange(page, pageSize) {
		this.refreshComponent(this.props.incidents, page, pageSize);
	},

	onRedirect() {
		this.props.redirect(this);
		RiskStore.off("paginatedIncidents");
	},

	onDismiss() {
		this.props.redirect(this);
		RiskStore.off("paginatedIncidents");
	},

	renderContent() {
		if (this.state.isLoading === true) {
			return (<LoadingGauge/>);
		} else if (this.state.isLoading === null) {
			return (<span>Nada cadastrado nesse período</span>);
		} else {
			return this.state.incidentList.map((callback, index) => (
				<div className="row" key={index}>
					<div className="col-sm-8 center">
						<label className="paddingTop5">
							{callback.incident.description}
						</label>
					</div>
					<div className="col-sm-4 center paddingTop5">
						<a
							href={
								"/#/forrisco/plan-risk/" + callback.incident.risk.unit.planRisk.id +
								"/unit/" + callback.incident.risk.unit.id + "/risk/" + callback.incident.risk.id + "/incident"
							}
							className="btn btn-sm btn-primary center"
							onClick={this.onRedirect}
						>
							Visualizar
						</a>
					</div>
				</div>
			))
		}
	},

	renderTitle() {
		if (this.state.incidentType === 1) {
			return (<span>Tipo: Ameaça</span>);
		} else if (this.state.incidentType === 2) {
			return (<span>Tipo: Oportunidade</span>);
		}
	},

	render() {
		return (
			<div>
				<div className="modal-dialog modal-md">
					<div className="modal-content">
						<div className="modal-header fpdi-modal-header">
							<div>
								<button type="button" className="close" onClick={this.onDismiss} aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<span><h1 className="modal-title">Incidentes</h1></span>
							</div>

							<div>
								{this.renderTitle()}
							</div>
						</div>

						<hr className="divider" />

						<div className="modal-body fpdi-modal-body">
							{this.renderContent()}
						</div>

						<div className="text-align-center">
							<Paginator ref="pagination" onChangePage={this.pageChange} totalOfIncidents={this.state.totalOfIncidents}/>
						</div>
					</div>
				</div>
			</div>
		)
	}

})
