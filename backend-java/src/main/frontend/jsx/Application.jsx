
import React from "react";

import MainMenu from "forpdi/jsx/core/widget/MainMenu.jsx";
import TopBar from "forpdi/jsx/core/widget/TopBar.jsx";

export default React.createClass({
  	getInitialState() {
      	return {
      	};
  	},
  	componentDidMount() {
      	var me = this;
      
  	},
  	componentWillUnmount() {
      
  	},
  	render() {
	    return (<main className='fpdi-app-container'>
	        <MainMenu />
	        <div className="fpdi-app-content">
	        	<TopBar />
	        	{this.props.children}
	        </div>
	   	</main>);
	}
});

