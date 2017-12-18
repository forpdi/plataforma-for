import React from "react";
import string from 'string';


export default React.createClass({
    render() {
        return (
            <div>
                <a href="http://forpdi.org/" target="_blanck">
                    <span className ="fpdi-nav-label fontSize12"> Portal ForPDI </span>
                </a>
                <br />
                <a href="http://forpdi.org/" target="_blanck">
				    <span className ="fpdi-nav-label fontSize12"> Download do Livro </span>
                </a>
                <a href="https://www.youtube.com/channel/UC1DP0ZGoTm0OlX-vOc-Ns5Q/videos" target="_blanck">
				    <span className ="fpdi-nav-label fontSize12"> Capacitação </span>
                </a>
                <a href="http://forpdi.org/manual.pdf" target="_blanck">
				    <span className ="fpdi-nav-label fontSize12"> Manual do usuário </span>
                </a>
            </div>
        );
    }
});