import React from "react";
import $ from "jquery";
import Messages from "forpdi/jsx/core/util/Messages";
import AttributeTypes from "@/planning/enum/AttributeTypes";
import FileStore from "forpdi/jsx/core/store/File.jsx"
import Validation from 'forpdi/jsx_forrisco/core/util/Validation.jsx';
import FPDIRichText from "forpdi/jsx/vendor/FPDIRichText";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

var Validate = Validation.validate;

$(document).ready(function () {

});

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

			//Tipo do campo (TextArea, ExportDocument) ENQUANTO INSTACIA DE EDIÇÃO
			fieldContent: null,
			fieldTypeOnEdit: this.props.vizualization ? null : this.props.field.isText ,
			fieldType: null,															//Tipo do campo (TextArea, ExportDocument)
			vizualization: this.props.vizualization,    								//Habilita a visualização do field
			description: null,															//Valor do TextArea
			fileData: null																//Informações do DOC/IMG anexados
		}
	},

	componentWillReceiveProps(newProps){
		this.state.fieldContent = newProps.field.fieldContent;
	},

	//MUDA A SELEÇÃO DO TIPO DO CAMPO ENQUANTO INSTACIA DE EDIÇÃO

	changeFieldTypeOnEdit() {
		var me = this;
		var selected =
			document.getElementById( 'fieldTypeOnEdit' + (this.props.index)) ?
				document.getElementById( 'fieldTypeOnEdit' + (this.props.index)).value : null;

		var typeTextField = this.state.types[0].id;
		var typeArquiveField = this.state.types[1].id;
		//
		if (selected === typeTextField && selected !== null) {
			me.setTypeOnEdit(true)
		}

		if (selected === typeArquiveField && selected !== null) {
			me.setTypeOnEdit(false)
		}

		if (selected === '') {
			me.setTypeOnEdit(null)
		}
	},

	setTypeOnEdit(bool) {
		this.setState({
			fieldTypeOnEdit: bool
		});

		this.props.editType(bool, this.props.index)
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

	setType(type) {
		this.setState({
			fieldType: type
		});
	},

	addField() {
		var validation = Validate.validationNewFieldType(this.refs, this.state.description);

		if (validation.errorField) {
			this.context.toastr.addAlertError(Messages.get("label.error.form")); //Validação dos campos
			return false;
		}

		console.log(validation);
		if (!validation.errorField) {
			if (validation.type.s === this.state.types[0].id) {
				if(validation.description !== null) {
					this.props.fields.push({
						fieldName: validation.name.s !== undefined ? validation.name.s : "",
						type: validation.type.s,
						value: validation.name.s,
						fieldContent: validation.description,
						editInstance: false,
						fileLink: "",
						isText: true
					});
				}
			}

			if (validation.type.s === this.state.types[1].id) {
				this.props.fields.push({
					fieldName: validation.name.s,
					type: validation.type.s,
					value: validation.name.s,
					fieldContent: validation.description,
					editInstance: false,
					fileLink: this.state.fileData.fileLink
				});
			}
		}
		this.resetTypes();
	},

	confirmEdit() {
		var validation = Validate.validationNewFieldType(this.refs, this.props.field.fieldName, this.props.field.fieldContent);

		if (validation.errorField) {
			this.context.toastr.addAlertError(Messages.get("label.error.form")); //Validação dos campos
			return false;
		}

		if(!validation.errorField) {
			this.props.editRichTextField(this.state.fieldContent, this.props.index);
			this.props.editFieldTitle(validation.name.s, this.props.index);
			this.props.setFieldValue(this.props.field, this.props.index);
		}
	},

	removeField() {
		this.props.deleteFields(this.props.index)
	},

	editField() {
		this.props.editFields(this.props.index, true)
	},

	setRichTextValueOnEdit(value) {
		this.setState({
			fieldContent: value
		})
	},

	setRichTextValue(value) {
		this.setState({
			description: value
		})
	},

	resetTypes() {
		this.props.editFields(this.props.index, false);
	},

	setImgValue(fieldImg) {
		this.props.editImg(fieldImg, this.props.index);
	},

	attachFileOnEdit() {

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
				fieldName: Modal.fileName,
				id: resp.data.id,
				fieldContent: Modal.fileName,
				fileLink: BACKEND_URL + "file/" + (resp.data.id),
				isText: false,
				levelInstance: {
					id: me.props.levelInstanceId
				}
			};

			me.setState({
				fileData: file,
				description: Modal.fileName
			});

			me.setImgValue(file);
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
				fileLink: BACKEND_URL + "file/" + (resp.data.id),
				levelInstance: {
					id: me.props.levelInstanceId
				}
			};

			me.setState({
				fileData: file,
				description: Modal.fileName
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
				{
					this.props.field ?

					<div>
						{
							// /* CADASTRAMENTO */
							this.props.field.name ?

								<div className="panel panel-default panel-margins">
									<div className="panel-heading attribute-input-opts">
										<b className="budget-title">
											{
												this.props.field.name ?
													this.props.field.value     //Proveniente do Cadastro
													:
													this.props.field.fieldName //Proveniente do Edição
											}

										</b>
										{
											<span type="submit" className="mdi mdi-delete attribute-input-edit inner"
												  onClick={this.removeField}
												  title={Messages.get("label.title.deleteField")}/>
										}

										{
											<span className="mdi mdi-pencil attribute-input-edit inner"
												  title={Messages.get("label.title.changeField")}
												  onClick={this.editField}/>
										}
									</div>
									<span className="pdi-normal-text">
										<h1>Hoje</h1>
										<div id={this.props.field.name}> Account{this.props.field.description} </div>
									</span>
								</div>

								// /* EDIÇÃO */
								:
								<div>
									{
										this.props.field.editInstance !== true ?

											this.props.field.isText === true ?
												<div className="panel panel-default panel-margins">
													<div className="panel-heading attribute-input-opts">
														<b className="budget-title">{this.props.field.fieldName} </b>
														{
															<span type="submit"
																  className="mdi mdi-delete attribute-input-edit inner"
																  onClick={this.removeField}
																  title={Messages.get("label.title.deleteField")}/>
														}

														{
															<span className="mdi mdi-pencil attribute-input-edit inner"
																  onClick={this.editField}
																  title={Messages.get("label.title.changeField")}/>
														}
													</div>
													<span className="pdi-normal-text">
														<div
															id={this.props.field.fieldName}> {
																this.props.field.fieldContent?
																this.props.field.fieldContent.replace(/(?:<style.+?>.+?<\/style>|<script.+?>.+?<\/script>|<(?:!|\/?[a-zA-Z]+).*?\/?>)/g, "")
																:""
															}
														</div>
													</span>
												</div>
												:
												<div className="panel panel-default panel-margins">
													<div className="panel-heading attribute-input-opts">
														<b className="budget-title"> {this.props.field.fieldName} </b>
														<span type="submit"
															  className="mdi mdi-delete attribute-input-edit inner"
															  onClick={this.removeField}
															  title={Messages.get("label.title.deleteField")}/>

														<span className="mdi mdi-pencil attribute-input-edit inner"
															  onClick={this.editField}
															  title={Messages.get("label.title.changeField")}/>
													</div>
													<span className="card-field-content pdi-normal-text">
														<div id={this.props.field.fieldName}>
															{this.props.field.fileLink ?
																<a target="_blank" rel="noopener noreferrer" href={this.props.field.fileLink}>
																	{this.props.field.fieldContent}
																</a>
																:
																$(this.props.field.fieldContent).text()
															}
															{/* {this.props.field.fieldContent} */}
														</div>
													</span>
												</div>
											:

											// /*INSTANCIA DE EDIÇÃO*/ //
											<div className="form-group form-group-sm marginTop20">
												<div className="row">
													{/* Nome do Campo*/}
													<div className="col-sm-6 col-md-4">
														<input type="text"
															   id="itemTitle"
															   name="itemTitle"
															   spellCheck={false}
															   className="form-control"
															   ref="newfield-name"
															   placeholder={Messages.get("label.field.name")}
															   defaultValue={this.props.field.fieldName}
															   maxLength="255"/>
													</div>

													{/* Tipo do Campo*/}
													<div className="col-sm-6 col-md-4">
														<select
															id={"fieldTypeOnEdit" + this.props.index}
															spellCheck={false}
															className="form-control"
															ref="newfield-type"
															onChange={this.changeFieldTypeOnEdit}
															placeholder={Messages.get("label.field.type")}>

															{/*<option value={this.state.types[0].id}>*/}
																{/*{this.state.types[0].label}*/}
															{/*</option>*/}

															{/*<option key={"attr-type-1"} ref={this.props.field.isText} value={this.state.types[1].id}>*/}
																{/*{this.state.types[1].label}*/}
															{/*</option>*/}

															<option
																value={this.props.field.isText === true ? this.state.types[0].id : this.state.types[1].id}>
																{this.props.field.isText === true ? this.state.types[0].label : this.state.types[1].label}
															</option>

															<option key={"attr-type-1"} ref={this.props.field.isText}
																	value={this.props.field.isText === true ? this.state.types[1].id : this.state.types[0].id}>
																{this.props.field.isText === true ? this.state.types[1].label : this.state.types[0].label}
															</option>

														</select>
													</div>

													{/*Botões Laterais*/}
													<div className="col-sm-12 col-md-4">
														<span className="mdi mdi-check btn btn-sm btn-success"
															  onClick={this.confirmEdit}
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

													{
														<div>
															{
																this.props.field.isText === true && this.state.fieldTypeOnEdit === true ?
																	<FPDIRichText
																		maxLength='6500'
																		className="form-control minHeight170"
																		id={this.props.field.isText}
																		placeholder="Insira seu texto..."
																		changeValue={this.setRichTextValueOnEdit}
																		defaultValue={this.props.field.fieldContent}
																		required={true}
																	/>

																	:

																	<div className="fpdi-tabs-nav fpdi-nav-hide-btn">
																		<a onClick={this.attachFileOnEdit}>
																			<span
																				className="fpdi-nav-icon mdi mdi-file-import icon-link"/>
																			<span className="fpdi-nav-label">
																				{Messages.getEditable("label.attachFiles", "fpdi-nav-label")}
																			</span>
																		</a>
																	</div>
															}
														</div>
													}
												</div>
											</div>
									}

								</div>
						}

					</div>

					//CADASTRAMENTO - Quando não á nenhum campo adicionado
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
									  onClick={this.props.fields ? this.addField : this.confirmEdit}
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
