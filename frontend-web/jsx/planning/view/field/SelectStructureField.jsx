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
					disabled
					className="form-control"
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					id={this.props.fieldId}
					ref={this.props.fieldDef.name}
					defaultValue={this.props.fieldDef.value}
					>
					{this.props.fieldDef.options.map((opt,idx) => {
						return (<option key={'field-opt-'+opt.get("id")+"-"+idx} defaultValue={opt.get("id")}
							data-placement="right" title={opt.get("name")}>
								 {(opt.get("name").length>20)?(string(opt.get("name")).trim().substr(0, 15).concat("...").toString()):(opt.get("name"))}
								</option>);
					})}
				</select>
			);
		} else {
			return (
				<select
					className="form-control"
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					id={this.props.fieldId}
					ref={this.props.fieldDef.name}
					onChange={this.props.fieldDef.onChange}
					defaultValue={this.props.fieldDef.value}
					>
					{this.props.fieldDef.options.map((opt,idx) => {
						return (<option key={'field-opt-'+opt.id+"-"+idx} value={opt.id}
							data-placement="right" title={opt.name}>
								 {(opt.get("name").length>20)?(string(opt.get("name")).trim().substr(0, 15).concat("...").toString()):(opt.get("name"))}
								</option>);
					})}
				</select>
			);
		}
	}

});