import React from "react";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({

    getInitialState() {
        return {

        };
    },

    componentDidMount(){
    	var me = this;
    },
	

	componentWillUnmount() {
	},

	componentWillReceiveProps(newProps){		
	},


	render() {
		return(
			<div className={this.props.className+" dashboard-goals-board"}>
				{this.props.loading ? 
					<div className="loading-text-goal-board">
						{Messages.getEditable("label.loading","fpdi-nav-label")}
					</div> :
					<div>
						<div className="dashboard-goals-board-number">{this.props.numberValue}</div>
						<div>{(this.props.numberValue!= 1)?(Messages.getEditable("label.goals","fpdi-nav-label")):(Messages.getEditable("label.goalSing","fpdi-nav-label"))}</div>
						<div>{(this.props.numberValue!= 1)?(this.props.goalSubLabel):(this.props.goalSubLabelSingular)}</div>
						<div className="dashboard-goals-board-see-more">{/*<i className="mdi mdi-plus" />ver mais*/}</div>
					</div>
				}				
			</div>
			);
	}
});