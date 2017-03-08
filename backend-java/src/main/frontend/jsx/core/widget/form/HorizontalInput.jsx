
import _ from "underscore";
import React from "react";

export default React.createClass({
	getDefaultProps() {
		return {
			fieldDef: {
				type: "text",
				name: "input-"+Date.now(),
				label: '',
				placeholder: '',
				value: null,
				onChange: null,
				helpBox: null
			}
		};
	},
	getInitialState() {
		return {
			fieldId: "field-"+this.props.fieldDef.name.replace(/\./g, "-")
		};
	},
	getValue() {
		if (this.refs[this.state.fieldId].type == 'checkbox')
			return this.refs[this.state.fieldId].checked;
		return this.refs[this.state.fieldId].value;
	},
	getInputNode() {
		return this.refs[this.state.fieldId];
	},
	render() {
		var fieldEl;
		if (this.props.fieldDef.type == 'checkbox') {
			fieldEl = (<div className="checkbox"><label>
				<input
					type={this.props.fieldDef.type}
					name={this.props.fieldDef.name}
					defaultValue={true}
					defaultChecked={this.props.fieldDef.value}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					onChange={this.props.fieldDef.onChange || _.noop}
				/> {this.props.fieldDef.placeholder}
			</label></div>);
		} else if (this.props.fieldDef.type == 'textarea') {
			fieldEl = (
				<textarea
					rows={this.props.fieldDef.rows}
					className="form-control"
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					defaultValue={this.props.fieldDef.value}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					onChange={this.props.fieldDef.onChange || _.noop}
				/>
			);
		} else if (this.props.fieldDef.type == 'select') {
			fieldEl = (
				<select
					className="form-control"
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					defaultValue={this.props.fieldDef.value}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					onChange={this.props.fieldDef.onChange || _.noop}
					>
						<option value="" data-placement="right" title={this.props.fieldDef.placeholder}>{this.props.fieldDef.placeholder}</option>
						{this.props.fieldDef.options ? this.props.fieldDef.options.map((opt,idx) => {
							return (<option key={'field-opt-'+this.state.fieldId+"-"+idx} value={opt.get(this.props.fieldDef.valueField)} 
								data-placement="right" title={opt.get(this.props.fieldDef.displayField)}>
									{opt.get(this.props.fieldDef.displayField)}
							</option>);
						}):''}
				</select>
			);
		} else {
			fieldEl = (<input
				className="form-control"
				type={this.props.fieldDef.type}
				name={this.props.fieldDef.name}
				defaultValue={this.props.fieldDef.value}
				id={this.state.fieldId}
				ref={this.state.fieldId}
				placeholder={this.props.fieldDef.placeholder}
				onChange={this.props.fieldDef.onChange || _.noop}
			/>);
		}
		return (
			<div className="form-group form-group-sm">
				<label htmlFor={this.state.fieldId} className="col-sm-2 fpdi-text-label">
					{this.props.fieldDef.label}
				</label>
				<div className="col-sm-10">
					{fieldEl}
					{this.props.fieldDef.helpBox}
				</div>
			</div>
		);
	}
});
