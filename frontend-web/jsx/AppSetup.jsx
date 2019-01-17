import Backbone from "backbone";
import Marked from "marked";
import Moment from 'moment';
import Numeral from "numeral";
import Toastr from 'toastr';

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

import PlanMacroDetails from 'forpdi/jsx/planning/view/plan/PlanMacroDetails.jsx';
import PlanMacroEdit from 'forpdi/jsx/planning/view/plan/PlanMacroEdit.jsx';

import LevelTab from 'forpdi/jsx/planning/view/plan/LevelTab.jsx';
import PlanMacroTab from 'forpdi/jsx/planning/view/plan/PlanMacroTab.jsx';

import DocumentSectionAttributes from 'forpdi/jsx/planning/view/document/DocumentSectionAttributes.jsx';
import DocumentDetails from 'forpdi/jsx/planning/view/document/DocumentDetails.jsx';

import LevelAttributeInstance from 'forpdi/jsx/planning/view/plan/LevelAttributeInstance.jsx';
import PlanRegister from 'forpdi/jsx/planning/view/plan/PlanRegister.jsx';
import StructureList from 'forpdi/jsx/planning/view/structure/StructureList.jsx';
import StructurePreview from 'forpdi/jsx/planning/view/structure/StructurePreview.jsx';

import Login from "forpdi/jsx/core/view/user/Login.jsx";
import RecoverPassword from "forpdi/jsx/core/view/user/RecoverPassword.jsx";
import Register from "forpdi/jsx/core/view/user/Register.jsx";
import ResetPassword from "forpdi/jsx/core/view/user/ResetPassword.jsx";
import UserEdit from "forpdi/jsx/core/view/user/UserEdit.jsx";
import UserInvite from "forpdi/jsx/core/view/user/UserInvite.jsx";
import Users from "forpdi/jsx/core/view/user/Users.jsx";
import DuplicatePlan from "forpdi/jsx/planning/view/plan/DuplicatePlan.jsx";
import ProfileUser from "forpdi/jsx/core/view/user/ProfileUser.jsx";

import Dashboard from "forpdi/jsx/dashboard/view/DashboardPanel.jsx";
import Community from "forpdi/jsx/dashboard/view/DashboardCommunityView.jsx";

import BudgetElement from "forpdi/jsx/planning/view/budget/BudgetElement.jsx";


/* Forrisco */
import Forrisco_Application from "forpdi/jsx/Application_Forrisco.jsx";
import Forrisco_Dashboard from "forpdi/jsx_forrisco/dashboard/view/DashboardPanel.jsx";
import Forrisco_PolicyEdit from "forpdi/jsx_forrisco/planning/view/policy/PolicyEdit.jsx";
import Forrisco_PolicyDetails from "forpdi/jsx_forrisco/planning/view/policy/PolicyDetails.jsx";
import Forrisco_ItemRegister from "forpdi/jsx_forrisco/planning/view/item/ItemRegister.jsx";
import Forrisco_SubItemRegister from "forpdi/jsx_forrisco/planning/view/item/SubItemRegister.jsx";
import Forrisco_PolicyTab from "forpdi/jsx_forrisco/planning/view/policy/PolicyTab.jsx";
import Forrisco_RiskList from "forpdi/jsx_forrisco/planning/view/risk/RiskList.jsx";

Moment.locale("pt_BR");
Numeral.language('pt-br', require("numeral/languages/pt-br.js"));
Numeral.language("pt-br");

Toastr.options.positionClass = "toast-top-full-width";
Toastr.options.timeOut = 4000;
Toastr.options.extendedTimeOut = 8000;

ReactDOM.render((
	<Router history={hashHistory}>
		<Route path="comunidade" component={Community} />
		<Route path="recover-password" component={RecoverPassword} />
		<Route path="reset-password/:token" component={ResetPassword} />
		<Route path="register/:token" component={Register} />


	<Route path="/forrisco" component={Forrisco_Application}>
		<Route path="home" component={Forrisco_Dashboard} />
		<Route path="risk" component={Forrisco_RiskList} />
		<Route path="policy" component={Forrisco_PolicyEdit} />

		<Route path="policy/:policyId">
			<IndexRedirect to="item" />
			<Route path="item" component={Forrisco_PolicyDetails}>
				<IndexRedirect to="overview" />
				<Route path="overview" component={Forrisco_PolicyTab} />
				<Route path="new" component={Forrisco_ItemRegister} />
				<Route path=":itemId/subitem/new" component={Forrisco_SubItemRegister} />
				<Route path=":itemId/subitem/:subitemId" component={Forrisco_SubItemRegister} />
				<Route path=":itemId" component={Forrisco_ItemRegister}/>
			</Route>
			<Route path="edit" component={Forrisco_PolicyDetails}>
				<IndexRedirect to="overview" />
				<Route path="overview" component={Forrisco_PolicyTab} />
			</Route>
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



	<Route path="/" component={Application}>
		<IndexRedirect to="login" />
		<Route path="home" component={Dashboard} />
		<Route path="login" component={Login} />
		<Route path="users" component={Users}>
			<Route path=":modelId/edit" component={UserEdit} />
			<Route path="new" component={UserInvite} />
			<Route path="profilerUser/:modelId" component={ProfileUser} />
		</Route>

		<Route path="structures" component={StructureList}>
			<Route path="preview/:modelId" component={StructurePreview} />
		</Route>


		<Route path="budget-element" component={BudgetElement} />

		<Route path="plan/new" component={PlanMacroEdit} />
		<Route path="plan/:id">
			<IndexRedirect to="document" />
			<Route path="edit" component={PlanMacroEdit} />
			<Route path="details" component={PlanMacroDetails}>
				<IndexRedirect to="overview" />
				<Route path="overview" component={PlanMacroTab} />
				<Route path="subplan/:subplanId" component={PlanRegister} />
				<Route path="subplans/new" component={PlanRegister} />
				<Route path="level/:subplanId/:levelId" component={LevelTab} />
				<Route path="subplan/level/:levelInstanceId" component={LevelAttributeInstance} />
				<Route path="duplicate" component={DuplicatePlan} />
			</Route>
			<Route path="document" component={PlanMacroDetails}>
				<IndexRedirect to="overview" />
				<Route path="overview" component={DocumentDetails} />
				<Route path="section/:sectionId" component={DocumentSectionAttributes} />
			</Route>
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
