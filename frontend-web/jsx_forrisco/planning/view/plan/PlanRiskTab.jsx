import React from 'react';
import Messages from "@/core/util/Messages";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";

export default React.createClass({
	getInitialState() {
		return {
			planRiskModel: null,
			submitLabel: "Salvar",
			cancelLabel: "Cancelar"
		};
	},

	setOptions() {
		var policiDescription = [];
		PolicyStore.on('unarchivedpolicylisted', (store) => {

			if (store.status === 400) {
				this.setState({domainError: true});
			}

			if (store.status === 200 || store.status === undefined) {
				store.data.map((attr) => {
					policiDescription.push(attr.description);
				});

				this.setState({
					options: policiDescription, domainError: false
				});
			}
		});

		return policiDescription
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
			value: this.state.planRiskModel ? this.state.planRiskModel.attributes.name : null,
		}, {
			name: "description",
			type: "textarea",
			placeholder: "Descrição da Política",
			maxLength: 9900,
			label: Messages.getEditable("label.descriptionPolicy", "fpdi-nav-label"),
			value: this.state.planRiskModel ? this.state.planRiskModel.attributes.description : null,
		}, {
			name: "linkedPolicy",
			type: "select",
			options: this.setOptions(),
			required: true,
			placeholder: "Selecone a Política",
			label: Messages.getEditable("label.descriptionPolicy", "dashboard-select-box"),
			value: this.state.planRiskModel ? this.state.planRiskModel.attributes.description : null,
		});

		return fields;
	},

	render() {
		return (
			<div>
				<h1 className="marginLeft115">{Messages.getEditable("label.newPlan", "fpdi-nav-label")}</h1>
				<div className="fpdi-card padding40">
					<form>
						{
							console.log(this.getFields())
							// this.getFields().map((field, index) => {
							// 	return (
							// 		<VerticalInput key={index} fieldDef={field}/>
							// 	);
							// })
						}

						<div className="fpdi-editable-data-input-group">
							<button type="submit" className="btn btn-success">{this.state.submitLabel}</button>
							<button type="submit" className="btn btn-default">{this.state.cancelLabel}</button>
						</div>
					</form>
				</div>
			</div>
		)
	}
});
