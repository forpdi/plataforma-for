import S from 'string';
import Messages from "forpdi/jsx/core/util/Messages";

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

	validationPolicyEdit: function(data, refs) {
        var msg =""

		//nome em branco
		if(data.name == "" ||  !!data.name.match(/^(\s)+$/) ){
			refs['ref-0'].refs['formAlertError'].innerHTML = Messages.get("label.error.fieldEmpty");
			refs['ref-0'].refs['field-name'].className += " borderError";
			msg = Messages.get("label.form.error");
		}else{
			if(refs['ref-0'].refs['field-name'].className){
				refs['ref-0'].refs['field-name'].className = "form-control";
				refs['ref-0'].refs['formAlertError'].innerHTML = "";
			}
		}

		//graus de risco e cores em branco
		for(var i=0 ; i<6 ; i++){
			var grau='grau-'+(i+1)
			var risklevel=refs[grau+'-0'];
			var color=refs[grau+'-1'];

			if(data.risk_level[1][i] == -1){
				color.refs['formAlertError'].innerHTML = Messages.get("label.error.fieldEmpty");
				color.refs['field-risk_cor_'+(i+1)].className += " borderError";
				msg = Messages.get("label.form.error");
			}else{
				if(color){
					color.refs['field-risk_cor_'+(i+1)].className = "form-control-h"
					color.refs['formAlertError'].innerHTML =""
					if(data.risk_level[0][i] == "" ||  (data.risk_level[0][i] && !!data.risk_level[0][i].match(/^(\s)+$/))){
						color.refs['formAlertError'].innerHTML ="<br/>"
					}
				}
			}

			if(data.risk_level[0][i] == "" ||  (data.risk_level[0][i] && !!data.risk_level[0][i].match(/^(\s)+$/))){
				risklevel.refs['formAlertError'].innerHTML = Messages.get("label.error.fieldEmpty");
				risklevel.refs['field-risk_level_'+(i+1)].className += " borderError";
				msg = Messages.get("label.form.error");
			}else{
				if(risklevel){
					risklevel.refs['field-risk_level_'+(i+1)].className = "form-control-h"
					risklevel.refs['formAlertError'].innerHTML = ""
					if(data.risk_level[1][i] == -1){
						risklevel.refs['formAlertError'].innerHTML = "<br/>"
					}
				}
			}
		}

		//graus de risco e cores repetidas
		for(var i=0 ; i<6 ; i++){
			for(var j=1 ; j<6 ; j++){
				if(data.risk_level[0][i] != null
				&& data.risk_level[0][i] != ""
				&& data.risk_level[0][i] == data.risk_level[0][j]
				&& i<j){
					if(msg==""){msg="Graus des risco não podem repetir."}
					refs['grau-'+(j+1)+'-0'].refs['field-risk_level_'+(j+1)].className += " borderError";
					refs['grau-'+(i+1)+'-0'].refs['field-risk_level_'+(i+1)].className += " borderError";
				}
				if(data.risk_level[1][i] != null
				&& data.risk_level[1][i] != -1
				&& data.risk_level[1][i] == data.risk_level[1][j]
				&& i<j){
					if(msg==""){msg="Graus des risco não podem repetir as cores."}
					refs['grau-'+(j+1)+'-1'].refs['field-risk_cor_'+(j+1)].className += " borderError";
					refs['grau-'+(i+1)+'-1'].refs['field-risk_cor_'+(i+1)].className += " borderError";
				}
			}
		}


		//linha e coluna em branco
		if(data.ncolumn == ""){
			refs['numero-1'].refs['formAlertError'].innerHTML = Messages.get("label.error.fieldEmpty");
			refs['numero-1'].refs['field-ncolumn'].className += " borderError";
			msg = Messages.get("label.form.error");
		}else{
			refs['numero-1'].refs['formAlertError'].innerHTML="<br/>"
			refs['numero-1'].refs['field-ncolumn'].className = "form-control-h"
		}

		if(data.nline == ""){
			refs['numero-0'].refs['formAlertError'].innerHTML = Messages.get("label.error.fieldEmpty");
			refs['numero-0'].refs['field-nline'].className += " borderError";
			msg = Messages.get("label.form.error");
		}else{
			refs['numero-0'].refs['formAlertError'].innerHTML="<br/>"
			refs['numero-0'].refs['field-nline'].className = "form-control-h"
		}


		//impacto e probabilidade em branco
		for(var i=0 ; i<6 ; i++){
			if(refs.policyEditForm['field-probability_'+(i+1)] !=null){
				if(refs.policyEditForm['field-probability_'+(i+1)].value==""){
					refs.policyEditForm['field-probability_'+(i+1)].className +=" borderError"
					if(msg==""){msg="Todas as probabilidades e impactos precisam estar preenchidos."}
				}else{
					refs.policyEditForm['field-probability_'+(i+1)].className="form-control-h"
				}
			}

			if(refs.policyEditForm['field-impact_'+(i+1)] != null){
				if(refs.policyEditForm['field-impact_'+(i+1)].value==""){
					refs.policyEditForm['field-impact_'+(i+1)].className +=" borderError"
					if(msg==""){msg="Todas as probabilidades e impactos precisam estar preenchidos."}
				}else{
					refs.policyEditForm['field-impact_'+(i+1)].className="form-control-h"
				}
			}
		}


		if(data.nline <2 || data.ncolumn <2){
			if(msg==""){msg="Política deve ter pelo menos 2 linhas e 2 colunas."}
		}

		if(data.matrix==""){
			if(msg==""){msg="Matriz de risco deve ser gerada."}
		}else{//matriz em branco
			for(var i=0; i<=data.nline; i++){
				for(var j=0; j<=data.ncolumn; j++){

					if( j!=0  &&  i!=data.nline){
						if(refs.policyEditForm['field-['+(i)+','+(j)+']'].value==""){
							refs.policyEditForm['field-['+(i)+','+(j)+']'].className +=" borderError"
							if(msg==""){msg="Matriz de risco deve ser completamente preenchida."}
						}else{
							refs.policyEditForm['field-['+(i)+','+(j)+']'].className ="form-control matrixSelect"
						}
					}else if(j==0){
						console.log(i,j,refs.policyEditForm['field-['+(i)+','+(j)+']'])
						if(refs.policyEditForm['field-['+(i)+','+(j)+']'].value==""){
							refs.policyEditForm['field-['+(i)+','+(j)+']'].className +=" borderError"
							if(msg==""){msg="Matriz de risco deve ser completamente preenchida."}
						}else{
							refs.policyEditForm['field-['+(i)+','+(j)+']'].className ="form-control matrixSelect frisco-probability"
						}
					}else if(i==data.nline){
						if(refs.policyEditForm['field-['+(i)+','+(j)+']'].value==""){
							refs.policyEditForm['field-['+(i)+','+(j)+']'].className +=" borderError"
							if(msg==""){msg="Matriz de risco deve ser completamente preenchida."}
						}else{
							refs.policyEditForm['field-['+(i)+','+(j)+']'].className ="form-control matrixSelect frisco-impact"
						}

					}



				}
			}
		}

		//probabilidade/impacto repetidos
		if(msg==""){
			for(var i=0; i<data.nline; i++){
				for(var j=1; j<data.nline; j++){
					if(refs.policyEditForm['field-['+(i)+',0]'].value == refs.policyEditForm['field-['+(j)+',0]'].value && i<j){
						refs.policyEditForm['field-['+(i)+',0]'].className +=" borderError"
						refs.policyEditForm['field-['+(j)+',0]'].className +=" borderError"
						msg="As probabilidades/impactos não podem repetir."
					}
				}
			}


			for(var i=1; i<=data.ncolumn; i++){
				for(var j=2; j<=data.ncolumn; j++){
					if(refs.policyEditForm['field-['+data.nline +','+(i)+']'].value == refs.policyEditForm['field-['+data.nline +','+(j)+']'].value && i<j){
						refs.policyEditForm['field-['+data.nline +','+(i)+']'].className +=" borderError"
						refs.policyEditForm['field-['+data.nline +','+(j)+']'].className +=" borderError"
						msg="As probabilidades/impactos não podem repetir."
					}
				}
			}
		}

		return msg;
	},

	validationNewFieldItem: function(newfield, description) {
		var name, type;
		name =  S(newfield['newfield-name'].value);
		type =  S(newfield['newfield-type'].value);

		var errorField = false;

		newfield['newfield-name'].className = "form-control";
		newfield['formAlertErrorName'].innerHTML = "";

		newfield['newfield-type'].className = "form-control";
		newfield['formAlertErrorType'].innerHTML = "";

		if (name.isEmpty() || type.isEmpty()) {
			if(newfield['newfield-name'].value.trim() == "") {
				newfield['newfield-name'].className += " borderError";
				newfield['formAlertErrorName'].innerHTML = Messages.get("label.alert.fieldEmpty");
				errorField = true;
			} else {
				//newfield['newfield-name'].className = "form-control";
				//newfield['formAlertErrorName'].innerHTML = "";
			}

			if(newfield['newfield-type'].value.trim() == "") {
				newfield['newfield-type'].className += " borderError";
				newfield['formAlertErrorType'].innerHTML = Messages.get("label.alert.fieldEmpty");
				errorField = true;
			} else {
				//newfield['newfield-type'].className = "form-control";
				//newfield['formAlertErrorType'].innerHTML = "";
			}
		}


		/*if(description.trim() == "") {
			//newfield['formAlertErrorDescription'].innerHTML = Messages.get("label.alert.fieldEmpty");
			errorField = true;
		} else {
			//newfield['formAlertErrorDescription'].innerHTML = "";
		}*/


		var aux = {
			errorField: errorField,
			name: name,
			type: type,
			description: description
		}
		return aux;
	},

	validationNewItem(newfield){

		var titulo =  S(newfield['field-description'].value);

		var errorField = false;

		if (titulo.isEmpty()) {
			if(newfield['field-description'].value.trim() == "") {
				newfield['field-description'].className = "form-control borderError";
				//newfield['field-description'].innerHTML = Messages.get("label.alert.fieldEmpty");
				errorField = true;
			} else {
				newfield['field-description'].className = "form-control";
				//newfield['formAlertErrorTitulo'].innerHTML = "";
			}
		}

		var aux = {
			errorField: errorField,
			titulo: titulo,
		}
		return aux;

	}
}

export default {
    validate: Validate
}
