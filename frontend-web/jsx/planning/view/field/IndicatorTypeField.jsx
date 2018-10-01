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
			return(
				<div>
					{this.props.fieldDef.options.map((option,idx) => {
						if(option[this.props.fieldDef.valueField]){
							return (
								<span key={"value-"+idx} className="fpdi-indicator-type-value">
									{this.props.fieldDef.name == "indicator-type" ? Messages.get("label.indicator"):""}{option[this.props.fieldDef.displayField]}
								</span>
							);
						}
					})}
					{this.props.fieldDef.extraRender()}
				</div>
			);
		} else {
			return (
				<div>
					<div className="row">
						{this.props.fieldDef.options.map((option, idx) => {		
							return (															
								<div className="fpdi-indicator-type-ctn col-sm-2" key={this.props.fieldDef.name+"-option-"+idx}>
									<label onClick={this.props.fieldDef.onClick}>
										<input
											className="col-sm-6"
											type="radio"
											name={this.props.fieldDef.name+idx}
											defaultValue={option[this.props.fieldDef.valueField]}
											defaultChecked={option[this.props.fieldDef.valueField]}	
											ref={this.props.fieldDef.name+"-option-"+idx}
											onChange={this.props.fieldDef.onChange || _.noop}
											onKeyPress={this.onKeyUp}
											onPaste={this.onKeyUp}
											onClick={this.props.fieldDef.onClick}/>
										<label className="fpdi-indicator-type-label col-sm-6" id={"label-"+idx}>{option[this.props.fieldDef.displayField]}</label>
									</label>
								</div>							
							);
						})}						
					</div>
					{this.props.fieldDef.extraRender ? this.props.fieldDef.extraRender() : ""}
				</div>
			);
		}
	}

});