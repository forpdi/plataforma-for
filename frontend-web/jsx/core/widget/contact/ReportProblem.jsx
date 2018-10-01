import _ from "underscore";
import React from "react";
import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import ContactStore from "forpdi/jsx/core/store/Contact.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

var VerticalForm = Form.VerticalForm;

export default React.createClass({
	getDefaultProps() {
		return {};
	},
	getInitialState(args) {
		return {
			fields: [{
				name: "screen",
				type: "select",
				options: [
					{'screen': Messages.get("label.painelBordo")},
					{'screen': Messages.get("label.contractUs")},
					{'screen': Messages.get("label.plans")},
					{'screen': Messages.get("label.settings")}
				],
				valueField: 'screen',
				displayField: 'screen',
				placeholder: Messages.get("label.selectView"),
				label: Messages.get("label.screenProblemOccurred")
			},{
				name: "description",
				type: "textarea",
				rows: 4,
				placeholder: "",
				label: Messages.get("label.describeProblemBetter")
			}]
		};
	},
	onSubmit(data) {
		ContactStore.dispatch({
			action: ContactStore.ACTION_CREATE_PROBLEM,
			data: data
		});
	},
	componentDidMount() {
		var me = this;
		ContactStore.on("sync", model => {
			_.defer(() => {
				ContactStore.remove(model);
				if (typeof me.props.onSubmit === 'function')
					me.props.onSubmit();
			});
		}, me);
	},
	componentWillUnmount() {
		ContactStore.off(null, null, this);
	},
	render() {
		return (
			<div ref="form-ct">
				<VerticalForm
					onCancel={this.props.onCancel}
					onSubmit={this.onSubmit}
					fields={this.state.fields}
					store={ContactStore}
					cancelLabel={Messages.get("label.discard")}
					submitLabel={Messages.get("label.send")}
				/>
			</div>
		);
	}
});
