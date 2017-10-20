import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import moment from 'moment';
import S from 'string';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

var Validate = {
    validateTitle: function (data, idx, editFunc) {
        if(data.refs['edit-input'] != undefined) {
            if(data.refs['edit-input'].value.trim() != ""){
                data.refs['edit-input'].className = "form-control";
                data.refs['formAlertError-edit-input'].innerHTML = "";
                editFunc(data.refs['edit-input'].value, idx);
                return true;
            }else{
                data.refs['formAlertError-edit-input'].innerHTML = Messages.get("label.thisFieldMustBeFilled");
                data.refs['edit-input'].className = "form-control borderError";
                return false;
            }
        }
    },

    validateNumber: function isNumber(n) {		
    	return !isNaN(parseFloat(n)) && isFinite(n);
	},

	validateSectionTitle : function(data,dataTitle) {
		if (data.value.trim() == "") {
			 dataTitle.innerHTML = Messages.get("label.thisFieldMustBeFilled");
			 data.className = "form-control borderError";
			return true

		} else {
			return false
		}

	},

    validateAttributePlan: function(model, levelForm, data, aggregate) {
        var planMacroDateBegin = model.data.plan.begin.split(" ");
		planMacroDateBegin = moment(planMacroDateBegin,"DD/MM/YYYY").toDate();
		var planMacroDateEnd = model.data.plan.end.split(" ");
		planMacroDateEnd = moment(planMacroDateEnd,"DD/MM/YYYY").toDate();

		var begin;
		var end;

		var attributes = [];
		var cmpTxtArea;
		var nome = levelForm.refs['name'].getValue();		

		var msg = Messages.get("label.form.error");
 		var boolMsg = false;
		
		var positionExpec = -1;
 		var expec = null;
		var positionMin = -1;
		var min = null;
		var positionMax = -1;
		var max = null;
		var positionReach = -1;
		var reach = null;	

		if (nome.trim() == "") {
			boolMsg = true;
			levelForm.refs.name.refs["field-name"].className += " borderError";
			levelForm.refs.name.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
		} else {
			if(levelForm.refs.name.refs["field-name"].className && levelForm.refs.name.refs["field-name"].className.indexOf('borderError')){
				levelForm.refs.name.refs["field-name"].className = "form-control";
				levelForm.refs.name.refs.formAlertError.innerHTML = "";
			}
		}
		
		var init = 1;
		if(model.data.level.indicator){
			init = 2 ;						
		}
		
		for (var i=init; i<Object.keys(data).length; i++) {		
			//var tr = (data[Object.keys(data)[i]] ? data[Object.keys(data)[i]].trim() : "");	
			var tr = (levelForm.refs[Object.keys(data)[i]].getValue() == undefined ? data[Object.keys(data)[i]] :
					levelForm.refs[Object.keys(data)[i]].getValue());
			//se o campo nao é obrigatório e está vazio nenhuma validação é feita
			
			if(model.data.level.attributes[i-init].required){			
				if ((tr.trim() == "" || tr == null) && model.data.level.attributes[i-init].type != AttributeTypes.NUMBER_FIELD) {
					boolMsg = true;
					levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
					levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].className += " borderError"
				} else {
				//if(levelForm.refs["attribute"+(i-init)].className && levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].className.indexOf('borderError')){
					levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].className = "form-control";
					levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = "";
				//}
				}

				if (model.data.level.attributes[i-init].type == AttributeTypes.NUMBER_FIELD) {

					
					if (tr == "") {
						boolMsg = true;
						levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
						levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].className += " borderError";
					}
					else if (isNaN(data[Object.keys(data)[i]].replace(",","."))) {
						boolMsg = true;
						levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = Messages.get("label.fillFieldNumbersOnly");
						levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].className += " borderError";
					}
					else {
						if(levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].className && levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].className.indexOf('borderError')){
							levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].className = "form-control";
							levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = "";
						}
					}
				}
			}					
			var value = (levelForm.refs[Object.keys(data)[i]].getValue() == undefined ? data[Object.keys(data)[i]] :
				levelForm.refs[Object.keys(data)[i]].getValue());	
			if (model.data.level.attributes[i-init].type == AttributeTypes.NUMBER_FIELD) {
				value = value.replace(",",".");
			}
			attributes.push({
				id: model.data.level.attributes[i-init].id,
				attributeInstance: {
					value: (value == "" ? null : value)
				}
			});
		 
			if(levelForm.refs["attribute"+(i-init)].props.fieldDef.type.trim().localeCompare(AttributeTypes.DATE_FIELD) == 0 && levelForm.refs["attribute"+(i-init)].props.fieldDef.label.trim().localeCompare("Início") == 0){
				begin = levelForm.refs["attribute"+(i-init)].props.fieldDef.value.split(" ");
				begin = moment(begin,"DD/MM/YYYY").toDate();
				if (levelForm.refs["attribute"+(i-init)].props.fieldDef.value.split(" ") == "") {
					levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
					levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].refs.input.refs.input.className = " form-control borderError";
				} else if(begin < planMacroDateBegin) {
					boolMsg = true;
					var month=planMacroDateBegin.getMonth() + 1;
					levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = Messages.get("label.dateCantBeEarlierThanTargetDateOfGoalPlan") +" " + planMacroDateBegin.getDate() + "/" + month + "/" + planMacroDateBegin.getFullYear();
					levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].refs.input.refs.input.className += " borderError";
				} else {
					levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].refs.input.refs.input.className= "form-control";
					levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = "";
					//dateBegin = begin;
				}
			}
			if(levelForm.refs["attribute"+(i-init)].props.fieldDef.type.trim().localeCompare(AttributeTypes.DATE_FIELD) == 0 && levelForm.refs["attribute"+(i-init)].props.fieldDef.label.trim().localeCompare("Fim") == 0){
				end = levelForm.refs["attribute"+(i-init)].props.fieldDef.value.split(" ");
				end = moment(end,"DD/MM/YYYY").toDate();
				if (levelForm.refs["attribute"+(i-init)].props.fieldDef.value.split(" ") == "") {
					levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
					levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].refs.input.refs.input.className+= " borderError";
				} else if(end > planMacroDateEnd) {
					boolMsg = true;
					var month=planMacroDateEnd.getMonth() + 1;
					levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = Messages.get("label.dateCantBeLaterThanEndDateOfGoalPlan") +" " + planMacroDateEnd.getDate() + "/" + month + "/" + planMacroDateEnd.getFullYear();
					levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].refs.input.refs.input.className += " borderError";
				} else if (end < begin) {
					boolMsg = true;
					levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = Messages.get("label.dateBeginAfterDateEnd");
					levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].refs.input.refs.input.className += " borderError";
				} else {
					levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].refs.input.refs.input.className = "form-control";
					levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = "";
					//dateEnd = end;
				}
			}

			
			if (model.data.level.attributes[i-1] && model.data.level.attributes[i-1].expectedField) {

				expec = data[Object.keys(data)[i]].replace(",",".");
				if (this.validateNumber(expec.replace(",","."))) {
					expec = parseFloat(data[Object.keys(data)[i]]);	
					positionExpec = i-1;
				} else {
					positionExpec = i-1;
					boolMsg = true;
					levelForm.refs["attribute"+positionExpec].refs.formAlertError.innerHTML = Messages.get("label.expectedFieldCantContainLetters");
					levelForm.refs["attribute"+positionExpec].refs["field-attribute"+positionExpec].className += " borderError";
					expec = null;
					msg = Messages.get("label.form.error");
				}
			}

			if (model.data.level.attributes[i-1] && model.data.level.attributes[i-1].minimumField) {
				min =(data[Object.keys(data)[i]]).replace(",",".");
				if (this.validateNumber(min.replace(",","."))) {
					min = parseFloat(data[Object.keys(data)[i]]);
					positionMin = i-1;
				} else {
					positionMin = i-1;
					boolMsg = true;
					levelForm.refs["attribute"+positionMin].refs.formAlertError.innerHTML = Messages.get("label.minFieldCantContainLetters");
					levelForm.refs["attribute"+positionMin].refs["field-attribute"+positionMin].className += " borderError";
					min = null;
					msg = Messages.get("label.form.error");
				}
			}


			if (model.data.level.attributes[i-1] && model.data.level.attributes[i-1].maximumField) {
				max = data[Object.keys(data)[i]].replace(",",".");
				if (this.validateNumber(max)) {
					positionMax = i-1;
					max = parseFloat(data[Object.keys(data)[i]]);
				} else {
					positionMax = i-1;
					boolMsg = true;
					levelForm.refs["attribute"+positionMax].refs.formAlertError.innerHTML = Messages.get("label.maxFieldCantContainLetters");
					levelForm.refs["attribute"+positionMax].refs["field-attribute"+positionMax].className += " borderError";
					max = null;
					msg = Messages.get("label.form.error");	
				}
			}

			if (model.data.level.attributes[i-1] && model.data.level.attributes[i-1].reachedField) {
				if (tr != ""){
					if (isNaN(data[Object.keys(data)[i]].replace(",","."))) {
							boolMsg = true;
							levelForm.refs["attribute"+(i-init)].refs.formAlertError.innerHTML = Messages.get("label.reachedFieldCantContainLetters");
							levelForm.refs["attribute"+(i-init)].refs["field-attribute"+(i-init)].className += " borderError";
					}
				}
			}  
	
		}		
		
		if (expec != null && min != null && max != null) {
			if (model.data.polarity == Messages.get("label.lowerBest")) {
				if (max > min) {
					boolMsg = true;
					levelForm.refs["attribute"+positionMax].refs.formAlertError.innerHTML = Messages.get("label.maxCantBeGreaterThanMin") +" "+ Messages.get("label.checkIndicatorPolarity");
					levelForm.refs["attribute"+positionMax].refs["field-attribute"+positionMax].className += " borderError";
					msg = Messages.get("label.form.error");
				} if (expec > min) {
					boolMsg = true;
					levelForm.refs["attribute"+positionExpec].refs.formAlertError.innerHTML = Messages.get("label.expectedCantBeGreaterThanMin") +" "+ Messages.get("label.checkIndicatorPolarity");
					levelForm.refs["attribute"+positionExpec].refs["field-attribute"+positionExpec].className += " borderError";
					msg = Messages.get("label.form.error");
				} if (expec < max) {
					boolMsg = true;
					levelForm.refs["attribute"+positionExpec].refs.formAlertError.innerHTML = Messages.get("label.expectedCantBeLessThanMax") +" "+ Messages.get("label.checkIndicatorPolarity");
					levelForm.refs["attribute"+positionExpec].refs["field-attribute"+positionExpec].className += " borderError";
					msg = Messages.get("label.form.error");
				}
			} else {
				if (max < min) {
					boolMsg = true;
					levelForm.refs["attribute"+positionMax].refs.formAlertError.innerHTML = Messages.get("label.maxCantBeLessThanMin");
					levelForm.refs["attribute"+positionMax].refs["field-attribute"+positionMax].className += " borderError";
					msg = Messages.get("label.form.error");
				} if (expec < min) {
					boolMsg = true;
					levelForm.refs["attribute"+positionExpec].refs.formAlertError.innerHTML = Messages.get("label.expectedCantBeLessThanMin");
					levelForm.refs["attribute"+positionExpec].refs["field-attribute"+positionExpec].className += " borderError";
					msg = Messages.get("label.form.error");
				} if (expec > max) {
					boolMsg = true;
					levelForm.refs["attribute"+positionExpec].refs.formAlertError.innerHTML = Messages.get("label.expectedCantBeGreaterThanMax");
					levelForm.refs["attribute"+positionExpec].refs["field-attribute"+positionExpec].className += " borderError";
					msg = Messages.get("label.form.error");
				}
			}
		}


		if (levelForm.refs['indicator-type'] && levelForm.refs['indicator-type'].refs['agg-ind-config'] &&
				levelForm.refs['indicator-type'].refs['agg-ind-config'].calculationValue == 1 && aggregate) {			
			if (levelForm.refs['indicator-type'].refs['agg-ind-config'].total != 100) {
				boolMsg = true;
				msg = "O total dos pesos precisa ser igual á 100%";
			}
		}

        var aux = {
            attributes: attributes,
            boolMsg: boolMsg,
            msg: msg,
            nome: nome
        };
        return aux;
    },

    validationDuplicatePlan: function(data, planMacroEditForm) {
		var msg = Messages.get("label.form.error");

		// Parte do codigo para contonar erro de datas
		var valDateBegin,valDateFinal;
		var difference = 0; // representa milesegundos
		var dataError = false;
		
		var boolMsg = false;
		if(Number.isNaN(data.begin.getDate())){
			planMacroEditForm.refs.begin.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className += " borderError";
			dataError = true;
			boolMsg = true;
		}
		else{
			var dataBegin = new Date (data.begin);
			data.begin = dataBegin.getDate()+"/"+(dataBegin.getMonth()+1)+"/"+dataBegin.getFullYear();
			if(planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className && planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className.indexOf('borderError')){
				planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className = "form-control";
				planMacroEditForm.refs.begin.refs.formAlertError.innerHTML = "";
			}
		}
		if(Number.isNaN(data.end.getDate())){
			planMacroEditForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			dataError = true;
			boolMsg = true;
		}
		else{
			if(planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className && planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className.indexOf('borderError')){
				planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className = "form-control";
				planMacroEditForm.refs.end.refs.formAlertError.innerHTML = "";
			}
			var dataEnd = new Date (data.end);
			data.end = dataEnd.getDate()+"/"+(dataEnd.getMonth()+1)+"/"+dataEnd.getFullYear();
		}
		
		if (!dataError) {
			valDateBegin = dataBegin.getTime();
			valDateFinal = dataEnd.getTime();
			difference = valDateFinal - valDateBegin;
		}

		if (!dataError && dataBegin.getTime() == dataEnd.getTime()) {
			planMacroEditForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.endDateMustBeAfterBeginDate");
			planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			boolMsg = true;
		// dataFinal - data Inicio caso menor 86400000 (um dia em milesegundos), quer dizer que a data inicio e maior data final
		} else if (!dataError && difference < 86400000) {
			planMacroEditForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.endDateMustBeAfterBeginDate");
			planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			boolMsg = true;
		}


		if(data.name == "" ||  !!data.name.match(/^(\s)+$/) ){
			boolMsg = true;
			planMacroEditForm.refs.name.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			planMacroEditForm.refs.name.refs["field-name"].className += " borderError";
		}else{
			if(planMacroEditForm.refs.name.refs["field-name"].className && planMacroEditForm.refs.name.refs["field-name"].className.indexOf('borderError')){
				planMacroEditForm.refs.name.refs["field-name"].className = "form-control";
				planMacroEditForm.refs.name.refs.formAlertError.innerHTML = "";
			}
		}
        var aux = {
            boolMsg: boolMsg,
            msg: msg
        }
        return aux;
    },

    validationPlanMacroEdit: function(data, planMacroEditForm) {
        var msg = Messages.get("label.form.error");
		var dataError = false;
		var boolMsg = false;

		// Parte do codigo para contonar erro de datas
		var valDateBegin,valDateFinal;
		var difference = 0; // representa milesegundos
	
		//if(data.name.length > 255) {
			//var msgLmtCaractres = "Limite de caracteres atingido nos campo(s) abaixo: + "Nome";
			//Toastr.remove();
			//Toastr.error(msgLmtCaractres);
			//this.context.toastr.addAlertError(msgLmtCaractres);
			//return;
		//}
		
		if(data.begin== null){
			planMacroEditForm.refs.begin.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className += " borderError";
			dataError = true;
			boolMsg = true;
		}
		else{
			var dataBegin = new Date (data.begin);
			data.begin = dataBegin.getDate()+"/"+(dataBegin.getMonth()+1)+"/"+dataBegin.getFullYear();
			//if(planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className && planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className.indexOf('borderError')){
				planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className = "form-control";
				planMacroEditForm.refs.begin.refs.formAlertError.innerHTML = "";
			//}
			
		}
		if(data.end== null){
			planMacroEditForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			dataError = true;
			boolMsg = true;
		}
		else{
			if(planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className && planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className.indexOf('borderError')){
				planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className = "form-control";
				planMacroEditForm.refs.end.refs.formAlertError.innerHTML = "";
			}
			var dataEnd = new Date (data.end);
			data.end = dataEnd.getDate()+"/"+(dataEnd.getMonth()+1)+"/"+dataEnd.getFullYear();
			
		}
		
		if (!dataError) {
			valDateBegin = dataBegin.getTime();
			valDateFinal = dataEnd.getTime();
			difference = valDateFinal - valDateBegin;
		}
		if (!dataError && dataBegin.getTime() == dataEnd.getTime()) {
			planMacroEditForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.endDateMustBeAfterBeginDate");
			planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			boolMsg = true;
		// dataFinal - data Inicio caso menor 86400000 (um dia em milesegundos), quer dizer que a data inicio e maior data final
		} else if (!dataError && difference < 86400000) {
			planMacroEditForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.endDateMustBeAfterBeginDate");
			planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			boolMsg = true;
		}
		if(data.name == "" ||  !!data.name.match(/^(\s)+$/) ){
			boolMsg = true;
			planMacroEditForm.refs.name.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			planMacroEditForm.refs.name.refs["field-name"].className += " borderError";
		}else{
			if(planMacroEditForm.refs.name.refs["field-name"].className && planMacroEditForm.refs.name.refs["field-name"].className.indexOf('borderError')){
				planMacroEditForm.refs.name.refs["field-name"].className = "form-control";
				planMacroEditForm.refs.name.refs.formAlertError.innerHTML = "";
			}
		} 
        var aux = {
            boolMsg: boolMsg,
            msg: msg
        }

		return aux;
    },

	validationPlanRegister: function(data, planRegisterForm, planMacro) {
		var planMacroBegin = Date();
		var dateBeginPlanMacro = planMacro.get("begin").split(" ");
		dateBeginPlanMacro = moment(dateBeginPlanMacro,"DD/MM/YYYY").toDate();
		var dataEndPlanMacro = planMacro.get("end").split(" ");
		dataEndPlanMacro = moment(dataEndPlanMacro,"DD/MM/YYYY").toDate();
		var msg = Messages.get("label.form.error");
		var boolMsg = false;
		var innerHtml = "";

		if(data.name == "" || data.name == undefined || data.name.trim() == "") {
			boolMsg = true;
			planRegisterForm.refs.name.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			planRegisterForm.refs.name.refs["field-name"].className = "form-control borderError";
		}else{
			planRegisterForm.refs.name.refs.formAlertError.innerHTML = "";
			planRegisterForm.refs.name.refs["field-name"].className = "form-control";
		}

		var dataError = false;

		var begin = planRegisterForm.refs["begin"].props.fieldDef.value.split(" ");
		begin = moment(begin,"DD/MM/YYYY").toDate();
		if(Number.isNaN(begin.getDate())){
			dataError = true;
			boolMsg = true;
			planRegisterForm.refs.begin.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			document.getElementById("field-begin").className = "form-control borderError";
		}
		else{
			var dataBegin = new Date (begin);
			data.begin = dataBegin.getDate()+"/"+(dataBegin.getMonth()+1)+"/"+dataBegin.getFullYear();
		}

		var end = planRegisterForm.refs["end"].props.fieldDef.value.split(" ");
		end = moment(end,"DD/MM/YYYY").toDate();
		if(Number.isNaN(end.getDate())){
			planRegisterForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			document.getElementById("field-end").className = "form-control borderError";
			dataError = true;
			boolMsg = true;
		}
		else{
			var dataEnd = new Date (end);
			data.end = dataEnd.getDate()+"/"+(dataEnd.getMonth()+1)+"/"+dataEnd.getFullYear();
		}
		
		if (!dataError && dataBegin>=dataEnd) {
			planRegisterForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.endDateMustBeAfterBeginDate");
			document.getElementById("field-end").className = "form-control borderError";
			boolMsg = true;
		}else{
			if(planRegisterForm.refs.end.refs.formAlertError == Messages.get("label.endDateMustBeAfterBeginDate")){
				planRegisterForm.refs.end.refs.formAlertError.innerHTML = "";
				document.getElementById("field-end").className = "form-control";
			}

		} 		

		if(dateBeginPlanMacro>dataBegin){
			planRegisterForm.refs.begin.refs.formAlertError.innerHTML = Messages.get("label.goalPlanBeginDateLessThanBeginDateOfMacroPlan");
			document.getElementById("field-begin").className = "form-control borderError";
			dataError = true;
			boolMsg = true;
		}else{
			
			if(planRegisterForm.refs.begin.refs.formAlertError.innerHTML == Messages.get("label.goalPlanBeginDateLessThanBeginDateOfMacroPlan")){
				planRegisterForm.refs.begin.refs.formAlertError.innerHTML = "";
				document.getElementById("field-begin").className = "form-control";
			}
		}
		if(dataEndPlanMacro<dataEnd){
			planRegisterForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.goalPlanEndDateGreaterThanEndDateOfMacroPlan");
			document.getElementById("field-end").className = "form-control borderError";
			dataError = true;
			boolMsg = true;
		}else{
			if(planRegisterForm.refs.end.refs.formAlertError.innerHTML == Messages.get("label.goalPlanEndDateGreaterThanEndDateOfMacroPlan")){
				planRegisterForm.refs.end.refs.formAlertError.innerHTML = "";
				document.getElementById("field-end").className = "form-control";
			}
		}

		var aux= {
			boolMsg: boolMsg,
			msg: msg
		};
		return aux;
	},

	validationCompanyDomainEdit: function(data, companyDomainEditForm) {
		var msg="";
		if(data.host == "" ||  !!data.host.match(/^(\s)+$/) ){
			msg = Messages.get("label.form.error");
			companyDomainEditForm.refs.host.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			companyDomainEditForm.refs.host.refs["field-host"].className += " borderError";
		} else {
			if(companyDomainEditForm.refs.host.refs["field-host"].className && companyDomainEditForm.refs.host.refs["field-host"].className.indexOf('borderError')){
				companyDomainEditForm.refs.host.refs["field-host"].className = "form-control";
				companyDomainEditForm.refs.host.refs.formAlertError.innerHTML = "";
			}
		}
		if(data.baseUrl == ""){
			msg = Messages.get("label.form.error");
			companyDomainEditForm.refs.baseUrl.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			companyDomainEditForm.refs.baseUrl.refs["field-baseUrl"].className += " borderError";
		} else {
			if(companyDomainEditForm.refs.host.refs["field-host"].className && companyDomainEditForm.refs.host.refs["field-host"].className.indexOf('borderError')){
				companyDomainEditForm.refs.baseUrl.refs["field-baseUrl"].className = "form-control";
				companyDomainEditForm.refs.baseUrl.refs.formAlertError.innerHTML = "";
			}
		}
		if(data.theme == "" ||  !!data.theme.match(/^(\s)+$/) ){
			msg = Messages.get("label.form.error");
			companyDomainEditForm.refs.theme.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			companyDomainEditForm.refs.theme.refs["field-theme"].className += " borderError";
		} else {
			if(companyDomainEditForm.refs.host.refs["field-host"].className && companyDomainEditForm.refs.host.refs["field-host"].className.indexOf('borderError')){
				companyDomainEditForm.refs.theme.refs["field-theme"].className = "form-control";
				companyDomainEditForm.refs.theme.refs.formAlertError.innerHTML = "";
			}
		}
		if(data.company.id == "" ||  !!data.company.id.match(/^(\s)+$/) ){
			msg = Messages.get("label.form.error");
			companyDomainEditForm.refs.company.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			companyDomainEditForm.refs.company.refs["field-company"].className += " borderError";
		} else {
			if(companyDomainEditForm.refs.host.refs["field-host"].className && companyDomainEditForm.refs.host.refs["field-host"].className.indexOf('borderError')){
				companyDomainEditForm.refs.company.refs["field-company"].className = "form-control";
				companyDomainEditForm.refs.company.refs.formAlertError.innerHTML = "";
			}
		}

		return msg;
	},

	validationCompanyEdit: function(data, companyEditForm) {
		var msg="";
		if(data.name.trim() == "" ){
			msg = Messages.get("label.form.error");
			companyEditForm.refs.name.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			companyEditForm.refs.name.refs["field-name"].className += " borderError";
		} else {
			if(companyEditForm.refs.name.refs["field-name"].className && companyEditForm.refs.name.refs["field-name"].className.indexOf('borderError')){
				companyEditForm.refs.name.refs["field-name"].className = "form-control";
				companyEditForm.refs.name.refs.formAlertError.innerHTML = "";
			}
		}
		if(data.localization.trim() == ""){
			msg = Messages.get("label.form.error");
			companyEditForm.refs.localization.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			companyEditForm.refs.localization.refs["field-localization"].className += " borderError";
		} else {
			if(companyEditForm.refs.localization.refs["field-localization"].className && companyEditForm.refs.localization.refs["field-localization"].className.indexOf('borderError')){
				companyEditForm.refs.localization.refs["field-localization"].className = "form-control";
				companyEditForm.refs.localization.refs.formAlertError.innerHTML = "";
			}
		}

		return msg;
	},

	validationLogin: function(login) {
		if(login.refs.email.getValue().trim() == "")
			login.refs.email.refs["field-email"].className = "form-control borderError";
		else login.refs.email.refs["field-email"].className = "form-control";
		
		if(login.refs.password.getValue().trim() == "")
			login.refs.password.refs["field-password"].className = "form-control borderError";
		else login.refs.password.refs["field-password"].className = "form-control";
	},
	
	validarCPF: function(cpf){
		if(cpf.length != 11 || cpf.replace(eval('/'+cpf.charAt(1)+'/g'),'') == '') {
		    return false;
		} else {
	   	    var d;
	   	    var c;
	        for(var n=9; n<11; n++) {
	            for(d=0, c=0; c<n; c++)
	            	d += cpf.charAt(c) * ((n + 1) - c);
	            d = ((10 * d) % 11) % 10;
	            if(cpf.charAt(c) != d)
	            	return false;
	        }
	      return true;
	   }
	},

	validationSendMenssageUser: function(data,profileEditUser) {
		var errorField = false;

		if (data.assunto.trim() == "") {
			profileEditUser.refs.assunto.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			profileEditUser.refs.assunto.refs["field-assunto"].className += " borderError";
			errorField = true;
		} else {
			profileEditUser.refs.assunto.refs.formAlertError.innerHTML = "";
			profileEditUser.refs.assunto.refs["field-assunto"].className = "form-control";
		}

		if (data.mensagem.trim() == "") {
			profileEditUser.refs.mensagem.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			profileEditUser.refs.mensagem.refs["field-mensagem"].className += " borderError";
			errorField = true;
		} else {
			profileEditUser.refs.mensagem.refs.formAlertError.innerHTML = "";
			profileEditUser.refs.mensagem.refs["field-mensagem"].className = "form-control";
		}


		return errorField;
		
	},

	validationProfileUser: function(data, profileEditUser) {
		var celNumber = [];
		var telNumber = [];
		var me = this;
		
		var msg;
		var errorField = false;
		var currentDate = new Date();

		if (data.birthdate != undefined) {
			var date = data.birthdate.split("/");
			var birthdate = new Date(date[2], date[1]-1, date[0]);
		}

		for (var i = 0; i < data.cellphone.length; i++) {
			if (data.cellphone[i] != "_") {
				celNumber.push(data.cellphone[i]);
			}
		}

		if (data.name.trim() == "") {
			profileEditUser.refs.name.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			profileEditUser.refs.name.refs["field-name"].className += " borderError";
			errorField = true;
		} else {
			profileEditUser.refs.name.refs.formAlertError.innerHTML = "";
			profileEditUser.refs.name.refs["field-name"].className = "form-control";
		}

		if (celNumber.length < 10) {
			msg = Messages.get("label.form.error");
			profileEditUser.refs.cellphone.refs["field-cellphone"].input.className = "form-control borderError";
			profileEditUser.refs.cellphone.refs.formAlertError.innerHTML = Messages.get("label.invalidPhoneNumber");
			errorField = true;
		} else if(profileEditUser.refs.cellphone.refs["field-cellphone"].input.className && profileEditUser.refs.cellphone.refs["field-cellphone"].input.className.indexOf('borderError')){
			profileEditUser.refs.cellphone.refs["field-cellphone"].input.className = "form-control";
			profileEditUser.refs.cellphone.refs.formAlertError.innerHTML = "";
		}

		if (data.birthdate == undefined) {
			profileEditUser.refs.birthdate.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			profileEditUser.refs.birthdate.refs["field-birthdate"].refs.input.refs.input.className = "form-control borderError";
			errorField = true;
		} else if (birthdate > currentDate) {
			profileEditUser.refs.birthdate.refs.formAlertError.innerHTML = Messages.get("label.invalidBirthDate");
			profileEditUser.refs.birthdate.refs["field-birthdate"].refs.input.refs.input.className = "form-control borderError";
			errorField = true;
		} else {
			profileEditUser.refs.birthdate.refs.formAlertError.innerHTML = "";
			profileEditUser.refs.birthdate.refs["field-birthdate"].refs.input.refs.input.className = "form-control";
		}

		if(data.cpf == "" ||  !!data.cpf.match(/^(\s)+$/) ){
			profileEditUser.refs.cpf.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			profileEditUser.refs.cpf.refs["field-cpf"].input.className = "form-control borderError";
			errorField = true;
		}else if(!this.validarCPF(data.cpf)){	
			profileEditUser.refs.cpf.refs.formAlertError.innerHTML = Messages.get("label.cpfInvalid");
			profileEditUser.refs.cpf.refs["field-cpf"].input.className = "form-control borderError";
			errorField = true;
		} else {
			if(profileEditUser.refs.cpf.refs["field-cpf"].input.className && profileEditUser.refs.cpf.refs["field-cpf"].input.className.indexOf('borderError')){
				profileEditUser.refs.cpf.refs["field-cpf"].input.className = "form-control";
				profileEditUser.refs.cpf.refs.formAlertError.innerHTML = "";
			}
		} 

		if (data.email != undefined) {
			if (data.email.trim() == "") {
				profileEditUser.refs.email.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
				profileEditUser.refs.email.refs["field-email"].className += " borderError";
				errorField = true;	
			} else {
				profileEditUser.refs.email.refs.formAlertError.innerHTML = "";
				profileEditUser.refs.email.refs["field-email"].className = "form-control";
			}
		}

		if (data.accessLevel != undefined) {
			if (data.accessLevel.trim() == "") {
				profileEditUser.refs.accessLevel.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
				profileEditUser.refs.accessLevel.refs["field-accessLevel"].className += " borderError";
				errorField = true;	
			} else {
				profileEditUser.refs.accessLevel.refs.formAlertError.innerHTML = "";
				profileEditUser.refs.accessLevel.refs["field-accessLevel"].className = "form-control";
			}
		}

		if(data.currentPassword != undefined) {
			
			if (data.currentPassword.trim() == "") {
				profileEditUser.refs.currentPassword.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
				profileEditUser.refs.currentPassword.refs["field-currentPassword"].className += " borderError";
				errorField = true;
			} else {
				profileEditUser.refs.currentPassword.refs.formAlertError.innerHTML = "";
				profileEditUser.refs.currentPassword.refs["field-currentPassword"].className = "form-control";
			}
		}

		if(data.newPassword != undefined) {

			if (data.newPassword.trim() == "" && profileEditUser.refs.newPassword.props.fieldDef.required) {
				profileEditUser.refs.newPassword.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
				profileEditUser.refs.newPassword.refs["field-newPassword"].className += " borderError";
				errorField = true;
			} else if (data.newPassword.length < 5 && data.newPassword.length > 0) {
				msg =Messages.get("label.form.error");
				profileEditUser.refs.newPassword.refs.formAlertError.innerHTML = Messages.get("label.passwordMinimumFiveCaracteres");
				profileEditUser.refs.newPassword.refs["field-newPassword"].className += " borderError";
				errorField = true;
			} else {
				profileEditUser.refs.newPassword.refs.formAlertError.innerHTML = "";
				profileEditUser.refs.newPassword.refs["field-newPassword"].className = "form-control";
			}
		}

		if(data.newPasswordTwo != undefined) {
			
			if (data.newPasswordTwo.trim() == "") {
				profileEditUser.refs.newPasswordTwo.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
				profileEditUser.refs.newPasswordTwo.refs["field-newPasswordTwo"].className += " borderError";
				errorField = true;
			} else {
				profileEditUser.refs.newPasswordTwo.refs.formAlertError.innerHTML = "";
				profileEditUser.refs.newPasswordTwo.refs["field-newPasswordTwo"].className = "form-control";
			}
		}

		if (data.newPassword != undefined && data.newPasswordTwo != undefined && !errorField) { 
			
			if (data.newPassword.trim() != "" && data.newPasswordTwo.trim() != "" ) {
				if (data.newPassword.trim().localeCompare(data.newPasswordTwo.trim()) != 0) {
					profileEditUser.refs.newPassword.refs.formAlertError.innerHTML = Messages.get("label.passwordNotMatch");
					profileEditUser.refs.newPasswordTwo.refs.formAlertError.innerHTML = Messages.get("label.passwordNotMatch");
					profileEditUser.refs.newPasswordTwo.refs["field-newPasswordTwo"].className += " borderError";
					profileEditUser.refs.newPassword.refs["field-newPassword"].className += " borderError";
					errorField = true;

				} else {
					profileEditUser.refs.newPassword.refs.formAlertError.innerHTML = "";
					profileEditUser.refs.newPasswordTwo.refs.formAlertError.innerHTML = "";
					profileEditUser.refs.newPassword.refs["field-newPassword"].className = "form-control";
					profileEditUser.refs.newPasswordTwo.refs["field-newPasswordTwo"].className = "form-control";
				}

			}

		} else if (data.password != undefined) {
			if(data.password == "" ||  !!data.password.match(/^(\s)+$/) ){
				msg = Messages.get("label.form.error");
				profileEditUser.refs.password.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
				profileEditUser.refs.password.refs["field-password"].className += " borderError";
				errorField = true;
			} else {
				if(profileEditUser.refs.password.refs["field-password"].className && profileEditUser.refs.password.refs["field-password"].className.indexOf('borderError')){
					profileEditUser.refs.password.refs["field-password"].className = "form-control";
					profileEditUser.refs.password.refs.formAlertError.innerHTML = "";
				}
			}
			if(data.password.length < 5 ||  !!data.password.match(/^(\s)+$/) ){
				msg = Messages.get("label.form.error");
				profileEditUser.refs.password.refs.formAlertError.innerHTML = Messages.get("label.passwordMinimumFiveCaracteres");
				profileEditUser.refs.password.refs["field-password"].className += " borderError";
				errorField = true;
			} else {
				if(profileEditUser.refs.password.refs["field-password"].className && profileEditUser.refs.password.refs["field-password"].className.indexOf('borderError')){
					profileEditUser.refs.password.refs["field-password"].className = "form-control";
					profileEditUser.refs.password.refs.formAlertError.innerHTML = "";
				}
			}
			if(data.passwordconfirm == "" ||  !!data.passwordconfirm.match(/^(\s)+$/) ){
				msg = Messages.get("label.form.error");
				profileEditUser.refs.passwordconfirm.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
				profileEditUser.refs.passwordconfirm.refs["field-passwordconfirm"].className += " borderError";
				errorField = true;
			} else {
				if(data.password.trim().localeCompare(data.passwordconfirm.trim()) != 0) {
					profileEditUser.refs.password.refs.formAlertError.innerHTML = Messages.get("label.passwordNotMatch");
					profileEditUser.refs.passwordconfirm.refs.formAlertError.innerHTML = Messages.get("label.passwordNotMatch");
					profileEditUser.refs.password.refs["field-password"].className += " borderError";
					profileEditUser.refs.passwordconfirm.refs["field-passwordconfirm"].className += " borderError";
					errorField = true;
				}
				else if(profileEditUser.refs.passwordconfirm.refs["field-passwordconfirm"].className && profileEditUser.refs.passwordconfirm.refs["field-passwordconfirm"].className.indexOf('borderError')){
					profileEditUser.refs.passwordconfirm.refs["field-passwordconfirm"].className = "form-control";
					profileEditUser.refs.passwordconfirm.refs.formAlertError.innerHTML = "";
				}
			}
		}

		return errorField;
		
	},

	emailIsValid: function(email) {
		var user = email.substr(0, email.indexOf('@'));
		var domain = email.substr(email.indexOf('@')+1, email.length);
		
		if ((user.length >=1) && (domain.length >=3) && (user.search("@")==-1) && 
			(domain.search("@")==-1) && (user.search(" ")==-1) && (domain.search(" ")==-1) &&
			(domain.search(".")!=-1) && (domain.indexOf(".") >=1) && (domain.lastIndexOf(".") < domain.length - 1)) {
				return true;
		}
		else {
			return false;
		}
	},

	validationConviteUser: function(refs) {
		var errorField = false;
		if (refs.nameUser.value.trim() == "") {
			refs.formAlertNameUser.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.nameUser.className += " borderError";
			errorField = true;
		} else {
			refs.formAlertNameUser.innerHTML = "";
			refs.nameUser.className = "budget-field-table";
		}
		if (refs.emailUser.value.trim() == "") {
			refs.formAlertEmail.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.emailUser.className += " borderError";
			errorField = true;
		} else if (!this.emailIsValid(refs.emailUser.value.trim())) {
			refs.formAlertEmail.innerHTML = Messages.get("label.emailInvalid");
			refs.emailUser.className += " borderError";
			errorField = true;
		} else {
			refs.formAlertEmail.innerHTML  = "";
			refs.emailUser.className = "budget-field-table";
		}
		if(refs.selectAccessLevels.value.trim() == "-1") {
			refs.formAlertTypeAccont.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.selectAccessLevels.className += " borderError";
			errorField = true;
		} else {
			refs.formAlertTypeAccont.innerHTML = "";
			refs.selectAccessLevels.className = "form-control user-select-box";
		}
		return errorField;
	},

	validationCreateUser: function(refs) {
		var errorField = false;
		if (refs.newNameUser.value.trim() == "") {
			refs.formAlertNewNameUser.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.newNameUser.className += " borderError";
			errorField = true;
		} else {
			refs.formAlertNewNameUser.innerHTML = "";
			refs.newNameUser.className = "budget-field-table";
		}
		if (refs.newEmailUser.value.trim() == "") {
			refs.formAlertNewEmail.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.newEmailUser.className += " borderError";
			errorField = true;
		} else if (!this.emailIsValid(refs.newEmailUser.value.trim())) {
			refs.formAlertNewEmail.innerHTML = Messages.get("label.emailInvalid");
			refs.newEmailUser.className += " borderError";
			errorField = true;
		} else {
			refs.formAlertNewEmail.innerHTML  = "";
			refs.newEmailUser.className = "budget-field-table";
		}
		if (refs.newPasswordUser.value.trim() == "") {
			refs.formAlertNewPasswordUser.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.newPasswordUser.className += " borderError";
			errorField = true;
		} else if (refs.newPasswordUser.value.trim().length < 5) {
			refs.formAlertNewPasswordUser.innerHTML = Messages.get("label.passwordMinimumFiveCaracteres");
			refs.newPasswordUser.className += " borderError";
			errorField = true;
		} else {
			refs.formAlertNewPasswordUser.innerHTML  = "";
			refs.newPasswordUser.className = "budget-field-table";
		}
		if(refs.newSelectAccessLevels.value.trim() == "-1") {
			refs.formAlertNewTypeAccont.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.newSelectAccessLevels.className += " borderError";
			errorField = true;
		} else {
			refs.formAlertNewTypeAccont.innerHTML = "";
			refs.newSelectAccessLevels.className = "form-control user-select-box";
		}
		return errorField;
	},

	validationNewFieldDocument: function(newfield) {
		var name, type;
		name =  S(newfield['newfield-name'].value);
		type =  S(newfield['newfield-type'].value);
		var errorField = false;

		if (name.isEmpty() || type.isEmpty()) {
			if(newfield['newfield-name'].value.trim() == "") {
				newfield['newfield-name'].className = "form-control borderError";
				newfield['formAlertErrorName'].innerHTML = Messages.get("label.alert.fieldEmpty");
				errorField = true;
			} else {
				newfield['newfield-name'].className = "form-control";
				newfield['formAlertErrorName'].innerHTML = "";
			}

			if(newfield['newfield-type'].value.trim() == "") {
				newfield['newfield-type'].className = "form-control borderError";
				newfield['formAlertErrorType'].innerHTML = Messages.get("label.alert.fieldEmpty");
				errorField = true;
			} else {
				newfield['newfield-type'].className = "form-control";
				newfield['formAlertErrorType'].innerHTML = "";
			}
		}
		
		var aux = {
			errorField: errorField,
			name: name,
			type: type
		}
		return aux;
	},

	validationNewActionPlan: function(state, refs) {		
		var dsc,rps;
		var dataBegin = new Date ();
		var dataEnd = new Date ();
		var dateError = false;
		var msg = Messages.get("label.form.error");
		var boolMsg = false;
		

		if(state.initDate != undefined) {
			dataBegin = state.initDate.toDate();
			refs.formAlertErrorBegin.innerHTML = "";
			refs.begin.refs.input.refs.input.className = "budget-field-table";
		}else {
			dateError=true;
			boolMsg=true;
			refs.formAlertErrorBegin.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.begin.refs.input.refs.input.className += " borderError"; 
		}
		
		if(state.endDate != undefined) {

			dataEnd = state.endDate.toDate();
			
			refs.formAlertErrorEnd.innerHTML = "";
			refs.end.refs.input.refs.input.className = "budget-field-table";
		}else {
			
			dateError=true;
			boolMsg=true;
			refs.formAlertErrorEnd.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.end.refs.input.refs.input.className += " borderError";  
		}

		dsc =  refs.descricao.value.trim();
		rps = refs.responsavel.value.trim();

		if (!dateError && dataBegin.getTime() > dataEnd.getTime()) {		
 			refs.formAlertErrorEnd.innerHTML = Messages.get("label.dateEndAfterDataBegin");		
 			refs.end.refs.input.refs.input.className += " borderError"; 		
 			boolMsg = true;		
 		} else if(!dateError) {		
 			refs.formAlertErrorEnd.innerHTML = "";		
 			refs.end.refs.input.refs.input.className = "budget-field-table";		
 		}
		if (dsc.length > 3999) {
			refs.formAlertErrorDescription.innerHTML = Messages.get("label.descLimitCaracteres");
			refs.descricao.className += " borderError"; 
			boolMsg = true;
		} else {
			refs.formAlertErrorDescription.innerHTML = "";
			refs.descricao.className = "budget-field-table"; 
		}

		if (dsc == "") {
			refs.formAlertErrorDescription.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.descricao.className += " borderError";
			boolMsg = true;
		} else {
			refs.formAlertErrorDescription.innerHTML = "";
			refs.descricao.className = "budget-field-table";
		}

		if (rps == "") {
			refs.formAlertErrorResponsible.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.responsavel.className += " borderError";
			boolMsg = true;			
		} else {
			refs.formAlertErrorResponsible.innerHTML = "";
			refs.responsavel.className = "budget-field-table";
		}

		var aux = {
			boolMsg: boolMsg,
			msg: msg,
			dataBegin: dataBegin,
			dataEnd: dataEnd
		};

		return aux;
	},

	validationEditActionPlan: function(state, refs) {
		var dataBegin = new Date ();
		var data;

		var initDate;
		var dataEnd = new Date ();
		
		var endDate;
		var dscEdt,rpsvEdt;
		dscEdt =  refs.descricaoEdit.value.trim();
		rpsvEdt = refs.responsavelEdit.value.trim();

		var dateError = false;

		var msg = Messages.get("label.form.error");
 		var boolMsg = false;
		
		if(state.initDate != undefined){
			dataBegin = state.initDate.toDate();
			initDate = state.initDate.format("DD/MM/YYYY");
			refs.formAlertErrorBeginEdit.innerHTML = "";
			refs.begin.refs.input.refs.input.className = "budget-field-table";
		}else{
			dateError=true;
			boolMsg=true;
			refs.formAlertErrorBeginEdit.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.begin.refs.input.refs.input.className += " borderError"; 
		}

		if(state.endDate != undefined){
			dataEnd = state.endDate.toDate();
			endDate = state.endDate.format("DD/MM/YYYY");
			refs.formAlertErrorEndEdit.innerHTML = "";
			refs.end.refs.input.refs.input.className = "budget-field-table";
		}else{
			dateError=true;
			boolMsg=true;
			refs.formAlertErrorEndEdit.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.end.refs.input.refs.input.className += " borderError";  
		}

		if (!dateError) {
			if (dataBegin.getTime() > dataEnd.getTime()) {
				refs.formAlertErrorEndEdit.innerHTML = Messages.get("label.dateEndAfterDataBegin");
				refs.end.refs.input.refs.input.className += " borderError";
				boolMsg = true;
			} else {
				refs.formAlertErrorEndEdit.innerHTML = "";
				refs.end.refs.input.refs.input.className = "budget-field-table";
			}
		}

		if (dscEdt == "") {
			refs.formAlertErrorDescriptionEdit.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.descricaoEdit.className += " borderError";
			boolMsg = true;
		} else {
			refs.formAlertErrorDescriptionEdit.innerHTML = "";
			refs.descricaoEdit.className = "budget-field-table";
		} 
		if (rpsvEdt == "") {
			refs.formAlertErrorResponsibleEdit.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.responsavelEdit.className += " borderError";
			boolMsg = true;
		} else {
			refs.formAlertErrorResponsibleEdit.innerHTML = "";
			refs.responsavelEdit.className = "budget-field-table";			
		}

		var aux = {
			boolMsg: boolMsg,
			msg: msg,
			initDate: initDate,
			endDate: endDate
		};
		return aux; 
	},

	validationNewBudgetField: function(refs) {
		var name = refs.budgetNameText.value.trim();
		var msg = Messages.get("label.form.error");
 		var boolMsg = false;


 		//if (name.length > 255) {
 			//Toastr.remove();
 			//Toastr.error("Limite de caractres atingido nos campo(s) abaixo: " + "Nome");
			//this.context.toastr.addAlertError("Limite de caractres atingido nos campo(s) abaixo: " + "Nome");
 			//return;
 		//}

		if(name == "") {
			boolMsg = true;
			refs.formAlertErrorName.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.budgetNameText.className += " borderError";
		} else {
			if(refs.budgetNameText.className && refs.budgetNameText.className.indexOf('borderError')){
				refs.budgetNameText.className = "budget-field-table";
				refs.formAlertErrorName.innerHTML = "";
			}
		}
		var aux = {
			msg: msg,
			boolMsg: boolMsg,
			name: name
		};
		return aux;
	},
	
	validationEditBudgetField: function(refs, idx) {
		var subAction = refs["subActions-edit-"+idx].state.value;
		var name= refs['inputName'+idx].value;
		var subActionEdit = subAction;
		var nomeEdit = name.trim();

		var msg = Messages.get("label.form.error");
 		var boolMsg = false;


		if(name.trim() == "") {
			boolMsg = true;
			refs.formAlertErrorName.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs['inputName'+idx].className += " borderError";
		} else {
			if(refs['inputName'+idx].className && refs['inputName'+idx].className.indexOf('borderError')){
				refs['inputName'+idx].className = "budget-field-table";
				refs.formAlertErrorName.innerHTML = "";
			}
		}

		if (subAction == "") {
			boolMsg = true;
			//Toastr.remove();
			refs.formAlertErrorSubAction.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs["subActions-edit-"+idx].className += " borderError";
		} else { 
			refs.formAlertErrorSubAction.innerHTML = "";
			//Toastr.remove();
		}
		var aux = {
			msg: msg,
			boolMsg: boolMsg,
			name: name,
			subAction: subAction
		};
		return aux;
	},

	validationNewSchedule: function(refs, state) {
		var dataBegin = new Date ();
		var data;
		var initDate;
		var msg = Messages.get("label.form.error");
		var boolMsg = false;
		var dateError = false;

		if(state.initDate != undefined){
			dataBegin = state.initDate.toDate();
			initDate = state.initDate.format("DD/MM/YYYY");
			refs.formAlertErrorBegin.innerHTML = "";
			refs.begin.className = "budget-field-table";
		}else{
			dateError=true;
			boolMsg=true;
			refs.formAlertErrorBegin.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.begin.className += " borderError"; 
		}

		var dataEnd = new Date ();
		var endDate;
		if(state.endDate != undefined){
			dataEnd = state.endDate.toDate();
			endDate = state.endDate.format("DD/MM/YYYY");
			refs.formAlertErrorEnd.innerHTML = "";
			refs.end.className = "budget-field-table";
		}else{
			dateError=true;
			boolMsg=true;
			refs.formAlertErrorEnd.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.end.className += " borderError";  
		}
		var desc,peri;
		desc = refs.scheduleDescription.value.trim();
		if(state.schedule.periodicityEnable)
			peri = refs.schedulePeriodicity.value.trim();


		if (!dateError) {
			if (dataBegin.getTime() > dataEnd.getTime()) {
				refs.formAlertErrorEnd.innerHTML = Messages.get("label.dateEndAfterDataBegin");
				refs.end.className += " borderError";
				boolMsg = true;
			} else {
				refs.formAlertErrorEnd.innerHTML = "";
				refs.end.className = "budget-field-table";
			}
		}



		if (desc == "") {
			boolMsg = true;
			refs.formAlertErrorDescription.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.scheduleDescription.className += " borderError";
		} else {
			if (desc.length > 4000) {
				boolMsg = true;
				refs.formAlertErrorDescription.innerHTML = Messages.get("label.descLimitCaracteres");
				refs.scheduleDescription.className += " borderError";
			} else if(refs.scheduleDescription.className && refs.scheduleDescription.className.indexOf('borderError')){
				refs.scheduleDescription.className = "budget-field-table";
				refs.formAlertErrorDescription.innerHTML = "";
			}
		} 
		if (state.schedule.periodicityEnable && peri == "") {
			boolMsg = true;			
			refs.formAlertErrorPediodicity.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.schedulePeriodicity.className += " borderError";
		} else {
			if(state.schedule.periodicityEnable && refs.schedulePeriodicity.className && refs.schedulePeriodicity.className.indexOf('borderError')){
				refs.schedulePeriodicity.className = "budget-field-table";
				refs.formAlertErrorPediodicity.innerHTML = "";
			}
		}
		var aux = {
			boolMsg: boolMsg,
			msg: msg,
			desc: desc,
			initDate: initDate,
			endDate: endDate,
			peri: peri
		};
		return aux;
	},

	validationEditSchedule: function(refs, state) {
		var dataBegin = new Date ();
		var data;

		var initDate;
		var dataEnd = new Date ();

		var endDate;

		var desc,peri;
		desc = refs.descriptionEdit.value.trim();
		if(state.schedule.periodicityEnable)
			peri = refs.periodicityEdit.value.trim();

		var msg = Messages.get("label.form.error");
 		var boolMsg = false;
		var dataError = false;

		if (state.initDate == undefined) {
			dataError = true;
			boolMsg = true;

			refs.formAlertErrorBeginEdit.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.begin.className += " borderError";

		} else {
			dataError = false;
			dataBegin = state.initDate.toDate();
			initDate = state.initDate.format("DD/MM/YYYY");
			refs.formAlertErrorBeginEdit.innerHTML = "";
			refs.begin.className = "budget-field-table";
		}

		if (state.endDate == undefined) {
			dataError = true;
			boolMsg = true;

			refs.formAlertErrorEndEdit.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.end.className += " borderError";

		} else {
			dataError = false;
			dataEnd = state.endDate.toDate();
			endDate = state.endDate.format("DD/MM/YYYY");
			refs.formAlertErrorEndEdit.innerHTML = "";
			refs.end.className = "budget-field-table";
		}

		if (!dataError) {
			if (dataBegin.getTime() > dataEnd.getTime()) {
			boolMsg = true;

			refs.formAlertErrorEndEdit.innerHTML = Messages.get("label.dateEndBeforeDataBegin");
			refs.end.className += " borderError";

			} else {
				dataError = false;
				dataEnd = state.endDate.toDate();
				endDate = state.endDate.format("DD/MM/YYYY");
				refs.formAlertErrorEndEdit.innerHTML = "";
				refs.end.className = "budget-field-table";
			}
		}

		if (desc == "") {
			boolMsg = true;
			refs.formAlertErrorDescriptionEdit.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.descriptionEdit.className += " borderError";
		} else {
			if (desc.length > 4000) {
				boolMsg = true;
				refs.formAlertErrorDescriptionEdit.innerHTML = Messages.get("label.descLimitCaracteres");
				refs.descriptionEdit.className += " borderError";
			} else if(refs.descriptionEdit.className && refs.descriptionEdit.className.indexOf('borderError')){
				refs.descriptionEdit.className = "budget-field-table";
				refs.formAlertErrorDescriptionEdit.innerHTML = "";
			}
		} if (state.schedule.periodicityEnable && peri == "") {
			boolMsg = true;			
			refs.formAlertErrorPediodicityEdit.innerHTML = Messages.get("label.alert.fieldEmpty");
			refs.periodicityEdit.className += " borderError";
		} else {
			if(state.schedule.periodicityEnable && refs.periodicityEdit.className && refs.periodicityEdit.className.indexOf('borderError')){
				refs.periodicityEdit.className = "budget-field-table";
				refs.formAlertErrorPediodicityEdit.innerHTML = "";
			}
		} 
		var aux = {
			boolMsg: boolMsg,
			msg: msg,
			desc: desc,
			initDate: initDate,
			endDate: endDate,
			peri: peri
		};
		return aux;
	},

	validationGoalsGenerate: function(refs) {
		var nameGoal = document.getElementById("nameGoal");
		var responsibleGoal =document.getElementById("responsibleGoal");
		var descriptionGoal = document.getElementById("descriptionGoal");
		var expectedGoal = document.getElementById("expectedGoal");
		var minimumGoal = document.getElementById("minimumGoal");
		var maximumGoal = document.getElementById("maximumGoal");
		var boolMsg = false;

		var msg = Messages.get("label.form.error");

		if (nameGoal.value.trim() == "") {
			refs.formAlertErrorName.innerHTML = Messages.get("label.notNameEmpty");
			refs["nameGoal"].className += " borderError";
			boolMsg = true;
		} else {
			refs.formAlertErrorName.innerHTML = "";
			refs["nameGoal"].className = "form-control";
		}

		if (responsibleGoal.value.trim() == "") {
			refs.formAlertErrorResponsavel.innerHTML = Messages.get("label.notRespEmpty");
			refs["responsibleGoal"].className += " borderError";
			boolMsg = true;
		} else {
			refs.formAlertErrorResponsavel.innerHTML = "";
			refs["responsibleGoal"].className = "form-control";
		}

		if (descriptionGoal.value.trim() == "") {
			refs.formAlertErrorDescription.innerHTML = Messages.get("label.notDescEmpty");
			refs["descriptionGoal"].className += " borderError";
			boolMsg = true;
		} else {
			refs.formAlertErrorDescription.innerHTML = "";
			refs["descriptionGoal"].className = "form-control";
		}

		if (expectedGoal.value.trim() == "" || isNaN(expectedGoal.value)) {
			refs.formAlertErrorExpected.innerHTML = Messages.get("label.valueInvalidExpected");
			refs["expectedGoal"].className += " borderError";
			boolMsg = true;
		} else {
			refs.formAlertErrorExpected.innerHTML = "";
			refs["expectedGoal"].className = "form-control";
		}

		if (minimumGoal.value.trim() == "" || isNaN(minimumGoal.value)) {
			refs.formAlertErrorMinimum.innerHTML = Messages.get("label.valueInvalidMin");
			refs["minimumGoal"].className += " borderError";
			boolMsg = true;
		} else {
			refs.formAlertErrorMinimum.innerHTML = "";
			refs["minimumGoal"].className = "form-control";
		}

		if (maximumGoal.value.trim() == "" || isNaN(maximumGoal.value)) {
			refs.formAlertErrorMaximum.innerHTML = Messages.get("label.valueInvalidMax");
			refs["maximumGoal"].className += " borderError";
			boolMsg = true;
		} else {
			refs.formAlertErrorMaximum.innerHTML = "";
			refs["maximumGoal"].className = "form-control";
		}

		var aux = {
			boolMsg:boolMsg,
			msg:msg,
			nameGoal:nameGoal,
			responsibleGoal:responsibleGoal,
			descriptionGoal:descriptionGoal,
			expectedGoal:expectedGoal,
			minimumGoal:minimumGoal,
			maximumGoal:maximumGoal
		};
		return aux;
	},

	tableNewFieldValidate(refs){
		var name = refs["new-column-name"].value.trim();
		var type = refs["new-column-type"].value.trim();
		
		if(name == undefined || name == ""  || type == undefined || type == ""){
			return false;
		}

		return true;
	},

	tableNewInstanceValidate(tableValues){
		var count=0;		
		tableValues.map((table) => {			
			if((table.value && table.value.trim() == "") ||
			 table.value == undefined ||
			 ((table.type == AttributeTypes.NUMBER_FIELD ||
			  table.type == AttributeTypes.PERCENTAGE_FIELD ||
			   table.type == AttributeTypes.CURRENCY_FIELD) && table.value.split(",").length > 2)) {
				count++;
			}
		});	

		return count <= 0;
	}
}

export default {
    validate: Validate
}