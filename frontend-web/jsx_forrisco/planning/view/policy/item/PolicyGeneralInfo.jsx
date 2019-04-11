import React from "react";
import _ from "underscore";
import { Link } from "react-router";
import moment from 'moment'

import Messages from "@/core/util/Messages";
import Modal from "@/core/widget/Modal";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		policy: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
	},

	getInitialState() {
		return {
			planRisk: [],
			fields: [],
			title: 'Informações Gerais',
			description: '',
			policy: '',
			isLoading: true,
			policyModel: null,
			risklevelModel: null,
		}
	},

	componentDidMount() {
		if (this.props.params.policyId) {
			PolicyStore.on("findpolicy", (model) => {
				this.setState({
					policyModel: model,
					ncolumn: model.data.ncolumn,
					nline: model.data.nline,
					isLoading: this.state.risklevelModel == null ? true : false,
				});

				//Construção da Aba Superior
				_.defer(() => {
					this.context.tabPanel.addTab(this.props.location.pathname, this.state.title);
				});

			}, this);

			PolicyStore.on("policyDeleted", (model) => {
				if (model.success) {
					this.context.router.push("/forrisco/home");
					PolicyStore.dispatch({
						action: PolicyStore.ACTION_FIND_UNARCHIVED_FOR_MENU
					});
				} else {
					if (model.message != null) {
						this.context.toastr.addAlertError(model.message);
					}
				}
			}, this);

			PolicyStore.on("retrieverisklevel", (model) => {
				this.setState({
					risklevelModel: model,
					isLoading: this.state.policyModel == null ? true : false,
				});
				this.forceUpdate();
			}, this);
		}
		this.refresh(this.props.params.policyId);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.params.policyId !== this.props.params.policyId) {
			this.refresh(newProps.params.policyId);
		}
	},

	refresh(policyId) {
		PolicyStore.dispatch({
			action: PolicyStore.ACTION_FIND_POLICY,
			data: policyId
		});
		PolicyStore.dispatch({
			action: PolicyStore.ACTION_RETRIEVE_RISK_LEVEL,
			data: policyId
		});
	},

	componentWillUnmount() {
		PolicyStore.off(null, null, this);
	},

	deletePolicy() {
		var me = this;
		var msg = "Você tem certeza que deseja excluir essa política?"

		if (this.state.policyModel.data) {
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_DELETE,
				data: me.state.policyModel.data.id
			});
		} else {
			Modal.confirmCustom(() => {
				Modal.hide();
				PolicyStore.dispatch({
					action: PolicyStore.ACTION_DELETE,
					data: me.state.policyModel.data.id
				});
			}, msg, me.refreshCancel);
		}
	},

	refreshCancel() {
		Modal.hide();
	},

	getMatrixValue(matrix, line, column) {

		var result = ""

		for (var i = 0; i < matrix.length; i++) {
			if (matrix[i][1] == line) {
				if (matrix[i][2] == column) {
					if (matrix[i][2] == 0) {
						return <div style={{"fontWeight": "400", "textAlignLast" : "center"}}>{matrix[i][0]}</div>
					} else if (matrix[i][1] == this.state.policyModel.data.nline) {
						return <div style={{ "textAlign": "-webkit-center", "margin": "5px", "fontWeight": "400" }}>{matrix[i][0]}</div>
					} else {

						var current_color = 0;
						var cor = ""
						if (this.state.risklevelModel != null) {
							for (var k = 0; k < this.state.risklevelModel.data.length; k++) {
								if (this.state.risklevelModel.data[k]['level'] == matrix[i][0]) {
									current_color = this.state.risklevelModel.data[k]['color']
								}
							}
						}

						switch (current_color) {
							case 0: cor = "Vermelho"; break;
							case 1: cor = "Marron"; break;
							case 2: cor = "Amarelo"; break;
							case 3: cor = "Laranja"; break;
							case 4: cor = "Verde"; break;
							case 5: cor = "Azul"; break;
							default: cor = "Cinza";
						}

						return <div className={"Cor " + cor}>{matrix[i][0]}</div>

					}
				}
			}
		}
		return ""
	},

	getMatrix() {
		if (this.state.policyModel == null) {
			return
		}

		var fields = [];
		if (typeof this.state.fields === "undefined" || this.state.fields == null) {
			fields.push({
				name: "description",
				type: AttributeTypes.TEXT_AREA_FIELD,
				placeholder: "",
				maxLength: 9000,
				label: Messages.getEditable("label.description", "fpdi-nav-label"),
				value: this.state.itemModel ? this.state.itemModel.get("description") : null,
				edit: false
			});
		} else {
			fields = this.state.fields
		}

		var aux = this.state.policyModel.data.matrix.split(/;/)
		var matrix = []

		for (var i = 0; i < aux.length; i++) {
			matrix[i] = new Array(3)
			matrix[i][0] = aux[i].split(/\[.*\]/)[1]
			matrix[i][1] = aux[i].match(/\[.*\]/)[0].substring(1, aux[i].match(/\[.*\]/)[0].length - 1).split(/,/)[0]
			matrix[i][2] = aux[i].match(/\[.*\]/)[0].substring(1, aux[i].match(/\[.*\]/)[0].length - 1).split(/,/)[1]
		}

		var table = []
		for (var i = 0; i <= this.state.policyModel.data.nline; i++) {
			var children = []
			for (var j = 0; j <= this.state.policyModel.data.ncolumn; j++) {
				children.push(<td key={j}>{this.getMatrixValue(matrix, i, j)} </td>)
			}
			table.push(<tr key={i} >{children}</tr>)
		}

		return (
			<div>
				<label htmlFor={this.state.fieldId} className="fpdi-text-label">
					{"MATRIZ DE RISCO"}
				</label>
				<br />
				<br />
				<table>
					<th>
						<td >
							<div style={{
								"width": "114px",
								"right": "74px",
								"top": ((this.state.policyModel.data.nline - 1) * 20 + 40) + "px",
								"position": "relative"
							}} className="vertical-text">PROBABILIDADE</div>
						</td>
						{table}
						<td></td>
						<td colSpan={this.state.policyModel.data.ncolumn} style={{ "text-align": "-webkit-center" }}>IMPACTO</td>
					</th>
				</table>

			</div>
		);
	},

	getDescriptions() {

		if (this.state.policyModel == null || this.state.policyModel.data.PIDescriptions == null) {
			return
		}

		var pdesc = JSON.parse(this.state.policyModel.data.PIDescriptions).PIDescriptions.pdescriptions
		var idesc = JSON.parse(this.state.policyModel.data.PIDescriptions).PIDescriptions.idescriptions

		var desc = [<br />]

		for (var i = 0; i < this.state.policyModel.data.nline; i++) {
			desc.push(<div>
				<b>{"Probabilidade " + pdesc[i].value}</b> {" - " + pdesc[i].description}
			</div>)
		}

		desc.push(<br />)

		for (var i = 0; i < this.state.policyModel.data.ncolumn; i++) {
			desc.push(<div>
				<b>{"Impacto " + idesc[i].value}</b> {" - " + idesc[i].description}
			</div>)
		}

		return desc
	},

	renderDropdown() {
		return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<Link
						to={"/forrisco/policy/" + this.props.params.policyId + "/edit"}>
						<span className="mdi mdi-pencil cursorPointer" title={Messages.get("label.title.editPolicy")}>
							<span id="menu-levels"> {"Editar Política"} </span>
						</span>
					</Link>
				</li>
				<li>
					<Link onClick={this.deletePolicy}>
						<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deletePolicy")}>
							<span id="menu-levels"> {"Deletar Política"} </span>
						</span>
					</Link>
				</li>
			</ul>
		);
	},

	renderBreadcrumb() {
		return (
			<div>
				<span>
					<Link className="fpdi-breadcrumb fpdi-breadcrumbDivisor"
						to={'/forrisco/policy/' + this.context.policy.id}
						title={this.context.policy.name}>{this.context.policy.name.length > 15 ? this.context.policy.name.substring(0, 15) + "..." : this.context.policy.name.substring(0, 15)
						}</Link>
					<span className="mdi mdi-chevron-right fpdi-breadcrumbDivisor"></span>
				</span>
				<span className="fpdi-breadcrumb fpdi-selectedOnBreadcrumb">
					{this.state.title > 15 ? this.state.title.substring(0, 15) + "..." : this.state.title.substring(0, 15)}
				</span>
			</div>
		);
	},

	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge />;
		}

		return (<div>
			<div className="fpdi-card fpdi-card-full floatLeft">
				<h1>
					{this.state.policyModel.data.name}
					{
						(this.context.roles.ADMIN ||
							_.contains(this.context.permissions, PermissionsTypes.FORRISCO_MANAGE_POLICY_PERMISSION))
						&&
						<span className="dropdown">
							<a className="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="true"
								title={Messages.get("label.actions")}>
								<span className="sr-only">{Messages.getEditable("label.actions", "fpdi-nav-label")}</span>
								<span className="mdi mdi-chevron-down" />
							</a>
							{this.renderDropdown()}
						</span>
					}
				</h1>

				<div>
					<br />
					<label className="fpdi-text-label">{"DESCRIÇÃO"}</label>
					<br />

					<span className="pdi-normal-text">
						<pre className="pre-info">{this.state.policyModel.data.description}</pre>
					</span>

					<br /><br />
					{
						this.state.policyModel.data.validityBegin && this.state.policyModel.data.validityEnd &&
						<div>
							<label className="fpdi-text-label">{"PRAZO DE VIGÊNCIA"}</label>
							<div className="padding5">
								<span>
									{this.state.policyModel.data.validityBegin && this.state.policyModel.data.validityBegin.split(' ')[0]}
									&nbsp;&nbsp;
									à
									&nbsp;&nbsp;
									{this.state.policyModel.data.validityEnd && this.state.policyModel.data.validityEnd.split(' ')[0]}
								</span>
							</div>
							<br /><br />
						</div>
					}
					{this.getMatrix()}
					<br />
					<br />
					<label className="fpdi-text-label">{"DESCRIÇÃO DOS TIPOS DE PROBABILIDADE E IMPACTO"}</label>
					<br />
					{this.getDescriptions()}
				</div>
			</div>
		</div>)
	}
})
