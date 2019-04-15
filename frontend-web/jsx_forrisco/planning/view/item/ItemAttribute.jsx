import React from "react";
import _ from 'underscore';
import ItemStore from "forpdi/jsx_forrisco/planning/store/Item.jsx";
import AttributeForm from "forpdi/jsx_forrisco/planning/widget/attributeForm/AttributeForm.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import TableFieldCreator from "forpdi/jsx/planning/view/document/TableFieldCreator.jsx";
import ScheduleFieldCreator from "forpdi/jsx/planning/view/document/ScheduleFieldCreator.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';


var Validate = Validation.validate;

var VerticalForm = AttributeForm.VerticalForm;

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object.isRequired,
		policy: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object.isRequired,
		toastr: React.PropTypes.object.isRequired,
		accessLevel: React.PropTypes.number.isRequired,
        accessLevels: React.PropTypes.object.isRequired,
        permissions: React.PropTypes.array.isRequired
	},

	getInitialState() {
		return {
			loading: true,
			model: null,
			subTitle: "",
			description: [],
			types: null,
			newField: false,
			tabPath: this.props.location.pathname,
			newFieldType: "",
			buttonsHide: false,
			tableColumns: [],
			vizualization: true
		};
	},

	getField(model) {
		var fields = [];
		if (model.attributes) {
			fields =[{

				id: model.attributes.id,
				name: "attribute-",
				type: model.attributes.type,
				required: model.attributes.required,
				placeholder: "",
				label: model.attributes.name,
				value: model.attributes.description
			}]
		}
		return fields;
	},

	getItemModel(Id){

		ItemStore.dispatch({
			action: ItemStore.ACTION_RETRIEVE_ITEM,
			data: {
				id: Id
			}
		});
	},

	componentDidMount() {
		var me = this;

		me.getItemModel(me.props.params.policyId);

		ItemStore.on('retrieveItem', (model) => {
			me.setState({
				loading: false,
				modelo: model,
				subTitle: model.attributes.name,
				description: model.attributes.description,
				vizualization: true,
				description: me.getField(model),
			})
		});
	},
	editingAttributes () {
		this.setState({
			vizualization: !this.state.vizualization
		});
	},

	onSubmit(data) {

		var me = this;
		var documentAttributes = [];
		var msg = Messages.get("label.msg.errorsForm");
		var boolMsg = false;

		var index = 0;

		//var validation = Validate.validateSectionTitle(this.refs["fieldName"],this.refs['formAlertTitleSection']);


		for (var i=0; i<Object.keys(data).length; i++) {
			var tr = data[Object.keys(data)[i]];
			if(tr != null || tr != undefined) {
				if(tr.replace("<p>","").replace("</p>","").replace("<br>","").replace("&nbsp;","").trim() ==""){
					tr = null;
				}
				/*if(tr == null && this.state.model.documentAttributes[i].required == true){
					boolMsg = true;
					msg += this.state.fields[i].label+'<br/>';
				}*/
				if(!boolMsg){
					while (this.state.model.documentAttributes[index].type == AttributeTypes.SCHEDULE_FIELD
							|| this.state.model.documentAttributes[index].type == AttributeTypes.TABLE_FIELD) {
						index = index+1;
					}
					documentAttributes.push({
						id: this.state.model.documentAttributes[index].id,
						value: tr
					});
					index = index+1;
				}
			}
		}
		if(boolMsg){
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError(msg);
			return;
		}


		if (validation) {
			this.context.toastr.addAlertError(msg);
			return;
		}



		var documentSection = {
			id: this.state.model.id,
			name: this.refs["fieldName"].value,
			documentAttributes: documentAttributes
		};
		DocumentStore.dispatch({
			action: DocumentStore.ACTION_SAVE_SECTIONATTRIBUTES,
			data: {
				documentSection: documentSection,
				refs: me.refs
			}
		});
	},

	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return(
		<div>
			<div>
				<h1>
					{this.state.subTitle}

					{((this.context.roles.MANAGER || _.contains(this.context.permissions, PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)))//&& !this.context.policy.get('id'))
					?
						(this.state.vizualization == true ?
							<i className="mdi mdi-pencil cursorPointer deleteIcon" onClick={this.editingAttributes} title={Messages.get("label.title.editInformation")}/>
						: "")
					: ""}

					{((this.context.roles.MANAGER || _.contains(this.context.permissions,
					    PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.policy.get('get'))  ? //&& this.state.model.leaf ?
						<i type="submit" className="mdi mdi-delete cursorPointer deleteIcon" onClick={this.deleteSection}
						title={Messages.get("label.deleteSection")} ></i>
					: ""}
				</h1>

				{	!this.state.vizualization ?
					<div className="panel panel-default panel-margins">
						<div className="panel-heading attribute-input-opts">
							<b className="budget-title">{Messages.getEditable("label.title", "fpdi-nav-label")}</b>
						</div>
						<div>
							<input
								className="form-control"
								maxLength='100'
								onKeyPress={this.onKeyUp}
								onPaste={this.onKeyUp}
								name='name'
								defaultValue={this.state.modelo.attributes.name}
								id='fieldName'
								ref='fieldName'
								placeholder={Messages.get("label.title.section")}
								onKeyPress={this.onKeyUp}

							/>
							<div ref="formAlertTitleSection" className="formAlertError"></div>

						</div>
					</div>
				: ""}

				<VerticalForm
					onSubmit={this.onSubmit}
					vizualization={this.state.vizualization}
					fields={this.state.description}
					store={ItemStore}
					alterable={true}
					undeletable={true}
					onCancel={this.onCancel}
					deleteFunc={this.deleteAttribute}
					editFunc={this.editAttribute}
					submitLabel={Messages.get("label.submitLabel")}
					isDocument={false}
					onSubmit={this.onSubmit}/>
				{this.state.newField ?
					<div className="form-group form-group-sm marginTop20">
						<div className="row">
							<div className="col-sm-6 col-md-4">
								<input
									type="text"
									spellCheck={false}
									className="form-control"
									ref="newfield-name"
									placeholder={Messages.get("label.field.name")}
									maxLength="255"
									/>
							</div>
							<div className="col-sm-6 col-md-4">
								<select
									type="text"
									spellCheck={false}
									className="form-control"
									ref="newfield-type"
									placeholder={Messages.get("label.field.type")}
									onChange={this.onSelectFieldType}
									id="selectFieldType">
										<option value="">{Messages.get("label.selectTypeField")}</option>
										{this.state.types.map((type, idx) => {
											return <option value={type.id} key={"attr-type-"+idx}>{type.label}</option>
										})}
								</select>
							</div>
							{this.state.buttonsHide?"":
								<div className="col-sm-12 col-md-4" >
									<span className="mdi mdi-check btn btn-sm btn-success" onClick={this.saveNewField2} title={Messages.get("label.submitLabel")}/>
					            	<span>&nbsp;</span>
					            	<span className="mdi mdi-close btn btn-sm btn-danger" onClick={this.tweakNewField} title={Messages.get("label.cancel")}/>
								</div>
							}
						</div>
						<div className="row">
							<div className="col-sm-6 col-md-4">
								<div className="formAlertError" ref="formAlertErrorName"></div>
							</div>
							<div className="col-sm-6 col-md-4">
								<div className="formAlertError" ref="formAlertErrorType"></div>
							</div>
						</div>
						<div>
							{this.state.newFieldType == AttributeTypes.TABLE_FIELD ?
								<TableFieldCreator cancelFunc={this.tweakNewField} confirmFunc={this.saveNewField2} deleteFunc={this.deleteAttribute}/>
							: <div></div>}
							{this.state.newFieldType == AttributeTypes.SCHEDULE_FIELD ?
								<ScheduleFieldCreator types={this.state.types} cancelFunc={this.tweakNewField}
								confirmFunc={this.saveNewField2}/>
							: <div></div>}
						</div>
					</div>
					:
					(((this.context.roles.MANAGER || _.contains(this.context.permissions,
					    PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.policy.get('id')) ? //&& !this.state.model.preTextSection) ?
						<button onClick={this.tweakNewField} id="addIconDocument" className="btn btn-sm btn-neutral marginTop20">
							<span className="mdi mdi-plus" /> {Messages.get("label.addNewField")}
						</button>
					:"")
					}
			</div>
	    </div>);
	}
});
