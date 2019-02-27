import React from "react";
import {Link} from 'react-router';
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import string from "string";
import HideTabsBox from "@/planning/widget/plan/PlanMacroTabPanel";


export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object.isRequired
	},

	childContextTypes: {
		planRisk: React.PropTypes.object,
		tabPanel: React.PropTypes.object
	},

	propTypes: {
		planRisk: React.PropTypes.object.isRequired
	},

	getChildContext() {
		return {
			planRisk: this.props.planRisk,
			tabPanel: this
		};
	},

	getInitialState() {
		return {
			tabs: [],
			tabsHash: '',
			tabsHidden: [],
			showTabsHidden: false
		};
	},

	componentWillUnmount() {},

	componentDidMount() {
		//this.context.router.push("/forrisco/plan-risk/" + this.props.params.planRiskId + "/item/" + this.props.params.planRiskId + "/info");
	},

	addTab(path, title) {
		var tab, tabIndex = -1, hash = "", findPlan = -1;

		for (var t = 0; t < this.state.tabs.length; t++) {

			if (this.state.tabs[t].props.to === path) {
				tabIndex = t;
				hash += path + "|||" + title + "|||";
			} else {
				hash += this.state.tabs[t].props.to + "|||" + this.state.tabs[t].props.title + "|||";

				if (this.state.tabs[t].props.to === "/plan-risk/1/details") {
					findPlan = t;
				}
			}
		}

		tab = (
			<Link className="fpdi-mainTabs" role="tab" to={path} title={title} activeClassName="active" key={path}>
				{title.length > 14 ? string(title).trim().substr(0, 14).concat("...").toString() : title}
				<span className="mdi mdi-close-circle" onClick={this.removeTabByPath.bind(this, path)}/>
			</Link>
		);

		if (tabIndex >= 0) {
			this.state.tabs[tabIndex] = tab;
		} else {
			var tabIndex = -1;
			for (var t = 0; t < this.state.tabsHidden.length; t++) {
				if (this.state.tabsHidden[t].props.to === tab.props.to) {
					tabIndex = t;
				}
			}
			if (tabIndex >= 0)
				this.removeTabHidden(tabIndex);

			if (this.state.tabs.length >= 3) {
				this.state.tabsHidden.push(this.state.tabs[0]);
				this.removeTab(0, 'newTab');
			}
			this.state.tabs.push(tab);
			hash += path + "|||" + title + "|||";
			var tabs = this.state.tabs;
			for (var t = 0; t < this.state.tabsHidden.length; t++) {
				for (var i = 0; i < this.state.tabs.length; i++) {
					if (this.state.tabsHidden[t].key === this.state.tabs[i].key) {
						tabs = this.state.tabs.splice(i, 1);
					}
				}
			}
		}

		this.setState({
			tabsHash: hash,
			showTabsHidden: false,
			tabs: this.state.tabs
		});
	},

	removeTabByPath(path, event) {
		event && event.preventDefault();
		var tabIndex = -1;
		for (var t = 0; t < this.state.tabs.length; t++) {
			if (this.state.tabs[t].props.to === path) {
				tabIndex = t;
			}
		}
		if (tabIndex >= 0) {
			this.removeTab(tabIndex);
		} else {
			for (var t = 0; t < this.state.tabsHidden.length; t++) {
				if (this.state.tabsHidden[t].props.to === path) {
					tabIndex = t;
				}
			}
			if (tabIndex >= 0)
				this.removeTabHidden(tabIndex);
		}
	},

	removeTab(index, event) {
		var newPath = null,
			hash = "",
			tabs = [];
		for (var t = 0; t < this.state.tabs.length; t++) {
			if (t != index) {
				tabs.push(this.state.tabs[t]);
				hash += this.state.tabs[t].props.to + "|||" + this.state.tabs[t].props.title + "|||";
			}
		}
		if (this.context.router.isActive(this.state.tabs[index].props.to, false)) {
			if (tabs.length == 0) {
				newPath = "/plan-risk/" + this.props.params.id + "/details";
			} else {
				newPath = tabs[(index >= tabs.length) ? (tabs.length - 1) : index].props.to;
			}
			this.context.router.replace(newPath);
		}
		if (this.state.tabsHidden.length > 0 && event !== 'newTab') {
			tabs.push(this.state.tabsHidden[0]);
			this.removeTabHidden(0);
		}
		this.setState({
			tabs: tabs,
			tabsHash: hash
		});
	},

	removeTabHidden(index, event) {
		var newPath = null,
			hash = "",
			tabsHidden = [];
		for (var t = 0; t < this.state.tabsHidden.length; t++) {
			if (t != index) {
				tabsHidden.push(this.state.tabsHidden[t]);
				hash += this.state.tabsHidden[t].props.to + "|||" + this.state.tabsHidden[t].props.title + "|||";
			}
		}
		this.setState({
			tabsHidden: tabsHidden,
			tabsHash: hash
		});
	},

	showAllTabs() {
		if (this.state.showTabsHidden) {
			this.setState({
				showTabsHidden: false
			})
		} else {
			this.setState({
				showTabsHidden: true
			})
		}
	},

	render() {
		return (
			<div className="fpdi-tabs">

				<ul className={"fpdi-tabs-nav" + (this.state.tabs.length < 2 ? " hide-close" : " show-close")}
					role="tablist">
					{/* Lista de abas superior */}
					{this.state.tabs}
					{this.state.tabsHidden.length > 0 ?
						(
							<div className={"fpdi-tabs-hidden"+(this.state.showTabsHidden ? " show-border":"")}
								 onClick={this.showAllTabs}>
								<span className="mdi mdi-chevron-double-right"/>
								<span className="tabsNumber">{this.state.tabsHidden.length}</span>
								{this.state.showTabsHidden ?
									<HideTabsBox hideTabs={this.showAllTabs} tabs={this.state.tabsHidden} /> :""}
							</div>
						)
						: ""}
				</ul>

				{/* Conteúdo {this.props.children}*/}
				<div className="fpdi-tabs-content container-fluid">
					{this.props.children}
				</div>
			</div>
		)
	}
})
