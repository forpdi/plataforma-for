import React from "react";
import $ from "jquery";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import Messages from "forpdi/jsx/core/util/Messages";
import AttributeTypes from "@/planning/enum/AttributeTypes";
import FileStore from "forpdi/jsx/core/store/File.jsx"
import Validation from 'forpdi/jsx_forrisco/core/util/Validation.jsx';
import FPDIRichText from "forpdi/jsx/vendor/FPDIRichText";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

var Validate = Validation.validate;

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		planRisk: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {
			types: [{
				label: "Área de Texto",
				id: AttributeTypes.TEXT_AREA_FIELD
			}, {
				label: "Upload de Arquivo(PDF ou Imagem)",
				id: AttributeTypes.ATTACHMENT_FIELD
			}],

			fieldType: null,							//Tipo do campo (TextArea, ExportDocument)
			vizualization: this.props.vizualization,    //Habilita a visualização do field
			description: null,							//Valor do TextArea
			fileData: null								//Informações do DOC/IMG anexados
		}
	},

	changeFieldType() {
		var me = this;
		var selected = $('#fieldType').val();
		var typeTextField = this.state.types[0].id;
		var typeArquiveField = this.state.types[1].id;

		if (selected === typeTextField && selected !== null) {
			me.setType(typeTextField)
		}

		if (selected === typeArquiveField && selected !== null) {
			me.setType(typeArquiveField)
		}

		if (selected === '') {
			me.setType(null)
		}
	},

	addField() {
		var validation = Validate.validationNewFieldItem(this.refs, this.state.description);

		if (validation.errorField) {
			this.context.toastr.addAlertError(Messages.get("label.error.form")); //Validação dos campos
		}

		if (!validation.errorField) {
			if(validation.type.s === this.state.types[0].id) {
				this.props.fields.push({
					name: validation.name.s,
					type: validation.type.s,
					value: validation.name.s,
					description: validation.description,
					edit: false,
					fileLink: "",
					isText: true
				});
			}

			if(validation.type.s === this.state.types[1].id) {
				this.props.fields.push({
					name: validation.name.s,
					type: validation.type.s,
					value: validation.name.s,
					description: validation.description,
					edit: false,
					fileLink: this.state.fileData.fileLink
				});
			}
		}

		this.resetTypes();
	},

	removeField() {
		this.props.deleteFields(this.props.index)
	},

	setRichTextValue(value) {
		this.setState({
			description: value
		})
	},

	setType(type) {
		this.setState({
			fieldType: type
		})
	},

	resetTypes() {
		this.props.toggle();
		this.setState({fieldType: null})
	},

	attachFile() {
		var me = this;
		var title = Messages.get("label.insertAttachment");
		var msg = (
			<div>
				<p>
					{Messages.get("label.selectFile")}
				</p>
			</div>
		);
		var url = FileStore.url + "/uploadlocal";

		var onSuccess = function (resp) {
			Modal.hide();
			var file = {
				name: Modal.fileName,
				id: resp.data.id,
				description: Modal.fileName,
				fileLink: BACKEND_URL + "file/" +(resp.data.id),
				levelInstance: {
					id: me.props.levelInstanceId
				}
			};

			me.setState({
				fileData: file,
				description:Modal.fileName
			});
		};

		var onFailure = function (resp) {
			Modal.hide();
			me.setState({error: resp.message});
		};

		var formatsBlocked = "(exe*)";
		var maxSize = 2;
		var formats = "Imagens: gif, jpg, jpeg, jpg2, jp2, bmp, tiff, png, ai, psd, svg, svgz, Documentos: pdf\n";
		var formatsRegex = "gif|jpg|jpeg|jpg2|jp2|bmp|tiff|png|ai|psd|svg|svgz|pdf";

		Modal.uploadFile(title, msg, url, formatsRegex, formatsBlocked, onSuccess, onFailure, formats, maxSize);
	},

	render() {
		return (
			<div>
				{this.props.field ?
					<div>
						<div className="panel panel-default panel-margins">
							<div className="panel-heading attribute-input-opts">
								<b className="budget-title">{this.props.field.value}</b>
								{
									<span type="submit" className="mdi mdi-delete attribute-input-edit inner"
										  onClick={this.removeField}
										  title={Messages.get("label.title.deleteField")}/>
								}

								{
									<span className="mdi mdi-pencil attribute-input-edit inner"
										  title={Messages.get("label.title.changeField")}/>
								}
							</div>
							<span className="pdi-normal-text">
								<div id={this.props.field.name}> {this.props.field.description} </div>
							</span>
						</div>
					</div>

					:

					<div className="form-group form-group-sm marginTop20">
						<div className="row">
							{/* Nome do Campo*/}
							<div className="col-sm-6 col-md-4">
								<input type="text"
									   spellCheck={false}
									   className="form-control"
									   ref="newfield-name"
									   placeholder={Messages.get("label.field.name")}
									   maxLength="255"/>
							</div>

							{/* Tipo do Campo*/}
							<div className="col-sm-6 col-md-4">
								<select
									id="fieldType"
									spellCheck={false}
									className="form-control"
									ref="newfield-type"
									onChange={this.changeFieldType}
									placeholder={Messages.get("label.field.type")}>

									<option value=''>{Messages.get("label.selectTypeField")}</option>
									{
										this.state.types.map((type, idx) => {
											return (
												<option value={type.id} key={"attr-type-" + idx}>{type.label}</option>
											)
										})
									}
								</select>
							</div>

							{/*Botões Laterais*/}
							<div className="col-sm-12 col-md-4">
							<span className="mdi mdi-check btn btn-sm btn-success"
								  onClick={this.addField}
								  title={Messages.get("label.submitLabel")}/>
								<span>&nbsp;</span>
								<span className="mdi mdi-close btn btn-sm btn-danger"
									  title={Messages.get("label.cancel")}
									  onClick={this.resetTypes}
								/>
							</div>
						</div>

						<div className="row">
							<div className="col-sm-6 col-md-4">
								<div className="formAlertError" ref="formAlertErrorName"/>
							</div>
							<div className="col-sm-6 col-md-4">
								<div className="formAlertError" ref="formAlertErrorType"/>
							</div>
						</div>

						<div>
							{/*changeValue={this.changeRichText}*/}
							{/*defaultValue ={this.props.field? this.props.field.description:null}*/}

							{this.state.fieldType ?
								<div>
									{
										this.state.fieldType === this.state.types[0].id ?
											<FPDIRichText
												maxLength='6500'
												className="form-control minHeight170"
												id="newfield-description"
												placeholder="Insira seu texto..."
												changeValue={this.setRichTextValue}
												defaultValue={null}
											/>

											:

											<div className="fpdi-tabs-nav fpdi-nav-hide-btn">
												<a onClick={this.attachFile}>
													<span className="fpdi-nav-icon mdi mdi-file-import icon-link"/>
													<span className="fpdi-nav-label">
													{Messages.getEditable("label.attachFiles", "fpdi-nav-label")}
												</span>
												</a>
											</div>
									}
								</div>
								: ""}
						</div>
					</div>
				}
			</div>
		)
	}
})
