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
import ForPDIApplication from 'forpdi/jsx/ForPDI.jsx';
import ForRiscoApplication from "forpdi/jsx/ForRisco.jsx";

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
import Forrisco_Dashboard from "forpdi/jsx_forrisco/dashboard/view/DashboardPanel.jsx";
import Forrisco_PolicyEdit from "forpdi/jsx_forrisco/planning/view/policy/PolicyEdit.jsx";
import Forrisco_PolicyDetails from "forpdi/jsx_forrisco/planning/view/policy/PolicyDetails.jsx";
import Forrisco_PolicyGeneralInfo from "forpdi/jsx_forrisco/planning/view/policy/item/PolicyGeneralInfo.jsx"
import Forrisco_PolicyTab from "forpdi/jsx_forrisco/planning/view/policy/PolicyTab.jsx";
import Forrisco_ItemRegister from "forpdi/jsx_forrisco/planning/view/policy/item/ItemRegister.jsx";
import Forrisco_SubItemRegister from "forpdi/jsx_forrisco/planning/view/policy/item/SubItemRegister.jsx";

import Forrisco_RegistryPlanRisk from "forpdi/jsx_forrisco/planning/view/plan/RegistryPlanRisk.jsx";
import Forrisco_PlanRiskTabPanel from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTabPanel.jsx";
import Forrisco_PlanRiskGeneralInfo from "forpdi/jsx_forrisco/planning/view/plan/item/PlanRiskGeneralInfo.jsx"
import Forrisco_EditPlanRisk from "forpdi/jsx_forrisco/planning/view/plan/EditPlanRisk.jsx";
import Forrisco_DuplicatePlanRisk from "forpdi/jsx_forrisco/planning/view/plan/DuplicatePlanRisk.jsx";
import Forrisco_DetailPlanRisk from "forpdi/jsx_forrisco/planning/view/plan/DetailPlanRisk.jsx";
import Forrisco_DetailPlanRiskItem from "forpdi/jsx_forrisco/planning/view/plan/item/DetailPlanRiskItem.jsx"
import Forrisco_DetailPlanRiskSubItem from "forpdi/jsx_forrisco/planning/view/plan/item/subitem/DetailPlanRiskSubItem.jsx";
import Forrisco_PlanRiskRegistryItem from "forpdi/jsx_forrisco/planning/view/plan/item/PlanRiskRegistryItem.jsx";
import Forrisco_PlanRiskRegistrySubItem from "forpdi/jsx_forrisco/planning/view/plan/item/subitem/PlanRiskRegistrySubItem.jsx";

import Forrisco_RegistryUnit from "forpdi/jsx_forrisco/planning/view/unit/RegistryUnit.jsx";
import Forrisco_RegistrySubunit from "forpdi/jsx_forrisco/planning/view/unit/RegistrySubunit.jsx";
import Forrisco_UnitTabPanel from "forpdi/jsx_forrisco/planning/widget/unit/UnitTabPanel.jsx";
import Forrisco_UnitRegistryItem from "forpdi/jsx_forrisco/planning/view/unit/item/UnitRegistryItem.jsx";
import Forrisco_UnitGeneralInfo from "forpdi/jsx_forrisco/planning/view/unit/item/UnitGeneralInfo.jsx";

import Forrisco_RiskRegister from "forpdi/jsx_forrisco/planning/view/risk/RiskRegister.jsx";
import Forrisco_RiskDetail from "forpdi/jsx_forrisco/planning/view/risk/RiskDetail.jsx";
import Forrisco_RiskList from "forpdi/jsx_forrisco/planning/view/risk/RiskList.jsx";

Moment.locale("pt_BR");
Numeral.language('pt-br', require("numeral/languages/pt-br.js"));
Numeral.language("pt-br");

Toastr.options.positionClass = "toast-top-full-width";
Toastr.options.timeOut = 4000;
Toastr.options.extendedTimeOut = 8000;

ReactDOM.render((
	<Router history={hashHistory}>
		<Route path="/login" component={Login} />
		<Route path="/comunidade" component={Community} />
		<Route path="/recover-password" component={RecoverPassword} />
		<Route path="/reset-password/:token" component={ResetPassword} />
		<Route path="/register/:token" component={Register} />

		<Route path="/" component={Application}>
			<IndexRedirect to="/login" />
			<Route path="forrisco" component={ForRiscoApplication}>
				<Route path="home" component={Forrisco_Dashboard} />
				<Route path="risk" component={Forrisco_RiskList} />


				<Route path="policy/new" component={Forrisco_PolicyEdit} />

				<Route path="policy/:policyId" component={Forrisco_PolicyDetails}>
					<IndexRedirect to="item" />
					<Route path="item">
						<IndexRedirect to="overview"/>
						<Route path="overview" component={Forrisco_PolicyGeneralInfo} />
						<Route path="new" component={Forrisco_ItemRegister} />
						<Route path=":itemId/subitem/new" component={Forrisco_SubItemRegister} />
						<Route path=":itemId/subitem/:subitemId" component={Forrisco_SubItemRegister} />
						<Route path=":itemId" component={Forrisco_ItemRegister} />
					</Route>
					<Route path="edit" >
						<IndexRedirect to="overview" />
						<Route path="overview" component={Forrisco_PolicyTab} />
					</Route>
				</Route>

				<Route path="plan-risk/new" component={Forrisco_RegistryPlanRisk} /> /* Cadastrar novo plano de risco*/

				<Route path="plan-risk/:planRiskId" component={Forrisco_DetailPlanRisk}> /* Detalhar plano de risco*/
					<IndexRedirect to="item" />
					<Route path="item">
						<IndexRedirect to="overview" />
						<Route path="overview" component={Forrisco_PlanRiskGeneralInfo} />
						<Route path="new" component={Forrisco_PlanRiskRegistryItem} /> /* Novo item do plano de risco*/
						<Route path=":itemId" component={Forrisco_DetailPlanRiskItem} /> /* Detalhar Item de um Plano*/
						{//<Route path=":itemId/info" component={Forrisco_PlanRiskGeneralInfo} /> /* Informações gerais do plano de risco*/
						}
						<Route path=":itemId/edit" component={Forrisco_EditPlanRisk} />
						<Route path=":itemId/duplicate" component={Forrisco_DuplicatePlanRisk} />
						<Route path=":itemId/subitem/new" component={Forrisco_PlanRiskRegistrySubItem}/>
						<Route path=":itemId/subitem/:subItemId" component={Forrisco_DetailPlanRiskSubItem}/>
					</Route>


					<Route path="unit">
						<IndexRedirect to="overview" />
						<Route path="overview" />
						<Route path="new" component={Forrisco_RegistryUnit} />  		/* Nova unidade*/

						<Route path=":unitId">
							<Route path="risk">
								<Route path="new" component={Forrisco_RiskRegister} />		/* Novo risco*/
								<Route path=":riskId" component={Forrisco_RiskDetail}/>
							</Route>
							<Route
								path="info"
								component={props =>
									<Forrisco_UnitGeneralInfo {...props} isSubunit={false} />
								}
							/>
						</Route>

						<Route path=":unitId">
							<Route path="subunit/new" component={Forrisco_RegistrySubunit} /> /* Nova subunidade*/
							<Route
								path="subunit/:subunitId"
								component={props =>
									<Forrisco_UnitGeneralInfo {...props} isSubunit={true} />
								}
							/> /* Detalhar subunidade*/
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
			</Route>

			<Route path="" component={ForPDIApplication}>
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
			</Route>
			<Route path="*" component={NotFound} />
		</Route>
	</Router>
),
	document.getElementById('main-body')
);
module.exports = true;
