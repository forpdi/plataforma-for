import React from "react";
import string from 'string';


export default React.createClass({
    render() {
        return (
            <div>
                <a href="http://forrisco.org/" target="_blanck">
                    <span className ="fpdi-nav-label forrisco-link-information fontSize12"> Portal ForRisco </span>
                </a>
                <br />
                <a href="http://forrisco.org/livro.php" target="_blanck">
				    <span className ="fpdi-nav-label forrisco-link-information fontSize12"> Download do Livro </span>
                </a>
                <a href="https://www.youtube.com/channel/UC1DP0ZGoTm0OlX-vOc-Ns5Q/videos" target="_blanck">
				    <span className ="fpdi-nav-label forrisco-link-information fontSize12"> Capacitação </span>
                </a>
                <a href="http://forpdi.org/manual.pdf" target="_blanck">
				    <span className ="fpdi-nav-label forrisco-link-information fontSize12"> Manual do usuário </span>
                </a>
            </div>
        );
    }
});