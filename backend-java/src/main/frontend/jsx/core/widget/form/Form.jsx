import $ from 'jquery';
import Toastr from 'toastr';
import React from "react";
import {Link, hashHistory} from "react-router";
import HorizontalInput from "forpdi/jsx/core/widget/form/HorizontalInput.jsx";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";


var VerticalForm =  React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},
	getDefaultProps() {
		return {
			fields: [],
			onCancel: null,
			onSubmit: null,
			onKeyup: null,
			store: null,
			hideCanel: false,
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
			this.backWrapper();
		}
	},
	submitWrapper(evt) {
		if(evt)
			evt.preventDefault();
		if (!this.props.onSubmit)
			console.warn("VerticalForm: You must pass your own onSubmit callback.");
		else {
			if (this.props.onSubmit(this.getValues()))
				$(this.refs['btn-submit']).attr("disabled", "disabled");
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
		var msg = "Você não salvou as modificações. Deseja continuar?";

		Modal.confirmCancelCustom(()=>{hashHistory.goBack(); Modal.hide()},msg,this.refreshCancel);
		//hashHistory.goBack();
	},
	refreshCancel () {
		Modal.hide();
		//this.setState({
		//	editUser: false
		//	
		//});
	},
	componentDidMount() {
		if (this.props.store) {
			this.props.store.on("invalid", this.handleValidation, this);
			this.props.store.on("fail", this.handleFailure, this);
			this.props.store.on(null, () => {
				$(this.refs['btn-submit']).removeAttr("disabled");
			}, this)
		}
	},
	componentWillUnmount() {
		if (this.props.store) {
			this.props.store.off(null, null, this);
		}
	},
	handleValidation(model, errors, opts) {
		this.setState({
			error: true,
			errorMessage: errors
		});
	},
	handleFailure(errors) {
		if (typeof this.state.errorMessage == 'string') {
			if(this.context.toastr == 'undefined'){
				Toastr.remove();
				Toastr.error(errors);
			}else{			
				this.context.toastr.addAlertError(errors);
			}
		} else if (typeof this.state.errorMessage == 'object') {
			var msg = "<ul>";
			_.each(errors, (err) => {
				msg += "<li>"+err+"</li>";
			});
			msg += "</ul>";

			if(this.context.toastr == 'undefined'){
				Toastr.remove();
				Toastr.error(msg);
			}else{			
				this.context.toastr.addAlertError(msg);
			}
		} else {			
			if(this.context.toastr == 'undefined'){
				Toastr.remove();
				Toastr.error("Um erro inesperado ocorreu.");
			}else{			
				this.context.toastr.addAlertError("Um erro inesperado ocorreu.");
			}
		}
		/*this.setState({
			error: true,
			errorMessage: errors
		});*/
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
					<span className="close mdi mdi-close" aria-label="Fechar Alerta" onClick={this.closeAlerts} />
					{this.state.errorMessage}
				</div>);
			} else if (typeof this.state.errorMessage == 'object') {
				var errNo = 0;
				alerts = (<div className="alert alert-danger animated fadeIn" role="alert">
					<span className="close mdi mdi-close" aria-label="Fechar Alerta" onClick={this.closeAlerts} />
					{this.state.errorMessage.map(err => {
						return <li key={"errmsg-"+(errNo++)}>{err}</li>;
					})}
				</div>);
			} else {
				alerts = (<div className="alert alert-danger animated fadeIn" role="alert">
					<span className="close mdi mdi-close" aria-label="Fechar Alerta" onClick={this.closeAlerts} />
					An unexpected error occurred.
				</div>);
			}
		}
		return (<form onSubmit={this.submitWrapper} id={this.props.id} ref={this.props.id}> 
			{this.props.fields.map((field, idx) => {
				return (<VerticalInput
					formId={this.props.id}
					fieldDef={field}
					ref={field.name}
					key={field.name}
					confirmKey={idx == (this.props.fields.length - 1) ? this.props.confirmKey : undefined}
					onConfirm={this.submitWrapper}
					/>
				);
			})}

			<p className="help-block">
				<span className="fpdi-required" /> {Messages.get("label.requiredFields")}
			</p>
			{alerts}

			{!!this.props.blockButtons ?
				(<div className="form-group">
					<button type="submit" className="btn btn-success btn-block">{this.props.submitLabel}</button>
					{!this.props.hideCanel ? (!this.props.cancelUrl ?
						<button className="btn btn-default  btn-block" onClick={this.cancelWrapper}>{this.props.cancelLabel}</button>
						:(
							<Link to={this.props.cancelUrl} className="btn btn-default btn-block">{this.props.cancelLabel}</Link>
						)):""}
				</div>)
				:
				(<div className="form-group text-left">
					<input type="submit" className="btn btn-sm btn-success" ref="btn-submit" value={this.props.submitLabel} />
					{!this.props.hideCanel ? (!this.props.cancelUrl ?
						<button className="btn btn-sm btn-default" onClick={this.cancelWrapper}>{this.props.cancelLabel}</button>
						:
						<Link className="btn btn-sm btn-default" to={this.props.cancelUrl}>{this.props.cancelLabel}</Link>
					):""}
				</div>)
			}
		</form>);
	}
});

export default {
	HorizontalForm: null,
	VerticalForm: VerticalForm
};
