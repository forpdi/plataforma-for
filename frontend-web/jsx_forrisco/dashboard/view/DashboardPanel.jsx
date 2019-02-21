import React from "react";
import DashboardAdminView from "forpdi/jsx_forrisco/dashboard/view/DashboardAdminView.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Toastr from 'toastr';

export default React.createClass({
    contextTypes: {
        router: React.PropTypes.object,
        accessLevel: React.PropTypes.number.isRequired,
        accessLevels: React.PropTypes.object.isRequired,
        permissions: React.PropTypes.array.isRequired,
        roles: React.PropTypes.object.isRequired
    },

    getInitialState() {
        return {
			selectedPlan:0,
			plans:[],
			units:[],
			risks:[],
			itens:[],
			risk_level:[]
        };
	},


	// PlanRisco Model //se PlanRisco >1 selectbox de planoRisco
	// Uinits Model
	// Riscos Model
	componentDidMount(){
		var me = this;

		RiskStore.on("riskbyplan",(model) =>{
			if(model.success){
				for(var i=0; i<model.data.length;i++){
					this.state.risks.push(model.data[i])
				}
			}
			this.setState({
				risks:this.state.risks,
				loading:false
			})
		},me);

		UnitStore.on("unitbyplan",(model) =>{
			if(model.data.length ==0){
				Toastr.error(Messages.get("label.noUnit"))
			}
			this.state.units = model.data
			me.setState({
				units: model.data,
				risks:[]
			});
		},me);

		PolicyStore.on("retrieverisklevel", (model) => {
			PolicyStore.off(null, null, this);
			me.setState({
				risk_level: model.data,
			});
		}, me)


		PlanRiskStore.on("listedunarchivedplanrisk", (response) => {
			PlanRiskStore.off(null, null, this);
			if (response.success === true) {
				if(response.data.length ==0 ){
					this.context.router.push("/forrisco/plan-risk/new");
				}else{
					var listedPlans = [];
					response.data.map(planRisk => {
						listedPlans.push({
							id: planRisk.id,
							name: planRisk.name,
							policyId: planRisk.policy.id,
						});
					});
					this.setState({
						plans: listedPlans
					})

					UnitStore.dispatch({
						action: UnitStore.ACTION_FIND_BY_PLAN,
						data: response.data[0].id
					});
					RiskStore.dispatch({
						action: RiskStore.ACTION_FIND_BY_PLAN,
						data: response.data[0].id
					});

					PolicyStore.dispatch({
						action: PolicyStore.ACTION_RETRIEVE_RISK_LEVEL,
						data: response.data[0].policy.id
					});

				}
			}else{

				console.log("listedunarchivedplanrisk",response.responseJSON)

				if(response.responseJSON){
					if(response.responseJSON.message =="Não possui domínio!"){
						this.context.router.push("/forrisco/policy");
					}
				}


				this.context.router.push("/forrisco/policy");
			}
		}, me);

		this.refresh()
	},


	refresh(){
		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_FIND_UNARCHIVED
		});
	},

    planRiscoChange(){
		this.state.risks=[]
		this.state.units=[]

		this.setState({
			selectedPlan:this.refs.selectPlan.selectedIndex,
		})

		UnitStore.dispatch({
			action: UnitStore.ACTION_FIND_BY_PLAN,
			data:this.state.plans[this.refs.selectPlan.selectedIndex].id
		});

		RiskStore.dispatch({
			action: RiskStore.ACTION_FIND_BY_PLAN,
			data: this.state.plans[this.refs.selectPlan.selectedIndex].id
		});

		PolicyStore.dispatch({
			action: PolicyStore.ACTION_RETRIEVE_RISK_LEVEL,
			data: this.state.plans[this.refs.selectPlan.selectedIndex].policyId
		});
	},

	componentWillUnmount() {
        PlanRiskStore.off(null, null, this);
		UnitStore.off(null, null, this);
		RiskStore.off(null, null, this);
		PolicyStore.off(null, null, this);
	},

	selectAllitens(){
		var i;
		for(i=0; i<this.state.itens.length; i++){
			if(document.getElementById("checkbox-item-"+i).disabled == false){
				document.getElementById("checkbox-item-"+i).checked = document.getElementById("selectall").checked;
			}
		}
	},

	renderRecords() {
		return (<div className="row">
			<div key="rootSection-selectall">
					<div className="checkbox marginLeft5 col-md-10" >
						<label name="labelSection-selectall" id="labelSection-selectall">
							<input type="checkbox" value="selectall" id="selectall" onChange={this.selectAllitens}></input>
							Selecionar todos
						</label>
					</div>
			</div>

			{this.state.itens.map((rootSection, idx) => {
				return (
				<div key={"rootSection-filled"+idx}>
					<div className="checkbox marginLeft5 col-md-10" >
						<label name={"labelSection-filled"+idx} id={"labelSection-filled"+idx}>
							<input type="checkbox" value={rootSection.id} id={"checkbox-item-"+idx} onClick={this.verifySelectAllitens}></input>
							{rootSection.name}
						</label>
					</div>

				</div>);
			})}
			</div>);
	},


	preClick(){
		this.visualization(true);
	},

	visualization(pre){

		var i = 0;
		var sections = "";
		var subsections = "";
		var author = document.getElementById("documentAuthor").value;
		var title = document.getElementById("documentTitle").value;

		for(i=0; i<this.state.itens.length; i++){
			if(document.getElementById("checkbox-item-"+i).checked == true){
				sections = sections.concat(this.state.itens[i].name+"%2C");
			}
		}

		var selecao = sections.substring(0, sections.length - 3);
		var elemError = document.getElementById("paramError");
		if(sections=='' || author.trim()=='' || title.trim()==''){
			elemError.innerHTML = Messages.get("label.exportError");
			if(author.trim()=='') {
				document.getElementById("documentAuthor").className = "borderError";
			}
			else {
				document.getElementById("documentAuthor").className = "";
			}
			if(title.trim()=='') {
				document.getElementById("documentTitle").className = "borderError";
			}
			else {
				document.getElementById("documentTitle").className = "";
			}
		}else{
			document.getElementById("documentAuthor").className = "";
			document.getElementById("documentTitle").className = "";

			var url = PlanRiskStore.url + "/exportReport" + "?planId="+this.state.plans[this.state.selectedPlan].id +"&title=" + title + "&author=" + author + "&pre=" + pre+ "&selecao=" + selecao
			url = url.replace(" ", "+");

			if(pre){
				window.open(url,title);
			}else{
				//this.context.router.push(url);
				window.open(url,title);
				Modal.hide();
			}
		}
	},


	exportReport(){
		//Lista de riscos de acordo com o seu grau  ameaça/oportunidade,
		//Lista de incidentes do tipo ameaça/oportunidade,
		//Lista de riscos com monitoramento próximo a vencer/em dia/atrasados/não iniciados.

		this.state.itens=[]

		for(var i=0; i<this.state.risk_level.length;i++){
			this.state.itens.push({name:'Riscos - '+this.state.risk_level[i].level+' Ameaças'})
		}
		for(var i=0; i<this.state.risk_level.length;i++){
			this.state.itens.push({name:'Riscos - '+this.state.risk_level[i].level+' Oportunidades'})
		}

		this.state.itens.push(
		{name:'Incidentes - Ameaças'},
		{name:'Incidentes - Oportunidades'},
		{name:'Riscos próximos a vencer'},
		{name:'Riscos em dia'},
		{name:'Riscos atrasados'},
		{name:'Riscos não iniciados'})

		Modal.exportDocument(
			Messages.get("label.exportConfirmation"),
			this.renderRecords(),
			() => {this.visualization(false)},
			({label:"Pré-visualizar",
			onClick:this.preClick,
			title:Messages.get("label.exportConfirmation")})
		);

		document.getElementById("paramError").innerHTML = "";
		document.getElementById("documentAuthor").className = "";
		document.getElementById("documentTitle").className = "";
	},

	render() {

		if(this.state.plans[this.state.selectedPlan] == null){
			return (<LoadingGauge/>)
		}

		return (
			<div className="dashboard-container">
				<h1 className="marginLeft30">{Messages.getEditable("label.dashboard","forrisco-nav-label")}</h1>
				{(
                    <div className="marginLeft30">
                        <span>
							{(this.state.plans.length > 1) ?
							<div>
								<span className = "fpdi-nav-label">{Messages.getEditable("label.risk.Plans","fpdi-nav-label")}&nbsp;</span>
								<select  onChange={this.planRiscoChange} ref="selectPlan" className={"form-control dashboard-select-box"}
									disabled={(this.state.selectedPlan<0)?("disabled"):("")}>
										{(this.state.plans)?(this.state.plans.map((attr, idx) =>{
											return(
												<option key={attr.id} value={idx} data-placement="right" title={attr.name}>
													{(attr.name.length>20)?(attr.name).trim().substr(0, 20).concat("...").toString():(attr.name)}
												</option>
												);
											}))
										: ""}
								</select>
							</div> : ""}
                        </span>
						<span onClick={this.exportReport} className="btn btn-sm btn-primary" style={{margin: "0 10px"}}>
								{Messages.getEditable("label.exportReport")}
						</span>
                    </div>)}
				<DashboardAdminView plan={this.state.plans[this.state.selectedPlan]} units={this.state.units}  risks={this.state.risks}/>
			</div>
		);
	}
});
