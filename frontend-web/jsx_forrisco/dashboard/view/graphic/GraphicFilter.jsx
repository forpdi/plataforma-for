import React from "react";

export default React.createClass({

	getInitialState() {
		return {

		};
	},

	render() {
		if (this.props.quantity) {
			return (<div>
				<div>
					<div title="Visualizar histórico"
						className="mdi mdi-chart-line"
						style={this.props.color == "Amarelo" ? { "left": "75px", "position": "relative", "color": "black", "padding": "0 0 16px 0" }
							: { "left": "75px", "position": "relative", "color": "white", "padding": "0 0 16px 0" }}
						onClick={() => this.props.onClick(this.props.level)} />
				</div>
			</div>);
		} else {
			return (
				<span className="legend-item icon-link  " onClick={() => this.props.onClick(this.props.level)}>
					<span type="text" className={"legend-risk marginLeft10 " + this.props.color} />{this.props.level.level}</span>
			)
		}
	}
});
