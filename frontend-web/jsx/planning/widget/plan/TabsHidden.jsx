import React from "react";
import _ from 'underscore';

var onClickOutside = require('react-onclickoutside');

export default onClickOutside(React.createClass({
    handleClickOutside: function(evt) {
		this.props.hideTabs();
	},
	render() {
		return (
		<div className='fpdi-tabsHidden-content'>
			<ul className="fpdi-tabsHidden-nav show-close" >
				{
					_.map(this.props.tabs, tab => tab.element)
				}
			</ul>
		</div>);
	}
}));
