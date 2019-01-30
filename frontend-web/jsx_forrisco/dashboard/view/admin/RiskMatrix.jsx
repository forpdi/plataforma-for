import React from "react";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";

import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
//import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";

import RiskQuantity from "forpdi/jsx_forrisco/dashboard/view/admin/RiskQuantity.jsx";

import Messages from "forpdi/jsx/core/util/Messages.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import Toastr from 'toastr';

var numeral = require('numeral');


export default React.createClass({

	getInitialState() {
        return {
			loading: true,
			msg:false,
			opportunities: false,
			threats: true,
			policyModel: null,
			risklevelModel: null,
			policyId: null,
			currentRisks: null,
			risks: [],
			unit:-1,
			units: [],
			plan:null
        };
    },

	componentDidMount(){
		var me = this;
		if(EnvInfo && EnvInfo.company == null){
			me.setState({
				loading: false
			});
		}

		/*RiskStore.on("riskbyunit",(model) =>{
			//console.log("riskbyunit",model)
			this.state.risks.push({unitid:  model.total ,array:model.data})
			this.setState({
				risks:this.state.risks,
				loading:false
			})
		},me);

		UnitStore.on("unitbyplan",(model) =>{
			this.state.risks=[]
			for(var i=0; i<model.data.length;i++){
					RiskStore.dispatch({
						action: RiskStore.ACTION_FIND_BY_UNIT,
						data: model.data[i].id
					});
			}
			me.setState({
				units: model.data,
				loading:true
			});
		},me);*/

		PolicyStore.on("retrieverisklevel", (model) => {

			me.setState({
				risklevelModel: model
			});
			me.forceUpdate();
		}, me);

		PolicyStore.on("retrieve", (model) => {

			me.setState({
				policyModel: model
			});

			if(model != null){
				PolicyStore.dispatch({
					action: PolicyStore.ACTION_RETRIEVE_RISK_LEVEL,
					data: model.id
				});
			}
		}, me);

    },



    componentWillReceiveProps(newProps){
		var me = this;

		this.state.plan=newProps.plan
		this.setState({
			plan: newProps.plan,
			risks: newProps.risks,
			units: newProps.units,
			loading: false,
		});

		if(newProps.units.length ==0 && !this.state.msg){
			 Toastr.error(Messages.get("label.noUnit"))
			 this.state.msg=true
		}


	this.refresh()
	},

	refresh(){
		this.state.plan=1

	/*	UnitStore.dispatch({
			action: UnitStore.ACTION_FIND_BY_PLAN,
			data: this.state.plan
		});
*/
		//PlanRiskStore.on("retrieve", (model) =>{
			this.state.policyId=2

				PolicyStore.dispatch({
					action: PolicyStore.ACTION_RETRIEVE,
					data: this.state.policyId
				});
		//	}, me);

		/*PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE,
			data: newProps.plan
		});*/
	},
    componentWillUnmount() {
		DashboardStore.off(null, null, this);
	 UnitStore.off(null, null, this);
	 PolicyStore.off(null, null, this);
 	},

	selectThreats(){
		this.setState({
			threats:true,
			opportunities:false,
		})

	},
	selectOpportunities(){
		this.setState({
			threats:false,
			opportunities:true,
		})
	},
	getRisks(){
		var risks=[]

		if(this.state.unit==-1){
			for(var i=0; i<this.state.risks.length; i++){
				for(var j=0; j<this.state.risks[i].array.length; j++){

					if((this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "ameaça")
					|| !this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "oportunidade"){
						risks.push(this.state.risks[i].array[j]);
					}
				}
			}
		}else{
			for(var i=0; i<this.state.risks.length; i++){
				if(this.state.risks[i].unitid==this.state.unit){
					var array=this.state.risks[i].array
					for(var j=0; j<array.length; j++){
						if((this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "ameaça")
						|| !this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "oportunidade"){
							risks.push(array[j]);
						}
					}
				}
			}
			return risks;
		}
		return risks;
	},

	showRisk(){
		console.log("//TODO mostrar lista de riscos")
	},

	countRisks(risks,impact, probability, color){
		var count=0;
			for(var i=0; i<risks.length; i++){
				if(risks[i].impact==impact && risks[i].probability==probability){
					risks[i].color=color
					count++;
				}
			}
		return count
	},
	getMatrixValue(risks, matrix, line, column) {

		for(var i=0; i<matrix.length;i++){
			if(matrix[i][1]==line){
				if(matrix[i][2]==column){
					if(matrix[i][2]==0 ){
						return <div style={{"text-align":"right"}}>{matrix[i][0]}&nbsp;&nbsp;&nbsp;&nbsp;</div>
					}else if(matrix[i][1]==this.state.policyModel.attributes.nline){
						return <div style={{"text-align":"-webkit-center",margin: "5px"}} className="">{/*&emsp;&emsp;&emsp;&nbsp;*/}{matrix[i][0]}</div>
					}else{

						var current_color=-1;
						var color=""
						if(this.state.risklevelModel != null){
							for(var k=0; k< this.state.risklevelModel.data.length;k++){
								if(this.state.risklevelModel.data[k]['level']==matrix[i][0]){
									current_color=this.state.risklevelModel.data[k]['color']
								}
							}
						}

						switch(current_color) {
							case 0: color="Vermelho"; break;
							case 1: color="Marron"; break;
							case 2: color="Amarelo"; break;
							case 3: color="Laranja"; break;
							case 4: color="Verde"; break;
							case 5: color="Azul"; break;
							default: color="Cinza";
						}

						var impact=matrix[this.state.policyModel.attributes.nline*(this.state.policyModel.attributes.ncolumn+1)+column-1][0]
						var probability=matrix[(line)*(this.state.policyModel.attributes.ncolumn+1)][0]

						return (<div  className={"icon-link Cor "+color} onClick={this.showRisk}>
									{this.countRisks(risks, impact, probability, color)}
								</div>)
					}
				}
			}
		}
		return ""
	},
	getMatrix() {

		if(this.state.policyModel ==null){
			return
		}

		this.state.currentRisks = this.getRisks();

		var fields = [];
		if(typeof this.state.fields === "undefined" || this.state.fields == null){
			fields.push({
				name: "description",
				type: AttributeTypes.TEXT_AREA_FIELD,
				placeholder: "",
				maxLength: 9000,
				label: Messages.getEditable("label.description","fpdi-nav-label"),
				value: this.state.itemModel ? this.state.itemModel.get("description") : null,
				edit:false
			});
		}else{
			fields=this.state.fields
		}

		var aux=this.state.policyModel.attributes.matrix.split(/;/)
		var matrix=[]

		for(var i=0; i< aux.length;i++){
			matrix[i]= new Array(3)
			matrix[i][0]=aux[i].split(/\[.*\]/)[1]
			matrix[i][1]=aux[i].match(/\[.*\]/)[0].substring(1,aux[i].match(/\[.*\]/)[0].length-1).split(/,/)[0]
			matrix[i][2]=aux[i].match(/\[.*\]/)[0].substring(1,aux[i].match(/\[.*\]/)[0].length-1).split(/,/)[1]
		}

		var table=[]
		for (var i=0; i<=this.state.policyModel.attributes.nline;i++){
			var children=[]
			for (var j=0; j<=this.state.policyModel.attributes.ncolumn;j++){
				children.push(<td key={j}>{this.getMatrixValue(this.state.currentRisks,matrix,i,j)} </td>)
			}
			table.push(<tr key={i} >{children}</tr>)
		}

		return (
			<div>
				<br/>
				<br/>
				<table style={{width: "-webkit-fill-available"}}>
				<th>

							{
								table
							}
					<tr>
						<th style={{bottom: ((this.state.policyModel.attributes.nline-2)*20+80)+"px" , right: "50px", position: "relative"}} >
							<div style={{width: "115px" }} className="vertical-text">PROBABILIDADE</div>
						</th>
					</tr>
					<tr>
						<div style={{"text-align":"-webkit-center", position: "relative", left: "75px"}}>IMPACTO</div>
					</tr>
					</th>
				</table>
			</div>
		);
	},

	onUnitChange(evnt){
		this.setState({
			unit:this.refs['selectUnits'].value
		})
	},
	render() {
		return (<div>
		<div className={"col-md-7"}>
			<div className={this.props.className}>
				<div className="panel panel-default dashboard-goals-info-ctn">

					{//select com todas as unidades deste plano de risco
					 //o plano de risco é uma propriedade da dashboardPanel
					}

					<div className="panel-heading dashboard-panel-title">
						<b className="budget-graphic-title" title={Messages.get("label.risksMatrix")}>{Messages.get("label.risksMatrix").toUpperCase()}</b>
						<span className="frisco-containerSelect"> {Messages.get("label.units")}
                        <select onChange={this.onUnitChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectUnits">
                            <option value={-1} data-placement="right" title={Messages.get("label.viewAll_")}> {Messages.get("label.viewAll_")} </option>
                            {this.state.units.map((attr, idy) =>{
                                    return(
                                        <option  key={attr.id} value={attr.id} data-placement="right" title={attr.name}>
                                            {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
                                        </option>
                                    );
                                })
                            }
                        </select>
						</span>
					</div>




					<div className= "frisco-containerOptions">
						<span className={this.state.threats? "active": ""} onClick={this.selectThreats}>{Messages.get("label.risk.threats")}</span>
						<span className={this.state.opportunities? "active": ""} onClick={this.selectOpportunities}>{Messages.get("label.risk.opportunities")}</span>
					</div>
					<div style={{padding: "0px 20px"}}>
					{this.state.loading ?  <LoadingGauge /> : this.getMatrix()}
					</div>
				</div>
			</div>

			</div>
				{this.state.loading ?  <LoadingGauge /> :
				<div className="col-md-5">
					<RiskQuantity
					plan={this.state.plan}
					risks={this.state.currentRisks}
					policyModel={this.state.policyModel}
					threats={this.state.threats}
					riskLevel={this.state.risklevelModel}
					/>
				</div>
				}
			</div>
		);
	}
});
