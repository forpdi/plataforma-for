import moment from 'moment';
import React from 'react';

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

//import Toastr from 'toastr';

var Validate = Validation.validate;

var onClickOutside = require('react-onclickoutside');

export default onClickOutside(React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},

	getInitialState() {				
		return {
			sonsGeneratedByGoals:undefined,
			loading: false
		};
	},

	getDefaultProps() {
		return {
			hiddenSearch: false,
		};
	},

	componentDidMount(){ 		
		var me = this;
		StructureStore.on("goalsGenerated", (model) => {
			this.setState({
				loading: true
			});
			//Toastr.remove();
			//Toastr.success("Metas geradas com sucesso");
			this.context.toastr.addAlertSuccess(Messages.get("label.goalsGeneratedSuccessfully"));
			/*console.log(model);*/
			this.props.hiddenSearch();
		}, me);


	},

	onlyNumber(evt){
		var key = evt.which;
		if(key == 13|| key != 46 && (key < 48 || key > 57)) {			
			evt.preventDefault();
			return;
		}
	},

	componentWillUnmount() {
		StructureStore.off(null, null, this);
	},

	cancel () {
		this.props.hiddenSearch();
	},

	goalsGenerate() {
		var validation = Validate.validationGoalsGenerate(this.refs);
		if(validation.boolMsg){
			this.context.toastr.addAlertError(validation.msg);
			return;
		} else {
			this.setState({
				loading: true
			});
		}

		Modal.confirm(
			Messages.get("label.attention"), 
			Messages.get("label.goalsGeneratedInSecondPlan"),
			() => {
				Modal.hide();
				StructureStore.dispatch({
					action: StructureStore.ACTION_GOALSGENERATE,
					data: {
						indicatorId: this.props.parentId,
						name: validation.nameGoal.value,
						responsible: validation.responsibleGoal.value,
						description: validation.descriptionGoal.value,
						expected: validation.expectedGoal.value,
						minimum: validation.minimumGoal.value,
						maximum: validation.maximumGoal.value
					}
				});			
			});	
	},

	handleClickOutside: function(evt) {
		this.cancel();
	},

	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return (
			
			<div className="level-search">
		  
  	    		<div className='displayFlex-level-search'>
   	        		<span className='mdi-level-search mdi mdi-close-circle pointer closeButton' onClick={this.props.hiddenSearch} title={Messages.get("label.close")}></span>
  	        	</div>

				<h1>{Messages.get("label.generatGoals")}</h1>
					
				<div className="level-search-keyword">
					<h3>{Messages.get("label.name")}<span className="requiredColor">*</span></h3>
					<input className="form-control" type="text" ref="nameGoal" id="nameGoal" maxLength="200"/>
					<div className="formAlertError" ref="formAlertErrorName"></div>
					<h3>{Messages.get("label.responsible")}<span className="requiredColor">*</span></h3>
					<select
						className="form-control fontSize12"
						name="responsibleGoal"
						ref="responsibleGoal"
						id="responsibleGoal"	
						defaultValue={this.props.users ? this.props.users[0].id : ""}
						>			
							{this.props.users ? 
								this.props.users.map((opt,idx) => {
									return (<option key={'goal-opt-'+idx} value={opt.id}
										 data-placement="right" title={opt.name}>
											{opt.name}</option>);
							}): ""}
					</select>
					<div className="formAlertError" ref="formAlertErrorResponsavel"></div>
					
					<h3>{Messages.get("label.description")}<span className="requiredColor">*</span></h3>
					<textarea  className="form-control" ref="descriptionGoal" id="descriptionGoal" maxLength="3000" rows="3"></textarea>
					<div className="formAlertError" ref="formAlertErrorDescription"></div>
					
					<div className="row">
						<div className="col-md-4">
						<h3>{Messages.get("label.goals.expected")}<span className="requiredColor">*</span></h3>
						<input className="form-control" type="text" ref="expectedGoal" id="expectedGoal" onKeyPress={this.onlyNumber} type="number"/>
						<div className="formAlertError" ref="formAlertErrorExpected"></div>
					</div>


					<div className="col-md-4">	
						<h3>{Messages.get("label.min")} <span className="requiredColor">*</span></h3>
						<input className="form-control" type="text" ref="minimumGoal" id="minimumGoal" onKeyPress={this.onlyNumber} type="number"/>
						<div className="formAlertError" ref="formAlertErrorMinimum"></div>
					</div>
						
						<div className="col-md-4">
							<h3>{Messages.get("label.max")} <span className="requiredColor">*</span></h3>
						<input className="form-control" type="text" ref="maximumGoal" id="maximumGoal" onKeyPress={this.onlyNumber} type="number"/>	
						<div className="formAlertError" ref="formAlertErrorMaximum"></div>
					</div>
					</div>
					
					<h3>{Messages.get("label.goalsWillBeGeneratedAccordingIndicatorPeriodicity")}</h3>
					<p className="requiredColor">* {Messages.get("label.requiredFields")}</p>
				</div> 
			
				<div className="level-search-buttons">
					<input type="submit" className="level-search-button-search" value="Gerar metas" onClick={this.goalsGenerate} />
					<input type="submit" className="level-search-button-clear" value="Cancelar" onClick={this.cancel} />
				</div>

			</div>);
	}
	
}));