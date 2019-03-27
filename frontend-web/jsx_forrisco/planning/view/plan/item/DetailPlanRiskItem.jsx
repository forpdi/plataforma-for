import React from "react";
import {Link} from "react-router";
import _ from "underscore";

import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import EditPlanRiskItem from "forpdi/jsx_forrisco/planning/view/plan/item/EditPlanRiskItem.jsx";
import Messages from "forpdi/jsx/core/util/Messages";
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
			onEdit: false,
		};
	},

	componentDidMount() {
		PlanRiskItemStore.on('detailItem', response => {
			const content = [];
			if (response.data.planRiskItemField.length !== 0) {
				response.data.planRiskItemField.map(field => {
					content.push({
						fieldName: field.name,
						fieldValue: field.name,
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
					isLoading: false,
					tabPath: this.props.location.pathname,
					onEdit: false
				});

			} else {

				this.setState({
					itemTitle: response.data.name,
					field: [],
					isLoading: false,
					tabPath: this.props.location.pathname,
					onEdit: false
				});
			}

			//Construção da Aba Superior
			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname, this.state.itemTitle);
			});
		}, this);
		this.refreshComponent(this.props.params.itemId);

	},

	componentWillReceiveProps(newProps) {
		if (this.props.params.itemId !== newProps.params.itemId) {
			this.refreshComponent(newProps.params.itemId);
		}
	},

	onEdit() {
		this.setState({
			onEdit: true
		})
	},

	offEdit() {
		this.setState({
			onEdit: false
		})
	},

	onDeleteItem() {
		var me = this;
		var msg = "Você tem certeza que deseja excluir esse item?";

		Modal.confirmCustom(() => {
			Modal.hide();
			this.dispatchDelete(this.props.params.itemId);
		}, msg, me.refreshCancel);

		PlanRiskItemStore.on('deletePlanRiskItem', response => {
			if(response.success === true) {
				const hasMinTabsLength = this.context.tabPanel.state.tabs.length <= 1 ? true : false;
				this.context.tabPanel.removeTabByPath(this.props.location.pathname);
				if (hasMinTabsLength) {
					this.context.router.push(`/forrisco/plan-risk/${this.props.params.planRiskId}/item/overview`);
				}

				this.context.toastr.addAlertSuccess('Item removido com sucesso');
			}
		})
	},

	dispatchDelete(itemId) {
		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_DELETE_ITEM,
			data: itemId
		});
	},

	refreshCancel () {
		Modal.hide();
	},

	refreshComponent(itemId) {
		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_DETAIL_ITEM,
			data: {
				id: itemId
			},
		});

		this.setState({
			field: []
		});
	},

	componentWillUnmount() {
		PlanRiskItemStore.off(null, null, this);
	},

	renderBreadcrumb() {

		return (
			<div>
				<span>
					<Link className="fpdi-breadcrumb fpdi-breadcrumbDivisor"
						  to={'/forrisco/plan-risk/' + this.props.params.planRiskId}
						  title={this.context.planRisk.attributes.name}>
						{
							this.context.planRisk.attributes.name.length > 15 ?
								this.context.planRisk.attributes.name.substring(0, 15) + "..." :
								this.context.planRisk.attributes.name.substring(0, 15)
						}
					</Link>
					<span className="mdi mdi-chevron-right fpdi-breadcrumbDivisor"/>
				</span>
				<span className="fpdi-breadcrumb fpdi-selectedOnBreadcrumb">
					{
						this.state.itemTitle.length > 15 ?
							this.state.itemTitle.substring(0, 15) + "..." :
							this.state.itemTitle.substring(0, 15)
					}
				</span>
			</div>
		)
	},

	renderDropdown() {
		return(
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<Link onClick={this.onEdit}>
						<span className="mdi mdi-pencil cursorPointer" title={Messages.get("label.title.editPlanRisk")}>
							<span id="menu-levels"> Editar Informações </span>
						</span>
					</Link>
				</li>
				<li>
					<Link onClick={this.onDeleteItem}>
						<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deletePlanRisk")}>
							<span id="menu-levels"> Deletar Item </span>
						</span>
					</Link>
				</li>
			</ul>
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

									<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
									<span className="mdi mdi-chevron-down" />

								</a>

								{this.renderDropdown()}
							</span>
						}
					</h1>
					{
						this.state.onEdit === false ?

							this.state.field.map((field, key) => {

								if (field.isText === true) {
									return (
										<div className="form-group form-group-sm" key={key}>
											<label className="fpdi-text-label"> {field.fieldName} </label>

											<div key={key}>
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
							}) :
							 <EditPlanRiskItem
								 itemTitle={this.state.itemTitle}
								 fieldsValues={this.state.field}
								 onEdit={this.state.onEdit}
								 offEdit={this.offEdit}
								 itemId={this.props.params.itemId}
								 planRiskId={this.props.params.planRiskId}
								 pathName={this.props.location.pathname}
							 />
					}
				</div>
			</div>
		)
	}
});
