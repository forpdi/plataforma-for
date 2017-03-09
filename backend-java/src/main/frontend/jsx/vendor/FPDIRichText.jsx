import React from 'react';
import ReactDOM from 'react-dom';
import ReactQuill from 'react-quill';
import RichTextToolbar from 'forpdi/jsx/vendor/RichTextToolbar.jsx';
import Modal from 'forpdi/jsx/core/widget/Modal.jsx';
import FileStore from "forpdi/jsx/core/store/File.jsx";
import 'react-quill/node_modules/quill/dist/quill.snow.css';
import 'react-quill/node_modules/quill/dist/quill.base.css';

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
            },
            selectedTxt: "",
            newLink: false
        }
    },

    componentDidMount(){
        
    },

    onLinkClick(){
        this.quill = this.refs['quill'].state.editor;
        this.selection = this.refs['quill'].state.selection;
        if(this.selection == undefined)
            return;
        var start = this.selection.start;
        var end = this.selection.end;
        var text = this.quill.getText().substring(start,end);
        if(text != ""){
            this.setState({
                selectedTxt: text,
                newLink: true
            });
        }
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
                    <RichTextToolbar imageHandler={this.onImageClick} linkHandler={this.onLinkClick} id={this.props.id}/>
                 
                        {/*<div className="fpdi-richtext-link-ctn">
                            <label htmlFor="url-input" className="fpdi-richtext-link-label">Link:</label>
                            <input type="url" defaultValue={this.state.selectedTxt} id="url-input"/>
                            <span className="mdi mdi-link-variant fpdi-richtext-link-btn" title="inserir link"/>
                        </div>*/}
                    
                    <div key="editor"
                        ref="editor"
                        className="quill-contents resizeVertical minHeight200"
                        spellCheck={true}
                        dangerouslySetInnerHTML={{__html:(this.value)}}/>

                </ReactQuill>
            </div>
        );
    }
});