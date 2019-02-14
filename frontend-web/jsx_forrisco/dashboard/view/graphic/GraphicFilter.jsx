import React from "react";

export default React.createClass({

    getInitialState(){
        return {

        };
	},
	/*componentWillReceiveProps(newProps){

	},*/

	render() {
		if(this.props.quantity){
			return (<div>
				<div>
					<a>
					<div title="Visualizar histórico"
						className="mdi mdi-chart-line"
						style={ this.props.color=="Amarelo" ? {"left":"75px","position": "relative", "color":"black"} : {"left":"75px", "position": "relative", "color":"white"}}
						onClick={() => this.props.onClick(this.props.level)}/>
					</a>
				</div>
			</div>);
		}else{
			return(
			<span className="legend-item icon-link  " onClick={() => this.props.onClick(this.props.level)}>
			<span type="text"  className={"legend-risk marginLeft10 "+this.props.color} />{this.props.level.level}</span>
			)

		}
	}
});
