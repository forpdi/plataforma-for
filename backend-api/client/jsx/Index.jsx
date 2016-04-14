
import React from "react";

export default React.createClass({
	render() {
		return (
			<div style={{backgroundImage: 'url(img/clouds.png)'}}>
				<div className="jumbotron text-center" style={{backgroundColor: 'rgba(0,0,0,0.5)', color: "#FFF", fontWeight: "bold", textShadow: '2px 2px rgba(0,0,0,0.25)'}}>
					<h1>Pro Cloud</h1>
					<h2>Let your dreams fly to the cloud.</h2>
				</div>
			</div>
		);
	}
});