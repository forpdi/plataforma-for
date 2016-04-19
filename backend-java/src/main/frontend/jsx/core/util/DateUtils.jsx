
var FULL_MONTHS = [
	"Janeiro",
	"Fevereiro",
	"Março",
	"Abril",
	"Maio",
	"Junho",
	"Julho",
	"Agosto",
	"Setembro",
	"Outubro",
	"Novembro",
	"Dezembro"
];

var SHORT_MONTHS = [
	"jan",
	"fev",
	"mar",
	"abr",
	"mai",
	"jun",
	"jul",
	"ago",
	"set",
	"out",
	"nov",
	"dez"
];

var FULL_DAYS = [
	"Domingo",
	"Segunda-feira",
	"Terça-feira",
	"Quarta-feira",
	"Quinta-feira",
	"Sexta-feira",
	"Sábado"
];

var SHORT_DAYS = [
	"dom",
	"seg",
	"ter",
	"qua",
	"qui",
	"sex",
	"sáb"
];

/**
	Formats a date following a format with the following qualifiers:

	%d The date number from 1 to 31.
	%D The date number from 01 to 31.
	%w The week day short name.
	%W The week day full name.
	%m The month number from 1 to 12.
	%M The month number from 01 to 12.
	%n The month short name.
	%N The month full name.
	%y The two digit year.
	%Y The four digit year.
*/
var FORMATTING_REGEX = /\%[dDwWmMnNyY]/g;
function formatDate(date, format) {
	if (!date || !format) {
		return "";
	}
	if (typeof date === 'string') {
		date = new Date(date);
	}
	return format.replace(FORMATTING_REGEX, (match) => {
		switch (match) {
			case "%d": return date.getDate();
			case "%D":
				if (date.getDate() < 10)
					return "0"+date.getDate();
				return date.getDate();
			case "%w": return SHORT_DAYS[date.getDay()];
			case "%W": return FULL_DAYS[date.getDay()];
			case "%m": return date.getMonth()+1;
			case "%M":
				if (date.getMonth() < 9)
					return "0"+(date.getMonth()+1);
				return date.getMonth()+1;
			case "%n": return SHORT_MONTHS[date.getMonth()];
			case "%N": return FULL_MONTHS[date.getMonth()];
			case "%y": return date.getFullYear()%100;
			case "%Y": return date.getFullYear();
		}
	});
}

export default {
	formatDate: formatDate
};
