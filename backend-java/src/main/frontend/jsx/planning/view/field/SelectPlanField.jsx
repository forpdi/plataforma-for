import React from "react";
import {Link} from 'react-router';
import _ from 'underscore';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

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

		};
	},

	componentDidMount()	{
		
	},

	componentWillUnmount() {
		
	},

	render() {
		if (this.props.vizualization) {
			if (this.props.fieldDef.selectPlans && this.props.fieldDef.selectPlans.length > 0) {
				var exist = false;
				for (var i=0; i<this.props.fieldDef.selectPlans.length; i++) {
					if (this.props.fieldDef.selectPlans[i].id == this.props.fieldDef.value) {
						exist = true;
						return(
							<div><span className="pdi-normal-text" dangerouslySetInnerHTML={{__html: this.props.fieldDef.selectPlans[i].name}}/></div>
						);
					}
				}
				if (!exist) {
					return (
						<div><span className="pdi-normal-text" dangerouslySetInnerHTML={{__html: ""}}/></div>
					);
				}
			} else {
				return(
					<div><span className="pdi-normal-text" dangerouslySetInnerHTML={{__html:Messages.get("label.title.goalPlanNotRegistered")}}/></div>
				);
			}
		} else {
			if (this.props.fieldDef.selectPlans.length > 0) {
				return (
					<select
						className="form-control"
						placeholder={this.props.fieldDef.placeholder}
						name={this.props.fieldDef.name}
						id={this.props.fieldId}
						ref={this.props.fieldId}
						type={this.props.fieldDef.type}
						onChange={this.props.fieldDef.onChange || _.noop}
						defaultValue={this.props.fieldDef.value}
						>
							{this.props.fieldDef.selectPlans.map((opt,idx) => {
								return (<option key={'field-opt-'+this.props.fieldId+"-"+idx} value={opt.id}
									data-placement="right" title={opt.name}>
									 {(opt.name.length>92)?(string(opt.name).trim().substr(0, 89).concat("...").toString()):(opt.name)}
									</option>);
							})}
					</select>
				);
			} else {
				return (
					<select
						disabled
						className="form-control"
						name={this.props.fieldDef.name}
						id={this.props.fieldId}
						ref={this.props.fieldId}
						type={this.props.fieldDef.type}
						>
						<option key='field-opt-' data-placement="right" title={Messages.get("label.title.goalPlanNotRegistered")}>
							{Messages.get("label.title.goalPlanNotRegistered")}
						</option>
					</select>
				);
			}
		}
	}

});