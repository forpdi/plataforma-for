
import React from "react";
import {Link} from 'react-router';

import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object
	},
	getInitialState() {
		return {
			loading: true,
			modelId: this.props.params.modelId,
			model: null
		};
	},
	componentDidMount() {
		var me = this;
		StructureStore.on("retrieve", (model) => {
			me.setState({
				loading: false,
				model: model
			});
		}, me);

		if (this.state.loading) {
			StructureStore.dispatch({
				action: StructureStore.ACTION_RETRIEVE,
				data: this.state.modelId
			});
		}
	},
	componentWillUnmount() {
		StructureStore.off(null, null, this);
	},
	
	renderLevels() {
		return <div>
			{this.state.model.get("levels").map((level) => {
				return <div>
					<div className="row">
						<div className="fpdi-card fpdi-card-level col-md-offset-4 col-md-4">
							<h3>{level.name}</h3>
							<p>{level.description}</p>
							<ul>
								{level.attributes.map((attr) => {
									return <li style={{fontWeight: attr.visibleInTables ? '500':'300'}}>
										{attr.label}, <i>{attr.required ? "obrigatório":"opcional"}</i>.
									</li>;
								})}
							</ul>
						</div>
					</div>
					{!level.leaf ? <div className="row">
						<div className="col-md-offset-5 col-md-2 text-center">
							<sapn className="mdi mdi-arrow-down-bold" />
						</div>
					</div>:""}
				</div>;
			})}
		</div>;
	},

	render() {
		console.log(this.state.model);
		return (<div className="container-fluid animated fadeIn">
			{this.state.loading ? <LoadingGauge />:<div>
				<h1>
					{this.state.model.get("name")}
				</h1>
				<p>{this.state.model.get("description")}</p>
				{this.renderLevels()}
			</div>}
		</div>);
	}
});
