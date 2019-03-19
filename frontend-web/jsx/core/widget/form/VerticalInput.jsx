
import _ from "underscore";
import React from "react";
import MaskedInput from 'react-maskedinput';
import string from 'string';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import moment from 'moment';
import Toastr from 'toastr';

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},

	getDefaultProps() {
		return {
			fieldDef: {
				type: "text",
				name: "input-"+Date.now(),
				label: '',
				placeholder: '',
				value: null,
				onChange: null,
				helpBox: null,
			}
		};
	},
	getInitialState() {
		if (this.props.fieldDef.type == 'custom') {
			return {
				fieldId: this.props.fieldDef.id
			};
		}

		if(this.props.fieldDef.fieldName){
			this.props.fieldDef.name=this.props.fieldDef.fieldName
		}

		return {
			fieldId: "field-"+this.props.fieldDef.name.replace(/\./g, "-")
		};
	},
	getValue() {
		var el = this.refs[this.state.fieldId];
		if (el == undefined)
			return el;
		if(el.props && el.props.type=='datepicker'){
			if(el.props.selected==null)
				return null;
			return el.props.selected.toDate();
		}
		if (el.type == 'checkbox')
			return el.checked;
		if (this.props.fieldDef.type == 'radio') {
			var radios = $("[type=radio]", el);
			for (var i = 0; i < radios.length; i++) {
				if (radios[i].checked)
					return radios[i].value;
			}
			return "";
		}
		if (el.type == 'date')
			return el.valueAsDate;
		if (el.type == 'number')
			return el.valueAsNumber;
		if (el.mask) {
			return el.mask.getRawValue().trim();
		}
		return el.value;
	},
	getInputNode() {
		return this.refs[this.state.fieldId];
	},

	onKeyUp(evt){
		this.maxLengthMask();
		var key = evt.which;
		if(key == 13 && key != this.props.confirmKey) {
			evt.preventDefault();
			return;
		}

		{/*else if(key == this.props.confirmKey){
			evt.preventDefault();
			this.props.onConfirm();
			return;
		} */}

		//if(this.refs[this.state.fieldId].value.length+1 > this.props.fieldDef.maxLength) {
		//	this.refs[this.state.fieldId].value = this.refs[this.state.fieldId].value.substr(0, this.props.fieldDef.maxLength-1);
		//}
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
	maxLengthMask(){
		if(this.refs[this.state.fieldId].value.length >= this.props.fieldDef.maxLength){
			if(this.context.toastr == 'undefined'){
				Toastr.remove();
				Toastr.error(Messages.get("label.error.limit") + " " +this.props.fieldDef.maxLength+" " + Messages.get("label.error.limitCaracteres"));
			}else{
				this.context.toastr.addAlertError(Messages.get("label.error.limit") + " " +this.props.fieldDef.maxLength + " " + Messages.get("label.error.limitCaracteres"));
			}
		}
	},

	render() {
		var fieldEl;
		if (this.props.fieldDef.type == 'checkbox') {
			fieldEl = (<div className="checkbox checkbox-company">
				<input
					type={this.props.fieldDef.type}
					name={this.props.fieldDef.name}
					defaultValue={true}
					defaultChecked={this.props.fieldDef.value}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					onChange={this.props.fieldDef.onChange || _.noop}
				/>
				<label htmlFor={this.state.fieldId} className="fpdi-text-label">
					<span id = "lbl-checkbox"> {this.props.fieldDef.label} </span>
					{this.props.fieldDef.required ? <span className="fpdi-required">&nbsp;</span>:""}
				</label>
				{this.props.fieldDef.placeholder}
			</div>);
		} else if (this.props.fieldDef.type == 'textarea') {
			fieldEl = (
				<textarea
					rows={this.props.fieldDef.rows || "8"}
					className="form-control minHeight50"
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					defaultValue={this.props.fieldDef.value}
					maxLength={this.props.fieldDef.maxLength*1.01}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					onChange={this.props.fieldDef.onChange || _.noop}
					onPaste={this.onKeyUp}
				/>
			);
		} else if (this.props.fieldDef.type == 'select') {
			fieldEl = (
				<select
					className= {"form-control "+this.props.fieldDef.className}
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					defaultValue={this.props.fieldDef.value}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					onChange={this.props.fieldDef.onChange || _.noop}
					>
						{string(this.props.fieldDef.placeholder).isEmpty() ? "":
							<option value="" data-placement="right" title={this.props.fieldDef.placeholder}>{this.props.fieldDef.placeholder}</option>
						}
						{this.props.fieldDef.options ? this.props.fieldDef.options.map((opt,idx) => {
							return (<option key={'field-opt-'+this.state.fieldId+"-"+idx} value={opt.get ? opt.get(this.props.fieldDef.valueField):opt[this.props.fieldDef.valueField]}
								data-placement="right" title={opt[this.props.fieldDef.displayField]}>
									{ this.props.fieldDef.renderDisplay ?
										this.props.fieldDef.renderDisplay(opt)
										:
										(opt.get ?string(opt.get(this.props.fieldDef.displayField)).trim().truncate(80, "...").toString() :opt[this.props.fieldDef.displayField])
									}
							</option>);
						}):''}
				</select>
			);
		} else if (this.props.fieldDef.type == 'radio') {
			fieldEl = (<div className="radio" ref={this.state.fieldId}>
				{this.props.fieldDef.options ? this.props.fieldDef.options.map((opt,idx) => {
					var fieldVal = opt.get ? opt.get(this.props.fieldDef.valueField):opt[this.props.fieldDef.valueField];
					var checked = fieldVal == this.props.fieldDef.value;
					return (<div key={'field-opt-'+this.state.fieldId+"-"+idx}><label><input
						type="radio"
						disabled={this.props.fieldDef.enabled}
						name={this.props.fieldDef.name}
						id={this.state.fieldId+"-"+idx}
						defaultChecked={(this.props.fieldDef.value == null)?(idx==0?"checked":null):(checked ? "checked":null)}
						value={fieldVal}
						onClick={this.props.fieldDef.onClick || _.noop}
						/> { this.props.fieldDef.renderDisplay ?
							this.props.fieldDef.renderDisplay(opt)
							:
							(opt.get ? opt.get(this.props.fieldDef.displayField):opt[this.props.fieldDef.displayField])
						}
					</label></div>);
				}):''}
			</div>);
		} else if (this.props.fieldDef.type == 'date') {
			fieldEl = (<div><DatePicker
					className="form-control"
					type="datepicker"
					ref={this.state.fieldId}
					dateFormat="DD/MM/YYYY"
					id={this.state.fieldId}
					selected={(this.props.fieldDef.value)?(moment(this.props.fieldDef.value, "DD/MM/YYYY")):(null)}
					onChange={this.props.fieldDef.onChange || _.noop}
					onKeyPress={this.onKeyUp}
					onPaste={this.onKeyUp}
					placeholderText="DD/MM/AAAA"
					showYearDropdown
					/></div>);
		} else if (this.props.fieldDef.type == 'custom-mask') {
			fieldEl = (<MaskedInput
				mask={this.props.fieldDef.mask}
				type="tel"
				className="form-control"
				name={this.props.fieldDef.name}
				value={this.props.fieldDef.value}
				id={this.state.fieldId}
				ref={this.state.fieldId}
				placeholder={this.props.fieldDef.placeholder}
				onChange={this.props.fieldDef.onChange || _.noop}
				// placeholder=""
				onKeyPress={this.onKeyUp}
				onPaste={this.onKeyUp}
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
				onKeyPress={this.onKeyUp}
				onPaste={this.onKeyUp}
			/>);
		} else if (this.props.fieldDef.type == 'subject') {
			fieldEl = (<div>
					<input
						className="form-control"
						type='text'
						name={this.props.fieldDef.name}
						defaultValue={this.props.fieldDef.value}
						id={this.state.fieldId}
						maxLength={this.props.fieldDef.maxLength}
						ref={this.state.fieldId}
						placeholder={this.props.fieldDef.placeholder}
						onChange={this.props.fieldDef.onChange || _.noop}
						onKeyPress={this.onKeyUp}
						onPaste={this.onKeyUp}/>
						<div id = "field-subject">
							<span> {Messages.get("label.maxCaracteresSeventy")} </span>
						</div>
			</div>);
		} else if (this.props.fieldDef.type == 'message') {
			fieldEl = (<div>
					<textarea
						rows={this.props.fieldDef.rows || "8"}
						className="form-control minHeight50"
						placeholder={this.props.fieldDef.placeholder}
						name={this.props.fieldDef.name}
						defaultValue={this.props.fieldDef.value}
						maxLength={this.props.fieldDef.maxLength}
						id={this.state.fieldId}
						ref={this.state.fieldId}
						onChange={this.props.fieldDef.onChange || _.noop}
						onPaste={this.onKeyUp}
						onKeyPress={this.onKeyUp}/>
						<div id = "field-message">
							<span> {Messages.get("label.maxCaracteresTwoHundred")} </span>
						</div>
			</div>);
		} else if (this.props.fieldDef.type == 'cpf') {
			if (this.props.fieldDef.disabled) {
				fieldEl = (<MaskedInput
					mask="111.111.111-11"
					className="form-control"
					name={this.props.fieldDef.name}
					value={this.props.fieldDef.value}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					placeholder={this.props.fieldDef.placeholder}
					onChange={this.props.fieldDef.onChange || _.noop}
					placeholder=""
					onKeyPress={this.onKeyUp}
					onPaste={this.onKeyUp}
					disabled
					title={this.props.fieldDef.disableMsg}
				/>);
			} else {
				fieldEl = (<MaskedInput
					mask="111.111.111-11"
					className="form-control"
					name={this.props.fieldDef.name}
					value={this.props.fieldDef.value}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					placeholder={this.props.fieldDef.placeholder}
					onChange={this.props.fieldDef.onChange || _.noop}
					placeholder=""
					onKeyPress={this.onKeyUp}
					onPaste={this.onKeyUp}
				/>);
			}
		} else if (this.props.fieldDef.type == 'tel') {
			fieldEl = (<MaskedInput
				mask="(11) 111111111"
				type="tel"
				className="form-control"
				name={this.props.fieldDef.name}
				value={this.props.fieldDef.value}
				id={this.state.fieldId}
				ref={this.state.fieldId}
				placeholder={this.props.fieldDef.placeholder}
				onChange={this.props.fieldDef.onChange || _.noop}
				placeholder=""
				onKeyPress={this.onKeyUp}
				onPaste={this.onKeyUp}
			/>);

		} else if (this.props.fieldDef.type == 'upload') {
			fieldEl = (
				<div>
					<a className="btn btn-sm btn-primary" onClick={this.props.fieldDef.extraFunction}>
						<span className="mdi mdi-import"/>
						{this.props.fieldDef.placeholder}
					</a>
				</div>
			);
		} else if (this.props.fieldDef.type == 'changePassword') {
			fieldEl = (
				<div>
					<label className = "fpdi-text-label"> SENHA </label>
					<br/>
					<a className="senhaUser" onClick={this.props.fieldDef.onClick}>
						{this.props.fieldDef.placeholder}
					</a>
				</div>
			);
		} else if (this.props.fieldDef.type == 'custom') {
			return this.props.fieldDef.el;
		} else {
			if (this.props.fieldDef.disabled) {
				fieldEl = (<input
					className="form-control"
					type={this.props.fieldDef.type}
					name={this.props.fieldDef.name}
					maxLength={this.props.fieldDef.maxLength}
					defaultValue={this.props.fieldDef.value}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					placeholder={this.props.fieldDef.placeholder}
					onChange={this.props.fieldDef.onChange || _.noop}
					onKeyPress={this.onKeyUp}
					onPaste={this.onKeyUp}
					disabled
					title={this.props.fieldDef.disableMsg}
				/>);
			} else {
				fieldEl = (<input
					className="form-control"
					type={this.props.fieldDef.type}
					name={this.props.fieldDef.name}
					maxLength={this.props.fieldDef.maxLength}
					defaultValue={this.props.fieldDef.value}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					placeholder={this.props.fieldDef.placeholder}
					onChange={this.props.fieldDef.onChange || _.noop}
					onKeyPress={this.props.fieldDef.onKeyPress || this.onKeyUp}
					onPaste={this.props.fieldDef.onPaste || this.onKeyUp}
				/>);
			}
		}

		return (
			<div className={"form-group form-group-sm" + (this.props.fieldDef.type == 'hidden' ? " hidden":"")}>
				{this.props.fieldDef.type != 'checkbox' ?
					<label htmlFor={this.state.fieldId} className="fpdi-text-label">
						{this.props.fieldDef.label}
						{this.props.fieldDef.required ? <span className="fpdi-required">&nbsp;</span>:""}
					</label>
				:""}

				{fieldEl}
				{typeof this.props.fieldDef.helpBox === 'string' ?
					<p className="help-block">{this.props.fieldDef.helpBox}</p>
					:this.props.fieldDef.helpBox
				}
				<div className="formAlertError" ref="formAlertError"></div>
			</div>
		);
	}
});
