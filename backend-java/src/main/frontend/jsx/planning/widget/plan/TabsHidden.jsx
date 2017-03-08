import React from "react";
import ReactDOM from "react-dom";

var onClickOutside = require('react-onclickoutside');

export default onClickOutside(React.createClass({
    handleClickOutside: function(evt) {
		this.props.hideTabs();
	},
	render() {
		return (
		<div className='fpdi-tabsHidden-content'>
			<ul className="fpdi-tabsHidden-nav show-close" >
				{this.props.tabs}
			</ul>
		</div>);
	}
}));