import React from "react";
import queryString from 'query-string';

var numeral = require('numeral');

export default React.createClass({

	getInitialState() {
		return {


		}
	},
render(){
  let params = queryString.parse(this.props.location.search);
  console.log("//TODO listar indicentes",params);

	return <div>

	</div>
	}
});
