import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes';
import moment from 'moment';
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
    }
}

export default {
    validate: Validate
}
