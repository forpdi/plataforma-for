import _ from 'underscore';
import React from "react";
import { Link } from 'react-router';
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import RiskRegister from 'forpdi/jsx_forrisco/planning/view/risk/RiskRegister.jsx';
import Monitor from 'forpdi/jsx_forrisco/planning/view/risk/Monitor.jsx';
import Incident from 'forpdi/jsx_forrisco/planning/view/risk/Incident.jsx';
import Contingency from 'forpdi/jsx_forrisco/planning/view/risk/Contingency.jsx';
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import RiskStore from 'forpdi/jsx_forrisco/planning/store/Risk.jsx';
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Toastr from 'toastr';

export default React.createClass({

	contextTypes: {
		router: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
	},
	childContextTypes: {
		policy: React.PropTypes.object,
		tabPanel: React.PropTypes.object,
		planRisk: React.PropTypes.object
	},
	propTypes: {
		policy: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {
			tabs: [],
			tabsHash: '',
			tabsHidden: [],
			showTabsHidden: false,
			selected:0,
			riskModel:null,
			visualization:true,
			loading:true,
		};
	},

	componentDidMount() {
		RiskStore.on("findRisk", (model) => {
			if(model.success){
				this.setState({
					riskModel:model.data,
					loading:false
				})
			}
		},this)

		RiskStore.on("riskDelete", (model) => {
			if(model.success){
				this.context.router.push("forrisco/plan-risk/"+this.props.params.planRiskId+"/unit/"+this.props.params.unitId+"/info")
			}else{
				var errorMsg = JSON.parse(model.responseText)
				Toastr.error(errorMsg.message);

			}
		},this)

		this.refresh(this.props)
	},

	componentWillReceiveProps(newProps) {
		this.refresh(newProps)
	},

	refresh(newProps){
		RiskStore.dispatch({
			action:RiskStore.ACTION_FIND_RISK,
			data: newProps.params.riskId
		})

		this.setState({
			visualization:true
		})
	},

	renderUnarchiveRisk(){

		return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<Link
						onClick={this.changeVizualization}>
						<span className="mdi mdi-pencil cursorPointer" title={Messages.get("label.title.editInformation")}>
						<span id="menu-levels"> {Messages.getEditable("label.title.editInformation","fpdi-nav-label")} </span>
						</span>
					</Link>
				</li>
				{this.state.undeletable ?
				<li>
					<Link>
						<span className="mdi mdi-delete disabledIcon cursorPointer" title={Messages.get("label.notDeletedHasChild")}>
							<span id="menu-levels"> {Messages.getEditable("label.deleteRisk","fpdi-nav-label")}</span>
						</span>
					</Link>
				</li>
				:
				<li>
					<Link
						onClick={this.deleteRisco}>
						<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deleteRisk")}>
							<span id="menu-levels"> {Messages.getEditable("label.deleteRisk","fpdi-nav-label")} </span>
						</span>
					</Link>
				</li>
				}
			</ul>
		);

	},

	changeVizualization() {
		this.setState({
			visualization: false,
		});
	},

	deleteRisco() {
		var me = this;
		if (me.state.riskModel != null) {
			console.log(me.state.riskModel)
			var msg = "Você tem certeza que deseja excluir esse Risco?"
			Modal.confirmCustom(() => {
				Modal.hide();
				RiskStore.dispatch({
					action: RiskStore.ACTION_DELETE,
					data: me.state.riskModel.id
				});
			},msg,me.refreshCancel);
		}
	},

	selectInfo(){
		switch(this.state.selected){
			case 0:
				return(<RiskRegister
					{...this.props}
					visualization={this.state.visualization}
					risk={this.state.riskModel}
				/>)

			case 1:
				return(
				<Monitor
					visualization={this.state.visualization}
					risk={this.state.riskModel}
					planRiskId={this.props.params.planRiskId}
				/>)

			case 2:
				return(
				<Incident
					visualization={this.state.visualization}
					risk={this.state.riskModel}
					planRiskId={this.props.params.planRiskId}
				/>)

			case 3:
				return(
				<Contingency
					visualization={this.state.visualization}
					risk={this.state.riskModel}
				/>)
		}
	},

	setInfo(select){
		this.setState({
			selected:select
		})

	},

	header(){

		return(<div style={{"display":"flex"}}>
			<div className={"frisco-link icon-link " + (this.state.selected ==0 ? "selecionado" :"")} onClick={() => this.setInfo(0)}>
			Informações
			</div>

			<div className={"frisco-link icon-link " + (this.state.selected ==1 ? "selecionado" :"")} onClick={() => this.setInfo(1)}>
			Monitoramento
			</div>

			<div className={"frisco-link icon-link " + (this.state.selected ==2 ? "selecionado" :"")} onClick={() => this.setInfo(2)}>
			Incidente
			</div>

			<div className={"frisco-link icon-link " + (this.state.selected ==3 ? "selecionado" :"")} onClick={() => this.setInfo(3)}>
			Contigenciamento
			</div>
		</div>
		)
	},

	render() {

		if (this.state.loading) {
			return <LoadingGauge />;
		}


		return (<div className="fpdi-card fpdi-card-full floatLeft">
			<h1>
				{this.state.riskModel ? this.state.riskModel.name : "Risco não encontrado"}
				<span className="dropdown">
						<a	className="dropdown-toggle"
							data-toggle="dropdown"
							aria-haspopup="true"
							aria-expanded="true"
							title={Messages.get("label.actions")}
							>
							<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
							<span className="mdi mdi-chevron-down" />
						</a>
							{this.renderUnarchiveRisk()}
				</span>
			</h1>
				<div>
				{this.header()}
			</div>
				{this.selectInfo()}
		</div>);
	  }
});
