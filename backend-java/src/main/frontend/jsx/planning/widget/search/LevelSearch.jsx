
import moment from 'moment';
import React from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import SearchResult from "forpdi/jsx/planning/widget/search/SearchResult.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

var onClickOutside = require('react-onclickoutside');

export default onClickOutside(React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},

	getDefaultProps() {
		return {
			searchText: Messages.get("label.search"),
			subplans: null,
			submit: null,
			plan:null,
			hiddenSearch: false,
			displayResult: false


		};
	},

	getInitialState() {
		return {
			initDate: null,
			endDate: null,
			hideDate: true,
			resultSearch: [],
			parentIdProps:this.props.plan,
			subplansSelectProps:null,
			levelsSelectProps:null,
			dataInitProps:null,
			dataEndProps:null,
			ordResultProps:null
		};
	},

	componentDidMount(){ 
		 PlanStore.on("planFind", (model) => {
		 
			if (model != null) {
				this.setState({
           			resultSearch:model.data
        		});

        		
  				this.props.hiddenSearch();
  				this.props.displayResult(); 
  				
  				
        		/*<SearchResult terms = {this.refs.termPesquisa != undefined ? this.refs.termPesquisa  : ""} parentId = {this.state.parentIdProps} resultSearch = {this.state.resultSearch} 
        			subPlansSelect = {this.state.subplansSelectProps} levelsSelect = {this.state.levelsSelectProps} dataInit = {this.state.dataInitProps} dataEnd = {this.state.dataEndProps} ordResult = {this.state.ordResultProps}  />*/
			}
		},this);				

	},

	componentWillUnmount() {
		PlanStore.off(null, null, this);
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
			this.context.toastr.addAlertError("Limite de 255 caracteres atingido!");
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

		PlanStore.dispatch({
			action: PlanStore.ACTION_FIND_TERMS,
			data: {
				parentId: this.props.plan,
				terms: this.refs.termPesquisa.value,
				subPlansSelect:subplansSelectP,
				levelsSelect:levelsSelectP,
				dataInit: dataInit,
				dataEnd: dataEnd,
				ordResult: valueResult,
				page:1,
				limit: 10
			},
			opts: {
				wait: true
			}
		});

		
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
					<h1>{Messages.get("label.advancedSearch")}</h1>
					
					<div className="level-search-keyword">
						<h3>{Messages.get("label.keyword")}</h3>
						<input type="text" maxLength="255" onChange={this.onKeyUp} defaultValue={this.props.searchText}  ref = "termPesquisa"/>
					</div> 
			

				<div className="level-search-checkbox">
					<h3>{Messages.get("label.goalsPlan")}</h3>
								
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

						{this.props.subplans.map( (opt,idx) => {
							return (
								<div key={'subplan-opt-'+idx}>
									<input
										onChange={this.selectSubplansOutherFilds}
										name={'subplan-opt'}
										key={'subplan-opt-'+idx}
										type="checkbox"
										value={opt.id}  
										defaultChecked = {true} />
									{opt.name}
								</div>
							);	
						})}
					</div>				
				</div>



				<div className="level-search-checkbox">
					<h3>{Messages.get("label.levels")}</h3>					
								
					<div className="level-search-checkbox-inputs">
						<div key={'level-opt-0'}>
							<input
								onChange={this.selectLevels}
								name={'level-opt'}
								key={'level-opt-0'}
								type="checkbox" 
								defaultChecked = {true} />
							{Messages.get("label.all")}
						</div>

						{this.props.subplans[0] ? this.props.subplans[0].structure.levels.map( (opt,idx) => {							
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
						}) : ""}
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
							<h3>Data de início</h3>
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
						<h3>Data de fim</h3>
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
					<input type="submit" className="level-search-button-search" value="Pesquisar" onClick={this.sendSearch} />
					<input type="submit" className="level-search-button-clear" value="Limpar" onClick={this.clearSearch} />
				</div>
			
			</div>);
		
	}
}));