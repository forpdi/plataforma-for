import React from 'react';
import ReactDOM from 'react-dom';
import ReactQuill from 'react-quill';
import RichTextToolbar from 'forpdi/jsx/vendor/RichTextToolbar.jsx';
import Modal from 'forpdi/jsx/core/widget/Modal.jsx';
import FileStore from "forpdi/jsx/core/store/File.jsx";
import 'quill/dist/quill.snow.css';

export default React.createClass({

    contextTypes: {
        toastr: React.PropTypes.object.isRequired
    },

    getInitialState(){
        this.value = this.props.defaultValue;
        return {            
            toolbarOptions: {
                toolbar: {
                    container: '#'+this.props.id                    
                }
            }
        }
    },

    componentDidMount(){
        
    },

    onImageClick(){
        this.quill = this.refs['quill'].state.editor;
        var title = "Insira uma imagem";
        var msg = (<div><p>Escolha uma imagem para ser adicionanda ao campo.</p></div>);
        var url = FileStore.url+"/upload";
        var fileType = "image/*";
        var typesBlocked = "(exe*)";
        var onSuccess = function (resp){            
            var image = resp.message;
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
        this.quill = this.refs['quill'].state.editor;
        var length = this.quill.getLength();        
        if(length > this.props.maxLength){
            this.context.toastr.addAlertError("Limite de "+this.props.maxLength+" caracteres atingido!");
            var text = this.quill.getText();
            var newText = text.slice(0, this.props.maxLength);
            this.quill.setText(newText);
        }
        this.value = content;
    },

    onKeyPress(evt){        
        this.quill = this.refs['quill'].state.editor;
        var length = this.quill.getLength();
        if(length >= this.props.maxLength){
            this.context.toastr.addAlertError("Limite de "+this.props.maxLength+" caracteres atingido!");
            evt.preventDefault();
            return;
        }
    },

    render(){
        return (
            <div>                
                <ReactQuill theme="snow"
                modules={this.state.toolbarOptions}
                onChange={this.onChange}
                onKeyPress={this.onKeyPress}
                onKeyDown={this.onKeyDown}
                onKeyUp={this.onKeyUp}
                value={this.value}
                ref="quill">
                    <RichTextToolbar imageHandler={this.onImageClick} id={this.props.id}/>
                    <div key="editor"
                        ref="editor"
                        className="quill-contents resizeVertical minHeight200"
                        wrap={true}
                        dangerouslySetInnerHTML={{__html:(this.value)}}/>

                </ReactQuill>
            </div>
        );
    }
});