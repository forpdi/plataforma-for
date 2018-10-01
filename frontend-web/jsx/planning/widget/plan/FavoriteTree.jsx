import moment from 'moment';
import React from 'react';
import {Link} from 'react-router';
import ReactTooltip from 'react-tooltip';
import FavoriteToolTip from "forpdi/jsx/planning/widget/plan/FavoriteToolTip.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {
			hidden: false,
			expandedIconCls: 'mdi mdi-chevron-down',
			iconCls: 'mdi mdi-chevron-right',
			favoriteSelectedIcon: 'mdi mdi-star-outline',
			favoriteIcon: 'mdi mdi-star',
			expanded: false,
			favoriteStore: null,
			levelInstanceId: null
		};
	},

	componentDidMount() {
		var me = this;

		StructureStore.on('favoritesListeds', store => {
			if(this.isMounted()){
				this.setState({
					favoriteStore: store.data
				});
			}
		}, me);

		StructureStore.on('favoriteSaved', model => {
			var favoriteStore = this.state.favoriteStore;
			favoriteStore.push(model.data);
			if(this.isMounted()){
				this.setState({
					favoriteStore: favoriteStore
				});
			}
		}, me);

		StructureStore.on('favoriteRemoved', model => {
			var favoriteStore = this.state.favoriteStore;
			var idx;
			for (var i=0; i<favoriteStore.length; i++) {
				if (favoriteStore[i].levelInstance.id == model.data.levelInstance.id)
					idx = i;
			}
			favoriteStore.splice(idx,1);

			//Evitar warning de unmounted
			if(this.isMounted()){
				this.setState({
					favoriteStore: favoriteStore
				});
			}	
		}, me);

		StructureStore.on('levelAttributeSaved', model => {
			var favoriteStore = this.state.favoriteStore;
			for (var i=0; i<favoriteStore.length; i++) {
				if (favoriteStore[i].levelInstance.id == model.data.id)
					favoriteStore[i].levelInstance.name = model.data.name;
			}

			//Evitar warning de unmounted
			if(this.isMounted()){
				this.setState({
					favoriteStore: favoriteStore
				});
			}	
		}, me);

		StructureStore.on('deleteLevelInstance', model => {
			if (this.state.favoriteStore && this.state.favoriteStore.length) {
				StructureStore.dispatch({
					action: StructureStore.ACTION_LIST_FAVORITES,
					data: {
						macroId: this.state.favoriteStore[0].levelInstance.plan.parent.id
					},
					opts: {
						wait: true
					}
				});
			}
		}, me);

		StructureStore.on('deletegoals', store => {
			if (this.state.favoriteStore && this.state.favoriteStore.length) {
				StructureStore.dispatch({
					action: StructureStore.ACTION_LIST_FAVORITES,
					data: {
						macroId: this.state.favoriteStore[0].levelInstance.plan.parent.id
					},
					opts: {
						wait: true
					}
				});
			}
		}, me);
	},

	componentWillUnmount() {
		StructureStore.off(null, null, this);
	},

	onIconClick(event) {
		if(this.isMounted()){
			this.setState({
				expanded: !this.state.expanded
			});
		}
	},

	deleteFavorite(levelInstance){
		if(this.isMounted()){
			this.setState({
				favoriteIdSelected: levelInstance.id
			});
		}
		
		var msg = Messages.get("label.msg.removeConfirmation") + levelInstance.name + " " + Messages.get("label.msg.favorites");
			Modal.confirmCancelCustom(() => {
				Modal.hide();

				StructureStore.dispatch({
					action: StructureStore.ACTION_REMOVE_FAVORITE,
					data: {
						levelInstanceId: levelInstance.id
					}
				});
			},msg,()=>{Modal.hide()}
		);
	},

	favoriteSelected(levelInstanceId){
		if(this.isMounted()){
			this.setState({
				favoriteIdSelected: levelInstanceId
			});
		}
	},

	render() {
		return ( 
			<div className={"fpdi-treeview marginBottom20"}>
				<div className={"fpdi-treeview-node"}>
					<a className={this.state.expanded ? this.state.expandedIconCls : this.state.iconCls}
						onClick={this.onIconClick} />
					<a onClick={this.onIconClick}>Meus favoritos</a>
				</div>	

				{this.state.expanded ? 
					(this.state.favoriteStore.length > 0 ?
						(this.state.favoriteStore.map((favorite, index) => {
							return (
								<div className="fpdi-treeview-node" key={"favorite"+index}>
									<div className="marginLeft20">
										{/*Ícone estrela de cada item no menu*/}
										<Link 
											className={this.state.favoriteIdSelected == favorite.levelInstance.id ? 
												this.state.favoriteIcon + " deleteIcon" :
												this.state.favoriteIcon + " deleteIcon"}
											to={"/plan/"+favorite.levelInstance.plan.parent.id+"/details/subplan/level/"+favorite.levelInstance.id}
											title={"Remover dos favoritos"}
											onClick={this.deleteFavorite.bind(this, favorite.levelInstance)}
										></Link>
										{/*Texto de cada item no menu*/}
										<Link 
											className={this.state.favoriteIdSelected == favorite.levelInstance.id ? 
												"active" : this.state.labelCls}
											to={"/plan/"+favorite.levelInstance.plan.parent.id+"/details/subplan/level/"+favorite.levelInstance.id}
											title={favorite.levelInstance.name}
											onClick={this.favoriteSelected.bind(this, favorite.levelInstance.id)}
										>
											{favorite.levelInstance.name.length > 25 ?
												favorite.levelInstance.name.trim().substr(0, 25).concat("...").toString()
											:
												favorite.levelInstance.name}
										</Link>
									</div>
								</div>
								)
							})
						)
					:  
						<div className="fpdi-treeview-node">
							<ReactTooltip id='favoriteTooltip' class="favoriteIconTooltip" aria-haspopup='true' role='example' place='bottom' effect="solid" border>
								<FavoriteToolTip />
							</ReactTooltip>

							<div className={"marginLeft20"}>
								{Messages.getEditable("label.haveNoFavorites","fpdi-nav-label")}
								{/*Ícone de informação*/}
								<span data-tip data-type='light' data-for='favoriteTooltip'>
									<i className="mdi mdi-information-outline fpdi-tooltip-info pointer deleteIcon"/>
								</span> 
							</div>
						</div>
					)
				: 	
					""
				}

			</div>		
		);
	}

});

