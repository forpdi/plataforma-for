import React from "react";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import ForPDIChart from "forpdi/jsx/core/widget/ForPDIChart.jsx"
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import GraphicFilter from "forpdi/jsx_forrisco/dashboard/view/graphic/GraphicFilter.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";


export default React.createClass({

    getInitialState(){
        return {
			unit:null,
			year:null,
			units:[],
			data:[],
			levelActive:[],
			History:[],
			loadingGraph:true,
			updated:false
        };
	},

	componentWillMount() {
		this.state.levelActive=this.props.levelActive
		this.state.History=this.props.history
		this.state.unit=this.props.unit
		this.state.year=(new Date).getFullYear()
		this.props.loading(true)
	 },

	 componentDidMount() {
		this.forceUpdate();
	 },

	 componentWillUpdate(nextProps, nextState) {
		if(this.state.updated==false){
			this.LoadIncidents();
		}
	 },

	 componenDidUpdate(){
		this.LoadIncidents();
	 },

	 componentWillUnmount(){

	 },

	componentWillReceiveProps(newProps){

		this.state.levelActive=newProps.levelActive
		//this.state.History=this.props.history
		this.state.unit=newProps.unit
		this.state.year=(new Date).getFullYear()
		this.props.loading(true)

		if(newProps.history.length>0 && this.state.History != newProps.history){
			this.state.units= newProps.units;
			//this.refresh()
		}
		this.state.updated=false
	},


	unmount(){
		this.props.loading(true)
	},

	onUnitChange(){
		this.state.unit=document.getElementById('selectUnits').value
		this.state.loadingGraph=false
		this.LoadIncidents();
	},

	onYearChange(){
		this.state.year=document.getElementById('selectYear').value
		this.state.loadingGraph=false
		this.LoadIncidents();
	},

	LoadIncidents(){

		var data=[]
		var levels=[]
		var head=['mes']
		var mes={1:'jan',2:'fev',3:'mar',4:'abr',5:'mai',6:'jun',7:'jul',8:'ago',9:'set',10:'out',11:'nov',12:'dez'}
		var month
		if(this.state.year==(new Date).getFullYear()){
			month=(new Date).getMonth()+1
		}else{
			month=12
		}

		//this.state.year//this.state.unit
		//-----------
		for(var j=0; j < this.state.levelActive.length; j++){
			levels.push(this.state.levelActive[j])
		}

		for(var j=0; j < levels.length ; j++){
			head.push(levels[j].level)
		}
		data.push(head)

		for(var i=1;i<=month;i++){
			var p=[]
			p.push(mes[i])

			for(var j=0; j < levels.length ; j++){
				var contain=false
				for(var k=0; k < this.state.History.length; k++){
					if(this.state.History[k].year==this.state.year 									//ano
					&& this.state.History[k].month==i												//mes
					&& (this.state.unit ==-1 || this.state.unit==this.state.History[k].unit.id) 	//unidade
					){
						if(( this.state.History[k].riskLevel && this.state.History[k].riskLevel.level== levels[j].level	)	//level || estado
							||(this.state.History[k].estado && this.state.History[k].estado ==levels[j].level )){
								p.push(this.state.History[k].quantity)
								contain=true
							break;
						}
					}
				}
				if (!contain){
					var lastHistory=null
					var month_aux=0
					//sem registro deste mês, pega o ultimo mês com registro se houver
					for(var k=0; k < this.state.History.length; k++){
						if((this.state.unit ==-1 || this.state.unit==this.state.History[k].unit.id)											//unidade
						&& (( this.state.History[k].riskLevel && this.state.History[k].riskLevel.level== this.state.levelActive[j].level)	//level || estado
						||(this.state.History[k].estado && this.state.History[k].estado ==this.state.levelActive[j].level ))){

							//mesmo ano
							if(this.state.History[k].year==this.state.year && this.state.History[k].month < i){
									if(month_aux<this.state.History[k].month){
										month_aux=this.state.History[k].month
										lastHistory=this.state.History[k]
									}
							//ano diferente
							}else if(this.state.History[k].year<this.state.year){
								if(month_aux<this.state.History[k].month){
									month_aux=this.state.History[k].month
									lastHistory=this.state.History[k]
								}
							}

							if(lastHistory!=null){
								if(this.state.History[k].id<lastHistory.id){
									lastHistory=this.state.History[k]
								}
							}
						}
					}

					if(lastHistory!=null){
						p.push(lastHistory.quantity)
						contain=true
					}
				}

				if (!contain){
					p.push(0)
				}
			}

			data.push(p)
		}
		//-----------

		this.state.data=data
		this.state.loadingGraph=false
		this.state.updated=true

		this.setState({
			data:data,
			loadingGraph:false,
		})

	},

	uniques(array){
		return array.filter(function (value, index, self) {
		  return self.indexOf(value) === index;
		});
	},

	Enable(risklevel){

		for(var i=0; i<this.state.levelActive.length; i++){
			if(this.state.levelActive[i].level==risklevel.level){
				if(this.state.levelActive.length>1){
					this.state.levelActive.splice(i,1)
					this.state.loadingGraph=false
					this.LoadIncidents();
				}
				return
			}
		}

		this.state.levelActive.push(risklevel)
		this.state.loadingGraph=false
		this.LoadIncidents();
		this.forceUpdate();
	},

	Graph(){
		var years=[(new Date).getFullYear()]

		for(var i=0; i < this.state.History.length; i++){
			years.push(this.state.History[i].year)
		}

		years = this.uniques(years).sort().reverse()
		var lines=[]
		var levels=[]
		var color

		for(var j=0; j<this.props.level.length;j++){
			color="Cinza"
			for(var i=0;i<this.state.levelActive.length;i++){
				if(this.state.levelActive[i].level==this.props.level[j].level){
					switch(this.props.level[j].color) {
						case 0: color="Vermelho";  break;
						case 1: color="Marron"; break;
						case 2: color="Amarelo"; break;
						case 3: color="Laranja";  break;
						case 4: color="Verde";  break;
						case 5: color="Azul";  break;
						default: color="Cinza";  break;
					}
				}
			}

			levels.push(<GraphicFilter
				level={this.props.level[j]}
				onClick={this.Enable}
				color={color}
				quantity={false}
			/>)
		}

		for(var i=0; i<this.props.levelActive.length; i++){
			switch(this.props.levelActive[i].color) {
				case 0: lines.push("red"); break;
				case 1: lines.push("brown"); break;
				case 2: lines.push("yellow"); break;
				case 3: lines.push("orange"); break;
				case 4: lines.push("green"); break;
				case 5: lines.push("blue"); break;
				default: lines.push("gray"); break;
			}
		}

		var max=0
		for(var i=0; i < this.state.data.length; i++){
			for(var j=1; j < this.state.data[i].length; j++){
				if(this.state.data[i][j]>max){
					max=this.state.data[i][j]
				}
			}
		}

		var options = {
			hAxis: {title: "Tempo", minValue: 1, maxValue: 12},
			vAxis: {title: 'Quantidade', minValue: 1, maxValue: max},
			legend: 'none',
			//interpolateNulls : true,
			//explorer: {axis: 'horizontal'},
			//bar: {groupWidth: '100%'},
			colors: lines,
		}
		/*var thematicAxes=[]
		var	selectedThematicAxes= -1
		var chartEvents= [{eventName : 'select', callback  : this.onChartClick}]
		*/
		//this.state.updated=false

		return (<div>
			<div className="dashboard-panel-title">
				<span className="frisco-containerSelect"> {Messages.get("label.units")}
				<select onChange={this.onUnitChange} className="form-control dashboard-select-box-graphs marginLeft10" id="selectUnits" >
					<option value={-1} data-placement="right" title={Messages.get("label.viewAll_")}> {Messages.get("label.viewAll_")} </option>
					{this.props.units.map((attr, idy) =>{
						return(
						<option  key={attr.id} value={attr.id} data-placement="right" title={attr.name} >
							{(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
						</option>);})
					}
				</select>
				</span>
			</div>
			<div style={{"text-align": "center"}}>
				<select onChange={this.onYearChange} className="form-control dashboard-select-box-graphs marginLeft10" id="selectYear" >
					{years.map((attr, idx) =>{
						return(
						<option key={idx} value={attr} data-placement="right" title={attr} >{attr}</option>);})
					}
				</select>
			</div>

			<div>
				<ForPDIChart
					chartType="LineChart"
					data={this.state.data}
					options={options}
					graph_id={"LineChart-"+this.props.title}
					width="100%"
					height="250px"
					legend_toggle={true}
					//pageSize={this.state.pageSize}
					//total={this.state.total}
					//onChangePage={this.getInfo}
					//chartEvents={this.state.chartEvents}
					/>
				<div className="colaborator-goal-performance-legend" key={this.props.title}>
					{levels}
				</div>
			</div>
		</div>);
	},

	render() {
		return(<div>
			{this.state.loadingGraph? <LoadingGauge/> :
			Modal.GraphHistory(this.props.title,this.Graph())}
		</div>)
	}

});
