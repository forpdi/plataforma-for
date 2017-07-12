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
					 	<h3 id = "TitleSubtitleNotification"> O PDI  </h3>
  	            	</div>

  	            	<div className='displayFlex-goals'>
					 		<p id = "TitleSubtitleNotification">
					 			{Messages.get("label.pdiInfoText")}
					 		</p>
					 		<p id = "TitleSubtitleNotification">
					 			{Messages.get("label.communityDashboardInfo")}
					 		</p>
					 </div>
  	          </div>			
			);
		
	}
});