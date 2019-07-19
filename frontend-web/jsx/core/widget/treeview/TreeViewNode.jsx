import $ from 'jquery';
import _ from 'underscore';
import React from "react";
import {Link} from 'react-router';
//import Toastr from 'toastr';
import string from 'string';

import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired
	},
	propTypes: {
		hidden: React.PropTypes.bool.isRequired,
		expandedIconCls: React.PropTypes.string,
		iconCls: React.PropTypes.string,
		label: React.PropTypes.string.isRequired,
		to: React.PropTypes.string,
		expanded: React.PropTypes.bool.isRequired,
		expandable: React.PropTypes.bool.isRequired,
		onNewNode: React.PropTypes.func,
		onExpand: React.PropTypes.func,
		onShrink: React.PropTypes.func,
		nodeIndex: React.PropTypes.number.isRequired,
		nodeLevel: React.PropTypes.number.isRequired,
		node: React.PropTypes.object.isRequired,
		newNodePlaceholder: React.PropTypes.string
	},
	getInitialState() {
		return {
			newNode: false
		};
	},
	getDefaultProps() {
		return {
			hidden: false,
			expandedIconCls: 'mdi mdi-chevron-down',
			iconCls: 'mdi mdi-chevron-right',
			expanded: false,
			expandable: false,
			newNodePlaceholder: Messages.get("label.nameLevel")
		};
	},
	componentDidMount() {
		var me = this;
	},
	componentWillUnmount() {

	},
	componentWillReceiveProps() {

	},

	onIconClick(event) {
		event.preventDefault();
		if (this.props.expanded) {
			if (typeof this.props.onShrink == 'function') {
				this.props.onShrink(this.props);
			}
		} else {
			if (typeof this.props.onExpand == 'function') {
				this.props.onExpand(this.props);
			}
		}
	},
	onLabelClick(event){
		if (this.props.expandable && !this.props.expanded) {
			if (typeof this.props.onExpand == 'function') {
				this.props.onExpand(this.props);
			}
		} else if(this.props.node.key == "newPlan"){
			location.assign("#"+this.props.to);
		} else if(this.props.labelCls == "fpdi-new-node-label" || this.props.className == "fpdi-new-node-label") {
			this.setState({
				newNode: true
			});
		}

	},

	onNewNodeClick(event){
		this.setState({
			newNode: true
		});
	},

	aproveNewNode(btn){
		if (typeof this.props.onNewNode == 'function'){
			if (this.props.onNewNode(this.refs.nodeName.value, this.props)) {
				this.setState({
					newNode: false
				});
			}
		} else{
			this.setState({
				newNode: false
			});
		}
	},

	cancelNewNode(){
		this.setState({
			newNode: false
		});
	},

	onKeyDown(evt){
		var key = evt.which;
		if(key == 13) {
			evt.preventDefault();
			this.aproveNewNode();
		}
		else if(key == 27) {
			evt.preventDefault();
			this.cancelNewNode();
		}
	},

	componentDidUpdate() {
		if (this.refs['nodeName']) {
			$(this.refs['nodeName']).focus();
		}
	},

	renderNewNode(){
		return(
			<div className={"fpdi-treeview-node"+(this.props.hidden ? " hidden":"")}>
				<textarea className="fpdi-new-node-textarea" placeholder={this.props.newNodePlaceholder} maxLength='255' ref="nodeName" onKeyDown={this.onKeyDown}/>
				<div className="row">
					<div className="col-xs-12 text-right">
						<span className="mdi mdi-check fdpi-accepted-new-node" onClick={this.aproveNewNode} title={Messages.get("label.save")}/>
						<span>&nbsp;</span>
						<span className="mdi mdi-close fdpi-rejected-new-node" onClick={this.cancelNewNode} title={Messages.get("label.cancel")}/>
					</div>
				</div>
			</div>
			);
	},

	render() {
		var iconCls = this.props.iconCls;
		var me = this;
		/*if(this.props.model && this.props.model.level && this.props.model.level.name == "Objetivo") {
			this.props.model.haveBudget = true;
		}*/
		return (
			(this.state.newNode ?
				this.renderNewNode()
				:
				(
					<div className={"fpdi-treeview-node"+(this.props.hidden ? " hidden":"")}>
					{
						this.props.expandable ?
							<a className={this.props.expanded ? this.props.expandedIconCls : iconCls}
							onClick={this.onIconClick} />
						:
							(this.props.model && this.props.model.aggregate ?
								<a className={this.props.iconCls}  onClick={me.onLabelClick} title={Messages.get("label.indicatorAggregate")}/>
							:
								<a className={this.props.iconCls}  onClick={me.onLabelClick} />)
					}
					{
						this.props.to ?
						(
							this.props.root ?
							<a onClick={this.onLabelClick} key={this.props.node.index}>{this.props.label}</a>
							:
							<Link
								activeClassName="active"
								className={this.props.labelCls}
								to={this.props.to}
								onClick={this.onLabelClick}
								title={this.props.label}
							>
							{
							this.props.label.length > 30 ?
								string(this.props.label).trim().substr(0, 30).concat("...").toString()
							:
								this.props.label
							}
							{
								(this.props.model && this.props.model.haveBudget)?
								<i className="mdi mdi-cash-usd budgetCoinIcon" title={Messages.get("label.title.levelHasBudget")}></i>
								:""
							}
							{/*
								(this.props.model && this.props.model.aggregate)?
								<span className="label label-primary aggregate-icon" title="Esse nível é um indicador agregado">A</span>
								:""
							*/}
							</Link>
							)
						:
						<a className={this.props.labelCls} onClick={this.onNewNodeClick}>{this.props.label}</a>
					}

					{this.props.expanded ? this.props.children:""}

					</div>
					)
				)
			);
	}
});
