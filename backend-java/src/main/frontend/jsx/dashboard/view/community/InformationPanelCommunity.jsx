import moment from 'moment';
import React from 'react';
import Messages from "forpdi/jsx/core/util/Messages.jsx";



export default React.createClass({
	getDefaultProps() {
		return {

		};
	},

	getInitialState() {
		return {
		};
	},

	componentDidMount(){ 

	},

	componentWillUnmount() {

	},

	
	
	render() {
	
			return (

				<div className="fpdi-ToolTipNotification">	

					 <div className='displayFlex-goals'>
					 	<h3 id = "TitleSubtitleNotification"> 
					 		{Messages.getEditable("label.titlePDI","fpdi-nav-label")} 
					 	</h3>
  	            	</div>

  	            	<div className='displayFlex-goals'>
					 		<p id = "TitleSubtitleNotification">
					 			{Messages.getEditable("label.pdiInfoText","fpdi-nav-label")}
					 		</p>
					 		<p id = "TitleSubtitleNotification">
					 			{Messages.getEditable("label.communityDashboardInfo","fpdi-nav-label")}
					 		</p>
					 </div>
  	          </div>			
			);
		
	}
});