import React from 'react';
import Marked from "marked";
import ItemStore from "forpdi/jsx_forrisco/planning/store/Item.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object.isRequired,
		policy: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			tabPath: this.props.location.pathname,
			item: ""
		};
	},
	componentDidMount() {

		if(this.context.policy.get('id')){
			ItemStore.dispatch({
				action: ItemStore.ACTION_RETRIEVE_INFO,
	 			data:{
					policyId:this.context.policy.get('id')
				 }
			 });

		/*
			 ItemStore.dispatch({
				action: ItemStore.ACTION_RETRIEVE_ITEM,
	 			id:this.context.policy.get('id')
	 		});
		*/

	 		ItemStore.on("retrieveInfo", (model) => {

				this.setState({
					loaded:true,
					item: model
				});
				this.context.tabPanel.addTab(this.state.tabPath, model.data.name);
			 });

 		} else {
 			this.context.router.push("forrisco/policy/"+this.context.policy.get('id')+"/"+this.context.policy.get('id'));
 		}
	},
	componentWillReceiveProps(newProps) {
		if (newProps.location.pathname != this.state.tabPath) {
			this.setState({
				tabPath: newProps.location.pathname
			});
			this.context.tabPanel.addTab(newProps.location.pathname, this.context.policy.get("name"));
		}
	},

	render() {
		if (!this.state.loaded) {
			return <LoadingGauge />;
		}
		return <div className="media-list">
			<h1>{this.state.item.data.name}</h1>
			<div className="media">
				<div className="media-body">
					<div className="fpdi-text-label">{Messages.get("label.title.description")}</div>
					<div className="pdi-normal-text"
						 dangerouslySetInnerHTML={{__html: Marked(this.state.item.data.description)}} />
					</div>
				</div>
			</div>;
		}
});
