import moment from 'moment';
import React from 'react';

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
   	                  <h3 id = "TitleSubtitleGoal" className="textAlign"> Escala de desempenho </h3> 
  	            </div>

				<div className="row">
					<div className="boxIcon">
					<br/>
					<input type="text"  className="btn-action-goals-minimumbelow"/>
					</div>
					<div className="box">
					<p className="toolTipGoalItem">Mínimo</p>
					 
					</div>
					<div className="boxIcon">
					<br/>
					<input type="text" className="btn-action-goals-minimum" />
					</div>
					<div className="box">
					<p className="toolTipGoalItem">Suficiente</p>
					 
					</div>
					<div className="boxIcon">
					 <br/>
					<input type="text" className="btn-action-goals-enoughabove" />
					</div>
					<div className="box">
					<p className="toolTipGoalItem">Máximo</p>				 
					</div>
					<div className="boxIcon">
					  <br/>
					<input type="text" className="btn-action-goals-maximumup" />
					</div>
					
  				</div>	

		</div>);
	}

});

