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

	validationPolicyEdit: function(data, policyEditForm) {
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


		if(data.name == "" ||  !!data.name.match(/^(\s)+$/) ){
			boolMsg = true;
			policyEditForm.refs.name.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			policyEditForm.refs.name.refs["field-name"].className += " borderError";
		}else{
			if(policyEditForm.refs.name.refs["field-name"].className && policyEditForm.refs.name.refs["field-name"].className.indexOf('borderError')){
				policyEditForm.refs.name.refs["field-name"].className = "form-control";
				policyEditForm.refs.name.refs.formAlertError.innerHTML = "";
			}
		}
        var aux = {
            boolMsg: boolMsg,
            msg: msg
        }


		return aux;
	},

	validationNewFieldItem: function(newfield, description) {
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


		if(description.trim() == "") {
			//newfield['formAlertErrorDescription'].innerHTML = Messages.get("label.alert.fieldEmpty");
			errorField = true;
		} else {
			//newfield['formAlertErrorDescription'].innerHTML = "";
		}


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
