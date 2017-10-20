import React from "react";
import {Link} from 'react-router';
//import Toastr from 'toastr';
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import BudgetStore from "forpdi/jsx/planning/store/Budget.jsx";
import SubActionSelectBox from "forpdi/jsx/planning/view/field/SubActionSelectBox.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import _ from 'underscore';
import Validation from 'forpdi/jsx/core/util/Validation.jsx';

//import Toastr from 'toastr';

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
			budgets: this.props.data,
			loading: false,
			hide: false,
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

	onKeyUp(evt){		
		var key = evt.which;
		if(key == 13) {
			evt.preventDefault();
			return;
		}
	},

	componentDidMount()	{
		if (this.isMounted()) {
			this.setState({
	    		adding: false,
	    	});
		}
    	BudgetStore.on("sync", model => {		
			this.state.budgets.push(model.attributes);
			if (this.isMounted()) {
				this.setState({
					adding: false
				});
			}
		},this);
		BudgetStore.on("fail", msg=>{
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError(msg);
		},this);
		BudgetStore.on("budgetDeleted", model => {			
			this.state.budgets.splice(this.state.idx,1);
			if (this.isMounted()) {
				this.setState({
					loading: false
				});
			}
		},this);
		BudgetStore.on("budgetUpdated", model => {
			
		
			if(model.data){
				this.state.budgets[this.state.idx].budget.name=model.data.budget.name; 
				this.state.budgets[this.state.idx].budget.subAction=model.data.budget.subAction; 
				this.state.budgets[this.state.idx].committed = model.data.budget.committed; 
				this.state.budgets[this.state.idx].realized = model.data.budget.realized; 
				this.state.budgets[this.state.idx].budgetLoa = model.data.budgetLoa;
				this.state.budgets[this.state.idx].balanceAvailable = model.data.balanceAvailable;
				this.state.budgets[this.state.idx].budget = model.data.budget;

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
	},

	componentWillUnmount() {
		BudgetStore.off(null, null, this);
	},

	cancelNewBudget(){
		if (this.isMounted()) {
			this.setState({
	    		adding: false
	    	});
		}
	},

	acceptNewBudget(){
		var validation = Validate.validationNewBudgetField(this.refs);
		if (validation.boolMsg) {
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError(validation.msg);
			return;
		}
		
		this.props.newFunc(this.refs.subActions.state.value,validation.name,parseFloat(this.refs.budgetCommitted.value),parseFloat(this.refs.budgetRealized.value)); 
	},

	deleteBudget(id, idx,evt){
		var msg = "Você tem certeza que deseja excluir " + this.state.budgets[idx].budget.subAction + "?";
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
				name:validation.name,
				subAction:validation.subAction,
				id: id,
				committed:parseFloat(this.refs['editCommitted'+idx].value),
				realized:parseFloat(this.refs['editRealized'+idx].value),
				idBudgetElement: this.refs["subActions-edit-"+idx].state.value
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
				<td><SubActionSelectBox className="" ref={"subActions-edit-"+idx} defaultValue={model.budget.budgetElement.id}/>
					<div className="formAlertError" ref="formAlertErrorSubAction"></div>
				</td>
				<td><input type='text' maxLength='255' className='budget-field-table' ref={'inputName'+idx}
				 	onKeyPress={this.onKeyUp} defaultValue={model.budget.name}/>
				 	<div className="formAlertError" ref="formAlertErrorName"></div>
				</td>
				<td> - </td>
				<td> - </td>
				<td> <input type='text' maxLength='255' className='budget-field-table' ref={'editCommitted'+idx}
							defaultValue={model.budget.committed} />
				</td>
				<td> <input type='text' maxLength='255' className='budget-field-table' ref={'editRealized'+idx}
							defaultValue={model.budget.realized} />
				</td>
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
				<td ref="tdSubAction"><SubActionSelectBox className="" ref="subActions"/>
					<div className="formAlertError" ref="formAlertErrorSubAction"></div>
				</td>
				<td ref="tdName"><input type='text' maxLength='255' className='budget-field-table' ref="budgetNameText" onKeyPress={this.onKeyUp}/>
					<div className="formAlertError" ref="formAlertErrorName"></div>	
				</td>
				<td>-</td>
				<td>-</td>
				<td ref="tdCommitted"><input type='text' maxLength='255' className='budget-field-table' ref="budgetCommitted" onKeyPress={this.onKeyUp}/></td>
				<td ref="tdRealized"><input type='text' maxLength='255' className='budget-field-table' ref="budgetRealized" onKeyPress={this.onKeyUp}/></td>
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

	render(){
		if (this.state.loading) {
			return <LoadingGauge />;
		}		
		return(
			<div className="panel panel-default panel-margins">
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
								<th>{Messages.getEditable("label.name","fpdi-nav-label")}<span className = "fpdi-required"/> </th>
								<th>{Messages.getEditable("label.budgetLoa","fpdi-nav-label")}</th>
								<th>{Messages.getEditable("label.balanceAvailable","fpdi-nav-label")}</th>
								<th>{Messages.getEditable("label.budget.committed","fpdi-nav-label")}</th>
								<th>{Messages.getEditable("label.budget.conducted","fpdi-nav-label")}</th>
								<th> </th>
							</tr>
						</thead>
						<tbody>
						{this.state.adding ? this.renderNewBudget() : undefined}
						{this.state.budgets.map((model, idx) => {
							//if( _.contains(this.state.editingIdx, idx) ){
							if(this.state.editingIdx == idx){
								return(this.renderEditLine(model, idx));
							}
							return(
								<tr key={"budget-"+idx}>
									<td id={'subAction'+idx}>{model.budget.subAction}</td>
									<td id={'name'+idx}>{model.budget.name}</td>	
									<td id={'budgetLoa' + idx}>{model.budgetLoa}</td>
									<td id = {'balanceAvailable' + idx}> {model.balanceAvailable}</td>
									<td id = {'committed' + idx}>{model.budget.committed}</td>
									<td id = {'realized' + idx}> {model.budget.realized}</td>
									{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
         								PermissionsTypes.MANAGE_PLAN_PERMISSION)) ?
										<td id={'options'+idx} className="edit-budget-col cn cursorDefault">
											<span className="mdi mdi-pencil cursorPointer marginRight10 inner" onClick={this.editBudget.bind(this,model.budget.id,idx)} title={Messages.get("label.title.editInformation")}/>
											<span className="mdi mdi-delete cursorPointer inner" onClick={this.deleteBudget.bind(this,model.budget.id,idx)} title={Messages.get("label.delete")}/>
										</td>
									: <td></td>}
								</tr>
							);
						})}
						</tbody>
					<tbody/>					
				</table>):("")}
			</div>
		);
	}

});