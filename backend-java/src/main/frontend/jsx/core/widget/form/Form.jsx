
import React from "react";
import HorizontalInput from "forpdi/jsx/core/widget/form/HorizontalInput.jsx";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";

var VerticalForm =  React.createClass({
	getDefaultProps() {
		return {
			fields: [],
			onCancel: null,
			onSubmit: null,
			store: null,
			cancelUrl: null,
			cancelLabel: "Cancelar",
			submitLabel: "Enviar",
			blockButtons: false
		};
	},
	getInitialState() {
		return {
			error: false,
			errorMessage: ""
		};
	},
	cancelWrapper(evt) {
		evt.preventDefault();
		if (typeof this.props.onCancel === 'function') {
			this.props.onCancel();
		} else {
			location.assign(this.props.cancelUrl);
		}
	},
	submitWrapper(evt) {
		evt.preventDefault();
		if (!this.props.onSubmit)
			console.warn("VerticalForm: You must pass your own onSubmit callback.");
		else {
			this.props.onSubmit(this.getValues());
		}
	},
	getValues() {
		var data = {};
		this.props.fields.forEach(field => {
			data[field.name] = this.refs[field.name].getValue();
		});
		return data;
	},
	backWrapper() {
		history.back();
	},
	componentWillMount() {
		if (this.props.store) {
			this.props.store.on("invalid", this.handleValidation, this);
			this.props.store.on("fail", this.handleFailure, this);
		}
	},
	componentWillUnmount() {
		if (this.props.store) {
			this.props.store.off("invalid", this.handleValidation, this);
			this.props.store.off("fail", this.handleFailure, this);
		}
	},
	handleValidation(model, errors, opts) {
		this.setState({
			error: true,
			errorMessage: errors
		});
	},
	handleFailure(errors) {
		this.setState({
			error: true,
			errorMessage: errors
		});
	},
	closeAlerts() {
		this.setState({
			error: false
		});
	},
	render() {
		var alerts = null;
		if (this.state.error) {
			if (typeof this.state.errorMessage == 'string') {
				alerts = (<div className="alert alert-danger animated fadeIn" role="alert">
					<button type="button" className="close" aria-label="Fechar Alerta" onClick={this.closeAlerts}>
						<span aria-hidden="true">&times;</span>
					</button>
					{this.state.errorMessage}
				</div>);
			} else if (typeof this.state.errorMessage == 'object') {
				var errNo = 0;
				alerts = (<div className="alert alert-danger animated fadeIn" role="alert">
					<button type="button" className="close" aria-label="Fechar Alerta" onClick={this.closeAlerts}>
						<span aria-hidden="true">&times;</span>
					</button>
					{this.state.errorMessage.map(err => {
						return <li key={"errmsg-"+(errNo++)}>{err}</li>;
					})}
				</div>);
			} else {
				alerts = (<div className="alert alert-danger animated fadeIn" role="alert">
					<button type="button" className="close" aria-label="Fechar Alerta" onClick={this.closeAlerts}>
						<span aria-hidden="true">&times;</span>
					</button>
					An unexpected error occurred.
				</div>);
			}
		}
		return (<form onSubmit={this.submitWrapper}>
			{alerts}
			{this.props.fields.map(field => {
				return (<VerticalInput
					fieldDef={field}
					ref={field.name}
					key={field.name}
					/>
				);
			})}
			{!!this.props.blockButtons ?
				(<div className="form-group">
					<button type="submit" className="btn btn-primary btn-block">{this.props.submitLabel}</button>
					{!this.props.cancelUrl ?
						"":(
							<a href={this.props.cancelUrl} className="btn btn-default btn-block">{this.props.cancelLabel}</a>
						)}
				</div>)
				:
				(<div className="form-group text-right">
					<button className="btn btn-default" onClick={this.cancelWrapper}>{this.props.cancelLabel}</button>
					<button type="submit" className="btn btn-primary">{this.props.submitLabel}</button>
				</div>)
			}
		</form>);
	}
});

export default {
	HorizontalForm: null,
	VerticalForm: VerticalForm
};
