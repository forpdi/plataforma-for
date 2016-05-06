
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
		if (this.props.fieldDef.type == 'custom') {
			return {
				fieldId: this.props.fieldDef.id
			};
		}
		return {
			fieldId: "field-"+this.props.fieldDef.name.replace(/\./g, "-")
		};
	},
	getValue() {
		var el = this.refs[this.state.fieldId];
		if (el == undefined)
			return el;
		if (el.type == 'checkbox')
			return el.checked;
		if (el.type == 'date')
			return el.valueAsDate;
		if (el.type == 'number')
			return el.valueAsNumber;
		return el.value;
	},
	getInputNode() {
		return this.refs[this.state.fieldId];
	},
	componentDidMount() {
		if (this.props.fieldDef.type == 'date') {
			$(this.getInputNode()).daterangepicker({
				autoApply: true,
				autoUpdateInput: true,
				locale: {
		            format: 'DD/MM/YYYY'
		        },
		        opens: 'right',
		        drops: 'down',
		        showDropdowns: true,
		        singleDatePicker: true
			});
		} else if (this.props.fieldDef.type == 'daterange') {
			$(this.getInputNode()).daterangepicker({
				autoApply: true,
				autoUpdateInput: true,
				locale: {
		            format: 'DD/MM/YYYY'
		        },
		        opens: 'right',
		        drops: 'down',
		        showDropdowns: true
			});
		}
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
						<option value="">{this.props.fieldDef.placeholder}</option>
						{this.props.fieldDef.options ? this.props.fieldDef.options.map((opt,idx) => {
							return (<option key={'field-opt-'+this.state.fieldId+"-"+idx}
								value={opt.get ? opt.get(this.props.fieldDef.valueField):opt[this.props.fieldDef.valueField]}>
									{ this.props.fieldDef.renderDisplay ?
										this.props.fieldDef.renderDisplay(opt)
										:
										(opt.get ? opt.get(this.props.fieldDef.displayField):opt[this.props.fieldDef.displayField])
									}
							</option>);
						}):''}
				</select>
			);
		} else if (this.props.fieldDef.type == 'date') {
			fieldEl = (<input
				className="form-control"
				type='text'
				name={this.props.fieldDef.name}
				defaultValue={this.props.fieldDef.value}
				id={this.state.fieldId}
				ref={this.state.fieldId}
				placeholder={this.props.fieldDef.placeholder}
				onChange={this.props.fieldDef.onChange || _.noop}
			/>);
		} else if (this.props.fieldDef.type == 'daterange') {
			fieldEl = (<input
				className="form-control"
				type='text'
				name={this.props.fieldDef.name}
				defaultValue={this.props.fieldDef.value}
				id={this.state.fieldId}
				ref={this.state.fieldId}
				placeholder={this.props.fieldDef.placeholder}
				onChange={this.props.fieldDef.onChange || _.noop}
			/>);
		} else if (this.props.fieldDef.type == 'custom') {
			return this.props.fieldDef.el;
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
			<div className={"form-group form-group-sm" + (this.props.fieldDef.type == 'hidden' ? " hidden":"")}>
				<label htmlFor={this.state.fieldId} className="fpdi-text-label">
					{this.props.fieldDef.label}
				</label>
				{fieldEl}
				{typeof this.props.fieldDef.helpBox === 'string' ?
					<p className="help-block">{this.props.fieldDef.helpBox}</p>
					:this.props.fieldDef.helpBox
				}
			</div>
		);
	}
});
