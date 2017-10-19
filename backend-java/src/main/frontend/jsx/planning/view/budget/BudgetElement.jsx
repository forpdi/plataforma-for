import S from 'string';
import React from "react";
import {Link} from 'react-router';

import Messages from "forpdi/jsx/core/util/Messages.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import BudgetStore from "forpdi/jsx/planning/store/Budget.jsx";
import SubActionSelectBox from "forpdi/jsx/planning/view/field/SubActionSelectBox.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import _ from 'underscore';
import Validation from 'forpdi/jsx/core/util/Validation.jsx';

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
		accessLevel: React.PropTypes.number.isRequired,
        accessLevels: React.PropTypes.object.isRequired,
        permissions: React.PropTypes.array.isRequired,
        roles: React.PropTypes.object.isRequired
	},
    
    getInitialState() {
        return {
            loading: false,
            hide: false,
			budgetElements	: [],
			editingIdx: -1
        };
    },
     
    newBudget(evt){
		if (this.isMounted()) {
	    	this.setState({
	    		adding: true,
	    		hide:false
	    	}); 
    	}   	
    },
    

	onKeyUp(evt){		
		var key = evt.which;
		if(key == 13) {
			evt.preventDefault();
			return;
		}
    },
    
	componentDidMount() {

        BudgetStore.dispatch({
        	action: BudgetStore.ACTION_GET_BUDGET_ELEMENT,        
          });
          
        BudgetStore.on("budgetElementSavedSuccess", model => {
			this.state.budgetElements.push(model.data);
			console.log("this.state.budgetElements");
			console.log(this.state.budgetElements);

			if (this.isMounted()) {
				this.setState({
					adding: false
				});
			}
            this.context.toastr.addAlertSuccess("Elemento orçamentário adicionado com sucesso");
        },this);
        
        BudgetStore.on("budgetElementRetrivied", (model) => {	
			if (this.isMounted()) {		
			    this.setState({
			    	budgetElements: model.data
			    });	 
            }
            console.log(this.state.budgetElements);	
          });
          
	},
	componentWillUnmount() {
	
    },

    cancelNewBudget(){
		if (this.isMounted()) {
			this.setState({
	    		adding: false
	    	});
		}
	},

    acceptNewBudget() {
    
        BudgetStore.dispatch({
            action: BudgetStore.ACTION_CREATE_BUDGET_ELEMENT,
            data: {
                subAction: this.refs.subAction.value,
                budgetLoa:parseFloat(this.refs.budgetLoa.value),
                companyId: EnvInfo.company.id
            }
        });
	},

	formatReal( int ){
		int = int *100;
        var tmp = int+'';
        var neg = false;
        if(tmp.indexOf("-") == 0)
        {
            neg = true;
            tmp = tmp.replace("-","");
        }
        
        if(tmp.length == 1) tmp = "0"+tmp
    
        tmp = tmp.replace(/([0-9]{2})$/g, ",$1");        
    
        if( tmp.length > 12)
            tmp = tmp.replace(/([0-9]{3}).([0-9]{3}).([0-9]{3}),([0-9]{2}$)/g,".$1.$2.$3,$4");
        else if( tmp.length > 9)
            tmp = tmp.replace(/([0-9]{3}).([0-9]{3}),([0-9]{2}$)/g,".$1.$2,$3");
        else if( tmp.length > 6)
            tmp = tmp.replace(/([0-9]{3}),([0-9]{2}$)/g, ".$1,$2");     
        
        if(tmp.indexOf(".") == 0) tmp = tmp.replace(".","");
        if(tmp.indexOf(",") == 0) tmp = tmp.replace(",","0,");
    
        return (neg ? '-'+tmp : tmp);
	},

	deleteBudget(id, idx,evt){
		var msg = "Você tem certeza que deseja excluir " + this.state.budgetElements[idx].budget.subAction + "?";
		Modal.confirmCustom(() => {
			Modal.hide();
			if (this.isMounted()) {
				this.setState({
					loading: true,
					idx: idx //index a ser deletado
				});
			}
			BudgetStore.dispatch({
				action: BudgetStore.ACTION_DELETE,
				data: {
					id: id
				}
			});

			},msg,()=>{Modal.hide()});

		/*Modal.deleteConfirmCustom(() => {
			Modal.hide();

			this.setState({
				loading: true,
				idx: idx //index a ser deletado
			});
			BudgetStore.dispatch({
				action: BudgetStore.ACTION_DELETE,
				data: {
					id: id
				}
			});
		},"Você tem certeza que deseja excluir " + this.state.budgets[idx].budget.subAction + "?");*/
	},

	acceptedEditbudget(id, idx){
		var validation = Validate.validationEditBudgetField(this.refs, idx);	
		//console.log("acceptedEditbudget");
		

		if (validation.boolMsg) {
			//Toastr.remove();
 			//Toastr.error(msg);
			this.context.toastr.addAlertError(validation.msg);
 			return;
		}
		
		if (this.isMounted()) {
	        this.setState({
				loading: true,
				idx: idx //index a ser editado
			});
	    }
		BudgetStore.dispatch({
			action: BudgetStore.ACTION_CUSTOM_UPDATE,
			data: {
				id: id,
				name:validation.name,
				subAction:validation.subAction
			}
		});		

	},

	rejectEditbudget(idx){
		//var array = this.state.editingIdx;
		//var i = array.indexOf(idx);
		//array.splice(i);
		if (this.isMounted()) {
			this.setState({
				editingIdx: -1
			});
		}
	},

	editBudget(id, idx, evt){
		//var array = this.state.editingIdx;
		//array.push(idx);
		if (this.isMounted()) {
			this.setState({
				editingIdx: idx
			});
		}
	},

	renderEditLine(model, idx){		
		return(
			<tr key={'new-budget-'+idx}>
				<td><SubActionSelectBox className="" ref={"subActions-edit-"+idx} defaultValue={model.budget.subAction}/>
					<div className="formAlertError" ref="formAlertErrorSubAction"></div>
				</td>
				<td><input type='text' maxLength='255' className='budget-field-table' ref={'inputName'+idx}
				 	onKeyPress={this.onKeyUp} defaultValue={model.budget.name}/>
				 	<div className="formAlertError" ref="formAlertErrorName"></div>
					</td>
				<td>{"R$"+this.formatBR(this.formatEUA(model.planned))}</td>
				<td>{"R$"+this.formatBR(this.formatEUA(model.committed))}</td>
				<td>{"R$"+this.formatBR(this.formatEUA(model.conducted))}</td>
				<td>				
                    <div className='displayFlex'>
                       	<span className='mdi mdi-check accepted-budget' onClick={this.acceptedEditbudget.bind(this, model.budget.id, idx)} title={Messages.get("label.submitLabel")}></span>
                      	<span className='mdi mdi-close reject-budget' onClick={this.rejectEditbudget.bind(this, idx)} title={Messages.get("label.cancel")}></span>
                   	</div>
	            </td>
			</tr>
		);
	},

	renderNewBudget(){
		return(			
			<tr key='new-budget'>
				<td ref="tdSubAction"><input type='text' maxLength='255' className='budget-field-table' ref="subAction" onKeyPress={this.onKeyUp}/>
					<div className="formAlertError" ref="formAlertErrorSubAction"></div>
				</td>
				<td ref="tdName"><input type='text' maxLength='255' className='budget-field-table' ref="budgetLoa" onKeyPress={this.onKeyUp}/>
					<div className="formAlertError" ref="formAlertErrorName"></div>	
				</td>
				<td> - </td>
				<td> - </td>
				<td>				
                    <div className='displayFlex'>
                       	<span className='mdi mdi-check accepted-budget' onClick={this.acceptNewBudget} title={Messages.get("label.submitLabel")}></span>
                      	<span className='mdi mdi-close reject-budget' onClick={this.cancelNewBudget} title={Messages.get("label.cancel")}></span>
                   	</div>
	            </td>
			</tr>
		);
	},

	hideFields() {
		if (this.isMounted()) {
			this.setState({
				hide: !this.state.hide
			})
		}
	},

	formatEUA(num){
	    var n = num.toFixed(2).toString(), p = n.indexOf('.');
	    return n.replace(/\d(?=(?:\d{3})+(?:\.|$))/g, function($0, i){
	        return p<0 || i<p ? ($0+',') : $0;
	    });
  	},

  	formatBR(str){
	    var x = str.split('.')[0];
	    x = this.replaceAll(x,",",".");
	    var decimal = str.split('.')[1];
	    if(decimal == undefined){
	      decimal = '00';
	    }
	    return x + "," + decimal;
  	},

	replaceAll(str, needle, replacement) {
	    var i = 0;
	    while ((i = str.indexOf(needle, i)) != -1) {
	        str = str.replace(needle, replacement);
	    }
	    return str;
      },
 	
	render() {
        if (this.state.loading) {
			return <LoadingGauge />;
        } 		
		return (<div className="fpdi-profile-user fpdi-budget-element">
			<div className="fpdi-tabs-content container-fluid animated fadeIn paddingLeft0">
				<h1>{Messages.getEditable("label.budgetElement","fpdi-nav-label")}</h1>
			</div>

            <div className="panel panel-default">
                <div className="panel-heading displayFlex">
                    <b className="budget-title"> {Messages.getEditable("label.budget","fpdi-nav-label")}</b>
                    {(this.state.adding)?
                        "":
                    <div className="budget-btns">
                        {(this.context.roles.MANAGER || _.contains(this.context.permissions, 
                            PermissionsTypes.MANAGE_PLAN_PERMISSION)) ?
                            <button type="button" className="btn btn-primary budget-new-btn" onClick={this.newBudget}>{Messages.getEditable("label.new","fpdi-nav-label")}</button>
                        :""}
                        <span className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
                    </div>}
                </div>
                {!this.state.hide ?(
                <table className="budget-field-table table">
                <thead/>					
                    <thead>
                        <tr>
                            <th>{Messages.getEditable("label.budgetAction","fpdi-nav-label")} <span className = "fpdi-required"/></th>
                            <th>{Messages.getEditable("label.budgetLoa","fpdi-nav-label")} <span className = "fpdi-required"/> </th>
                            <th>{Messages.getEditable("label.balanceAvailable","fpdi-nav-label")}</th>
                            <th>{Messages.getEditable("label.linkedObjects","fpdi-nav-label")}</th>
                            <th> </th>
                        </tr>
                    </thead>
                    <tbody>
                    {this.state.adding ? this.renderNewBudget() : undefined}
					{this.state.budgetElements.map((model, idx) => {
							if(this.state.editingIdx == idx){
								return(this.renderEditLine(model, idx));
							}
							return(
								<tr key={"budget-element"+idx}>
									<td id={'subAction'+idx}>{model.subAction.toUpperCase()}</td>
									<td id={'budgetLoa'+idx}>{model.budgetLoa}</td>
									<td>{"R$"+(model.balanceAvailable)}</td>
									<td id={'linkedObjects'+idx}>{model.linkedObjects}</td>
									<td> </td>
								</tr>
							);
						})}
                    </tbody>
                <tbody/>
                </table>):("")}
            </div>  
		</div>);
	  }
	});
