
import React from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import MaskedInput from 'react-maskedinput';
import string from "string";
import moment from 'moment';
//import Toastr from 'toastr';


import AccessLevels from 'forpdi/jsx/core/store/AccessLevels.json';

export default React.createClass({
	statics: {
		CPF_RENDERER: (raw) => {
			var s = string(raw);
			if (s.isEmpty())
				return raw;
			if (raw.length != 11)
				return raw;
			return raw.substr(0,3)+"."+raw.substr(3,3)+"."+raw.substr(6,3)+"-"+raw.substr(9,2);
		},
		PHONE_RENDERER: (raw) => {
			var s = string(raw);
			if (s.isEmpty())
				return raw;
			if (raw.length < 10)
				return raw;
			return "("+raw.substr(0,2)+") "+raw.substr(2,9);
		}
	},
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired
	},
	propTypes: {
		store: React.PropTypes.object.isRequired,
		modelId: React.PropTypes.any.isRequired,
		field: React.PropTypes.string.isRequired,
		type: React.PropTypes.string,
		limitDisplayLength: React.PropTypes.number,
		displayRenderer: React.PropTypes.func
	},
	getDefaultProps() {
		return {
			className: '',
			store: null,
			modelId: null,
			field: null,
			value: null,
			type: 'text',
			limitDisplayLength: 0,
			dateFormat: "DD/MM/YYYY"
		};
	},
	getInitialState() {
		var value = this.props.value;
		if (this.props.type == 'date' && value) {
			value = moment(value, this.props.dateFormat);
		}

		var renderer = this.props.displayRenderer ? this.props.displayRenderer : this.defaultRenderer;
		if (this.props.type == 'date') {
			renderer = this.dateRenderer;
		} else if (this.props.type == 'accessLevel') {
			renderer = this.accessLevelRenderer;
		}
		return {
			editing: false,
			value: value,
			displayRenderer: renderer,
			formValue: value,
			accessLevels: AccessLevels.list.filter((level) => {
				return level.accessLevel <= this.context.accessLevel;
			})
		};
	},

	getEditValue() {
		if (!this.state.editing) {
			return this.state.value;
		}
		var el = this.refs['data-input'];
		if (!el) {
			return el;
		}
		if(this.props.type=='date') {
			return this.state.formValue;
		}
		if (el.mask) {
			return el.mask.getRawValue().trim();
		}

		return el.value;
	},
	onChange(newValue) {
		this.setState({formValue: newValue});
	},

	onKeyPress(event) {
		if (event.key == "Enter") {
			this.saveValue();
		} else if (event.key == "Escape") {
			this.cancelEdit();
		}
	},
	cancelEdit() {
		this.setState({
			editing: false,
			formValue: this.state.value
		});
	},
	toggleEdit() {
		this.setState({
			editing: true
		});
	},
	componentDidUpdate() {
		if (this.state.editing) {
			var input = this.refs['data-input'];
			input.focus && input.focus();
		}
	},
	saveValue() {
		var newValue = this.getEditValue();
		if(this.props.isBirthdate){
			var date = moment(newValue);
			var actualDate = moment();
			if(date>actualDate){
				//Toastr.remove();
				//Toastr.error('Data de nascimento maior que a data atual!');
				this.context.toastr.addAlertError('Data de nascimento maior que a data atual!');
				return;
			}
		}
		this.props.store.dispatch({
			action: this.props.store.ACTION_UPDATE_FIELD,
			data: {
				id: this.props.modelId,
				field: this.props.field,
				value: newValue.format ? newValue.format(this.props.dateFormat):newValue
			}
		});
		this.setState({
			editing: false,
			value: newValue
		});
	},
	defaultRenderer(rawValue) {
		if (this.props.limitDisplayLength > 0) {
			return string(rawValue).truncate(this.props.limitDisplayLength).s;
		}
		return rawValue;
	},
	dateRenderer(rawValue) {
		if (!rawValue)
			return rawValue;
		return rawValue.format(this.props.dateFormat);
	},
	accessLevelRenderer(rawValue) {
		if (!rawValue)
			return rawValue;
		return AccessLevels.mapped[rawValue];
	},

	render() {
		var edit;
		if (this.state.editing) {
			if (this.props.type === 'textarea') {
				edit = (<div className="fpdi-editable-data-input-group">
					<textarea
						ref="data-input"
						rows="3"
						className="fpdi-editable-data-textarea form-control"
						defaultValue={this.state.value}
						onKeyUp={this.onKeyPress}
						maxLength={this.props.maxLength}/>
					<div>
						<button className="btn btn-success" onClick={this.saveValue}><span className="mdi mdi-check" title="Salvar" /></button>
						<button className="btn btn-danger"><span className="mdi mdi-close" title="Cancelar"/></button>
					</div>
				</div>);
			} else if (this.props.type === 'date') {
				edit = (<div className="fpdi-editable-data-input-group" onKeyUp={this.onKeyPress}>
					<DatePicker
						ref="data-input"
						type="datepicker"
						className="fpdi-editable-data-input form-control"
						selected={this.state.formValue}
						dateFormat={this.props.dateFormat}
						showYearDropdown={true}
						onChange={this.onChange}
						placeholderText="DD/MM/AAAA"
					/>
					<div>
						<button className="btn btn-success" onClick={this.saveValue}>
							<span className="mdi mdi-check" title="Salvar"/>
						</button>
						<button className="btn btn-danger" onClick={this.cancelEdit}>
							<span className="mdi mdi-close" title="Cancelar"/>
						</button>
					</div>
				</div>);
			} else if (this.props.type === 'accessLevel') {
				edit = (<div className="fpdi-editable-data-input-group">
					<select
						ref="data-input"
						className="fpdi-editable-data-input form-control"
						defaultValue={this.state.value}
						onKeyPress={this.onKeyUp}>
							{this.state.accessLevels.map((level) => {
								return <option key={"perm-"+level.accessLevel} value={level.accessLevel} data-placement="right" title={level.name}>
									{level.name}
								</option>;
							})}
					</select>
					<div>
						<button className="btn btn-success" onClick={this.saveValue}>
							<span className="mdi mdi-check" title="Salvar"/>
						</button>
						<button className="btn btn-danger" onClick={this.cancelEdit}>
							<span className="mdi mdi-close" title="Cancelar"/>
						</button>
					</div>
				</div>);
			} else if (this.props.type === 'tel') {
				edit = (<div className="fpdi-editable-data-input-group" onKeyUp={this.onKeyPress}>
					<MaskedInput
						ref="data-input"
						mask="(11) 111111111"
						type="tel"
						className="fpdi-editable-data-input form-control"
						value={this.state.value}
						placeholderChar=" "
						onKeyPress={this.onKeyUp}
					/>
					<div>
						<button className="btn btn-success" onClick={this.saveValue}>
							<span className="mdi mdi-check" title="Salvar"/>
						</button>
						<button className="btn btn-danger" onClick={this.cancelEdit}>
							<span className="mdi mdi-close" title="Cancelar"/>
						</button>
					</div>
				</div>);
			} else {
				edit = (<div className="fpdi-editable-data-input-group">
					<input
						ref="data-input"
						type={this.props.type}
						className="fpdi-editable-data-input form-control"
						defaultValue={this.state.value}
						onKeyUp={this.onKeyPress}
						maxLength={this.props.maxLength}
					/>
					<div>
						<button className="btn btn-success" onClick={this.saveValue}>
							<span className="mdi mdi-check" title="Salvar"/>
						</button>
						<button className="btn btn-danger" onClick={this.cancelEdit}>
							<span className="mdi mdi-close" title="Cancelar"/>
						</button>
					</div>
				</div>);
			}
		}
		return (<div className="fpdi-editable-data-view ">
				<div className={"fpdi-editable-data-view-value "+this.props.className}
					ref="data-view"
					style={this.props.style}
					onClick={this.toggleEdit}>
						{this.state.displayRenderer(this.state.value)} <span className="mdi mdi-pencil" />
				</div>
				{edit ? edit:""}
		</div>);
	}
});
