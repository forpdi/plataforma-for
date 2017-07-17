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
				name: "subject",
				type: "select",
				options: [
					{'subject': Messages.get("label.difficultyUse")},
					{'subject': Messages.get("label.doubt")},
					{'subject': Messages.get("label.newFeatures")},
					{'subject': Messages.get("label.paymentSubscription")},
					{'subject': Messages.get("label.perfilUser")},
					{'subject': Messages.get("label.improvementSuggestion")}
				],
				valueField: 'subject',
				displayField: 'subject',
				placeholder: Messages.get("label.selectSubject"),
				label: Messages.get("label.subject")
			},{
				name: "description",
				type: "textarea",
				rows: 4,
				placeholder: "",
				label: Messages.get("label.feedback")
			}]
		};
	},
	onSubmit(data) {
		ContactStore.dispatch({
			action: ContactStore.ACTION_CREATE_FEEDBACK,
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
					cancelLabel={Messages.get("label.cancel")}
					submitLabel={Messages.get("label.sendMenssage")}
				/>
			</div>
		);
	}
});
