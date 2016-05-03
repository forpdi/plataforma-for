
import Backbone from "backbone";
import Numeral from "numeral";
import React from "react";
import ReactDOM from "react-dom";
import { Router, Route, IndexRedirect, IndexRoute, hashHistory } from 'react-router';

import Application from 'forpdi/jsx/Application.jsx';

import MainMenu from "forpdi/jsx/core/widget/MainMenu.jsx";
import TopBar from "forpdi/jsx/core/widget/TopBar.jsx";
import NotFound from "forpdi/jsx/core/view/NotFound.jsx";

import SystemManagement from 'forpdi/jsx/core/view/system/SystemManagement.jsx';
import Companies from 'forpdi/jsx/core/view/system/companies/Companies.jsx';
import CompanyEdit from 'forpdi/jsx/core/view/system/companies/CompanyEdit.jsx';

import Login from "forpdi/jsx/core/view/user/Login.jsx";
import RecoverPassword from "forpdi/jsx/core/view/user/RecoverPassword.jsx";
import ResetPassword from "forpdi/jsx/core/view/user/ResetPassword.jsx";

Numeral.language('pt-br', require("numeral/languages/pt-br.js"));
Numeral.language("pt-br");

ReactDOM.render((
	<Router history={hashHistory}>
		<Route path="/" component={Application}>
			<IndexRedirect to="login" />
			<Route path="login" component={Login} />
			<Route path="recover-password" component={RecoverPassword} />
			<Route path="reset-password/:token" component={ResetPassword} />

			<Route path="system" component={SystemManagement}>
				<IndexRedirect to="general" />
				<Route path="companies" component={Companies}>
					<Route path="new" component={CompanyEdit} />
					<Route path="edit/:modelId" component={CompanyEdit} />
				</Route>
				<Route path="*" component={NotFound} />
			</Route>
			
			<Route path="*" component={NotFound} />
		</Route>
	</Router>
  	),
  	document.getElementById('main-body')
);

module.exports = true;
