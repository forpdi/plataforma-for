import React from 'react';

import LoadingGif from 'forpdi/img/loading2-white.gif';

export default React.createClass({
	render() {		
		return (
			<div className="center-block"
				style={{
					backgroundImage: 'url('+LoadingGif+')',
					backgroundPosition: 'center center',
					backgroundRepeat: 'no-repeat',
					width:'175px',
					height:'175px'
				}}
			/>);
	}
});