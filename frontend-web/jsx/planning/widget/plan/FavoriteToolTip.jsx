import moment from 'moment';
import React from 'react';
import ImgTooltip from 'forpdi/img/tooltipFavorite.png';
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
			<div className="fpdi-favoriteTooltip">
				<div className="tooltipText">
					{Messages.get("label.favoriteLevels")}<br/>
					<div className="tooltipImage"
						style={{
							backgroundImage: 'url('+ImgTooltip+')',
							backgroundPosition: 'center center',
							backgroundRepeat: 'no-repeat',
							width:'175px',
							height:'38px'
						}}
					/>
					{Messages.getEditable("label.maxTenFavorites","fpdi-nav-label")}
				</div>
			</div>
		);
	}

});

