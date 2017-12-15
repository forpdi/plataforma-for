import React from "react";
import string from 'string';


export default React.createClass({
    render() {
        return (
            <div>
                <span className ="fpdi-nav-label fontSize12"> Portal ForPDI: http://forpdi.org/ </span>
                <br /> 
				<span className ="fpdi-nav-label fontSize12"> Download do Livro: http://forpdi.org/... </span>
				<span className ="fpdi-nav-label fontSize12 white-space"> Capacitação: https://www.youtube.com/channel/UC1DP0ZGoTm0OlX-vOc-Ns5Q/videos </span>
				<span className ="fpdi-nav-label fontSize12"> Manual do usuário: http://forpdi.org/manual.pdf </span>
            </div>
        );
    }
});