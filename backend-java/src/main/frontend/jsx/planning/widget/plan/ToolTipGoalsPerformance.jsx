import moment from 'moment';
import React from 'react';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

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
	componentWillReceiveProps() {
		
	},


	render() {
		return (
			<div className="fpdi-ToolTipGoals">	

				 <div className='displayFlex-goals'>
   	                  <h3 id = "TitleSubtitleGoal" className="textAlign"> {Messages.get("label.peformanceScale")} </h3> 
  	            </div>

				<div className="row">
					<div className="boxIcon">
					<br/>
					<input type="text"  className="btn-action-goals-minimumbelow"/>
					</div>
					<div className="box">
					<p className="toolTipGoalItem">{Messages.get("label.min")}</p>
					 
					</div>
					<div className="boxIcon">
					<br/>
					<input type="text" className="btn-action-goals-minimum" />
					</div>
					<div className="box">
					<p className="toolTipGoalItem">{Messages.get("label.goals.reached")}</p>
					 
					</div>
					<div className="boxIcon">
					 <br/>
					<input type="text" className="btn-action-goals-enoughabove" />
					</div>
					<div className="box">
					<p className="toolTipGoalItem">{Messages.get("label.max")}</p>				 
					</div>
					<div className="boxIcon">
					  <br/>
					<input type="text" className="btn-action-goals-maximumup" />
					</div>
					
  				</div>	

		</div>);
	}

});

