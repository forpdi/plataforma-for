import React from "react";
import {Link} from "react-router";
import _ from "underscore";

import Messages from "forpdi/jsx/core/util/Messages.jsx";

import Logo from 'forpdi/img/logo.png';

export default React.createClass({
    contextTypes: {
        
    },
    getInitialState() {
        return {
            hidden: false
        };
    },
    componentDidMount() {        
       
    },
    componentWillUnmount() {
    
    },
    tweakHidden() {
        this.setState({
            hidden: !this.state.hidden
        });
    },

    

  render() {
    return (<div className={(this.state.hidden ? 'fpdi-app-sidebar-hidden':'fpdi-app-sidebar')+' fpdi-tabs-stacked'}>
        <div className="fpdi-tabs-nav fpdi-nav-hide-btn">
            <a onClick={this.tweakHidden}>
                <span className={"fpdi-nav-icon mdi "+(this.state.hidden ? "mdi-arrow-right-bold-circle":"mdi-arrow-left-bold-circle")}
                    /> <span className="fpdi-nav-label">
                        {Messages.getEditable("label.collapseMenu","fpdi-nav-label")}
                    </span>
            </a>
        </div>
        <span className="fpdi-fill" />
   	</div>);
  }
});
