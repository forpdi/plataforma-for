
import 'react-quill/dist/quill.snow.css';

import React from 'react';
import ReactQuill from 'react-quill';
import Modal from 'forpdi/jsx/core/widget/Modal.jsx';
import FileStore from "forpdi/jsx/core/store/File.jsx";

export default React.createClass({
    contextTypes: {
        toastr: React.PropTypes.object.isRequired
    },
    getInitialState(){
        return {
            selectedTxt: "",
			newLink: false,
			value: this.props.defaultValue,
        }
    },

    componentDidMount(){

    },

    onLinkClick(){
        this.selection = this.refs['quill'].state.selection;
        var text = "";
        var start = undefined;
        var end = undefined;
        if(this.selection != undefined){
            start = this.selection.start;
            end = this.selection.end;
            text = this.quill.getText().substring(start,end);
        }
        Modal.confirm("Insira o endereço",
            <p className="fpdi-richtext-link-ctn">
                <label htmlFor="url-input" className="fpdi-richtext-link-label">Link:</label>
                <input type="url" defaultValue={text} id="url-input" className="width100percent padding5"
                 placeholder="insira o endereço url desejado"/>
                {/*<span className="mdi mdi-link-variant fpdi-richtext-link-btn" title="inserir link"/>*/}
            </p>,
            this.insertLink.bind(this, text, start, end));
    },

    insertLink(text, start, end){
        var url = document.getElementById("url-input").value;
        if(text == ""){
            text = url;
            this.quill.insertText(start, text, 'link', url);
        } else {
            this.quill.deleteText(start, end);
            this.quill.insertText(start, text, 'link', url);
        }

        Modal.hide();
    },

    onImageClick(){
        var title = "Insira uma imagem";
        var msg = (<div><p>Escolha uma imagem para ser adicionanda ao campo.</p></div>);
        var url = FileStore.url+"/upload";
        var fileType = "image/*";
        var typesBlocked = "(exe*)";
        var onSuccess = function (resp){
            var image = resp.message.replace("https://", "http://");
            Modal.hide();
            this.quill.insertEmbed(this.quill.editor.delta.length(), 'image', image);
            this.quill.focus();
        }
        var onFailure = function (resp){
            Modal.hide();
        }
        var validSamples = "jpg, jpeg, gif, png, svg.";
        var maxSize = 2;
        Modal.uploadFile(title, msg, url, fileType, typesBlocked, onSuccess.bind(this), onFailure, validSamples, maxSize);
    },

    onChange(content, delta, source, editor){
		var length = editor.getLength();
        if(length > this.props.maxLength){
            this.context.toastr.addAlertError("Limite de "+this.props.maxLength+" caracteres atingido!");
        } else {
			this.setState({
				value: content,
			});
		}

		this.props.changeValue(content)
    },
    render(){

		//const toolbarId = `${this.props.id}-toolbar`;

        return (
            <div>
                <ReactQuill
					onChange={this.onChange}
					value={this.state.value}
					ref={(c) => { this.quill = (c && c.editor) || this.quill; }}
					modules = {{
						toolbar: [
						[{ 'font': [] }],
						[{ 'header': [1, 2, 3, 4, 5, 6, false] }],, // custom button values
						['bold', 'italic', 'underline','strike',],   // toggled buttons
						[{ 'color': [] }, { 'background': [] }],          // dropdown with defaults from theme
						[{ 'align': [] }],
						[{'list': 'ordered'}, {'list': 'bullet'}, {'indent': '-1'}, {'indent': '+1'}], // outdent/indent
						['link'], //, 'image'],

						['blockquote', 'code-block'],
						['clean'],										// remove formatting button
						[{ 'script': 'sub'}, { 'script': 'super' }],      // superscript/subscript
						[{ 'direction': 'rtl' }],                         // text direction
					  ]
					  }}
				/>
		    </div>
        );
    }
});
