import React from "react";
import {Link} from "react-router";
import _ from "underscore";

import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import EditPlanRiskSubItem from "forpdi/jsx_forrisco/planning/view/plan/item/subitem/EditPlanRiskSubItem.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "@/core/util/Messages";
import Modal from "@/core/widget/Modal";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";

export default React.createClass({
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
		permissions: React.PropTypes.array.isRequired,
		planRisk: React.PropTypes.object.isRequired
	},

	propTypes: {
		location: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {
			itemTitle: "",
			field: [{
				fieldName: [],
				fieldContent: [],
				isText: ""
			}],
			tabPath: "",
			isLoading: true,
			edit: false,
			itemModel: null
		};
	},

	componentDidMount() {
		PlanRiskItemStore.on('detailSubItem', response => {
			var content = [];
			if (response.data.length !== 0) {
				response.data.planRiskSubItemField.map(field => {
					content.push({
						fieldName: field.name,
						fieldContent: field.description,
						isText: field.isText,
						fileLink: field.fileLink,
						editInstance: false,
						required: true
					})
				});

				this.setState({
					itemTitle: response.data.name,
					field: content,
					isLoading: this.state.itemModel == null,
					tabPath: this.props.location.pathname,
					edit: false
				});

			} else {

				this.setState({
					itemTitle: response.data.name,
					field: [],
					isLoading: false,
					tabPath: this.props.location.pathname,
					edit: false
				});
			}

			//Construção da Aba Superior
			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname, this.state.itemTitle);
			});
		}, this);

		PlanRiskItemStore.on('detailItem', response => {
			this.setState({
				itemModel:response,
				isLoading: this.state.field == null
			})
		},this);

		this.refreshComponent(this.props.params.subItemId);
	},

	componentWillReceiveProps(newProps) {
		if (this.props.params.subItemId !== newProps.params.subItemId) {
			this.refreshComponent(newProps.params.subItemId);
		}
	},

	componentWillUnmount() {
		PlanRiskItemStore.off(null, null, this);
	},

	refreshComponent(subItemId) {
		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_DETAIL_SUBITEM,
			data: {
				id: subItemId
			},
		});
		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_DETAIL_ITEM,
			data: {
				id: this.props.params.itemId
			},
		});

	},

	onDelete() {
		var me = this;
		var msg = "Você tem certeza que deseja excluir esse subitem?";

		Modal.confirmCustom(() => {
			Modal.hide();
			this.dispatchDelete(this.props.params.subItemId);
		}, msg, me.refreshCancel);

		PlanRiskItemStore.on('deletePlanRiskSubItem', response => {
			if(response.success === true) {
				const hasMinTabsLength = this.context.tabPanel.state.tabs.length <= 1 ? true : false;

				this.context.tabPanel.removeTabByPath(this.props.location.pathname);
				if (hasMinTabsLength) {
					this.context.router.push("/forrisco/plan-risk/" + this.props.params.planRiskId + "/item/" + this.props.params.itemId);
				}

				this.context.toastr.addAlertSuccess('Item removido com sucesso');
			}
		})
	},

	dispatchDelete(subItemId) {
		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_DELETE_SUBITEM,
			data: subItemId
		});
	},

	refreshCancel () {
		Modal.hide();
	},

	onEdit() {
		this.setState({
			edit: true
		})
	},

	offEdit() {
		this.setState({
			edit: false
		})
	},

	renderDropdown() {
		return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<Link onClick={this.onEdit}>
						<span className="mdi mdi-pencil cursorPointer" title={Messages.get("label.title.editPolicy")}>
							<span id="menu-levels" > Editar Informações </span>
						</span>
					</Link>
				</li>
				<li>
					<Link onClick={this.onDelete}>
					<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deletePolicy")}>
						<span id="menu-levels"> Deletar Item </span>
					</span>
					</Link>
				</li>
			</ul>
		)
	},

	renderBreadcrumb() {
		return (
			<div>
				<span>
					<Link className="fpdi-breadcrumb fpdi-breadcrumbDivisor"
						  to={'/forrisco/plan-risk/' + this.props.params.planRiskId}
						  title={this.context.planRisk.attributes.name}>
						{this.context.planRisk.attributes.name.length > 15 ?
								this.context.planRisk.attributes.name.substring(0, 15) + "..." :
								this.context.planRisk.attributes.name.substring(0, 15)}
					</Link>
					<span className="mdi mdi-chevron-right fpdi-breadcrumbDivisor"/>
				</span>

				{<span>
					<Link className="fpdi-breadcrumb fpdi-breadcrumbDivisor"
						to={'/forrisco/plan-risk/' + this.props.params.planRiskId+"/item/"+this.props.params.itemId}
						title={"item"}>{this.state.itemModel.data.name}
					</Link>
					<span className="mdi mdi-chevron-right fpdi-breadcrumbDivisor"></span>
				</span>
				}

				<span className="fpdi-breadcrumb fpdi-selectedOnBreadcrumb">
					{this.state.itemTitle.length > 15 ?
							this.state.itemTitle.substring(0, 15) + "..." :
							this.state.itemTitle.substring(0, 15)}
				</span>
			</div>
		)
	},




	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge/>;
		}

		return (
			<div>
				{this.renderBreadcrumb()}

				<div className="fpdi-card fpdi-card-full floatLeft">
					<h1>
						{this.state.itemTitle}
						{
							(this.context.roles.ADMIN ||
								_.contains(this.context.permissions, PermissionsTypes.FORRISCO_MANAGE_PLAN_RISK_PERMISSION))
							&&
							<span className="dropdown">
								<a className="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="true"
								title={Messages.get("label.actions")}>

									<span
										className="sr-only">{Messages.getEditable("label.actions", "fpdi-nav-label")}</span>
									<span className="mdi mdi-chevron-down"/>

								</a>

								{this.renderDropdown()}
							</span>
						}
					</h1>
					{
						this.state.edit === false ?

							this.state.field.map((field, key) => {

								if (field.isText === true) {
									return (
										<div className="form-group form-group-sm" key={key}>
											<label className="fpdi-text-label"> {field.fieldName} </label>

											<div>
												<span className="pdi-normal-text" dangerouslySetInnerHTML={{__html: field.fieldContent}}/>
											</div>
										</div>
									)
								} else {
									return (
										<div className="form-group form-group-sm" key={key}>
											<label className="fpdi-text-label"> {field.fieldName} </label>

											<div className="panel panel-default">
												<table className="budget-field-table table">
													<tbody>
													<tr>
														<td className="fdpi-table-cell">
															<a target="_blank" rel="noopener noreferrer"
															   href={field.fileLink}>
																{field.fieldContent}
															</a>
														</td>
													</tr>
													</tbody>
												</table>
											</div>
										</div>
									)
								}
							})
							: <EditPlanRiskSubItem
								itemTitle={this.state.itemTitle}
								fieldsValues={this.state.field}
								onEdit={this.state.onEdit}
								offEdit={this.offEdit}
								itemId={this.props.params.itemId}
								subitemId={this.props.params.subItemId}
								planRiskId={this.props.params.planRiskId}
								pathName={this.props.location.pathname}
							/>

					}
				</div>
			</div>
		)
	}
});
