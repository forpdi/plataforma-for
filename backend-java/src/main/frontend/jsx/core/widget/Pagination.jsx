
import _ from 'underscore';
import React from "react";
import {Store} from 'forpdi/jsx/core/store/Fluxbone.jsx';
import Modal from 'forpdi/jsx/core/widget/Modal.jsx';

export default React.createClass({
	getDefaultProps() {
		return {
			store: Store,
			extraParams: {},
			customAction: null
		};
	},
	getInitialState() {
		return {
			page: 0,
			pages: 0,
			pageLength: 1,
			pageSize: 10,
			total: 0
		};
	},
	componentDidMount() {
		this.props.store.on("sync", this.onSync, this);
		this.load(0);
	},
	componentWillUnmount() {
		this.props.store.off(null, null, this);
	},

	onSync() {
		var total = this.props.store.total;
		if (!(total > 0)) {
			this.setState({
				pages: 0
			});
		} else {
			this.setState({
				pages: 1+(total / this.state.pageSize),
				total: total,
				pageLength: this.props.store.models.length
			});
		}
	},
	load(page) {
		this.props.store.dispatch({
			action: this.props.customAction || this.props.store.ACTION_FIND,
			data: _.extend({
				start: (page*this.state.pageSize),
				limit: this.state.pageSize,
				page: page
			}, this.props.extraParams)
		});
		this.setState({page: page});
	},
	reload() {
		this.props.store.dispatch({
			action: this.props.customAction || this.props.store.ACTION_FIND,
			data: _.extend({
				start: (this.state.page*this.state.pageSize),
				limit: this.state.pageSize,
				page: this.state.page
			}, this.props.extraParams)
		});
	},
	loadPrevious() {
		if (this.state.page > 0) {
			this.load(this.state.page-1);
		}
	},
	loadNext() {
		if (1 < (this.state.pages - this.state.page - 1)) {
			this.load(this.state.page+1);
		}
	},
	render() {
		if (!(this.state.pages > 0)) {
			return (<div></div>);
		}
		var pagesToShow = [];
		if (this.state.page > 1) {
			pagesToShow.push(this.state.page-2);
			pagesToShow.push(this.state.page-1);
		} else if (this.state.page > 0) {
			pagesToShow.push(this.state.page-1);
		}
		pagesToShow.push(this.state.page);
		if (1 < (this.state.pages - this.state.page - 1)) {
			pagesToShow.push(this.state.page+1);
		}
		if (2 < (this.state.pages - this.state.page - 1)) {
			pagesToShow.push(this.state.page+2);
		}
		return (<div className="row">
			<div className='col-sm-4'>
				<span>
					Exibindo {1+(this.state.page*this.state.pageSize)} a {(this.state.page*this.state.pageSize)+this.state.pageLength} de {this.state.total} registros.
				</span>
			</div>
			<div className='col-sm-8 text-right'>
				<nav>
		  			<ul className="pagination pagination-sm">
					    {this.state.page > 0 ? (<li>
					      <a aria-label="Previous" onClick={this.loadPrevious}>
					        <span aria-hidden="true" className="glyphicon glyphicon-chevron-left"></span>
					      </a>
					    </li>):""}
					    {pagesToShow.map((page) => {
					    	if (this.state.page == page) {
					    		return (<li key={"pagination-bt-page-"+page} className="active"><a>{page+1}</a></li>);
					    	}
					    	return (<li key={"pagination-bt-page-"+page}><a onClick={this.load.bind(this,page)}>{page+1}</a></li>);
					    })}
					    {1 < (this.state.pages - this.state.page - 1) ? (<li>
					      <a aria-label="Next" onClick={this.loadNext}>
					        <span aria-hidden="true" className="glyphicon glyphicon-chevron-right"></span>
					      </a>
					    </li>):""}
				  	</ul>
				</nav>
			</div>
		</div>);
	}
});
