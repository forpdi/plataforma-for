
import React from 'react';
import string from "string";

export default React.createClass({
	propTypes: {
		store: React.PropTypes.object.isRequired,
		modelId: React.PropTypes.any.isRequired,
		field: React.PropTypes.string.isRequired,
		type: React.PropTypes.string
	},
	getDefaultProps() {
		return {
			className: '',
			store: null,
			modelId: null,
			field: null,
			value: null,
			type: 'text',
			limitDisplayLength: 0
		};
	},
	getInitialState() {
		return {
			editing: false,
			value: this.props.value
		};
	},
	onKeyPress(event) {
		if (event.key == "Enter") {
			this.saveValue();
		} else if (event.key == "Escape") {
			this.setState({
				editing: false
			});
		}
	},
	toggleEdit() {
		this.setState({
			editing: true
		});
	},
	componentDidUpdate() {
		if (this.state.editing) {
			var input = this.refs['data-input'];
			if (this.props.type === 'date') {
				$(input).daterangepicker({
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
			}
			input.focus();
		}
	},
	saveValue() {
		this.props.store.dispatch({
			action: this.props.store.ACTION_UPDATE_FIELD,
			data: {
				id: this.props.modelId,
				field: this.props.field,
				value: this.refs['data-input'].value
			}
		});
		this.setState({
			editing: false,
			value: this.refs['data-input'].value
		});
	},
	defaultRenderer(rawValue) {
		if (this.props.limitDisplayLength > 0) {
			return string(rawValue).truncate(this.props.limitDisplayLength).s;
		}
		return rawValue;
	},

	render() {
		if (this.state.editing) {
			if (this.props.type === 'textarea') {
				return (<div className="fpdi-editable-data-input-group input-group input-group-sm">
					<textarea
						ref="data-input"
						rows="3"
						className="fpdi-editable-data-textarea form-control"
						defaultValue={this.state.value}
						onBlur={this.saveValue}
						onKeyUp={this.onKeyPress} />
				</div>);
			} else if (this.props.type === 'date') {
				return (<div className="fpdi-editable-data-input-group input-group input-group-sm">
					<input
						ref="data-input"
						type="text"
						className="fpdi-editable-data-input form-control"
						defaultValue={this.state.value}
						onKeyUp={this.onKeyPress} />
				</div>);
			}
			return (<div className="fpdi-editable-data-input-group input-group input-group-sm">
				<input
					ref="data-input"
					type={this.props.type}
					className="fpdi-editable-data-input form-control"
					defaultValue={this.state.value}
					onBlur={this.saveValue}
					onKeyUp={this.onKeyPress} />
			</div>);
		}
		return (<div
			ref="data-view"
			className={"fpdi-editable-data-view "+this.props.className}
			style={this.props.style}
			onClick={this.toggleEdit}>
				{this.props.displayRenderer ?
					this.props.displayRenderer(this.state.value)
					:
					this.defaultRenderer(this.state.value)
				}
		</div>);
	}
});
