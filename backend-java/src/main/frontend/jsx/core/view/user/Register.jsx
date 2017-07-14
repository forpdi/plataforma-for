
import React from "react";
import Toastr from 'toastr';
import { Router, Route, IndexRedirect, IndexRoute, hashHistory } from 'react-router';


import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";

import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

import Messages from "forpdi/jsx/core/util/Messages.jsx";
import moment from 'moment';

import AppLogo from "forpdi/img/logoLogin.png";

import Validation from 'forpdi/jsx/core/util/Validation.jsx';

//import Toastr from 'toastr';

var Validate = Validation.validate;

var VerticalForm = Form.VerticalForm;
var ReactToastr = require("react-toastr");
var {ToastContainer} = ReactToastr; 
var ToastMessageFactory = React.createFactory(ReactToastr.ToastMessage);

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
	},
	getInitialState() {
		return {
			fields: [{
				name: "name",
				type: "text",
				placeholder: "",
				required: true,
				maxLength:255,
				label: Messages.get("label.name")
			},{
				name: "cpf",
				type: "cpf",
				placeholder: "",
				required: true,
				label: Messages.get("label.cpf")
			},{
				name: "birthdate",
				type: "date",
				placeholder: "",
				required: true,
				onChange:this.onBirthDateChange,
				label: Messages.get("label.birthdate")
				
			},{
				name: "cellphone",
				type: "tel",
				placeholder: "",
				required: true,
				label: Messages.get("label.cellphone")
			},{
				name: "phone",
				type: "tel",
				placeholder: "",
				label: Messages.get("label.phone")
			},{
				name: "department",
				type: "text",
				placeholder: "",
				maxLength:255,
				label: Messages.get("label.department")
			},{
				name: "password",
				type: "password",
				placeholder: "",
				required: true,
				maxLength:255,
				label: Messages.get("label.password")
			},{
				name: "passwordconfirm",
				type: "password",
				placeholder: "",
				required: true,
				maxLength:255,
				label: Messages.get("label.passwordConfirm")
			}],
			loading: true,
			valid: false
		};
	},
	componentDidMount() {
		if (!!UserSession.get("logged")) {
			location.assign("#/home");
		} else {
			this.setState({loaded: true});
		}
		var me = this;
		UserSession.on("register", model => {
			me.addAlertSuccess(Messages.get("label.sucess.registrationCompleted"));
			me.setState({
				valid: false,
				confirmed: true
			})
			//location.assign("#/");
		}, me);

		UserSession.on("registertoken", (valid) => {
			me.setState({
				valid: valid,
				loading: false
			});
		}, me);

		UserSession.dispatch({
			action: UserSession.ACTION_CHECK_REGISTER_TOKEN,
			data: this.props.params.token
		});
	},
	componentWillUnmount() {
		UserSession.off(null, null, this);
	},

	addAlertError(msg) {
		this.refs.container.clear(); 
		this.refs.container.error(
			msg,null, {
				timeOut: 5000,
				extendedTimeOut: 10000,
				closeButton:true,
				showAnimation: null,
				hideAnimation: 'flash'
			}
		);
	},
	addAlertSuccess(msg) {
		this.refs.container.clear();
		this.refs.container.success(
			msg,null, {
				timeOut: 5000,
				extendedTimeOut: 10000,
				closeButton:true,
				showAnimation: null,
				hideAnimation: 'flash'
			}
		);
	},

	onBirthDateChange(data){
		var model = this.state.model;
		this.setState({
			model:model,
		});

		// FIXME Por que não colocou isso no VerticalInput?
		if(data != null){
			this.refs.registerForm.refs.birthdate.props.fieldDef.value = data.format('DD/MM/YYYY');
		}else{
			this.refs.registerForm.refs.birthdate.props.fieldDef.value = "";
		}
	},

	validarCPF(cpf){
		if(cpf.length != 11 || cpf.replace(eval('/'+cpf.charAt(1)+'/g'),'') == '') {
		    return false;
		} else {
	   	    var d;
	   	    var c;
	        for(var n=9; n<11; n++) {
	            for(d=0, c=0; c<n; c++)
	            	d += cpf.charAt(c) * ((n + 1) - c);
	            d = ((10 * d) % 11) % 10;
	            if(cpf.charAt(c) != d)
	            	return false;
	        }
	      return true;
	   }
	},

	onSubmit(data) {
		data.token = this.props.params.token;
		data.birthdate = this.refs.registerForm.refs.birthdate.props.fieldDef.value;
		var birthdate = moment(data.birthdate,"DD/MM/YYYY");
		var actualDate = moment();
		var errorField = Validate.validationProfileUser(data, this.refs.registerForm);
		
		if(errorField){
			this.addAlertError(Messages.get("label.error.form"));
			return;
		}

		UserSession.dispatch({
			action: UserSession.ACTION_REGISTER_USER,
			data: data
		});
		return true;
	},
	
	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return (

			<div className="container-fluid">
				<ToastContainer ref="container" className="toast-top-center" />
				<div className="row">
					<div className="col-xs-12 text-center">
						<div className="fpdi-login-header">
							<img className="fpdi-login-brand" src={AppLogo} alt={Messages.get("label.forPdiLogo")} />
							<h3 className="fpdi-login-subtitle">{Messages.get("label.login.titleComplement")}<br/>{Messages.get("label.login.title")}</h3>
						</div>
					</div>
				</div>

				<div className="row">
					{this.state.valid ?
							<div className="col-md-4 col-md-offset-4">
								<div className="fpdi-card-login">		
									<div className="panel panel-default">
									  <div className="panel-heading"><p className="fpdi-login-title">{Messages.get("label.completeRegistration")}</p></div>
									  	<div className="panel-body">
									  		  <p className="fpdi-recover-password-title">{Messages.get("label.dataCompleteRegistration")}</p>
												<div className="fpdi-login-body">
														<VerticalForm
															ref="registerForm"
															onSubmit={this.onSubmit}
															fields={this.state.fields}
															store={UserSession}
															submitLabel={Messages.get("label.submit.finishRegistration")}
															blockButtons={true}
														/>
												</div>
										</div>
									</div>
								</div>
							</div>
						:
						<div className="col-md-4 col-md-offset-4">
							<div className="fpdi-login-header">
								<h1>{this.state.confirmed ? Messages.get("label.registrationSuccessfullyCompleted")  :
								 Messages.get("label.pageNotAvailable")}</h1>
								<p><a className="btn btn-sm btn-primary" href="#/">{Messages.get("label.returnToHomePage")}</a></p>
							</div>
						</div>
					}
				</div>

			</div>
		);
	}
});
