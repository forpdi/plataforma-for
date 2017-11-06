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


var Validate = Validation.validate;

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
	onlyNumber(evt){
		var key = evt.which;
		if(key == 13|| key != 44 && (key < 48 || key > 57)) {
			evt.preventDefault();
			return;
		}
	},
   
   onlyNumberPaste(evt){
	   var value = evt.clipboardData.getData('Text');
	   if (!(!isNaN(parseFloat(value)) && isFinite(value)) || parseFloat(value) < 0) {
		   evt.preventDefault();
		   return;
	   }
   },
    
	componentDidMount() {
        BudgetStore.dispatch({
			action: BudgetStore.ACTION_GET_BUDGET_ELEMENT,
			data: {
                companyId: EnvInfo.company.id
            }
          });
          
        BudgetStore.on("budgetElementSavedSuccess", model => {
			this.state.budgetElements.push(model.data);
	
			if (this.isMounted()) {
				this.setState({
					adding: false
				});
			}
            this.context.toastr.addAlertSuccess("Elemento orçamentário adicionado com sucesso");
        },this);

        BudgetStore.on("budgetElementSavedError", (error) => {
            this.context.toastr.addAlertError(error.responseJSON.message);
        },this);
        
        BudgetStore.on("budgetElementRetrivied", (model) => {	
			if (this.isMounted()) {		
			    this.setState({
			    	budgetElements: model.data
			    });	 
            }
		  },this);
		  
		  BudgetStore.on("budgetElementUpdated", model => {
			if(model.data) {
				if (this.state.idx != undefined) {
					this.state.budgetElements[this.state.idx].subAction = model.data.subAction; 
					this.state.budgetElements[this.state.idx].balanceAvailable=model.data.balanceAvailable; 
					this.state.budgetElements[this.state.idx].budgetLoa = model.data.budgetLoa; 
				}
				//Toastr.remove();
				//Toastr.success("Orçamento editado com sucesso!");
				this.context.toastr.addAlertSuccess(Messages.get("label.success.budgetEdited"));
				this.rejectEditbudget(this.state.idx);
			}else{
				var errorMsg = JSON.parse(model.responseText)
				//Toastr.remove();
				//Toastr.error(errorMsg.message);
				this.context.toastr.addAlertError(errorMsg.message);
			}
			if (this.isMounted()) {
				this.setState({
					loading: false,
					editingIdx: -1
				});
			}
		},this);

		BudgetStore.on("budgetElementDeleted", model => {			
			if (model.success) {
				this.state.budgetElements.splice(this.state.idx,1);
				this.context.toastr.addAlertSuccess(Messages.get("label.deleted.budgetElement"));
				if (this.isMounted()) {
					this.setState({
						loading: false
					});
				} 
				
			} else  {
				var errorMsg = JSON.parse(model.responseText)
				this.context.toastr.addAlertError(errorMsg.message);
				if (this.isMounted()) {
					this.setState({
						loading: false
					});
				} 

			}
		},this);
          
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
	  
	converteMoedaFloat(valor){
		var valorFormated = valor.toString();
		valorFormated =  valorFormated.replace(".",",");		
		return valorFormated;
	 },

	acceptNewBudget() {

		var validation = Validate.validationNewBudgetElementField(this.refs);	
		
		if (validation.boolMsg) {
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError(validation.msg);
			return;
		}
		
        BudgetStore.dispatch({
            action: BudgetStore.ACTION_CREATE_BUDGET_ELEMENT,
            data: {
                subAction: this.refs.subAction.value,
                budgetLoa:this.refs.budgetLoa.value,
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
		var msg = "Você tem certeza que deseja excluir " + this.state.budgetElements[idx].subAction + "?";
		
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			if (this.isMounted()) {
				this.setState({
					loading: true,
					idx: idx //index a ser deletado
				});
			}

			this.forceUpdate();

			BudgetStore.dispatch({
				action: BudgetStore.ACTION_DELETE_BUDGET_ELEMENT,
				data: {
					id: id
				}
			});

			},msg,()=>{Modal.hide()});
	},

	acceptedEditbudget(id, idx){
		var validation = Validate.validationEditBudgetElementField(this.refs, idx);	

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

		this.forceUpdate();

		BudgetStore.dispatch({
			action: BudgetStore.ACTION_GET_UPDATE_BUDGET_ELEMENT,
			data: {
				idBudgetElement: id,
				subAction:this.refs['nameBudgetElement'+idx].value,
				budgetLoa:this.refs['budgetLoaEdit'+idx].value
			}
		});	
		
		
	},
	rejectEditbudget(idx){
		if (this.isMounted()) {
			this.setState({
				editingIdx: -1
			});
			this.forceUpdate();
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

		this.forceUpdate();
		
	},

	renderEditLine(model, idx){		
		return(
			<tr key={'new-budgetElement-'+idx}>
				<td><input type='text' maxLength='255' className='budget-field-table' ref={'nameBudgetElement'+idx}
				 	onKeyPress={this.onKeyUp} defaultValue={model.subAction}/>
				 	<div className="formAlertError" ref="formAlertErrorSubActionEdit"></div>
				</td>
				<td><input type='text' maxLength='255' className='budget-field-table' ref={'budgetLoaEdit'+idx} defaultValue={this.converteMoedaFloat(model.budgetLoa)} onKeyPress={this.onlyNumber}
					onPaste={this.onlyNumberPaste}/>
				 	<div className="formAlertError" ref="formAlertErrorBudgetLoaEdit"></div>
				</td>
				<td> - </td>
				<td> - </td>
				<td> </td>
				<td>				
                    <div className='displayFlex'>
                       	<span className='mdi mdi-check accepted-budget' onClick={this.acceptedEditbudget.bind(this, model.id,idx)} title={Messages.get("label.submitLabel")}></span>
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
				<td ref="tdName"><input type='text' maxLength='255' className='budget-field-table' ref="budgetLoa" onKeyPress={this.onlyNumber}
						onPaste={this.onlyNumberPaste}/>
					<div className="formAlertError" ref="formAlertErrorBudgetLoa"></div>	
				</td>
				<td> - </td>
				<td> - </td>
				<td></td>
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
									<td id={'budgetLoa'+idx}>{"R$" + this.formatBR(this.formatEUA(model.budgetLoa))} </td>
									<td> {"R$" + this.formatBR(this.formatEUA(model.balanceAvailable))}</td>
									<td id={'linkedObjects'+idx}>{model.linkedObjects}</td>
									<td> </td>
									{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
         								PermissionsTypes.MANAGE_PLAN_PERMISSION)) ?
										<td id={'options'+idx} className="edit-budget-col cn cursorDefault">
											<span className="mdi mdi-pencil cursorPointer marginRight10 inner" onClick={this.editBudget.bind(this,model.id,idx)} title={Messages.get("label.title.editInformation")}/>
											<span className="mdi mdi-delete cursorPointer inner" onClick={this.deleteBudget.bind(this,model.id,idx)} title={Messages.get("label.delete")}/>
										</td>
									: <td></td>}
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
