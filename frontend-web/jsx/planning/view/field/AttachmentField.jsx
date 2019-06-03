import React from "react";
import AttachmentStore from "forpdi/jsx/planning/store/Attachment.jsx"
import FileStore from "forpdi/jsx/core/store/File.jsx"
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import _ from 'underscore';
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx"
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
		accessLevel: React.PropTypes.number.isRequired,
		accessLevels: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		roles: React.PropTypes.object.isRequired
	},

	getDefaultProps() {
		return {}
	},

	getInitialState() {
		return {
			hide: false,
			list: [],
			total: 0,
			editId: null,
			anyCheck: false,
			loading: true
		}
	},

	componentDidMount()	{
		var me = this;
		me.getAttachments(1, 5);

		AttachmentStore.on("sync", (model) => {
			me.context.toastr.addAlertSuccess(Messages.get("label.success.savedAttachment"));
			me.getAttachments(1,5);
		}, this);
		AttachmentStore.on("attachmentList", (model) => {
			me.setState({
				list: model.data,
				total: model.total,
				loading: false
			});
		}, this);
		AttachmentStore.on("attachmentDeleted", (model) => {
			me.context.toastr.addAlertSuccess(Messages.get("label.success.attachmentDelete"));
			me.getAttachments(1,5);
		}, this);
		AttachmentStore.on("attachmentUpdated", (model) => {
			me.context.toastr.addAlertSuccess(Messages.get("label.success.attachmentRefresh"));
			me.state.list.map((attachment) => {
				if(attachment.id == model.data.id){
					attachment.description = model.data.description;
				}
			});
			me.cancelEdit();
		}, this);
	},

	componentWillUnmount() {
		AttachmentStore.off(null, null, this);
		FileStore.off(null, null, this);
	},

	hideFields(){
		this.setState({
			hide: !this.state.hide
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
		var url = FileStore.url+"/upload";
		var formatsRegex = "gif|jpg|jpeg|jpg2|jp2|bmp|tiff|png|ai|psd|svg|svgz|"+
		"pdf|doc|docx|odt|rtf|txt|xml|xlsx|xls|ods|csv|ppt|pptx|ppsx|odp|"+
		"mp3|wav|wma|ogg|aac|"+
		"avi|mov|wmv|mp4|flv|mkv|"+
		"zip|rar|7z|tar|targz|tar.bz2";
		var formatsBlocked = "(exe*)";
		var onSuccess = function (resp) {
			Modal.hide();
			var file = {
				name: Modal.fileName,
				description: "",
				fileLink: resp.message,
				levelInstance: {
					id: me.props.levelInstanceId
				}
			}
			me.setState({
				loading: true
			});
			AttachmentStore.dispatch({
				action: AttachmentStore.ACTION_CREATE,
				data: file
			});
			//me.context.toastr.addAlertSuccess("Anexo salvo com sucesso! Talvez seja necessário atualizar a página para que os arquivos apareçam na lista.");
		};
		var onFailure = function (resp) {
			Modal.hide();
			me.setState({error: resp.message});
		};
		var formats = "Imagens: gif, jpg, jpeg, jpg2, jp2, bmp, tiff, png, ai, psd, svg, svgz\n"+
			"Documentos: pdf, doc, docx, odt, rtf, txt, xml, xlsx, xls, ods, csv, ppt, pptx, ppsx, odp\n"+
			"Áudio: MP3, WAV, WMA, OGG, AAC\n"+
			"Vídeos: avi, mov, wmv, mp4, flv, mkv\n"+
			"Arquivos: zip, rar, 7z, tar, tar.gz, tar.bz2\n";
		var maxSize = 2;

		Modal.uploadFile(title, msg, url, formatsRegex, formatsBlocked, onSuccess, onFailure, formats, maxSize);
	},

	getAttachments(page, pageSize){
		var me = this;
		if (me.refs["attach-checkbox-all"])
			me.refs["attach-checkbox-all"].checked = false;
		me.checkAll();
		me.setState({
			loading: true
		});
		AttachmentStore.dispatch({
			action: AttachmentStore.ACTION_FIND,
			data: {
				id: me.props.levelInstanceId,
				page: page,
				pageSize: pageSize
			}
		});
	},

	deleteAttachment(attachment){
		var me = this;

		Modal.confirmCancelCustom(
			() => {
				me.setState({
					loading: true
				});

				AttachmentStore.dispatch({
					action: AttachmentStore.ACTION_DELETE,
					data: {
						id: attachment.id
					}
				});
				Modal.hide();
			},
			 Messages.get("label.msg.deletedAttachment") + " " +attachment.name+"?",
			() => {Modal.hide()}
		);
	},

	deleteSelected(){
		var me = this;
		var array = [];
		me.state.list.map((attach, idx) => {
			if(this.refs["attach-checkbox-"+idx].checked){
			 	array.push(attach.id);
			}
		});
		Modal.confirmCustom(
			() => {
				me.setState({
					loading: true
				});
				AttachmentStore.dispatch({
					action: AttachmentStore.ACTION_DELETE_SELECTED,
					data: {
						list: array,
						total: array.length
					}
				});
				Modal.hide();
			},
			Messages.get("label.msg.removeAttachment") + " " + array.length + " " + Messages.get("label.attachment"),
			() => {Modal.hide()}
		);
	},

	editAttachment(attachment){
		this.setState({
			editId: attachment.id
		});
	},

	confirmEdit(attachment){
		var me = this;
		AttachmentStore.dispatch({
			action: AttachmentStore.ACTION_UPDATE_DESCRIPTION,
			data: {
				id: attachment.id,
				description: me.refs["edit-description"].value
			}
		});
	},

	cancelEdit(){
		this.setState({
			editId: null
		});
	},

	checkAll(){
		this.state.list.map((attachment, idx) => {
			if(this.refs["attach-checkbox-"+idx])
				this.refs["attach-checkbox-"+idx].checked = this.refs["attach-checkbox-all"] ? this.refs["attach-checkbox-all"].checked : false;
		});
		this.setState({
			anyCheck: this.refs["attach-checkbox-all"] ? this.refs["attach-checkbox-all"].checked : false
		});
	},

	checkAttachment(){
		var bool = false;
		this.state.list.map((attachment, idx) => {
			if(this.refs["attach-checkbox-"+idx].checked){
				bool = true;
			} else {
				this.refs["attach-checkbox-all"].checked = false;
			}
		});

		this.setState({
			anyCheck: bool
		});
	},

	render(){
		if(this.state.loading)
			return(<LoadingGauge/>)

		return(
			<div className="panel panel-default panel-margins">
				<div className="panel-heading dashboard-panel-title">
					<b className="budget-graphic-title">{Messages.getEditable("label.attachFiles","fpdi-nav-label")}</b>

					{this.state.list[0] ? (this.context.roles.MANAGER ||
							_.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION) ?
						<div className = "floatLeft">
							{this.state.anyCheck ?
								<button type="button" className={"btn btn-danger delete-all-btn floatLeft marginLeft105"}
								onClick={this.deleteSelected}>
									<i className="mdi mdi-delete positionStatic"/>
								</button>
							:
								<button type="button" className={"btn btn-danger delete-all-btn floatLeft marginLeft105"}
								onClick={this.deleteSelected} disabled>
									<i className="mdi mdi-delete positionStatic"/>
								</button>
							}
						</div>
				    : ""): ""}

					<div className="budget-btns">
						{this.context.roles.MANAGER ||
								_.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION) ||
								(this.props.responsible && UserSession.get("user").id == this.props.responsible.id) ?
							<button type="button" className="btn btn-primary budget-new-btn" onClick={this.attachFile}>{Messages.getEditable("label.attachFiles","fpdi-nav-label")}</button>
						:""}
						<span className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}
						onClick={this.hideFields}/>
					</div>
				</div>
				{this.state.hide ? undefined :
					<div>
						<table className="table budget-field-table">
							<thead>
								<tr>
									<th>
										<input ref={"attach-checkbox-all"} type="checkbox" onClick={this.checkAll}/>
									</th>
									<th>
										{Messages.getEditable("label.msg.file","fpdi-nav-label")}
									</th>
									<th>
										{Messages.getEditable("label.description","fpdi-nav-label")}
									</th>
									<th>
										{Messages.getEditable("label.author","fpdi-nav-label")}
									</th>
									<th>
										{Messages.getEditable("label.uploadData","fpdi-nav-label")}
									</th>
									{this.context.roles.MANAGER ||
										_.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION) ||
										(this.props.responsible && UserSession.get("user").id == this.props.responsible.id) ?
									<th>
										<center>
											{Messages.getEditable("label.actions","fpdi-nav-label")}
										</center>
									</th> : undefined}
								</tr>
							</thead>
							<tbody>
								{this.state.list.map((attachment, idx) => {
									return(
										<tr key={"attach-"+idx}>
											<td>
												<input ref={"attach-checkbox-"+idx} type="checkbox" onClick={this.checkAttachment}/>
											</td>
											<td>
												<a href={attachment.fileLink} download>{attachment.name}</a>
											</td>
											<td>
												{attachment.id == this.state.editId ?
													<input defaultValue={attachment.description} ref="edit-description" maxLength={250}/>
												: attachment.description}
											</td>
											<td>
												{attachment.author != null ? attachment.author.name : "" }
											</td>
											<td>
												{attachment.creation.split(" ")[0]}
											</td>
											{this.context.roles.MANAGER ||
												_.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION) ||
												(this.props.responsible && UserSession.get("user").id == this.props.responsible.id) ?
											<td>
												{attachment.id == this.state.editId ?
													<center className='displayFlex'>
									                   	<span className='mdi mdi-check accepted-budget' onClick={this.confirmEdit.bind(this, attachment)}
									                   	 title={Messages.get("label.submitLabel")}/>
									                  	<span className='mdi mdi-close reject-budget' onClick={this.cancelEdit}
									                  	 title={Messages.get("label.cancel")}/>
													</center> :
													<center>
														<span className="mdi mdi-pencil cursorPointer" onClick={this.editAttachment.bind(this, attachment)}/>
														<span className="mdi mdi-delete cursorPointer" onClick={this.deleteAttachment.bind(this, attachment)}/>
													</center>
												}
											</td> : undefined}
										</tr>
									);
								})}
							</tbody>
						</table>
						<TablePagination
							total={this.state.total}
	                  		onChangePage={this.getAttachments}
	                  		tableName={"attachment-table"+this.props.levelInstanceId}
                  		/>
	                </div>
				}
			</div>
		);
	}
});
