import React from 'react';

export default React.createClass({
	render() {
		return (<div className="center-block"
				style={{
					backgroundImage: 'url(img/loading.gif)',
					backgroundPosition: 'center center',
					backgroundRepeat: 'no-repeat',
					width:'175px',
					height:'175px'
				}}
				></div>);
	}
});