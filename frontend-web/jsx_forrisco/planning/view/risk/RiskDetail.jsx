import _ from 'underscore';
import React from "react";
import { Link } from 'react-router';
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import RiskRegister from 'forpdi/jsx_forrisco/planning/view/risk/RiskRegister.jsx';
import Monitor from 'forpdi/jsx_forrisco/planning/view/risk/Monitor.jsx';
import Incident from 'forpdi/jsx_forrisco/planning/view/risk/Incident.jsx';
import Contingency from 'forpdi/jsx_forrisco/planning/view/risk/Contingency.jsx';

import RiskStore from 'forpdi/jsx_forrisco/planning/store/Risk.jsx';



export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object.isRequired
	},
	childContextTypes: {
		policy: React.PropTypes.object,
		tabPanel: React.PropTypes.object
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
		};
	},

	componentDidMount() {
		/*var me = this;
		StructureStore.on('levelAttributeSaved', (model) => {
			var tabActive = document.getElementsByClassName("fpdi-mainTabs active");
			//Consulta para encontrar qual aba está ativo
			if(tabActive.length>0){// Caso encontre um valor, o texto dele será alterado pelo nome atual do nó
				if(model.data.name.length>14){
					tabActive[0].innerHTML = (model.data.name.substring(0, 14)+"...")+"<span class='mdi mdi-close-circle'/>";
					tabActive[0].title = (model.data.name.substring(0, 14)+"...");
				}else{
					tabActive[0].innerHTML = model.data.name +"<span class='mdi mdi-close-circle'/>";
					tabActive[0].title = model.data.name;
				}
				tabActive[0].getElementsByClassName("mdi mdi-close-circle")[0].onclick =
				this.removeTabByPath.bind(this, tabActive[0].hash.replace("#",""));
			}
		});*/

		RiskStore.on("findRisk", (model) => {
			if(model.success){
				this.setState({
					riskModel:model.data
				})
			}
		})

		this.refresh()
	},

	componentWillUnmount() {

	},

	componentWillReceiveProps(newProps) {
		this.refresh()
	},

	refresh(){
		RiskStore.dispatch({
			action:RiskStore.ACTION_FIND_RISK,
			data: this.props.params.riskId
		})
	},

	renderUnarchiveRisk(){

					//to={"/forrisco/plan-risk/"+this.props.params.planRiskId+"/unit/"+this.props.params.unitId >

		return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<Link
						//to={"/forrisco/plan-risk/"+this.props.params.planRiskId+"/unit/"+this.props.params.unitId+"/risk/"+this.props.params.riskId+"/details"}
						onClick={this.changeVizualization}>
						<span className="mdi mdi-pencil cursorPointer" title={Messages.get("label.title.editInformation")}>
						<span id="menu-levels"> {Messages.getEditable("label.title.editInformation","fpdi-nav-label")} </span>
						</span>
					</Link>
				</li>
				{this.state.undeletable ?
				<li>
					<Link
						//to={"/forrisco/plan-risk/"+this.props.params.planRiskId+"/unit/"+this.props.params.unitId+"/risk/"+this.props.params.riskId+"/details"}
						>
						<span className="mdi mdi-delete disabledIcon cursorPointer" title={Messages.get("label.notDeletedHasChild")}>
							<span id="menu-levels"> {Messages.getEditable("label.deleteItem","fpdi-nav-label")}</span>
						</span>
					</Link>
				</li>
				:
				<li>
					<Link
						//to={"/forrisco/plan-risk/"+this.props.params.planRiskId+"/unit/"+this.props.params.unitId+"/risk/"+this.props.params.riskId+"/details"}
						onClick={this.deleteItem}>
						<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deleteItem")}>
							<span id="menu-levels"> {Messages.getEditable("label.deleteItem","fpdi-nav-label")} </span>
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
			var msg = "Você tem certeza que deseja excluir esse Risco?"
			Modal.confirmCustom(() => {
				Modal.hide();
				ItemStore.dispatch({
					action: ItemStore.ACTION_DELETE,
					data: me.state.itemModel.id
				});
			},msg,me.refreshCancel);
		}
	},

	selectInfo(){
		console.log(this.state.selected)
		//return
		switch(this.state.selected){
			case 0:
				console.log("RiskRegister")
				return(<div>
				{/*<RiskRegister
					visualization={this.state.visualization}
					risk={this.state.riskModel}
				/>)*/} </div>)

			case 1:
				console.log("Monitor")
				return(
				<Monitor
					risk={this.state.riskModel}
				/>)

			case 2:
				console.log("Incident")
				return(
				<Incident
					risk={this.state.riskModel}
				/>)

			case 3:
				console.log("Contingency")
				return(
				<Contingency
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
		console.log("props",this.props)
		console.log("state",this.state)
		return (<div className="fpdi-card fpdi-card-full floatLeft">
				<h1>
					{this.state.riskModel ? this.state.riskModel.name : "Risco não encontrado"}
					<span className="dropdown">
							<a
								className="dropdown-toggle"
								data-toggle="dropdown"
								aria-haspopup="true"
								aria-expanded="true"
								title={Messages.get("label.actions")}
								>
								<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
								<span className="mdi mdi-chevron-down" />
							</a>
							{ this.renderUnarchiveRisk()}
						</span>
				</h1>

				<div>
					{this.header()}
				</div>
				{this.selectInfo()}

				</div>);
	  }
});
