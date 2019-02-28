import moment from 'moment';
import React from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

var onClickOutside = require('react-onclickoutside');

export default onClickOutside(React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},

	getDefaultProps() {
		return {
			searchText: Messages.get("label.search"),
			subitensSelect:[],
			//itensSelect:null,
			subplans: null,
			submit: null,
			plan:null,
			hiddenSearch: false,
			displayResult: false


		};
	},

	getInitialState() {
		return {
			subitensSelect:[],
			initDate: null,
			endDate: null,
			hideDate: true,
			resultSearch: [],
			parentIdProps:this.props.plan,
			subplansSelectProps:null,
			levelsSelectProps:null,
			dataInitProps:null,
			dataEndProps:null,
			ordResultProps:null,
			subplans: []
		};
	},

	componentDidMount() {
		PlanRiskItemStore.on("allSubItensByPlan",(model) => {
			this.setState({
				subitensSelect: model.data
			})

		},this);

		if (this.props.planRisk) {
			PlanRiskItemStore.dispatch({
				action: PlanRiskItemStore.ACTION_GET_SUB_ITENS_BY_PLANRISK,
				data: this.props.planRisk
			});
		}

		this.props.subplans.shift();
		this.props.subplans.pop();

		this.setState({
			subplans: this.props.subplans
		})
	},

	componentWillUnmount() {
		PlanRiskItemStore.off(null, null, this);
	},

	selectSubplans() {
		var subplans = document.getElementsByName("subplan-opt");
		var outherFields = true;


		if (subplans[0].checked) {
			for (var i = subplans.length - 1; i > 0; i--) {
				subplans[i].checked = true;
			}
			outherFields = false;
		} else if (subplans[0].checked == false) {
			for (var i = subplans.length - 1; i > 0; i--) {
				subplans[i].checked = false;
			}
			outherFields = false;
		}

		if (outherFields == true) {
			subplans[0].checked = false
		}
	},


	selectSubplansOutherFilds () {
		var subplans = document.getElementsByName("subplan-opt");
		subplans[0].checked = false;
	},

	selectLevels() {
		var levels = document.getElementsByName("level-opt");

		if (levels[0].checked) {
			for (var i = levels.length - 1; i > 0; i--) {
				levels[i].checked = true;
			}
		} else if (levels[0].checked == false) {
			for (var i = levels.length - 1; i > 0; i--) {
				levels[i].checked = false;
			}
		}
	},

	selectLevelsOutherFilds () {
		var levels = document.getElementsByName("level-opt");
		levels[0].checked = false;
	},


	selectDate() {
		var selectedDate = document.getElementById("select-date");

		var value = selectedDate.options[selectedDate.selectedIndex].value;

		this.setState({
			initDate: moment(),
			endDate: moment(),
			hideDate: false
		});

		var month = moment().month();
		var year = moment().year();
		var day = moment().date();

		if(value == 3) {
			this.setState({
				initDate: moment([year, month, day-1])
			});
		}
		else if(value == 4) {
			this.setState({
				initDate: moment([year, month, day-7])
			});
		}
		else if(value == 5) {
			this.setState({
				initDate: moment([year, month-1, day])
			});
		}
	},

	onChangeInit(data) {
		this.setState({
			initDate: data
		});
	},

	onChangeEnd(data) {
		this.setState({
			endDate: data
		});
	},

	clearSearch() {
		var subplans = document.getElementsByName("subplan-opt");

		for (var i = subplans.length - 1; i >= 0; i--) {
			subplans[i].checked = false;
		}

		var levels = document.getElementsByName("level-opt");

		for (var i = levels.length - 1; i >= 0; i--) {
			levels[i].checked = false;
		}

		this.refs.termPesquisa.value = "";
	},


	maxLengthMask(){
		if(this.refs.termPesquisa.value.length >= 255){
			this.context.toastr.addAlertError(Messages.get("label.limitCaracteres"));
		}
	},

	onKeyUp(evt){
		this.maxLengthMask();
	},


	sendSearch() {
		var searchText = this.props.searchText;

		var subplans = document.getElementsByName("subplan-opt");
		var subplansSelect = [];

		for (var i = subplans.length - 1; i >= 0; i--) {
			if(subplans[i].checked) {
				subplansSelect.push(subplans[i].value);
			}
		}

		var levels = document.getElementsByName("level-opt");
		var levelsSelect = [];

		for (var i = levels.length - 1; i >= 0; i--) {
			if(levels[i].checked) {
				levelsSelect.push(levels[i].value);
			}
		}

		if (this.state.initDate != null && this.state.endDate != null) {
			var dataInit = this.state.initDate.format("DD/MM/YYYY");
			var dataEnd = this.state.endDate.format("DD/MM/YYYY");
		} else {
			dataInit = "";
			dataEnd = "";
		}

		var valueResult = 0;//parseInt(document.getElementById("select-result").value);
		var subplansSelectP = [];
		var levelsSelectP = [];
		for (var i = 0; i < subplansSelect.length; i++) {
			if (subplansSelect[i] != 'on') {
				subplansSelectP[i] = subplansSelect[i];
			}
		}

		for (var i = 0; i < levelsSelect.length; i++) {
			if (levelsSelect[i] != 'on') {
				levelsSelectP[i] = levelsSelect[i];
			}
		}

		this.setState({
			subplansSelectProps:subplansSelectP,
			levelsSelectProps:levelsSelectP,
			dataInitProps:dataInit,
			dataEndProps:dataEnd,
			ordResultProps:valueResult
		});

		if(this.refs.termPesquisa.value== "" ||  !!this.refs.termPesquisa.value.match(/^(\s)+$/) ){
			this.props.hiddenSearch();
			return
		}

		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_SEARCH_BY_KEY,
			data: {
				planRiskId: this.props.planRisk,
				terms:this.refs.termPesquisa.value,
				itensSelect: subplansSelectP,
				subitensSelect: levelsSelectP,
				page:1,
				limit:10
			},
			opts: {
				wait: true
			}
		});

		this.props.hiddenSearch();
		this.props.displayResult();
	},

	handleClickOutside: function(evt) {
		this.props.hiddenSearch();
	},

	render() {

		return (

			<div className="level-search">

				<div className='displayFlex-level-search'>
					<span className='mdi-level-search mdi mdi-close-circle cursorPointer' onClick={this.props.hiddenSearch} title={Messages.get("label.close")}></span>
				</div>
				<h1>{Messages.getEditable("label.advancedSearch","fpdi-nav-label")}</h1>

				<div className="level-search-keyword">
					<h3>{Messages.getEditable("label.keyword","fpdi-nav-label")}</h3>
					<input type="text" maxLength="255" onChange={this.onKeyUp} defaultValue={this.props.searchText}  ref = "termPesquisa"/>
				</div>


				<div className="level-search-checkbox">
					<h3>{Messages.getEditable("label.items","fpdi-nav-label")}</h3>

					<div className="level-search-checkbox-inputs">
						<div key={'subplan-opt-0'}>
							<input
								onChange={this.selectSubplans}
								name={'subplan-opt'}
								key={'subplan-opt-0'}
								type="checkbox"
								defaultChecked = {true} />
							Todos
						</div>

						{this.state.subplans.map( (opt,idx) => {
							return (
								<div key={'subplan-opt-' + idx}>
									<input
										onChange={this.selectSubplansOutherFilds}
										name={'subplan-opt'}
										key={'subplan-opt-'+idx}
										type="checkbox"
										value={opt.id}
										defaultChecked = {true} />
									{opt.label}
								</div>
							);
						})}
					</div>
				</div>



				<div className="level-search-checkbox">
					<h3>{Messages.getEditable("label.subitems","fpdi-nav-label")}</h3>

					<div className="level-search-checkbox-inputs">
						<div key={'level-opt-0'}>
							<input
								onChange={this.selectLevels}
								name={'level-opt'}
								key={'level-opt-0'}
								type="checkbox"
								defaultChecked = {true} />
							{Messages.getEditable("label.all","fpdi-nav-label")}
						</div>

						{this.state.subitensSelect.map( (opt,idx) => {
							return (
								<div key={'level-opt-'+idx}>
									<input
										onChange={this.selectLevelsOutherFilds}
										name={'level-opt'}
										key={'level-opt-'+idx}
										type="checkbox"
										value={opt.id}
										defaultChecked = {true} />
									{opt.name}
								</div>
							);
						})}
					</div>

				</div>

				{/*
				<div className="level-search-select">
					<h3>Data de modificação</h3>
					<select type="text" placeholder="Selecione" id="select-date" onChange={this.selectDate}>
						<option value="0"  data-placement="right" title="Selecione uma data">Selecione uma data</option>
						<option value="1"  data-placement="right" title="Qualquer data">Qualquer data</option>
						<option value="2"  data-placement="right" title="Hoje">Hoje</option>
						<option value="3"  data-placement="right" title="Ontem">Ontem</option>
						<option value="4"  data-placement="right" title="Últimos 7 dias">Últimos 7 dias</option>
						<option value="5"  data-placement="right" title="Últimos 30 dias">Últimos 30 dias</option>
						<option value="6"  data-placement="right" title="Personalizado">Personalizado</option>
					</select>
				</div>
			*/}
				{!this.state.hideDate ?
					(<div className="level-search-date">
						<div className="level-search-date-init">
							<h3>{Messages.getEditable("label.dateBegin","fpdi-nav-label")}</h3>
							<DatePicker
								type="datepicker"
								ref='begin'
								dateFormat="DD/MM/YYYY"
								selected={this.state.initDate}
								onChange={this.onChangeInit}
								placeholderText="DD/MM/AAAA"
								showYearDropdown
							/>
						</div>

						<div className="level-search-date-init">
							<h3>{Messages.getEditable("label.dataEnd","fpdi-nav-label")}</h3>
							<DatePicker
								type="datepicker"
								ref='end'
								dateFormat="DD/MM/YYYY"
								selected={this.state.endDate}
								onChange={this.onChangeEnd}
								placeholderText="DD/MM/AAAA"
								showYearDropdown
							/>
						</div>
					</div>) : ("")}



				{/*<div className="level-search-select">
					<h3>Ordenar resultados</h3>
					<select type="text" placeholder="Selecione" id="select-result">
						<option value="1"  data-placement="right" title="Criação mais antiga">Criação mais antiga </option>
						<option value="2"  data-placement="right" title="Criação mais recente">Criação mais recente </option>
						<option value="3"  data-placement="right" title="Atualização mais recente">Atualização mais recente</option>
						<option value="4"  data-placement="right" title="Atualização mais antiga">Atualização mais antiga</option>
					</select>
				</div>*/}


				<div className="level-search-buttons">
					<input type="submit" className="level-search-button-search" value={Messages.get("label.research")} onClick={this.sendSearch} />
					<input type="submit" className="level-search-button-clear" value={Messages.get("label.clean")} onClick={this.clearSearch} />
				</div>

			</div>);

	}
}));
