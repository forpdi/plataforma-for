import _ from 'underscore';
import React from "react";
import {Link} from 'react-router';
import ReactTooltip from 'react-tooltip';
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import ToolTipGoalsPerformance from "forpdi/jsx/planning/widget/plan/ToolTipGoalsPerformance.jsx";
import moment from 'moment';
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import GoalsGenerate from "forpdi/jsx/planning/widget/plan/GoalsGenerate.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx"
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';


export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
		accessLevel: React.PropTypes.number.isRequired,
        accessLevels: React.PropTypes.object.isRequired,
        permissions: React.PropTypes.array.isRequired,
        roles: React.PropTypes.object.isRequired,
        tabPanel: React.PropTypes.object.isRequired,
        router: React.PropTypes.object
	},
	getInitialState() {				
		return {
			loading: true,
			model: undefined,
			title: "",
			hide: false,
			order: undefined,
			screenGoals: false,
			sortingBy: [],
			sonsGeneratorGoals: undefined			
		};
	},

	componentDidMount() {	
		if(this.isMounted()){
			this.setState({
    			adding: false
    		});	
		}
		
		var me = this;
		StructureStore.on("levelSonsRetrieved", (model) => {
			if(typeof this.props.isParent == 'function'){
				this.props.isParent(model);
			}			
			if(this.isMounted()){
				me.setState({
					loading: false,
					model: model,
					title: model.data.sons.total>0 ? model.data.sons.list[0].level.name : ""
				});
			}			
		}, me);
		StructureStore.on("levelGoalClosed", (model) => {
			
			var aux = this.state.model.data.level.name == "Meta" ? this.state.model.data : this.state.model.data.sons.list[this.state.sonIndex];
			aux.closed = model.data.closed;
			aux.closedDate = model.data.closedDate;
			
			if (this.state.model.data.level.name == "Meta")
				this.state.model.data = aux;
			else
				this.state.model.data.sons.list[this.state.sonIndex] = aux;

			if(this.isMounted()){
				this.setState({
					loading: false
				});
			}
			
			//Toastr.remove();
			{aux.closed == false ? 
				/*Toastr.success("Meta aberta com successo")*/
				this.context.toastr.addAlertSuccess("Meta aberta com sucesso")
				:/*Toastr.success("Meta concluída com successo")*/ 
				this.context.toastr.addAlertSuccess("Meta concluída com sucesso")
			}
		}, me);
		this.getLevelSons(this.props.parentId, 1, 5);


		StructureStore.on("goalsGenerated", (model) => {
			if (this.state.model.data.sons && this.state.model.data.sons.list && this.state.model.data.sons.list.length>0) {
				if (model.data.levelInstances && model.data.levelInstances.length>0) {
					for (var i = 0; i < model.data.levelInstances.length; i++) {
						for (var j = 0; j < model.data.levelInstances[i].level.attributes.length; j++) {
							var attributeInstances = this.state.model.data.sons.list[0].level.attributes[j].attributeInstances;
							attributeInstances.push(model.data.levelInstances[i].level.attributes[j].attributeInstances[i]);
							model.data.levelInstances[i].level.attributes[j].attributeInstances = attributeInstances;
						}
						this.state.model.data.sons.list.push(model.data.levelInstances[i]);
					}
				}
			} else {
				var data = this.state.model;
				data.data.sons = model.data.levelInstances;
				if(this.isMounted()){
					this.setState({
						model: data
					});
				}
			}
		},me);
		StructureStore.on('levelAttributeSaved', (model) => {
			var sons = this.state.model != undefined? this.state.model.data.sons : null; //recupera os filhos do nível antes de editar o mesmo
			model.data.sons = sons; //model está vindo sem filhos, inserindo os filhos garantimos que ao salvar edição os filhos apareçam na tela sem que seja necessário dar F5
			if(this.isMounted()){
				this.setState({
					model:model
				});
			}
		});

		StructureStore.on("levelUpdate", (model) => {
			var aux = this.state.model.data.sons.list[this.state.editingFielIdx];
			aux.progressStatus = model.data.progressStatus;
			//aux.level.attributes[this.state.editingAtributeJustify].attributeInstances[this.state.editingFielIdx] = model.data.level.attributes[this.state.editingAtributeJustify].attributeInstances[0];
			aux.level.attributes[this.state.editingAtributeReached].attributeInstances[this.state.editingFielIdx] = model.data.level.attributes[this.state.editingAtributeReached].attributeInstances[0];
		
			if(this.isMounted()){
				this.setState({	
					loading: false
				});
			}
			//Toastr.remove();
			//Toastr.success("Meta editado com sucesso");
			this.context.toastr.addAlertSuccess("Meta editada com sucesso");
			this.cancelEditGoal();
		}, me);
		
		StructureStore.on('deleteLevelInstanceByTable', store => {
			var model = this.state.model;
			var i=0;		
			//console.log(store.data.id);
			var sons = [];
			sons = model.data.sons;
			while(model.data.sons.list[i].id != store.data.id && i < model.data.sons.length){
				i++;
			}
			model.data.sons.list.splice(i-1,1);
			if(this.isMounted()){
				this.setState({
					loading: false,
					model: model
				});
			}
			
			StructureStore.dispatch({
				action: StructureStore.ACTION_RETRIEVE_LEVELSONS,
				data: {
					parentId: me.props.parentId,
					page: null,
					pageSize: null
				}
			});
			this.context.toastr.addAlertSuccess(Messages.get("notification.structure.delete"));	

		}, me);

		StructureStore.on('deletegoals', store => {
			if(this.isMounted()){
				this.setState({
					loading: false,
					goalChecked: false
				});
			}
			
			StructureStore.dispatch({
				action: StructureStore.ACTION_RETRIEVE_LEVELSONS,
				data: {
					parentId: me.props.parentId,
					page: null,
					pageSize: null
				}
			});
			this.context.toastr.addAlertSuccess("Metas excluidas com sucesso.");
		}, me);
	},

	componentWillReceiveProps(newProps){
		if(this.isMounted()){
			this.setState({
				enabled: newProps.enabled
			});
		}
	},

	
	componentWillUnmount() {
		StructureStore.off(null, null, this);
	},

	getLevelSons(parentId, page, pageSize){
		StructureStore.dispatch({
			action: StructureStore.ACTION_RETRIEVE_LEVELSONS,
			data: {
				parentId: parentId,
				page: page,
				pageSize: pageSize
			}
		});
	},

	hideFields() {
		if(this.isMounted()){
			this.setState({
				hide: !this.state.hide
			});
		}
		
	},

	editGoal(id,idx) {
		if(this.isMounted()){
			this.setState({
				editingFieldGoal: id,
				editingFielIdx: idx
			});
		}
		
	},

	editGoalAttributeInstanceField (idJusf,idRe) {
		if(this.isMounted()){
			this.setState({
				editingAtributeJustify: idJusf,
				editingAtributeReached: idRe
			});
		}

	},

	onlyNumber(value, evt){
		var key = evt.which;
		var content = value.refs.alcancadoEdit.value;
		if(key == 44) {
			if (content.indexOf(",") != -1) {
				evt.preventDefault();
				return;
			}
		}
		if(key == 13|| key != 44 && (key < 48 || key > 57)) {			
			evt.preventDefault();
			return;
		} 
	},

	onlyNumberPaste(evt){
		var value = evt.clipboardData.getData('Text');
		value = value.replace(",",".");
		if (!(!isNaN(parseFloat(value)) && isFinite(value))) {
			evt.preventDefault();
			return;
		}
	},

	cancelEditGoal(){
		if(this.isMounted()){
			this.setState({
	    		adding: false,
	    		editingFieldGoal: 0,
				editingFielIdx: null
	    	});
		}
		
	},


	confirmCompleteGoal(id, idx, closeOpenGoal) {
		
		var url = window.location.hash;
  		
		var msg = "";
		var errorMsg ="A meta não pode ser fechada pois existem campos em branco na meta!";
		var error = false;
		
		for(var count=0;count<this.state.model.data.sons.list[idx].level.attributes.length;count++){
			if(this.state.model.data.sons.list[idx].level.attributes[count].required &&
				this.state.model.data.sons.list[idx].level.attributes[count].attributeInstances[idx] ==null){
				error = true;
			}
		}
		errorMsg += " Por favor, acesse a meta e edite os campos para que seja possível fechá-la!";
		if(error){
			//Toastr.remove();
			//Toastr.error(errorMsg);
			this.context.toastr.addAlertError(errorMsg);
			return;
		}

		{closeOpenGoal ? 
			msg = "Tem certeza que deseja concluir a meta?"

		:msg = "Tem certeza que deseja abrir a meta?" }
		
		Modal.confirmCustom(() => {
				Modal.hide();
				if(this.isMounted()){
					this.setState({
						sonIndex: idx,
						loading: true
					});
				}
				
				StructureStore.dispatch({
					action: StructureStore.ACTION_CLOSE_GOAL,
					data: {
						id: id,
						openCloseGoal:closeOpenGoal,
						url: url
					}
				});

			},msg,()=>{Modal.hide()});


		/*Modal.completeGoalCustom(() => {
			Modal.hide();
			this.setState({
				sonIndex: idx,
				loading: true
			});
			StructureStore.dispatch({
				action: StructureStore.ACTION_CLOSE_GOAL,
				data: {
					id: id,
					openCloseGoal:closeOpenGoal
				}
			});
		},msg);*/
	},

	acceptUpdateGoal(id,idx,IdAttributeJust,IdAttributeRea) {			
		this.editGoalAttributeInstanceField(IdAttributeJust,IdAttributeRea);	
		StructureStore.dispatch({
			action: StructureStore.ACTION_UPDATE_GOAL,
			data: {
				id: id,
				reached: this.refs.alcancadoEdit.value.replace(",",".")
				//justification: this.refs.justificativaEdit.value
			}
		});
	},

	sonsGoals (sons) {
		if(this.isMounted()){
			this.setState({
				sonsGeneratorGoals:sons	
			});
		}
	},

	renderEditGoal (model,idx) {
		var IdAttributeJustify;
		var IdAttributeReached;
		return (
			<tr  key={"son-"+idx}>
				{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         		PermissionsTypes.MANAGE_PLAN_PERMISSION)) ? <td/> : undefined}
				<td>{model.name}</td>
				{model.level.attributes.map((attribute, id) => {
					if(attribute.type == AttributeTypes.BUDGET_FIELD ||
						attribute.type == AttributeTypes.ACTION_PLAN_FIELD ||
						attribute.type == AttributeTypes.SCHEDULE_FIELD || 
						!attribute.visibleInTables){
							return;
						}


						if (attribute.reachedField == true) {
							IdAttributeReached = id;
							var reachedReplace = attribute.attributeInstances[idx] && attribute.attributeInstances[idx].value != undefined ?
							 attribute.attributeInstances[idx].value.toString(): "";
							 reachedReplace = reachedReplace.replace(".",",");
							
							return (
								<td className="fdpi-table-cell" key={"attribute-"+id}> 
									<input maxLength="255" className="budget-field-table" ref='alcancadoEdit' type='text' 
									defaultValue={reachedReplace} onKeyPress={this.onlyNumber.bind(this, this)} 
									onPaste={this.onlyNumberPaste}/>
								</td>
							);
						}
					{/* Campo justificativa
						if (attribute.justificationField == true) {
							IdAttributeJustify = id;
							return (
								<td className="fdpi-table-cell" key={"attribute-"+id}>
									<input maxLength="255" className="budget-field-table" ref='justificativaEdit' type='text'
									 defaultValue={attribute.attributeInstances[idx] ? attribute.attributeInstances[idx].value : ""}
									  onKeyPress={this.onKeyUp}/>
								</td>
							);
						} 
					*/} 
						
						if (attribute.type == AttributeTypes.RESPONSIBLE_FIELD) {
							for (var i=0; i<this.props.users.length; i++) {
								if (this.props.users[i].id == attribute.attributeInstances[idx].value) {
									return(
										<td className="fdpi-table-cell" key={"attribute-"+id}>
											{this.props.users[i].name}
										</td>
									);
								}
							}
						} else {
							if (!attribute.justificationField) {
								return (
									<td className="fdpi-table-cell" key={"attribute-"+id}>
										{attribute.attributeInstances[idx] ? 
											(attribute.attributeInstances[idx].formattedValue ?
												attribute.attributeInstances[idx].formattedValue :
											 	attribute.attributeInstances[idx].value) : ""}
									</td>
								);	

							}							
							
						}					
					}
				)}
				
				{model.level.goal ?								 	
					<td className="fdpi-table-cell">
						{model.progressStatus == 1 ? <div className="btn-action-goals-minimumbelow"></div>
 							: model.progressStatus == 2 ? <div className="btn-action-goals-minimum"></div>
 							: model.progressStatus == 3 ? <div className="btn-action-goals-enoughabove"></div> 
 							: model.progressStatus == 4 ? <div className="btn-action-goals-maximumup"></div> 
 						: ""}
					</td>
				:<td/>}

				{model.level.goal ?
				<td className="fdpi-table-cell" className = "column-goals-perfomance">
					<center>
						<span className='mdi mdi-check accepted-budget' title="Salvar" onClick = {this.acceptUpdateGoal.bind(this,model.id,idx,IdAttributeJustify,IdAttributeReached)} ></span>
	              		<span className='mdi mdi-close reject-budget' title="Cancelar" onClick = {this.cancelEditGoal} />
	              	</center>
				</td>
				:<td/>}
			</tr>
		);
	},


	quickSortByName(vet, esq, dir,sorting) {
		var ce = esq;
		var cd = dir;
		var meio = parseInt((ce + cd)/ 2);

		while(ce < cd) {


			if (sorting == "desc") {
				while(vet[ce].name < vet[meio].name) {
					ce++;				
				}

				while(vet[cd].name > vet[meio].name) {
		    	 	cd--;
				}	

			}

			if (sorting == "asc") {
				while(vet[ce].name > vet[meio].name) {
					ce++;				
				}

				while(vet[cd].name < vet[meio].name) {
		    	 	cd--;
				}	

			}

			

			if(ce <= cd) {
	  			var temp = vet[ce];
	  			vet[ce] = vet[cd];
	  			
	  			for (var i = 0; i < vet[ce].level.attributes.length; i++) {
	  					vet[ce].level.attributes[i].attributeInstances[ce] = vet[cd].level.attributes[i].attributeInstances[cd];
	  			}

	  			vet[cd] = temp;

	  			for (var j = 0; j < vet[cd].level.attributes.length; j++) {
	  					vet[cd].level.attributes[j].attributeInstances[cd] = temp.level.attributes[j].attributeInstances[ce];
	  			}

	  			ce++;
	  			cd--;
			}			
		}		
		if(cd > esq)
			this.quickSortByName(vet, esq, cd,sorting);		
	
		if(ce < dir)
  			this.quickSortByName(vet, ce, dir,sorting);		
	},


	quickSortByLevel(vetor, left, right,column,sorting) {
		var le = left;
		var rg = right;
		var meioo = parseInt((le + rg)/ 2);
		var objLe,objPvo,objRg; 
			
			while(le < rg) {

				if (vetor[le].level.attributes[column].attributeInstances[le] != null) {
					objLe = vetor[le].level.attributes[column].attributeInstances[le].value;
				} else {
					objLe = "";
				}

				if (vetor[meioo].level.attributes[column].attributeInstances[meioo] != null) {
					objPvo =  vetor[meioo].level.attributes[column].attributeInstances[meioo].value;
				} else {
					objPvo = "";
				}

				if (vetor[rg].level.attributes[column].attributeInstances[rg] != null) {
					objRg = vetor[rg].level.attributes[column].attributeInstances[rg].value
				} else {
					objRg = "";
				}
				
				if (sorting == "desc") {

					while(objLe < objPvo) {
						le++;

						if (vetor[le].level.attributes[column].attributeInstances[le] == null) {
							objLe = "";
						} else {
							objLe = vetor[le].level.attributes[column].attributeInstances[le].value;
						}
					}
						
							
					while(objRg > objPvo) {
		    			rg--;
		    			if (vetor[rg].level.attributes[column].attributeInstances[rg] == null) {
		    				objRg = "";
		    			} else {
		    				objRg = vetor[rg].level.attributes[column].attributeInstances[rg].value;
		    			}

					}	
					
				}

				if (sorting == "asc") {
					
						while(objLe > objPvo) {
							le++;

							if (vetor[le].level.attributes[column].attributeInstances[le] == null) {
								objLe = "";
							} else {
								objLe = vetor[le].level.attributes[column].attributeInstances[le].value;
							}
						}
						
							
						while(objRg < objPvo) {
		    				rg--;

		    				if (vetor[rg].level.attributes[column].attributeInstances[rg] == null) {
		    					objRg = "";
		    				} else {
		    					objRg = vetor[rg].level.attributes[column].attributeInstances[rg].value;
		    				}

						}	

				}	

				if(le <= rg) {

	  				var temp = vetor[le];
	  				vetor[le] = vetor[rg];
	  				
	  				
	  					for (var i = 0; i < vetor[le].level.attributes.length; i++) {
	  						vetor[le].level.attributes[i].attributeInstances[le] = vetor[rg].level.attributes[i].attributeInstances[rg];
	  					}

	  					vetor[rg] = temp;

	  					for (var j = 0; j < vetor[rg].level.attributes.length; j++) {
	  						vetor[rg].level.attributes[j].attributeInstances[rg] = temp.level.attributes[j].attributeInstances[le];
	  					}
	  				
	  				le++;
	  				rg--;
			
				}
			}		
			if(rg > left)
				this.quickSortByLevel(vetor, left,rg,column,sorting);		
	
			if(le < right)
  				this.quickSortByLevel(vetor, le, right,column,sorting);		
	},


	quickSortByProgressStatus(vt, lft, rgt,sorting) {
		var l = lft;
		var r = rgt;
		var pivo = parseInt((l + r)/ 2);
		
			while(l < r) {
				

				if (sorting == "desc") {

					while(vt[l].progressStatus < vt[pivo].progressStatus) {
						l++;				
					}	

					while(vt[r].progressStatus > vt[pivo].progressStatus) {
		    			 r--;
					}
				}

				if (sorting == "asc") {
					while(vt[l].progressStatus > vt[pivo].progressStatus) {
						l++;				
					}	

					while(vt[r].progressStatus < vt[pivo].progressStatus) {
		    			 r--;
					}
				}	

				if(l <= r) {
	  				var temp = vt[l];
	  				vt[l] = vt[r];
	  				
	  			
	  					for (var i = 0; i < vt[l].level.attributes.length; i++) {
	  						vt[l].level.attributes[i].attributeInstances[l] = vt[r].level.attributes[i].attributeInstances[r];
	  					}

	  					vt[r] = temp;

	  					for (var j = 0; j < vt[r].level.attributes.length; j++) {
	  						vt[r].level.attributes[j].attributeInstances[r] = temp.level.attributes[j].attributeInstances[l];
	  					}
	  				
	  				l++;
	  				r--;
			
				}
			}		
			if(r > lft)
				this.quickSortByProgressStatus(vt, lft,r,sorting);		
	
			if(l < rgt)
  				this.quickSortByProgressStatus(vt, l, rgt,sorting);		
	},



	quickSortByLevelNumber(vt, lft, rgt,column,sorting) {
		var l = lft;
		var r = rgt;
		var pivo = parseInt((l + r)/ 2);
		var objL,objPivo,objR;

			while(l < r) {

				if (vt[l].level.attributes[column].attributeInstances[l] != null) {
					objL = vt[l].level.attributes[column].attributeInstances[l].valueAsNumber;
				} else {
					objL = 0;
				}

				if (vt[pivo].level.attributes[column].attributeInstances[pivo] != null) {
					objPivo =  vt[pivo].level.attributes[column].attributeInstances[pivo].valueAsNumber;
				} else {
					objPivo = 0;
				}

				if (vt[r].level.attributes[column].attributeInstances[r] != null) {
					objR = vt[r].level.attributes[column].attributeInstances[r].valueAsNumber;
				} else {
					objR = 0;
				}
		
				if (sorting == "desc") {

					while(objL < objPivo) {
						l++;

						if (vt[l].level.attributes[column].attributeInstances[l] != null) {
							objL = vt[l].level.attributes[column].attributeInstances[l].valueAsNumber;
						} else {
							objL = 0;
						}
					}	

					while(objR > objPivo) {
		    			 r--;

		    			 if (vt[r].level.attributes[column].attributeInstances[r] != null) {
							objR = vt[r].level.attributes[column].attributeInstances[r].valueAsNumber;
						} else {
							objR = 0;
						}
					}
				}

				if (sorting == "asc") {

					while(objL > objPivo) {
						l++;

						if (vt[l].level.attributes[column].attributeInstances[l] != null) {
							objL = vt[l].level.attributes[column].attributeInstances[l].valueAsNumber;
						} else {
							objL = 0;
						}
					}	

					while(objR < objPivo) {
		    			 r--;

		    			 if (vt[r].level.attributes[column].attributeInstances[r] != null) {
							objR = vt[r].level.attributes[column].attributeInstances[r].valueAsNumber;
						} else {
							objR = 0;
						}
					}
				}	

				if(l <= r) {
	  				var temp = vt[l];
	  				vt[l] = vt[r];
	  				
	  			
	  					for (var i = 0; i < vt[l].level.attributes.length; i++) {
	  						vt[l].level.attributes[i].attributeInstances[l] = vt[r].level.attributes[i].attributeInstances[r];
	  					}

	  					vt[r] = temp;

	  					for (var j = 0; j < vt[r].level.attributes.length; j++) {
	  						vt[r].level.attributes[j].attributeInstances[r] = temp.level.attributes[j].attributeInstances[l];
	  					}
	  				
	  				l++;
	  				r--;
			
				}
			}		
			if(r > lft)
				this.quickSortByLevelNumber(vt, lft,r,column,sorting);		
	
			if(l < rgt)
  				this.quickSortByLevelNumber(vt, l, rgt,column,sorting);		
	},



	quickSortByDates (vt, lft, rgt,column,sorting) {
		var l = lft;
		var r = rgt;
		var pivo = parseInt((l + r)/ 2);
		var d1, d2;

		
		while(l < r) {

			if (sorting == "desc") {
				if(vt[l].level.attributes[column].attributeInstances[l] == null){
					d1 = moment(1, "x"); // setando uma data muito baixa
				}else{
					d1 = moment(vt[l].level.attributes[column].attributeInstances[l].value,"DD/MM/YYYY");	
				}

				if(vt[pivo].level.attributes[column].attributeInstances[pivo] == null){	
					d2 = moment(1, "x"); // setando uma data muito baixa
				}else{
					d2 =moment(vt[pivo].level.attributes[column].attributeInstances[pivo].value,"DD/MM/YYYY");	
				}			
				
				while(d1.isBefore(d2)) {
					l++;
					if(vt[l].level.attributes[column].attributeInstances[l] == null){
						d1 = moment(1, "x"); // setando uma data muito baixa
					}else{
						d1 = moment(vt[l].level.attributes[column].attributeInstances[l].value,"DD/MM/YYYY");	
					}
				}


				if(vt[r].level.attributes[column].attributeInstances[r] == null){
					d1 = moment(1, "x"); // setando uma data muito baixa
				}else{
					d1 = moment(vt[r].level.attributes[column].attributeInstances[r].value,"DD/MM/YYYY");
				}
				
				while(d1.isAfter(d2)) {
	    			r--;
	    			if(vt[r].level.attributes[column].attributeInstances[r] == null){
						d1 = moment(1, "x"); // setando uma data muito baixa
					}else{
						d1 = moment(vt[r].level.attributes[column].attributeInstances[r].value,"DD/MM/YYYY");
					}
				}
				
			}


			if (sorting == "asc") {

				if(vt[l].level.attributes[column].attributeInstances[l] == null){
					d1 = moment(1, "x"); // setando uma data muito baixa
				}else{
					d1 = moment(vt[l].level.attributes[column].attributeInstances[l].value,"DD/MM/YYYY");	
				}

				if(vt[pivo].level.attributes[column].attributeInstances[pivo] == null){	
					d2 = moment(1, "x"); // setando uma data muito baixa
				}else{
					d2 =moment(vt[pivo].level.attributes[column].attributeInstances[pivo].value,"DD/MM/YYYY");	
				}		

				while(d1.isAfter(d2)) {
					l++;
					if(vt[l].level.attributes[column].attributeInstances[l] == null){
						d1 = moment(1, "x"); // setando uma data muito baixa
					}else{
						d1 = moment(vt[l].level.attributes[column].attributeInstances[l].value,"DD/MM/YYYY");	
					}
				}
				
				if(vt[r].level.attributes[column].attributeInstances[r] == null){
					d1 = moment(1, "x"); // setando uma data muito baixa
				}else{
					d1 = moment(vt[r].level.attributes[column].attributeInstances[r].value,"DD/MM/YYYY");
				}
				while(d1.isBefore(d2)) {
	    			r--;
	    			if(vt[r].level.attributes[column].attributeInstances[r] == null){
						d1 = moment(1, "x"); // setando uma data muito baixa
					}else{
						d1 = moment(vt[r].level.attributes[column].attributeInstances[r].value,"DD/MM/YYYY");
					}
				}
				
			}		

			if(l <= r) {
	  			var temp = vt[l];
	  			vt[l] = vt[r];
	  				
	  			for (var i = 0; i < vt[l].level.attributes.length; i++) {
	  				vt[l].level.attributes[i].attributeInstances[l] = vt[r].level.attributes[i].attributeInstances[r];
	  			}

	  			vt[r] = temp;

	  			for (var j = 0; j < vt[r].level.attributes.length; j++) {
	  				vt[r].level.attributes[j].attributeInstances[r] = temp.level.attributes[j].attributeInstances[l];
	  			}
	  				
	  			l++;
	  			r--;
			
			}
		}		
		if(r > lft)
			this.quickSortByDates(vt, lft,r,column,sorting);		
	
		if(l < rgt)
  			this.quickSortByDates(vt, l, rgt,column,sorting);		
	},

	generatingGoals() {		
		var begin = moment(this.props.dateBegin,"DD/MM/YYYY").toDate();
		var end = moment(this.props.dateEnd,"DD/MM/YYYY").toDate();
		if(Object.prototype.toString.call(begin) === "[object Date]" &&
		 Object.prototype.toString.call(end) === "[object Date]"){
			if(!isNaN(begin.getTime()) || !isNaN(end.getTime())){
				if(this.isMounted()){
					this.setState({
						screenGoals:!this.state.screenGoals
					});
				}
				return;	
			} 		 
		}		

		this.context.toastr.addAlertError("Por favor, edite o indicador e preencha os campos obrigatórios para poder gerar metas");					
	},


	sortByProduce(data,sorting){
	  
	  	if(sorting == "asc"){
	  		data.sort(function(a, b) { 
	  			if (a.levelValue > b.levelValue) {
           			return -1;
           		}

           		if (a.levelValue < b.levelValue) {
           			return 1;
           		}

          		return 0; 
       		})
     	} else if(sorting == "desc"){
       		data.sort(function(a, b) { 
          		if (a.levelValue > b.levelValue) {
          			return 1;
          		}
				
				if (a.levelValue < b.levelValue) {
          			return -1;
          		}
     
          		return 0; 
       		})
    	} 

   },

	orderByDefault(column,sorting) {
		var id = 0;
		var vet = [];

		if(this.isMounted()){
			this.setState({
	    		order: column
	    	});
		}


    	if (column == "name") {
    		vet[0] = sorting;

    		for (var j = 1; j < 9; j++) {
    			vet[j] = undefined;
    		}

    		if(this.isMounted()){
    			this.setState ({
	    			sortingBy: vet
	    		});
    		}
    			
    	} else {
	    	var forit;

	    	for (forit = 0; forit < 9; forit++) {
	    		vet[forit] = undefined
	    	}

    		if (column == "performance") {
	    		vet[8] = sorting;

	    		if(this.isMounted()){
	    			this.setState({
	    				sortingBy: vet
	    			});
	    		}
	  
	    	} else if(column == "produce") {
	    		vet[7] = sorting;
	    		if(this.isMounted()){
	    			this.setState({
	    				sortingBy: vet
	    			});
	    		}
	    	} 

	    	else {
	    		vet[column] = sorting;
	    		if(this.isMounted()){
	    			this.setState ({
	    				sortingBy: vet
	    			});
	    		}
    		}

    	}
    		    			
		var model = this.state.model;

		if (column == "name") {
			this.quickSortByName(model.data.sons.list,0,(model.data.sons.list.length-1),sorting);
		} else if (column == "0") {
			this.quickSortByLevel(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);	
		} else if (column == "1") {
				this.quickSortByLevel(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);	
		} else if (column == "2") {
			this.quickSortByDates(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
			//this.quickSortByLevel(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "3") {
			this.quickSortByLevelNumber(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "4") {
			this.quickSortByLevelNumber(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "5") {
			this.quickSortByLevelNumber(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "6") {
			this.quickSortByLevelNumber(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "7") {
			this.quickSortByLevel(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "performance") {
			this.quickSortByProgressStatus(model.data.sons.list,0,(model.data.sons.list.length-1),sorting);
		} else if (column == "produce") {
			this.sortByProduce(model.data.sons.list,sorting);
		}

		if(this.isMounted()){
			this.setState({
	    		model: model
	    	});
		}

	},


	orderBy(column,sorting) {
		
		var vet = [];

		if(this.isMounted()){
			this.setState({
	    		order: column
	    	});
		}

		if (column == "name") {
    		vet[0] = sorting;

    		for (var j = 1; j < 9; j++) {
    			vet[j] = undefined;
    		}

    		if(this.isMounted()){
    			this.setState ({
	    			sortingBy: vet
	    		});
    		}
    		
    			
    	} else {
			var forit;

	    	for (forit = 0; forit < 9; forit++) {
	    		vet[forit] = undefined
	    	}

    		if (column == "performance") {
	    		vet[8] = sorting;

	    		if(this.isMounted()){
	    			this.setState({
	    				sortingBy: vet
	    			});
	    		}

	    	} else if (column == "produce") {
	    		vet[7] = sorting;
	    		if(this.isMounted()){
    				this.setState ({
	    				sortingBy: vet
	    			});	
    			}
	    	} else {
	    		vet[column] = sorting;
    			this.setState ({
    				sortingBy: vet
    			});
    		}
    	}

		var model = this.state.model;

		if (column == "name") {
			this.quickSortByName(model.data.sons.list,0,(model.data.sons.list.length-1),sorting);
		} else if (column == "0") {
			this.quickSortByLevel(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);	
		} else if (column == "1") {
				this.quickSortByLevel(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);	
		} else if (column == "2") {
			this.quickSortByDates(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
			//this.quickSortByLevel(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "3") {
			this.quickSortByLevelNumber(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "4") {
			this.quickSortByLevelNumber(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "5") {
			this.quickSortByLevelNumber(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "6") {
			this.quickSortByLevelNumber(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "7") {
			this.quickSortByLevel(model.data.sons.list,0,(model.data.sons.list.length-1),column,sorting);
		} else if (column == "performance") {
			this.quickSortByProgressStatus(model.data.sons.list,0,(model.data.sons.list.length-1),sorting);
		} else if (column == "produce") {
			this.sortByProduce(model.data.sons.list,sorting);
		}

		if(this.isMounted()){
			this.setState({
	    		model: model
	    	});
		}

	},

	deleteLevelAttribute(idGoal) {
		var msg = "Você tem certeza que deseja excluir ?";
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			if(this.isMounted()){
				this.setState({
					loading: true
				});
			}
			
			var levelInstance = {
				id: idGoal,
			};
			StructureStore.dispatch({
				action: StructureStore.ACTION_DELETE_LEVELINSTANCE_BY_TABLE,
				data: levelInstance
			});
		},msg,()=>{Modal.hide()});
	},

	selectAllGoals(){
		this.state.model.data.sons.list.map((goal, idx) => {
			if(!goal.closed)
				this.refs["goal-checkbox-"+idx].checked = this.refs["all-goals-checkbox"].checked;
		});	
		if(this.isMounted()){
			this.setState({
				goalChecked: this.refs["all-goals-checkbox"].checked
			});
		}
	},

	checkGoal(){
		var bool = false;
		this.state.model.data.sons.list.map((goal, idx) => {
			if (this.refs["goal-checkbox-"+idx].checked == false) {
				this.refs["all-goals-checkbox"].checked = false;		
			}

			if(this.refs["goal-checkbox-"+idx].checked){
				bool = true;
			}

		});

		if(this.isMounted()){
			this.setState({
				goalChecked: bool
			});
		}
	},

	deleteSelectedGoals(){
		var goals = [];

		this.state.model.data.sons.list.map((goal, idx) => {
			if(this.refs["goal-checkbox-"+idx].checked){
				goals.push(goal.id);
			} 
		});
		if(goals.length <= 0){
			return;
		}
		var msg = "Deseja mesmo excluir "+goals.length+" metas?";
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			if(this.isMounted()){
				this.setState({
					loading: true
				});	
			}
					
			StructureStore.dispatch({
				action: StructureStore.ACTION_GOALS_DELETE,
				data: {
					list: goals,
					total: goals.length
				}
			});
		},msg,()=>{Modal.hide()});
	},
	
	renderGoalField(edit, model, idx) {
		var isEditGoal;
		var isEditGoalReached;
		var responsible;
		if (edit) {
				return this.renderEditGoal(model,idx);
		} else {
			
			isEditGoal = true;
			isEditGoalReached = true;

			if (model.level.attributes[0].attributeInstances[idx] != null) {	
				if (model.level.attributes[0].attributeInstances[idx].value == "") {
						isEditGoal = false;
				}

							
			} else {
				isEditGoal = false;
				
			}

			if (model.level.attributes[5] != undefined) {
				if (model.level.attributes[5].attributeInstances[idx] != null) {
					if (model.level.attributes[5].attributeInstances[idx].value == "") {
						isEditGoalReached = false;
					}					
				} 

				else {
					isEditGoalReached = false;
				}
			}

			return (
				<tr  key={"son-"+idx}>
				{model.level.goal && (this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         		PermissionsTypes.MANAGE_PLAN_PERMISSION)) ? 
				<td>
					<center>
						<input type="checkbox" ref={"goal-checkbox-"+idx} onClick={this.checkGoal} 
						disabled={model.closed}/>
					</center>					
				</td> : undefined}
				<td>{(model.name.length>70) ? 
					<Link
						to={"/plan/"+model.plan.parent.id+"/details/subplan/level/"+model.id}
						title={model.name}>
							{(model.name.substring(0, 70)).concat("...")}
					</Link>
				: 
					<Link
						to={"/plan/"+model.plan.parent.id+"/details/subplan/level/"+model.id}
						title={(model.name)}>
							{(model.name)}
					</Link>
				}</td>
				{model.level.attributes.map((attribute, id) => {					
					if(attribute.type == AttributeTypes.BUDGET_FIELD ||
						attribute.type == AttributeTypes.ACTION_PLAN_FIELD ||
						attribute.type == AttributeTypes.SCHEDULE_FIELD || 
						!attribute.visibleInTables){
							return;
						}

						if (model.deadlineStatus == 3) {
							if (attribute.finishDate == true) {
								return (
									<td className="fdpi-table-cell" key={"attribute-"+id}>
										{attribute.attributeInstances[idx] ? attribute.attributeInstances[idx].value : ""} 
										<i className="mdi-clock-late-goal mdi mdi-clock pointer"  title="Meta atrasada"/>
									</td>
								);
							}
						}
						if (!attribute.justificationField) { 
							if(attribute.attributeInstances[idx] != null && attribute.attributeInstances[idx].value && attribute.attributeInstances[idx].value.length >=70){
								return (
									<td className="fdpi-table-cell" key={"attribute-"+id}>
										{attribute.attributeInstances[idx] ? attribute.attributeInstances[idx].value.substring(0, 70) : ""}
										<div>
											<br/>
											<Link
												to={"/plan/"+model.plan.parent.id+"/details/subplan/level/"+model.id}
												title="ver mais">
													ver mais ...
											</Link>
										</div>
									</td>
								);
							}
						}
						
						if (attribute.attributeInstances[idx]) {
							if (!attribute.justificationField) {
								if (attribute.type == AttributeTypes.RESPONSIBLE_FIELD) {
									for (var i=0; i<this.props.users.length; i++) {
										if (this.props.users[i].id == attribute.attributeInstances[idx].value) {
											responsible = this.props.users[i];	
											if (this.context.roles.MANAGER  || _.contains(this.context.permissions, 
												"org.forpdi.core.user.authz.permission.ManageUsersPermission") || _.contains(this.context.permissions, 
												"org.forpdi.core.user.authz.permission.ViewUsersPermission")) {
												return(
													<td className="fdpi-table-cell" key={"attribute-"+id}>
														{this.props.users[i].name}
													</td>
												);
											} else {
												return(
													<td className="fdpi-table-cell" key={"attribute-"+id}>
														{this.props.users[i].name}
													</td>
												);
											}
										}
									}
								
								} else {
									if(attribute.type == AttributeTypes.NUMBER_FIELD) {
										return (
											<td className="fdpi-table-cell" key={"attribute-"+id}>
												{attribute.attributeInstances[idx].formattedValue}
											</td>
										);
									}
									return (
										<td className="fdpi-table-cell" key={"attribute-"+id}>
											{attribute.attributeInstances[idx].value}
										</td>
									);
								}
							}
						} else {
							if (!attribute.justificationField) {
								return (
									<td className="fdpi-table-cell" key={"attribute-"+id}>
										{""}
									</td>
								);

							}
							
						}
						
				})}
				
				{/* Rendimento
					<td className="fdpi-table-cell">{model.levelValue ? model.levelValue.toFixed(2).replace(".", ",")+"%" : ""}</td>
				*/}
				{model.level.goal ?								 	
					<td className="fdpi-table-cell">
						{model.progressStatus == 1 ? <div className="btn-action-goals-minimumbelow"></div>
 							: model.progressStatus == 2 ? <div className="btn-action-goals-minimum"></div>
 							: model.progressStatus == 3 ? <div className="btn-action-goals-enoughabove"></div> 
 							: model.progressStatus == 4 ? <div className="btn-action-goals-maximumup"></div> 
 						: ""}
					</td>
				:<td/>}
					

				{model.level.goal ?
					
					<td className="fdpi-table-cell">
						<center>
							{
								!model.closed ? (this.context.roles.MANAGER 
								|| _.contains(this.context.permissions, PermissionsTypes.MANAGE_PLAN_PERMISSION) 
								|| (responsible && responsible.id == UserSession.get("user").id)) ?  (
									<i className  = {isEditGoal == true ? "mdi mdi-pencil edit-open" :"mdi mdi-pencil edit-close"} 
										onClick= {isEditGoal == true ? this.editGoal.bind(this,model.id,idx) : ""} 
										title= {isEditGoal == false ? "Não é possível editar a meta sem estar preenchida" : "Editar"} 
										 data-placement="top" data-container="body" >
									</i>) :("")
	         					:""
							}


							{((this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         					PermissionsTypes.MANAGE_PLAN_PERMISSION)) && !model.closed)  ?
								<i type="submit" className="mdi mdi-delete cursorPointer" title="Excluir"
								onClick = {this.deleteLevelAttribute.bind(this,model.id)} />						
							:""}
							
							{this.context.roles.MANAGER || _.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION)  
								|| (responsible && responsible.id == UserSession.get("user").id) ?
									(
										isEditGoalReached == true ? 
											(<i className= {model.closed == false ? "mdi mdi-lock-open-outline lockGoal-open":"mdi  mdi-lock lockGoal-close"} 
												onClick={model.closed == false ? this.confirmCompleteGoal.bind(this,model.id, idx,true) : 
													((this.context.roles.MANAGER || _.contains(this.context.permissions, PermissionsTypes.MANAGE_PLAN_PERMISSION)) ?
													 this.confirmCompleteGoal.bind(this, model.id, idx,false) : "") } title= {model.closed == false ? "Concluir Meta: Ao concluir a meta, você indica ao sistema que ela está finalizada" :
													  ((this.context.roles.MANAGER || _.contains(this.context.permissions, PermissionsTypes.MANAGE_PLAN_PERMISSION)) ? "Abrir Meta" : "Você não pode reabrir a meta, entre em contato com o administrador")}  
													  data-placement="top" data-container="body">
											</i>)
										: (<i className = "mdi  mdi-lock lockGoal-locked" onClick={""} 
											title= {"Concluir Meta: O campo alcançado deve estar preenchido"}  data-placement="top"
											 data-container="body"> </i>)
									)
								: ""
							}
						</center>
					</td>
				:<td/>}																																	
			</tr>
		);
	}
	},

	pageChange(page, pageSize){	
		var me = this;
		this.refs["all-goals-checkbox"].checked = false;	
		this.selectAllGoals();
		this.getLevelSons(this.props.parentId, page, pageSize);


		if (this.state.model.data.sons && this.state.model.data.sons.length>0 && this.state.model.data.sons[0].level.goal) {
			if(this.isMounted()){
				this.setState({
					goalChecked: true
				});
			}
		}

	},

	renderLevelSons(sons) {		
		if (sons && sons.length>0) {
			return(
			<div className="panel panel-default panel-margins">
				<ReactTooltip class='goalToolTip' id='toolTipGoalProgress' aria-haspopup='true' role='example' data-place='top' effect='solid' border>
					<ToolTipGoalsPerformance />
				</ReactTooltip>
				<div className="panel-heading dashboard-panel-title">
					<b className="budget-graphic-title">{this.state.title}</b>
					{sons[0].level.goal ? (this.context.roles.MANAGER || 
							_.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION) ?
							<div className = "floatLeft">
								{this.state.goalChecked ? 
									<button type="button" className={"btn btn-danger delete-all-btn floatLeft "} 
									onClick={this.deleteSelectedGoals}>
										<i className="mdi mdi-delete positionStatic"/>
									</button>  : 
									<button type="button" className={"btn btn-danger delete-all-btn floatLeft "} 
									onClick={this.deleteSelectedGoals} disabled>
										<i className="mdi mdi-delete positionStatic"/>
									</button> }
							</div> : ""): ""
					}
					<div className="budget-btns">
						{sons[0].level.goal ? (this.context.roles.MANAGER || 
							_.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION) ? 								
								<button type="button" className="btn btn-primary budget-new-btn" onClick={this.generatingGoals}>
									Gerar metas 
								</button>
							:""
							)							
						:""}


						{this.state.screenGoals ?
							<div className = "container Generate-Goal-Position">
							<GoalsGenerate users={this.props.users}
 								hiddenSearch={this.generatingGoals} parentId={this.props.parentId} ref = "GoalsGenerate"
  							/> 
  							</div> : ""
						}

						<span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
					</div>						
				</div>				
					{!this.state.hide ?
						<div>
							<table className="budget-field-table table">
								<tbody>
									<tr>
										{sons[0].level.goal && (this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         							PermissionsTypes.MANAGE_PLAN_PERMISSION))? 
											<th className = "column-goals-perfomance"> 
												<input type="checkbox" title="Todos" onClick={this.selectAllGoals} ref="all-goals-checkbox"/>
											</th>
											: undefined}
										
											<th className = "column-goals-perfomance"> Nome  
												<span className= {this.state.order == "name" ? (this.state.sortingBy[0] == "asc" ? 
													"mdi mdi-sort-descending cursorPointer " : "mdi mdi-sort-ascending cursorPointer" ) :
													 "mdi mdi mdi-sort cursorPointer"} onClick = {this.state.sortingBy[0] != undefined ?
													  (this.state.sortingBy[0] == "asc" ? this.orderBy.bind(this,"name","desc") :
													   this.orderBy.bind(this,"name","asc") ) : this.orderByDefault.bind(this,"name","desc") }
													    title="Ordenar"/>
											</th>
											
										{sons[0].level.attributes.map((attribute, id) => {							
											if(attribute.type == AttributeTypes.BUDGET_FIELD||
												attribute.type == AttributeTypes.ACTION_PLAN_FIELD ||
												attribute.type == AttributeTypes.SCHEDULE_FIELD || 
												!attribute.visibleInTables){
												return;
											}
											
											if (sons[0].level.goal ) {
												if (!attribute.justificationField) {											
													return ( 
														<th key={"att-"+id} className = "column-goals-perfomance"> 
															<span> {attribute.label} </span>
															{attribute.finishDate || attribute.reachedField ? 
																<span  className= {this.state.order == id ? 
																	(this.state.sortingBy[id] == "asc" ? " mdi mdi-sort-descending cursorPointer" : 
																		"mdi mdi-sort-ascending cursorPointer" ) : "mdi mdi mdi-sort cursorPointer"}
																 		onClick = {this.state.sortingBy[id] != undefined ? 
																 			(this.state.sortingBy[id] == "asc" ? this.orderBy.bind(this,id,"desc") :
																 	 		this.orderBy.bind(this,id,"asc") ) : this.orderByDefault.bind(this,id,"desc")  } title="Ordenar">
																</span>
															:""}  
														</th>
													);
												}

											} else {
												if (!attribute.justificationField) {
													return ( <th key={"att-"+id} className = "column-goals-perfomance"> {attribute.label}   </th>);
												}
											}
												
										})}		
										{/*
											<th className = "column-goals-perfomance"> Rendimento 
													<span  className= {this.state.order == "produce" ? (this.state.sortingBy[7] == "asc" ?
													 	"mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort-ascending cursorPointer" ) : "mdi mdi mdi-sort cursorPointer"}
												  		onClick = {this.state.sortingBy[7] != undefined ? (this.state.sortingBy[7] == "asc" ?
												   		this.orderBy.bind(this,"produce","desc") : this.orderBy.bind(this,"produce","asc") ) :
												    	this.orderByDefault.bind(this,"produce","desc") } title="Ordenar">
													</span>										 
											</th>
										*/}					

										{sons[0].level.goal ?
											<th className = "column-goals-perfomance"> 
												Desempenho 
												<span data-tip data-type='light' data-for='toolTipGoalProgress'>
													<i className="mdi mdi-information-outline fpdi-tooltip-info pointer"/>
												</span>
												{/* 
													<span  className= {this.state.order == "performance" ? (this.state.sortingBy[8] == "asc" ?
												 		"mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort-ascending cursorPointer" ) : "mdi mdi mdi-sort cursorPointer"}
												  		onClick = {this.state.sortingBy[8] != undefined ? (this.state.sortingBy[8] == "asc" ?
												   		this.orderBy.bind(this,"performance","desc") : this.orderBy.bind(this,"performance","asc") ) :
												    	this.orderByDefault.bind(this,"performance","desc") } title="Ordenar">
													</span>
												*/}
											</th>
										: <th/>}

										{sons[0].level.goal ?
											<th className = "column-goals-perfomance column-goals-perfomance-action"> Ações </th>
										: <th/>}
									
									
									</tr>
									{sons.map((son, idx) => {
										return this.renderGoalField((son.id == this.state.editingFieldGoal),son,idx);									
									})	
								}

								</tbody>
							</table>
							<TablePagination 
								total={this.state.model.data.sons.total}
								onChangePage={this.pageChange}
								tableName={"sons-table"+this.state.model.data.id}
							/>
						</div>: ("")
					}
			</div>);
		} else if(!this.state.model.data.aggregate && !this.state.model.data.level.goal) {
			return (<div className="panel panel-default panel-margins">
				<div className="panel-heading displayFlex">
					<b className="budget-title"> {this.state.model.data.levelSon ? this.state.model.data.levelSon.name : ""}</b>
					<div className="budget-btns">
						{this.state.model.data.levelSon && this.state.model.data.levelSon.goal ?
							<button type="button" className="btn btn-primary budget-new-btn" onClick={this.generatingGoals}>Gerar metas </button>
						:""}


						{this.state.screenGoals ?
							<div className = "container Generate-Goal-Position">
							<GoalsGenerate users={this.props.users}
 								hiddenSearch={this.generatingGoals} parentId={this.props.parentId} ref = "GoalsGenerate"
  							/> 
  							</div> : ""
						}


						<span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
					</div>	
					</div>
					{!this.state.hide ? (
					<div className='table-empty-sons'> 
						Não possui nenhum(a) {this.state.model.data.levelSon ? this.state.model.data.levelSon.name.toLowerCase() : " filho"}
				    </div>):("")}
				</div>);
		}
	},
	render() {
		//|| !this.state.enabled
	
		if (this.state.loading) {
			return null;
		}

		return(
			<div>
				{this.renderLevelSons(this.state.model.data.sons.list)}
			</div>
		);
	}		
});
