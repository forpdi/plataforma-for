import React from "react";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import FPDIRichText from 'forpdi/jsx/vendor/FPDIRichText';
import Validation from 'forpdi/jsx_forrisco/core/util/Validation.jsx';
import FileStore from "forpdi/jsx/core/store/File.jsx"
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import $ from "jquery";

var Validate = Validation.validate;

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		policy: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			fileData: null,
			description: null,
			fileLink: null,
			newFieldType: null,
			buttonsErrorMark: false,
			types:[{label:"Área de Texto", id:AttributeTypes.TEXT_AREA_FIELD},{ label:"Upload de Arquivo(PDF ou imagem)", id:AttributeTypes.ATTACHMENT_FIELD}]
		}
	},

	componentWillReceiveProps(newProps){
		if (newProps.field) {
			this.state.description=newProps.field.description;
			newProps.field.isText ? null : this.state.fileLink = newProps.field.fileLink;
		}
		this.state.buttonsErrorMark = newProps.buttonsErrorMark;
	},
	onSelectFieldType(){
		var me = this;
			me.setState({
				newFieldType: document.getElementById("selectFieldType-"+(this.props.index)) ?  document.getElementById("selectFieldType-"+(this.props.index)).value : null
			});
	},


	attachFile(){
		var me = this;
		var title = Messages.get("label.insertAttachment");
		var msg = (
			<div>
				<p>
					{Messages.get("label.selectFile")}
				</p>
			</div>
		);
		var url = FileStore.url+"/uploadlocal";

		var onSuccess = function (resp) {
			Modal.hide();
			var file = {
				name: Modal.fileName,
				id: resp.data.id,
				description: Modal.fileName,
				fileLink: BACKEND_URL+"file/"+(resp.data.id),
				levelInstance: {
					id: me.props.levelInstanceId
				}
			}

			me.setState({
				//loading: true
				//description: Modal.fileName,
				fileData:{
					file: file
				},
				description:Modal.fileName
			});
			/*AttachmentStore.dispatch({
				action: AttachmentStore.ACTION_CREATE,
				data: file
			});*/
			//me.context.toastr.addAlertSuccess("Anexo salvo com sucesso! Talvez seja necessário atualizar a página para que os arquivos apareçam na lista.");
			me.context.toastr.addAlertSuccess("Arquivo enviado com sucesso");
		};
		var onFailure = function (resp) {
			Modal.hide();
			me.setState({error: resp.message});
			me.context.toastr.addAlertError("Falha no envio do arquivo");
		};

		var formatsBlocked = "(exe*)";
		var maxSize = 2;
		var formats = "Imagens: gif, jpg, jpeg, jpg2, jp2, bmp, tiff, png, ai, psd, svg, svgz, Documentos: pdf\n"
			//+", doc, docx, odt, rtf, txt, xml, xlsx, xls, ods, csv, ppt, pptx, ppsx, odp\n"
			//+"Áudio: MP3, WAV, WMA, OGG, AAC\n"
			//+"Vídeos: avi, mov, wmv, mp4, flv, mkv\n"
			//+"Arquivos: zip, rar, 7z, tar, tar.gz, tar.bz2\n";
		var formatsRegex = "gif|jpg|jpeg|jpg2|jp2|bmp|tiff|png|ai|psd|svg|svgz|pdf"
		//+"|doc|docx|odt|rtf|txt|xml|xlsx|xls|ods|csv|ppt|pptx|ppsx|odp|"+
		//"mp3|wav|wma|ogg|aac|"+
		//"avi|mov|wmv|mp4|flv|mkv|"+
		//"zip|rar|7z|tar|targz|tar.bz2";

		Modal.uploadFile({}, title, msg, url, formatsRegex, formatsBlocked, onSuccess, onFailure, formats, maxSize);
	},


	addNewField(extra, periodicity, evt) {
		var me = this;
		var validation = Validate.validationNewFieldItem(this.refs);

		if (validation.errorField) {
			this.context.toastr.addAlertError(Messages.get("label.error.form"));
		}else{
			if(this.props.field){
				if(this.props.field.edit){

					this.state.newFieldType = this.state.newFieldType ==null ? this.props.field.type : this.state.newFieldType
					var istext = this.state.newFieldType==AttributeTypes.TEXT_AREA_FIELD ? true : false

					this.props.setItem(this.props.index,{
						label: this.props.field.label,
						name: "attribute-"+me.props.getLength(),
						type: this.state.newFieldType,
						value: validation.name.s,
						description: validation.description,
						isText: istext ,
						fileLink:
							istext ?
								null
								:
								((this.state.fileData || this.state.fileLink) ?
									this.state.fileData ?
										this.state.fileData.file.fileLink
										:
										this.state.fileLink
									:
									null
								),
						edit:false
					})

					return
				}
			}
			if(validation.type.s == AttributeTypes.TEXT_AREA_FIELD){
				me.props.fields.push({
					name: "attribute-"+me.props.getLength(),
					type: validation.type.s,
					value: validation.name.s,
					description: validation.description,
					edit:false,
					fileLink:""
				});
			}else if(validation.type.s == AttributeTypes.ATTACHMENT_FIELD){
				if(this.state.fileData == null){
					this.context.toastr.addAlertError(Messages.get("label.error.file"));
					return
				}
				me.props.fields.push({
					name: "attribute-"+me.props.getLength(),
					type: validation.type.s,
					value: validation.name.s,
					description: validation.description,
					edit:false,
					fileLink: this.state.fileData.file.fileLink
				});
			}

			this.props.reset()
		}
	},

	tweakNewField() {
		if(this.props.index != null){
			this.props.editFunc(this.props.index,false)
		}
		this.setState({
			buttonsErrorMark: false,
		})
		this.props.reset()
	},
	delete(){
		this.props.deleteFunc(this.props.index)
	},
	edit(){
		this.props.editFunc(this.props.index,true)
	},

	render(){
		return (
		<div>
			{this.props.field && !this.props.field.edit ?
			<div>
				<div className="panel panel-default panel-margins">
					<div className="panel-heading attribute-input-opts">
						<b className="budget-title">{this.props.field.value}</b>
						{<span type="submit" className="mdi mdi-delete attribute-input-edit inner"
							title={Messages.get("label.title.deleteField")} onClick={this.delete}/>
						}
						{<span className="mdi mdi-pencil attribute-input-edit inner"
							title={Messages.get("label.title.changeField")} onClick={this.edit}/>
						}
					</div>
					<span className="pdi-normal-text">
						<div className="card-field-content" id={this.props.field.name}>
							{this.props.field.isText ?
								$(this.props.field.description).text()
								:
								<a target="_blank" rel="noopener noreferrer" href={this.props.field.fileLink}>
									{this.props.field.description}
								</a>
							}
						</div>
					</span>
				</div>
			</div>
			:
			<div className="form-group form-group-sm marginTop20">
				<div className="row">
					<div className="col-sm-6 col-md-4">
						{this.props.field && this.props.field.edit ?
							<input
								type="text"
								spellCheck={false}
								className="form-control"
								ref="newfield-name"
								placeholder={Messages.get("label.field.name")}
								maxLength="255"
								defaultValue ={this.props.field.value}
								/>
							:
							<input
								type="text"
								spellCheck={false}
								className="form-control"
								ref="newfield-name"
								placeholder={Messages.get("label.field.name")}
								maxLength="255"
								/>
						}
					</div>
					<div className="col-sm-6 col-md-4">
						{this.props.field && this.props.field.edit ?
							<select
							type="text"
							spellCheck={false}
							className="form-control"
							ref="newfield-type"
							placeholder={Messages.get("label.field.type")}
							onChange={this.onSelectFieldType}
							id={"selectFieldType-"+(this.props.index)}>
								<option value={this.props.field.type == this.state.types[0].id ? this.state.types[0].id : this.state.types[1].id}>{this.props.field.type == this.state.types[0].id ? this.state.types[0].label : this.state.types[1].label }</option>
								<option value={this.props.field.type == this.state.types[0].id ? this.state.types[1].id :this.state.types[0].id}
								 		key={"attr-type-1"}>{this.props.field.type == this.state.types[0].id ? this.state.types[1].label :this.state.types[0].label}</option>

						</select>
						: <select
								type="text"
								spellCheck={false}
								className="form-control"
								ref="newfield-type"
								placeholder={Messages.get("label.field.type")}
								onChange={this.onSelectFieldType}
								id={"selectFieldType-"+(this.props.index)}>
									<option value="">{Messages.get("label.selectTypeField")}</option>
									{this.state.types.map((type, idx) => {
										return <option value={type.id} key={"attr-type-"+idx}>{type.label}</option>
									})}
							</select>
						}
					</div>
					{this.state.buttonsHide?"":
						<div className="col-sm-12 col-md-4" >
							<span className={this.state.buttonsErrorMark ? 'buttons-check-and-close-error' : ''}>
								<span className="mdi mdi-check btn btn-sm btn-success" onClick={this.addNewField} title={Messages.get("label.submitLabel")}/>
								&nbsp;
								<span className="mdi mdi-close btn btn-sm btn-danger" onClick={this.tweakNewField} title={Messages.get("label.cancel")}/>
							</span>
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

					{(this.state.newFieldType==null) ? this.onSelectFieldType():""}

					{this.state.newFieldType || this.props.field ?
					<div>
						{this.state.newFieldType == this.state.types[0].id ?
							<FPDIRichText
								maxLength='6500'
								className="form-control minHeight170"
								id="newfield-description"
								placeholder="Insira seu texto..."
								ref="newfield-description"
								defaultValue ={this.props.field? this.props.field.description:null}
							/>
							:
							<div className="fpdi-tabs-nav fpdi-nav-hide-btn">
								<a onClick={this.attachFile}>
									<span className="fpdi-nav-icon mdi mdi-file-import icon-link"/>
									<span className="fpdi-nav-label">
										{ this.state.description ? this.state.description : (console.log(this.state), Messages.getEditable("label.attachFiles","fpdi-nav-label")) }
									</span>
								</a>
							</div>
						}
					</div>
					:""}
				</div>
			</div>
			}
		</div>)
	}
});



