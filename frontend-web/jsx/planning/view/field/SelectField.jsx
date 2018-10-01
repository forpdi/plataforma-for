import React from "react";
import {Link} from 'react-router';
import _ from 'underscore';

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

	render(){
		if (this.props.fieldDef.disabled) {
			return (
				<select
					className="form-control"
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					id={this.props.fieldId}
					ref={this.props.fieldId}
					type={this.props.fieldDef.type}
					onChange={this.props.fieldDef.onChange || _.noop}
					defaultValue="Porcentagem"
					disabled
					>
						{this.props.fieldDef.optionsField ? this.props.fieldDef.optionsField.map((opt,idx) => {
							return (<option key={'field-opt-'+this.props.fieldId+"-"+idx} defaultValue={opt.get ? opt.label : ""}
								data-placement="right" title={opt.label}>
									{opt.label}</option>);
						}):''}
				</select>
			);
		} else {
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
						{this.props.fieldDef.optionsField ? this.props.fieldDef.optionsField.map((opt,idx) => {
							return (<option key={'field-opt-'+this.props.fieldId+"-"+idx} defaultValue={opt.get ? opt.label : ""}
								data-placement="right" title={opt.label}>
									{opt.label}</option>);
						}):''}
				</select>
			);
		}
	}

});