
import React from 'react';
import Marked from "marked";
import DocumentStore from "forpdi/jsx/planning/store/Document.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object.isRequired,
		planMacro: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			tabPath: this.props.location.pathname
		};
	},
	componentDidMount() {
		if(this.context.planMacro.get('documented')){
			DocumentStore.dispatch({
				action: DocumentStore.ACTION_RETRIEVE,
	 			data: this.context.planMacro.get('id')
	 		});

	 		DocumentStore.on("retrieve", (model) => {
				this.setState({
					loaded:true,
					document:model.get('document')
				});
				this.context.tabPanel.addTab(this.state.tabPath, model.get("document").title);
	     	});
 		} else {
 			this.context.router.push("/plan/"+this.context.planMacro.get('id')+"/details");
 		}
	},
	componentWillReceiveProps(newProps) {
		if (newProps.location.pathname != this.state.tabPath) {
			this.setState({
				tabPath: newProps.location.pathname
			});
			this.context.tabPanel.addTab(newProps.location.pathname, this.context.planMacro.get("name"));


		}
	},

	render() {
		if (!this.state.loaded) {
			return <LoadingGauge />;
		}
		return <div className="media-list">
			<h1>{this.state.document.title}</h1>
			<div className="media">
				<div className="media-body">
					<div className="fpdi-text-label">{Messages.get("label.title.description")}</div>
					<div className="pdi-normal-text"
						 dangerouslySetInnerHTML={{__html: Marked(this.state.document.description)}} />
				</div>
			</div>
		</div>;
	}
});
