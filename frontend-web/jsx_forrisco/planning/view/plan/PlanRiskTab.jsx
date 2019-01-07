import React from 'react';
import Messages from "@/core/util/Messages";
import VerticalInput from "forpdi/jsx_forrisco/planning/view/policy/PolicyEdit";
import Form from "react-bootstrap/es/Form";

export default React.createClass({
	getInitialState() {
		return {
			policyModel: null,
		};
	},

	getFields() {
		var fields = [];
		fields.push({
			name: "name",
			type: "text",
			required: true,
			maxLength: 240,
			placeholder: "Novo Plano de Gestão de Riscos",
			label: Messages.getEditable("label.name", "fpdi-nav-label"),
			value: this.state.policyModel ? this.state.policyModel.attributes.name : null,
		}, {
			name: "description",
			type: "textarea",
			placeholder: "Descrição da Política",
			maxLength: 9900,
			label: Messages.getEditable("label.descriptionPolicy", "fpdi-nav-label"),
			value: this.state.policyModel ? this.state.policyModel.attributes.description : null,
		})

		return fields;

	},

	render() {
		return (
			<div>
				<h1 className="marginLeft115">{Messages.getEditable("label.newPlan", "fpdi-nav-label")}  </h1>
				<div className="fpdi-card padding40">
					<form>
						{
							this.getFields().map((field, idx) => {
								return (
									<input key={idx} type={field.type} placeholder={field.placeholder}/>
								);
							})
						}
					</form>
				</div>
			</div>
		)
	}
});


/*if (this.context.router.isActive("forrisco/plan/new")) {
    return (<div><span> eae </span></div>)

}*/
