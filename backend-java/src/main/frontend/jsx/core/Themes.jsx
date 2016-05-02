import DefaultTheme from "forpdi/sass/theme-default.scss";
import RedTheme from "forpdi/sass/theme-red.scss";

var themes = {
	default: DefaultTheme,
	red: RedTheme
};

function changeTheme(theme) {
	if (!themes[theme]) {
		console.warn("Undefined theme:",theme);
		return false;
	}
	document.getElementById("fpdi-theme-styles").href = themes[theme];
	return true;
}

(function() {
	var linkEl = document.createElement("link");
	linkEl.id = "fpdi-theme-styles";
	linkEl.rel = "stylesheet";
	linkEl.type = "text/css";
	linkEl.href = themes.default;
	document.getElementsByTagName("head")[0].appendChild(linkEl);
}());

export default {
	themes: themes,
	changeTheme: changeTheme
};