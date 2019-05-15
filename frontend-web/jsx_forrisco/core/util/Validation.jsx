import S from 'string';
import moment from 'moment'

import Messages from "forpdi/jsx/core/util/Messages";

var Validate = {
	validateTitle: function (data, idx, editFunc) {
		if (data.refs['edit-input'] != undefined) {
			if (data.refs['edit-input'].value.trim() != "") {
				data.refs['edit-input'].className = "form-control";
				data.refs['formAlertError-edit-input'].innerHTML = "";
				editFunc(data.refs['edit-input'].value, idx);
				return true;
			} else {
				data.refs['formAlertError-edit-input'].innerHTML = Messages.get("label.thisFieldMustBeFilled");
				data.refs['edit-input'].className = "form-control borderError";
				return false;
			}
		}
	},

	validateNumber: function isNumber(n) {
		return !isNaN(parseFloat(n)) && isFinite(n);
	},

	validateSectionTitle: function (data, dataTitle) {
		if (data.value.trim() == "") {
			dataTitle.innerHTML = Messages.get("label.thisFieldMustBeFilled");
			data.className = "form-control borderError";
			return true

		} else {
			return false
		}

	},

	validationPolicyEdit: function (data, refs) {
		var msg = ""

		//nome em branco
		if (data.name == "" || !!data.name.match(/^(\s)+$/)) {
			refs['ref-0'].refs['formAlertError'].innerHTML = Messages.get("label.error.fieldEmpty");
			refs['ref-0'].refs['field-name'].className += " borderError";
			msg = Messages.get("label.form.error");
		} else {
			if (refs['ref-0'].refs['field-name'].className) {
				refs['ref-0'].refs['field-name'].className = "form-control";
				refs['ref-0'].refs['formAlertError'].innerHTML = "";
			}
		}

		// data de vigencia invalida
		if (data.validityBegin && data.validityEnd) {
			var validityBegin = moment(data.validityBegin, 'DD/MM/YYYY').toDate();
			var validityEnd = moment(data.validityEnd, 'DD/MM/YYYY').toDate();
			if (validityBegin > validityEnd) {
				var validityBeginInput = refs['risk-vigencia-begin']
					.refs['field-risk-vigencia-begin']
					.refs['input']
					.refs['input'];
				var validityEndInput = refs['risk-vigencia-end']
					.refs['field-risk-vigencia-end']
					.refs['input']
					.refs['input'];
				validityBeginInput.className += ' borderError';
				validityEndInput.className += ' borderError';
				var errorElement = refs['risk-vigencia-begin'].refs['formAlertError'];
				errorElement.innerHTML = 'Data de início superior à data de término';
				msg = "A data de início do prazo de vigência não deve ser superior à data de término";
			} else {
				refs['risk-vigencia-begin']
					.refs['field-risk-vigencia-begin']
					.refs['input']
					.refs['input'].className = "form-control-h"
				refs['risk-vigencia-begin'].refs['formAlertError'].innerHTML = "<br/>"
			}
		} else if ((!data.validityBegin && data.validityEnd) || (data.validityBegin && !data.validityEnd)) {
			var nameRefValidityEmpty = !data.validityBegin && data.validityEnd
				? 'risk-vigencia-begin'
				: 'risk-vigencia-end';
			var validityInputEmpty = refs[nameRefValidityEmpty]
				.refs[`field-${nameRefValidityEmpty}`]
				.refs['input']
				.refs['input'];
			validityInputEmpty.className += ' borderError';
			var errorElementEmpty = refs[nameRefValidityEmpty].refs['formAlertError'];
			errorElementEmpty.innerHTML = 'A data deve ser preenchida';
			msg = "Não é permitido preencher somente uma das datas do prazo de vigência";
		}

		//graus de risco e cores em branco
		for (var i = 0; i < 6; i++) {
			var grau = 'grau-' + (i + 1)
			var risklevel = refs[grau + '-0'];
			var color = refs[grau + '-1'];

			if (data.risk_level[1][i] == -1) {
				color.refs['formAlertError'].innerHTML = Messages.get("label.error.fieldEmpty");
				color.refs['field-risk_cor_' + (i + 1)].className += " borderError";
				msg = Messages.get("label.form.error");
			} else {
				if (color) {
					color.refs['field-risk_cor_' + (i + 1)].className = "form-control-h"
					color.refs['formAlertError'].innerHTML = ""
					if (data.risk_level[0][i] == "" || (data.risk_level[0][i] && !!data.risk_level[0][i].match(/^(\s)+$/))) {
						color.refs['formAlertError'].innerHTML = "<br/>"
					}
				}
			}

			if (data.risk_level[0][i] == "" || (data.risk_level[0][i] && !!data.risk_level[0][i].match(/^(\s)+$/))) {
				risklevel.refs['formAlertError'].innerHTML = Messages.get("label.error.fieldEmpty");
				risklevel.refs['field-risk_level_' + (i + 1)].className += " borderError";
				msg = Messages.get("label.form.error");
			} else {
				if (risklevel) {
					risklevel.refs['field-risk_level_' + (i + 1)].className = "form-control-h"
					risklevel.refs['formAlertError'].innerHTML = ""
					if (data.risk_level[1][i] == -1) {
						risklevel.refs['formAlertError'].innerHTML = "<br/>"
					}
				}
			}
		}

		//graus de risco e cores repetidas
		for (var i = 0; i < 6; i++) {
			for (var j = 1; j < 6; j++) {
				if (data.risk_level[0][i] != null
					&& data.risk_level[0][i] != ""
					&& data.risk_level[0][i] == data.risk_level[0][j]
					&& i < j) {
					if (msg == "") { msg = "Graus des risco não podem repetir." }
					refs['grau-' + (j + 1) + '-0'].refs['field-risk_level_' + (j + 1)].className += " borderError";
					refs['grau-' + (i + 1) + '-0'].refs['field-risk_level_' + (i + 1)].className += " borderError";
				}
				if (data.risk_level[1][i] != null
					&& data.risk_level[1][i] != -1
					&& data.risk_level[1][i] == data.risk_level[1][j]
					&& i < j) {
					if (msg == "") { msg = "Graus des risco não podem repetir as cores." }
					refs['grau-' + (j + 1) + '-1'].refs['field-risk_cor_' + (j + 1)].className += " borderError";
					refs['grau-' + (i + 1) + '-1'].refs['field-risk_cor_' + (i + 1)].className += " borderError";
				}
			}
		}


		//linha e coluna em branco
		if (data.nline == "") {
			refs['numero-linha'].refs['formAlertError'].innerHTML = Messages.get("label.error.fieldEmpty");
			refs['numero-linha'].refs['field-nline'].className += " borderError";
			msg = Messages.get("label.form.error");
		} else {
			refs['numero-linha'].refs['formAlertError'].innerHTML = "<br/>"
			refs['numero-linha'].refs['field-nline'].className = "form-control-h"
		}


		if (data.ncolumn == "") {
			refs['numero-coluna'].refs['formAlertError'].innerHTML = Messages.get("label.error.fieldEmpty");
			refs['numero-coluna'].refs['field-ncolumn'].className += " borderError";
			msg = Messages.get("label.form.error");
		} else {
			refs['numero-coluna'].refs['formAlertError'].innerHTML = "<br/>"
			refs['numero-coluna'].refs['field-ncolumn'].className = "form-control-h"
		}



		//impacto e probabilidade em branco
		for (var i = 0; i < 6; i++) {
			if (refs.policyEditForm['field-probability_' + (i + 1)] != null) {
				if (refs.policyEditForm['field-probability_' + (i + 1)].value == "") {
					refs.policyEditForm['field-probability_' + (i + 1)].className += " borderError"
					if (msg == "") { msg = "Todas as probabilidades e impactos precisam estar preenchidos." }
				} else {
					refs.policyEditForm['field-probability_' + (i + 1)].className = "form-control-h"
				}
			}

			if (refs.policyEditForm['field-impact_' + (i + 1)] != null) {
				if (refs.policyEditForm['field-impact_' + (i + 1)].value == "") {
					refs.policyEditForm['field-impact_' + (i + 1)].className += " borderError"
					if (msg == "") { msg = "Todas as probabilidades e impactos precisam estar preenchidos." }
				} else {
					refs.policyEditForm['field-impact_' + (i + 1)].className = "form-control-h"
				}
			}
		}


		if (data.nline < 2 || data.ncolumn < 2) {
			if (msg == "") { msg = "Política deve ter pelo menos 2 linhas e 2 colunas." }
		}

		if (data.matrix == "") {
			if (msg == "") { msg = "Matriz de risco deve ser gerada." }
		} else {//matriz em branco
			for (let i = 0; i <= data.nline; i++) {
				for (let j = 0; j <= data.ncolumn; j++) {
					if (j != 0 && i != data.nline) {
						if (refs.policyEditForm['field-[' + (i) + ',' + (j) + ']'].value == "") {
							refs.policyEditForm['field-[' + (i) + ',' + (j) + ']'].className += " borderError"
							if (msg == "") {
								msg = "Matriz de risco deve ser completamente preenchida."
							}
						} else {
							refs.policyEditForm['field-[' + (i) + ',' + (j) + ']'].className = "form-control matrixSelect"
						}
					} else if (j == 0 && i != data.nline) {
						if (refs.policyEditForm['field-[' + (i) + ',' + (j) + ']'].value == "") {
							refs.policyEditForm['field-[' + (i) + ',' + (j) + ']'].className += " borderError"
							if (msg == "") {
								msg = "Matriz de risco deve ser completamente preenchida."
							}
						} else {
							refs.policyEditForm['field-[' + (i) + ',' + (j) + ']'].className = "form-control matrixSelect frisco-probability"
						}
					} else if (j != 0 && i == data.nline) {
						if (refs.policyEditForm['field-[' + (i) + ',' + (j) + ']'].value == "") {
							refs.policyEditForm['field-[' + (i) + ',' + (j) + ']'].className += " borderError"
							if (msg == "") {
								msg = "Matriz de risco deve ser completamente preenchida."
							}
						} else {
							refs.policyEditForm['field-[' + (i) + ',' + (j) + ']'].className = "form-control matrixSelect frisco-impact"
						}
					}
				}
			}
		}

		//probabilidade/impacto repetidos
		if (msg == "") {
			for (var i = 0; i < data.nline; i++) {
				for (var j = 1; j < data.nline; j++) {
					if (refs.policyEditForm['field-[' + (i) + ',0]'].value == refs.policyEditForm['field-[' + (j) + ',0]'].value && i < j) {
						refs.policyEditForm['field-[' + (i) + ',0]'].className += " borderError"
						refs.policyEditForm['field-[' + (j) + ',0]'].className += " borderError"
						msg = "As probabilidades/impactos não podem repetir."
					}
				}
			}


			for (var i = 1; i <= data.ncolumn; i++) {
				for (var j = 2; j <= data.ncolumn; j++) {
					if (refs.policyEditForm['field-[' + data.nline + ',' + (i) + ']'].value == refs.policyEditForm['field-[' + data.nline + ',' + (j) + ']'].value && i < j) {
						refs.policyEditForm['field-[' + data.nline + ',' + (i) + ']'].className += " borderError"
						refs.policyEditForm['field-[' + data.nline + ',' + (j) + ']'].className += " borderError"
						msg = "As probabilidades/impactos não podem repetir."
					}
				}
			}
		}

		return msg;
	},

	validationNewFieldItem: function (newfield, description) {
		var name, type;
		name = S(newfield['newfield-name'].value);
		type = S(newfield['newfield-type'].value);

		var errorField = false;

		newfield['newfield-name'].className = "form-control";
		newfield['formAlertErrorName'].innerHTML = "";

		newfield['newfield-type'].className = "form-control";
		newfield['formAlertErrorType'].innerHTML = "";

		if (name.isEmpty() || type.isEmpty()) {
			if (newfield['newfield-name'].value.trim() == "") {
				newfield['newfield-name'].className += " borderError";
				newfield['formAlertErrorName'].innerHTML = Messages.get("label.alert.fieldEmpty");
				errorField = true;
			} else {
				//newfield['newfield-name'].className = "form-control";
				//newfield['formAlertErrorName'].innerHTML = "";
			}

			if (newfield['newfield-type'].value.trim() == "") {
				newfield['newfield-type'].className += " borderError";
				newfield['formAlertErrorType'].innerHTML = Messages.get("label.alert.fieldEmpty");
				errorField = true;
			} else {
				//newfield['newfield-type'].className = "form-control";
				//newfield['formAlertErrorType'].innerHTML = "";
			}
		}

		var aux = {
			errorField: errorField,
			name: name,
			type: type,
			description: description
		}
		return aux;
	},

	validationNewFieldType: function (newfield, description) {
		var type, name;

		name = S(newfield['newfield-name'].value);
		type = S(newfield['newfield-type'].value);

		var errorField = false;

		newfield['newfield-type'].className = "form-control";
		newfield['formAlertErrorType'].innerHTML = "";

		if (type.isEmpty()) {
			if (newfield['newfield-type'].value.trim() == "") {
				newfield['newfield-type'].className += " borderError";
				newfield['formAlertErrorType'].innerHTML = Messages.get("label.alert.fieldEmpty");
				errorField = true;
			}
		}

		return {
			errorField: errorField,
			type: type,
			name: name,
			description: description
		};
	},

	validationNewItem(newfield) {

		var titulo = S(newfield['field-description'].value);

		var errorField = false;

		if (titulo.isEmpty()) {
			if (newfield['field-description'].value.trim() == "") {
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

	},


	validationRiskRegister: function (data, refs) {
		var msg = ""
		var name = refs["field-name"].refs.name
		var user = refs["field-1"].refs.user
		var probability = refs["field-4"].refs.probability
		var impact = refs["field-5"].refs.impact
		var periodicity = refs["field-7"].refs.periodicity
		var type = refs["field-8"].refs.type
		var tipology = refs["field-9"].refs.tipology

		if (name.refs["field-name"].value != null) {
			if (name.refs["field-name"].value == "") {
				name.refs["field-name"].className += " borderError"
				name.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldEmpty");
				if (msg == "") { msg = "Nome precisa estar preenchido." }
			} else {
				name.refs["field-name"].className = "form-control"
				name.refs["formAlertError"].innerHTML = ""
			}
		}

		//if(user.refs["field-user"].value != null){
		if (user.refs["field-user"].state.value == null || user.refs["field-user"].state.value == "") {
			user.refs["field-user"].className += " borderError"
			user.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldNotselected");
			if (msg == "") { msg = "Usuário precisa estar preenchido." }
		} else {
			user.refs["field-user"].className = "form-control"
			user.refs["formAlertError"].innerHTML = ""
		}
		//}

		if (probability.refs["field-probability"] != null) {
			if (probability.refs["field-probability"].value == "") {
				probability.refs["field-probability"].className += " borderError"
				probability.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldEmpty");
				if (msg == "") { msg = "A probabilidade precisa estar selecionada." }
			} else {
				probability.refs["field-probability"].className = "form-control"
				probability.refs["formAlertError"].innerHTML = "<br/>"
			}
		}

		if (impact.refs["field-impact"] != null) {
			if (impact.refs["field-impact"].value == "") {
				impact.refs["field-impact"].className += " borderError"
				impact.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldNotselected");
				if (msg == "") { msg = "O impacto precisa estar selecionado." }
			} else {
				impact.refs["field-impact"].className = "form-control"
				impact.refs["formAlertError"].innerHTML = "<br/>"
			}
		}

		if (periodicity.refs["field-periodicity"] != null) {
			if (periodicity.refs["field-periodicity"].value == "") {
				periodicity.refs["field-periodicity"].className += " borderError"
				periodicity.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldNotselected");
				if (msg == "") { msg = "A periodicidade precisa estar selecionada." }
			} else {
				periodicity.refs["field-periodicity"].className = "form-control"
				periodicity.refs["formAlertError"].innerHTML = "<br/>"
			}
		}

		if (tipology.refs["field-tipology"] != null) {
			if (tipology.refs["field-tipology"].value == "") {
				tipology.refs["field-tipology"].className += " borderError"
				tipology.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldNotselected");
				if (msg == "") { msg = "A tipologia precisa estar selecionada." }
			} else {
				tipology.refs["field-tipology"].className = "form-control"
				tipology.refs["formAlertError"].innerHTML = "<br/>"
			}
		}

		if (type.refs["field-type"] != null) {
			if (type.refs["field-type"].value == "") {
				type.refs["field-type"].className += " borderError"
				type.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldNotselected");
				if (msg == "") { msg = "O tipo precisa estar selecionado." }
			} else {
				type.refs["field-type"].className = "form-control"
				type.refs["formAlertError"].innerHTML = "<br/>"
			}
		}

		// if(tipology.refs["field-tipology"] != null){
		// 	if(tipology.refs["field-tipology"].value==""){
		// 		tipology.refs["field-tipology"].className +=" borderError"
		// 		tipology.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldNotselected");
		// 		if(msg==""){msg="A tipologia precisa estar selecionada."}
		// 	}else{
		// 		tipology.refs["field-tipology"].className="form-control"
		// 		tipology.refs["formAlertError"].innerHTML = ""
		// 	}
		// }


		var i = 0
		while (refs["pa-" + (i) + "-0"] != null) {
			var process = refs["pa-" + (i) + "-0"]
			var activity = refs["pa-" + (i) + "-1"]

			if (process != null) {
				if (process.refs["field-process_" + (i)].value == "") {
					process.refs["field-process_" + (i)].className += " borderError"
					process.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldNotselected");
					if (msg == "") { msg = "O processo precisa estar selecionado." }
				} else {
					process.refs["field-process_" + (i)].className = "form-control-h"

					if (activity.refs["field-activity_" + (i)].value == "") {
						process.refs["formAlertError"].innerHTML = "</br>"
					} else {
						process.refs["formAlertError"].innerHTML = ""
					}
				}
			}
			if (activity != null) {
				if (activity.refs["field-activity_" + (i)].value == "") {
					activity.refs["field-activity_" + (i)].className += " borderError"
					activity.refs["formAlertError"].innerHTML = Messages.get("label.error.fieldEmpty");
					if (msg == "") { msg = "Aa tividade precisa estar preenchida." }
				} else {
					activity.refs["field-activity_" + (i)].className = "form-control-h"
					activity.refs["formAlertError"].innerHTML = ""

					if (process.refs["field-process_" + (i)].value == "") {
						activity.refs["formAlertError"].innerHTML = "</br>"
					} else {
						activity.refs["formAlertError"].innerHTML = ""
					}
				}
			}
			i++;
		}



		return msg
	}
}

export default {
	validate: Validate
}
