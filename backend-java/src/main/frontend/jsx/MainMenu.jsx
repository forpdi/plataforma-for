import React from "react";
import {Link} from "react-router";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import _ from "underscore";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Observables from "forpdi/jsx/core/util/Observables.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";

import Logo from 'forpdi/img/logo.png';

export default React.createClass({
    contextTypes: {
        router: React.PropTypes.object,
        accessLevel: React.PropTypes.number.isRequired,
        accessLevels: React.PropTypes.object.isRequired,
        permissions: React.PropTypes.array.isRequired,
        roles: React.PropTypes.object.isRequired
    },
    getInitialState() {
        return {
            user: UserSession.get("user"),
            logged: !!UserSession.get("logged"),
            hidden: false,
            plans: [],
            domainError:true,
            archivedPlans: [],
            archivedPlansHidden:true
        };
    },
    componentDidMount() {
        var me = this;
        if (this.state.logged && EnvInfo.company != null) {
            me.refreshPlans();
        }
        UserSession.on("login", session => {
            me.setState({
                user: session.get("user"),
                logged: true
            });

            if(EnvInfo.company != null){
                me.refreshPlans();
            }
        }, me);
        UserSession.on("logout", session => {
            me.setState({
                user: null,
                logged: false,
                plans: null
            });
        }, me);

        PlanMacroStore.on("unarchivedplanmacrolisted", (store) => {
             if(store.status == 400){
                me.setState({
                    domainError:true
                });
            }else if(store.status == 200 || store.status == undefined){
                me.setState({
                    plans:  store.data,
                    domainError:false
                });
            }
        }, me);

        PlanMacroStore.on("archivedplanmacrolisted", (store) => {
            if(store.status == 400){
                me.setState({
                    domainError:true
                });
            }else if(store.status == 200 || store.status == undefined){
                me.setState({
                    archivedPlans: store.data,
                    domainError:false
                });
            }
        }, me);

        PlanMacroStore.on("planmacroarchived", (model) => {
            me.setState({
                archivedPlansHidden:false
            });
        }, me);

        PlanMacroStore.on("sync", (model) => {
            for(var i = 0; i < this.state.plans.length; i++) {
                if(this.state.plans[i].id == model.attributes.id) {
                    var plans = this.state.plans;
                    plans[i] = model.attributes;
                    this.setState({
                        plans: plans
                    })
                }
            }
        }, me);

        StructureStore.on("retrieve-level-instance-performance", (models) => {
            if (models && (models.length > 0)) {
                this.setState({
                    hidden: true
                });
            }
        }, me);

        /*PlanMacroStore.trigger("fail", (error) => {
            console.log(error);
            me.setState({
                domainError:true
            });
        }, me);    */

        me.checkRoute(me.props.location.pathname);

        //me.context.router.listenBefore((opts, next) => {
        //     /*me.checkRoute(opts.pathname);*/
        //    if (next)
        //        next();
        //});
    },
    componentWillUnmount() {
        UserSession.off(null, null, this);
        PlanMacroStore.off(null, null, this);
        StructureStore.off(null, null, this);
    },
    checkRoute(pathname) {
        this.setState({
            hidden: false
        });
        /*if (pathname && pathname.startsWith("/plan/")) {
            this.setState({
                hidden: false
            });
        } else {
            this.setState({
                hidden: false
            });
        }*/
    },


    onLogout() {
        UserSession.dispatch({
            action: UserSession.ACTION_LOGOUT
        });
    },
    tweakHidden() {
        var hidden = !this.state.hidden;
        this.setState({
            hidden: hidden
        });
        Observables.ResizeMenu.notify();

        PlanMacroStore.dispatch({
            action: PlanMacroStore.ACTION_MAIN_MENU_STATE,
            data: hidden
        });
    },

    refreshPlans() {
        PlanMacroStore.dispatch({
            action: PlanMacroStore.ACTION_FIND_UNARCHIVED
        });
        PlanMacroStore.dispatch({
            action: PlanMacroStore.ACTION_FIND_ARCHIVED
        });
    },

    listArchivedPlans(){
        if(this.state.archivedPlansHidden){
                this.setState({
                    archivedPlansHidden: false
                });
        }else{
            this.setState({
                    archivedPlansHidden: true
            });
        }
    },

    render() {
        if (!this.state.logged) {
            return <div style={{display: 'none'}} />;
        }
        return (<div className={(this.state.hidden ? 'fpdi-app-sidebar-hidden':'fpdi-app-sidebar')+' fpdi-tabs-stacked'}>

            <div className="fpdi-tabs-nav">
    			<Link to="/home" activeClassName="active">
                    <span className="fpdi-nav-icon mdi mdi-view-dashboard icon-link"
                    /> {Messages.getEditable("label.dashboard","fpdi-nav-label")}
                </Link>
    		</div>
            <div style={{height: "10px"}} />
            {this.state.plans && (this.state.plans.length > 0) ?
                this.state.plans.map((plan, index) => {
                    return <div className="fpdi-tabs-nav" key={"open-plan-"+index}>
                        <Link to={"/plan/"+plan.id+"/"} activeClassName="active">
                            <span className="fpdi-nav-icon mdi mdi-chart-bar icon-link" title = {plan.name}
                                />  <span className="fpdi-nav-label" title = {plan.name}>
                                    {(plan.name).length <= 24?plan.name:(plan.name).split("",20).concat(" ...")}
                                </span>
                        </Link>
                    </div>;
                })
            :""}

            {((this.context.roles.ADMIN || _.contains(this.context.permissions,
             PermissionsTypes.MANAGE_PLAN_MACRO_PERMISSION)) && !this.state.domainError) ?
                <div>
                    <div className="fpdi-tabs-nav">
                        <Link to="/plan/new" activeClassName="active">
                            <span className="fpdi-nav-icon mdi mdi-plus icon-link"/>
                                <span className="fpdi-nav-label">
                                    {Messages.getEditable("label.newPlan","fpdi-nav-label")}
                                </span>
                        </Link>
                    </div>
                    {this.state.archivedPlans && (this.state.archivedPlans.length > 0) ?
                        <div>
                            <div className="fpdi-tabs-nav">
                                <a onClick={this.listArchivedPlans}>
                                    <span className="fpdi-nav-icon mdi mdi-folder-lock icon-link"
                                        /> <span className="fpdi-nav-label">
                                            Planos arquivados
                                        </span>
                                        {this.state.hidden? "" : <span className={this.state.archivedPlansHidden ? "mdi mdi-chevron-down floatRight icon-link" : "mdi mdi-chevron-up floatRight icon-link"}/>}

                                </a>
                            </div>
                            {!this.state.archivedPlansHidden && !this.state.hidden ?
                                this.state.archivedPlans.map((plan, index) => {
                                    return <div className="fpdi-tabs-nav" key={"archived-plan-"+index}>
                                        <Link to={"/plan/"+plan.id+"/"} activeClassName="active marginLeft35" className="marginLeft35">
                                            <span className="fpdi-nav-icon mdi mdi-chart-bar icon-link" title = {plan.name}
                                                />  <span className="fpdi-nav-label" title = {plan.name}>
                                                    {(plan.name.length) <= 24?plan.name:(plan.name.split("",12).concat(" ..."))}
                                                </span>
                                        </Link>
                                    </div>;
                                })
                            :""}
                        </div>
                    :""}
                </div>
            : ""}



            <div style={{height: "10px"}} />
            <div className="fpdi-tabs-nav fpdi-nav-hide-btn">
                <a onClick={this.tweakHidden}>
                    <span className={"fpdi-nav-icon mdi "+(this.state.hidden ? "mdi-arrow-right-bold-circle icon-link":"mdi-arrow-left-bold-circle icon-link")}
                        /> <span className="fpdi-nav-label">
                            {Messages.getEditable("label.collapseMenu","fpdi-nav-label")}
                        </span>
                </a>
            </div>

            <span className="fpdi-fill" />
       	</div>);
    }
});
