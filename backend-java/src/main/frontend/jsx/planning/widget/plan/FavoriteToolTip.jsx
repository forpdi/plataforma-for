import moment from 'moment';
import React from 'react';
import ImgTooltip from 'forpdi/img/tooltipFavorite.png';

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
			<div className="fpdi-favoriteTooltip">	
				<div className="tooltipText">
					Favorite os níveis desejados através do menu do nível.<br/>
					<div className="tooltipImage"
						style={{
							backgroundImage: 'url('+ImgTooltip+')',
							backgroundPosition: 'center center',
							backgroundRepeat: 'no-repeat',
							width:'175px',
							height:'38px'
						}}
					/>
					Lembre-se: você pode ter no máximo 10 favoritos.
				</div>
			</div>
		);
	}

});

