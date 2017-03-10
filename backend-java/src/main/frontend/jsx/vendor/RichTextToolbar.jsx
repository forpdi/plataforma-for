import React from 'react';
import ReactDOM from 'react-dom';
import ReactQuill from 'react-quill';

export default React.createClass({
	render(){
		return(
		<div id={this.props.id} className="toolbar ql-toolbar ql-snow">
		    <span className="ql-format-group">
		        <select title="Font" className="ql-font" defaultValue="sans-serif">
		            <option value="sans-serif"/>
		            <option value="serif"/>
		            <option value="monospace"/>
		            {/*<option value="arial">Arial</option>
		            <option value="times">Times</option>*/}
		        </select>
		        <select title="Size" className="ql-size" defaultValue="13px">		            
		            <option value="13px">Normal</option>
		            <option value="18px">Grande</option>
		            <option value="32px">Enorme</option>
		        </select>		        
		    </span>
		    <span className="ql-format-group">
		        <span title="Bold" className="ql-format-button ql-bold"/>
		        <span className="ql-format-separator"/>
		        <span title="Italic" className="ql-format-button ql-italic"/>
		        <span className="ql-format-separator"/>
		        <span title="Underline" className="ql-format-button ql-underline"/>
		        <span className="ql-format-separator"/>
		        <span title="Strikethrough" className="ql-format-button ql-strike"/>		        
		    </span>		    
		    <span className="ql-format-group">
		        <select title="Text Color" className="ql-color" defaultValue="rgb(0, 0, 0)">
		          <option value="rgb(0, 0, 0)" style={{backgroundColor:'rgb(0, 0, 0)'}}/>
		          <option value="rgb(230, 0, 0)" style={{backgroundColor:'rgb(230, 0, 0)'}}/>
		          <option value="rgb(255, 153, 0)" style={{backgroundColor:'rgb(255, 153, 0)'}}/>
		          <option value="rgb(255, 255, 0)" style={{backgroundColor:'rgb(255, 255, 0)'}}/>
		          <option value="rgb(0, 138, 0)" style={{backgroundColor:'rgb(0, 138, 0)'}}/>
		          <option value="rgb(0, 102, 204)" style={{backgroundColor:'rgb(0, 102, 204)'}}/>
		          <option value="rgb(153, 51, 255)" style={{backgroundColor:'rgb(153, 51, 255)'}}/>
		          <option value="rgb(250, 204, 204)" style={{backgroundColor:'rgb(250, 204, 204)'}}/>
		          <option value="rgb(255, 235, 204)" style={{backgroundColor:'rgb(255, 235, 204)'}}/>
		          <option value="rgb(255, 255, 204)" style={{backgroundColor:'rgb(255, 255, 204)'}}/>
		          <option value="rgb(204, 232, 204)" style={{backgroundColor:'rgb(204, 232, 204)'}}/>
		          <option value="rgb(204, 224, 245)" style={{backgroundColor:'rgb(204, 224, 245)'}}/>
		          <option value="rgb(235, 214, 255)" style={{backgroundColor:'rgb(235, 214, 255)'}}/>
		          <option value="rgb(187, 187, 187)" style={{backgroundColor:'rgb(187, 187, 187)'}}/>
		          <option value="rgb(240, 102, 102)" style={{backgroundColor:'rgb(240, 102, 102)'}}/>
		          <option value="rgb(255, 194, 102)" style={{backgroundColor:'rgb(255, 194, 102)'}}/>
		          <option value="rgb(255, 255, 102)" style={{backgroundColor:'rgb(255, 255, 102)'}}/>
		          <option value="rgb(102, 185, 102)" style={{backgroundColor:'rgb(102, 185, 102)'}}/>
		          <option value="rgb(102, 163, 224)" style={{backgroundColor:'rgb(102, 163, 224)'}}/>
		          <option value="rgb(194, 133, 255)" style={{backgroundColor:'rgb(194, 133, 255)'}}/>
		          <option value="rgb(136, 136, 136)" style={{backgroundColor:'rgb(136, 136, 136)'}}/>
		          <option value="rgb(161, 0, 0)" style={{backgroundColor:'rgb(161, 0, 0)'}}/>
		          <option value="rgb(178, 107, 0)" style={{backgroundColor:'rgb(178, 107, 0)'}}/>
		          <option value="rgb(178, 178, 0)" style={{backgroundColor:'rgb(178, 178, 0)'}}/>
		          <option value="rgb(0, 97, 0)" style={{backgroundColor:'rgb(0, 97, 0)'}}/>
		          <option value="rgb(0, 71, 178)" style={{backgroundColor:'rgb(0, 71, 178)'}}/>
		          <option value="rgb(107, 36, 178)" style={{backgroundColor:'rgb(107, 36, 178)'}}/>
		          <option value="rgb(68, 68, 68)" style={{backgroundColor:'rgb(68, 68, 68)'}}/>
		          <option value="rgb(92, 0, 0)" style={{backgroundColor:'rgb(92, 0, 0)'}}/>
		          <option value="rgb(102, 61, 0)" style={{backgroundColor:'rgb(102, 61, 0)'}}/>
		          <option value="rgb(102, 102, 0)" style={{backgroundColor:'rgb(102, 102, 0)'}}/>
		          <option value="rgb(0, 55, 0)" style={{backgroundColor:'rgb(0, 55, 0)'}}/>
		          <option value="rgb(0, 41, 102)" style={{backgroundColor:'rgb(0, 41, 102)'}}/>
		          <option value="rgb(61, 20, 102)" style={{backgroundColor:'rgb(61, 20, 102)'}}/>
		        </select>
		        <span className="ql-format-separator"/>
		        <select title="Background Color" className="ql-background" defaultValue="rgb(255, 255, 255)">
		          <option value="rgb(0, 0, 0)" style={{backgroundColor:'rgb(0, 0, 0)'}}/>
		          <option value="rgb(230, 0, 0)" style={{backgroundColor:'rgb(230, 0, 0)'}}/>
		          <option value="rgb(255, 153, 0)" style={{backgroundColor:'rgb(255, 153, 0)'}}/>
		          <option value="rgb(255, 255, 0)" style={{backgroundColor:'rgb(255, 255, 0)'}}/>
		          <option value="rgb(0, 138, 0)" style={{backgroundColor:'rgb(0, 138, 0)'}}/>
		          <option value="rgb(0, 102, 204)" style={{backgroundColor:'rgb(0, 102, 204)'}}/>
		          <option value="rgb(153, 51, 255)" style={{backgroundColor:'rgb(153, 51, 255)'}}/>
		          <option value="rgb(255, 255, 255)" style={{backgroundColor:'rgb(255, 255, 255)'}}/>
		          <option value="rgb(250, 204, 204)" style={{backgroundColor:'rgb(250, 204, 204)'}}/>
		          <option value="rgb(255, 235, 204)" style={{backgroundColor:'rgb(255, 235, 204)'}}/>
		          <option value="rgb(255, 255, 204)" style={{backgroundColor:'rgb(255, 255, 204)'}}/>
		          <option value="rgb(204, 232, 204)" style={{backgroundColor:'rgb(204, 232, 204)'}}/>
		          <option value="rgb(204, 224, 245)" style={{backgroundColor:'rgb(204, 224, 245)'}}/>
		          <option value="rgb(235, 214, 255)" style={{backgroundColor:'rgb(235, 214, 255)'}}/>
		          <option value="rgb(187, 187, 187)" style={{backgroundColor:'rgb(187, 187, 187)'}}/>
		          <option value="rgb(240, 102, 102)" style={{backgroundColor:'rgb(240, 102, 102)'}}/>
		          <option value="rgb(255, 194, 102)" style={{backgroundColor:'rgb(255, 194, 102)'}}/>
		          <option value="rgb(255, 255, 102)" style={{backgroundColor:'rgb(255, 255, 102)'}}/>
		          <option value="rgb(102, 185, 102)" style={{backgroundColor:'rgb(102, 185, 102)'}}/>
		          <option value="rgb(102, 163, 224)" style={{backgroundColor:'rgb(102, 163, 224)'}}/>
		          <option value="rgb(194, 133, 255)" style={{backgroundColor:'rgb(194, 133, 255)'}}/>
		          <option value="rgb(136, 136, 136)" style={{backgroundColor:'rgb(136, 136, 136)'}}/>
		          <option value="rgb(161, 0, 0)" style={{backgroundColor:'rgb(161, 0, 0)'}}/>
		          <option value="rgb(178, 107, 0)" style={{backgroundColor:'rgb(178, 107, 0)'}}/>
		          <option value="rgb(178, 178, 0)" style={{backgroundColor:'rgb(178, 178, 0)'}}/>
		          <option value="rgb(0, 97, 0)" style={{backgroundColor:'rgb(0, 97, 0)'}}/>
		          <option value="rgb(0, 71, 178)" style={{backgroundColor:'rgb(0, 71, 178)'}}/>
		          <option value="rgb(107, 36, 178)" style={{backgroundColor:'rgb(107, 36, 178)'}}/>
		          <option value="rgb(68, 68, 68)" style={{backgroundColor:'rgb(68, 68, 68)'}}/>
		          <option value="rgb(92, 0, 0)" style={{backgroundColor:'rgb(92, 0, 0)'}}/>
		          <option value="rgb(102, 61, 0)" style={{backgroundColor:'rgb(102, 61, 0)'}}/>
		          <option value="rgb(102, 102, 0)" style={{backgroundColor:'rgb(102, 102, 0)'}}/>
		          <option value="rgb(0, 55, 0)" style={{backgroundColor:'rgb(0, 55, 0)'}}/>
		          <option value="rgb(0, 41, 102)" style={{backgroundColor:'rgb(0, 41, 102)'}}/>
		          <option value="rgb(61, 20, 102)" style={{backgroundColor:'rgb(61, 20, 102)'}}/>
		        </select>
		    </span>
		    <span className="ql-format-group">
		        <span title="List" className="ql-format-button ql-list"/>
		        <span className="ql-format-separator"/>
		        <span title="Bullet" className="ql-format-button ql-bullet"/>
		        <span className="ql-format-separator"/>
			    <select title="Text Alignment" className="ql-align" defaultValue="left">
			      <option value="left" label="Left"/>
			      <option value="center" label="Center"/>
			      <option value="right" label="Right"/>
			      <option value="justify" label="Justify"/>
			    </select>
		    </span>
		  	<span className="ql-format-group">
		  		<span className="ql-format-button ql-image" title="Image" onClick={this.props.imageHandler}/>
		    </span>
		    <span className="ql-format-group">
		  		<span className="ql-format-button ql-link" title="Link" onClick={this.props.linkHandler}/>
		    </span>
		</div>);
	}
});