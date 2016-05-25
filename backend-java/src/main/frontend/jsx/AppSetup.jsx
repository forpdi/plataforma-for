
import Backbone from "backbone";
import Numeral from "numeral";
import React from "react";
import ReactDOM from "react-dom";
import { Router, Route, IndexRedirect, IndexRoute, hashHistory } from 'react-router';

import Messages from 'forpdi/jsx/core/util/Messages.jsx';

import Application from 'forpdi/jsx/Application.jsx';

import NotFound from "forpdi/jsx/core/view/NotFound.jsx";

import SystemManagement from 'forpdi/jsx/core/view/system/SystemManagement.jsx';
import Companies from 'forpdi/jsx/core/view/system/companies/Companies.jsx';
import CompanyEdit from 'forpdi/jsx/core/view/system/companies/CompanyEdit.jsx';
import CompanyDomains from 'forpdi/jsx/core/view/system/domains/CompanyDomains.jsx';
import CompanyDomainEdit from 'forpdi/jsx/core/view/system/domains/CompanyDomainEdit.jsx';

import StructureList from 'forpdi/jsx/planning/view/structure/StructureList.jsx';
import StructurePreview from 'forpdi/jsx/planning/view/structure/StructurePreview.jsx';

import Login from "forpdi/jsx/core/view/user/Login.jsx";
import RecoverPassword from "forpdi/jsx/core/view/user/RecoverPassword.jsx";
import Register from "forpdi/jsx/core/view/user/Register.jsx";
import ResetPassword from "forpdi/jsx/core/view/user/ResetPassword.jsx";
import UserEdit from "forpdi/jsx/core/view/user/UserEdit.jsx";
import UserInvite from "forpdi/jsx/core/view/user/UserInvite.jsx";
import Users from "forpdi/jsx/core/view/user/Users.jsx";

Numeral.language('pt-br', require("numeral/languages/pt-br.js"));
Numeral.language("pt-br");

ReactDOM.render((
	<Router history={hashHistory}>
		<Route path="/" component={Application}>
			<IndexRedirect to="login" />
			<Route path="login" component={Login} />
			<Route path="recover-password" component={RecoverPassword} />
			<Route path="register/:token" component={Register} />
			<Route path="reset-password/:token" component={ResetPassword} />
			<Route path="users" component={Users}>
				<Route path="edit/:modelId" component={UserEdit} />
				<Route path="new" component={UserInvite} />
			</Route>

			<Route path="structures" component={StructureList}>
				<Route path="preview/:modelId" component={StructurePreview} />
			</Route>

			<Route path="system" component={SystemManagement}>
				<IndexRedirect to="general" />
				<Route path="companies" component={Companies}>
					<Route path="new" component={CompanyEdit} />
					<Route path="edit/:modelId" component={CompanyEdit} />
				</Route>
				<Route path="domains" component={CompanyDomains}>
					<Route path="new" component={CompanyDomainEdit} />
					<Route path="edit/:modelId" component={CompanyDomainEdit} />
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
