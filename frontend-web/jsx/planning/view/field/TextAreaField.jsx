import React from "react";
import {Link} from 'react-router';
import _ from 'underscore';

import RichText from 'forpdi/jsx/vendor/FPDIRichText.jsx';

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
		if(this.props.isDocument){
			return (
				<RichText
					rows={this.props.fieldDef.rows}
					maxLength='6500'
					className="form-control minHeight170"
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					defaultValue={this.props.fieldDef.value}
					id={this.props.fieldId}
					ref={this.props.fieldId}
					onChange={this.props.fieldDef.onChange || _.noop}
					onKeyPress={this.onKeyUp}
					onPaste={this.onKeyUp}
				/>
			);			
		} else {
			return (
				<textarea
					rows={this.props.fieldDef.rows}
					maxLength='4000'
					className="form-control minHeight170"
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					defaultValue={this.props.fieldDef.value}
					id={this.props.fieldId}
					ref={this.props.fieldId}
					onChange={this.props.fieldDef.onChange || _.noop}
					onKeyPress={this.onKeyUp}
					onPaste={this.onKeyUp}
				/>
			);
		}
	}

});