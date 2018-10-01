
import _ from 'underscore';
import $ from 'jquery';
import React from 'react';
import ReactDOM from 'react-dom';

var EL = document.getElementById("main-global-popover");

var Popover = {
	$el: EL,
	$init() {
		this.hide();
	},
	hide() {
		ReactDOM.render(<span />, this.$el);
		$(this.$el).css("display", "none");
	},
	show() {
		$(this.$el).css("display", "block");
	},

	render(element, target) {
		ReactDOM.render(element, this.$el);
		this.show();
		var top, left,
			targetRect = target.getBoundingClientRect(),
			rect = this.$el.getBoundingClientRect();
		top = targetRect.bottom+1;
		left = targetRect.right-rect.width-1;
		$(this.$el).css("top", top).css("left", left);
	},

};

$(() => {
	Popover.$init();
});

export default Popover;
