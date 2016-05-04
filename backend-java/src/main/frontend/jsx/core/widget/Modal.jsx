
import _ from 'underscore';
import $ from 'jquery';
import React from 'react';
import ReactDOM from 'react-dom';

import FeedbackPost from "forpdi/jsx/core/widget/contact/FeedbackPost.jsx";
import ReportProblem from "forpdi/jsx/core/widget/contact/ReportProblem.jsx";

var EL = document.getElementById("main-global-modal");

var AlertModal = React.createClass({
	render() {
		return (
			<div className="modal-dialog modal-sm">
				<div className="modal-content">
					<div className="modal-header fpdi-modal-header">
	        			<button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h4 className="modal-title" id="myModalLabel">{this.props.title}</h4>
	      			</div>
	      			<div className="modal-body fpdi-modal-body">
	        			<p>{this.props.message}</p>
	      			</div>
	      			<div className="modal-footer fpdi-modal-footer">
	        			<button type="button" className="btn btn-sm btn-default" data-dismiss="modal">Ok</button>
	      			</div>
				</div>
			</div>
		);
	}
});

var ConfirmModal = React.createClass({
	render() {
		return (
			<div className="modal-dialog modal-sm">
				<div className="modal-content">
					<div className="modal-header">
	        			<button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h4 className="modal-title" id="myModalLabel">{this.props.title}</h4>
	      			</div>
	      			<div className="modal-body">
	        			{this.props.message}
	      			</div>
	      			<div className="modal-footer">
	        			<button type="button" className="btn btn-sm btn-default" data-dismiss="modal">Cancelar</button>
	        			<button type="button" className="btn btn-sm btn-primary" onClick={this.props.onConfirm}>Confirmar</button>
	      			</div>
				</div>
			</div>
		);
	}
});

var DeleteConfirmModal = React.createClass({
	getDefaultProps() {
		return {
			text: "Você tem certeza que deseja excluir este registro?",
			confirmText: "Excluir",
			declineText: "Cancelar"
		};
	},
	render() {
		return (
			<div className="modal-dialog modal-sm">
				<div className="modal-content">
					<div className="modal-header fpdi-modal-header">
	        			<button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h4 className="modal-title" id="myModalLabel">Atenção</h4>
	      			</div>
	      			<div className="modal-body fpdi-modal-body">
	        			<p>{this.props.text}</p>
	      			</div>
	      			<div className="modal-footer fpdi-modal-footer">
	        			<button type="button" className="btn btn-sm btn-default" data-dismiss="modal">{this.props.declineText}</button>
	        			<button type="button" className="btn btn-sm btn-primary" onClick={this.props.onConfirm}>{this.props.confirmText}</button>
	      			</div>
				</div>
			</div>
		);
	}
});

var ReadTextModal = React.createClass({
	onConfirmWrapper(evt) {
		var me = this,
			value = me.refs['text-input'].value;
		_.defer(() => {
			me.props.onConfirm(value);
		});
	},
	render() {
		return (
			<div className="modal-dialog modal-sm">
				<div className="modal-content">
					<div className="modal-header">
	        			<button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h4 className="modal-title" id="myModalLabel">{this.props.title}</h4>
	      			</div>
	      			<div className="modal-body">
	        			<p>{this.props.message}</p>
	        			<input type='text' className='form-control' ref="text-input" />
	      			</div>
	      			<div className="modal-footer">
	        			<button type="button" className="btn btn-sm btn-default" data-dismiss="modal">Cancelar</button>
	        			<button type="button" className="btn btn-sm btn-primary" onClick={this.onConfirmWrapper}>Confirmar</button>
	      			</div>
				</div>
			</div>
		);
	}
});

var FileUploadModal = React.createClass({
	render() {
		return (
			<div className="modal-dialog">
				<div className="modal-content">
					<div className="modal-header">
	        			<button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h4 className="modal-title" id="myModalLabel">{this.props.title}</h4>
	      			</div>
	      			<div className="modal-body">
	        			<div>{this.props.message}</div>
	        			<input type='file' name="file" className='form-control' ref="file-upload-field" id="file-upload-field" />
	        		</div>
	        		<div className="panel-footer">
	        			<div id="file-upload-progress" className="progress" style={{border: '1px solid #ccc'}}>
		        			<div className="progress-bar progress-bar-success"></div>
		    			</div>
	    			</div>
				</div>
			</div>
		);
	}
});

var MediumModal = React.createClass({
	getDefaultProps() {
		return {
			title: null
		};
	},
	render() {
		return (
			<div className="modal-dialog modal-md">
				<div className="modal-content">
					<div className="modal-header fpdi-modal-header">
	        			<button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        			<h4 className="modal-title" id="myModalLabel">{this.props.title}</h4>
	      			</div>
	      			<div className="modal-body fpdi-modal-body">
	        			{this.props.children}
	      			</div>
				</div>
			</div>
		);
	}
});

var Modal = {
	$el: EL,
	$init() {
		$(this.$el).modal({
			show: false
		});
	},
	hide() {
		$(this.$el).modal("hide");
		_.defer(() => {
			ReactDOM.render((
				<div></div>
			),this.$el);
		});
	},
	show() {
		$(this.$el).modal("show");
	},
	detailsModal(title, Components) {
		ReactDOM.render((
			<MediumModal title={title}>
				{Components}
			</MediumModal>
		),this.$el);
		$(this.$el).modal('show');
	},
	alert(title, msg) {
		ReactDOM.render((
			<AlertModal title={title} message={msg} />
		),this.$el);
		$(this.$el).modal('show');
	},
	confirm(title, msg, cb) {
		ReactDOM.render((
			<ConfirmModal onConfirm={cb}  title={title} message={msg} />
		),this.$el);
		$(this.$el).modal('show');
	},
	deleteConfirm(cb) {
		ReactDOM.render((
			<DeleteConfirmModal onConfirm={cb} />
		),this.$el);
		$(this.$el).modal('show');
	},
	readText(title, msg, cb) {
		ReactDOM.render((
			<ReadTextModal title={title} message={msg} onConfirm={cb} />
		),this.$el);
		$(this.$el).modal('show');
	},

	feedbackPost() {
		ReactDOM.render((
			<MediumModal title="Enviar feedback">
				<FeedbackPost
					onCancel={this.hide.bind(this)}
					onSubmit={this.hide.bind(this)}
				/>
			</MediumModal>
		),this.$el);
		$(this.$el).modal('show');
	},
	reportProblem() {
		ReactDOM.render((
			<MediumModal title="Reportar problema">
				<ReportProblem
					onCancel={this.hide.bind(this)}
					onSubmit={this.hide.bind(this)}
				/>
			</MediumModal>
		),this.$el);
		$(this.$el).modal('show');
	},

	uploadFile(title, msg, url, cb) {
		var me = this;
		ReactDOM.render((
			<FileUploadModal title={title} message={msg} />
		),this.$el);
		this.show();

		$('#file-upload-field').fileupload({
	        url: url,
	        dataType: 'json',
	        done: function (evt, fied) {
	            if ((evt.type == 'fileuploaddone') && (typeof cb == 'function'))
	            	cb.call(me);
	        },
	        progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#file-upload-progress .progress-bar').css(
	                'width',
	                progress + '%'
	            );
	        }
	    }).prop('disabled', !$.support.fileInput)
	        .parent().addClass($.support.fileInput ? undefined : 'disabled');
	}
};

$(() => {
	Modal.$init();
});

export default Modal;
