import React from "react";
import _ from "underscore";
import ReactTable from 'react-table';
import { Button } from 'react-bootstrap';
import ReactMultiSelectCheckboxes from 'react-multiselect-checkboxes';
import { Link } from "react-router";
import $ from "jquery";

import ProcessStore from "forpdi/jsx_forrisco/planning/store/Process.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import FileStore from "forpdi/jsx/core/store/File.jsx"
import Messages from "@/core/util/Messages";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
	},

	getInitialState() {
		return {
			processes: [],
			units: [],
			process: null,
			selectedUnits: [],
			fileData: null,
			newRowDisplayed: false,
			loading: true,
		}
	},

	componentDidMount() {
		ProcessStore.on('processListed', response => {
			const filteredProcesses = _.map(response.data, process => ({
					...process,
					relatedUnits: _.filter(process.relatedUnits, unit => (
						unit.id !== parseInt(this.props.unitId)
					)),
				}
			));;
			this.setState({
				processes: _.map(filteredProcesses, value =>
					_.assign(
						value,
						{ tools: this.getTools() },
						{
							fileData: {
								fileName: value.fileName,
								fileLink: value.fileLink,
							}
						}
					)
				),
				newRowDisplayed: false,
				loading: false,
			});
		});
		ProcessStore.on('processCreated', response => {
			if (response.success) {
				ProcessStore.dispatch({
					action: ProcessStore.ACTION_LIST,
					data: {
						id: this.props.unitId,
					},
				});
			} else {
				this.context.toastr.addAlertError(response.responseJSON.message);
			}
		});
		UnitStore.on('unitbyplan', response => {
			const filteredUnits = _.filter(response.data, unit => (
				unit.id !== parseInt(this.props.unitId)
			));
			this.setState({
				units: _.map(filteredUnits, unit => ({
					label: unit.name,
					value: unit,
				})),
			});
		});
		ProcessStore.dispatch({
			action: ProcessStore.ACTION_LIST,
			data: {
				id: this.props.unitId,
			},
		});
		UnitStore.dispatch({
			action: UnitStore.ACTION_FIND_BY_PLAN,
			data: {
				planId: this.props.planRiskId,
			},
		});
	},

	componentWillUnmount() {
		UnitStore.off(null, null, this);
		ProcessStore.off(null, null, this);
	},

	insertNewRow() {
		if (this.state.newRowDisplayed || this.state.updateRowDisplayed) {
			return;
		}
        const newRow = {
			name: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-process-name",
					type: "text",
					placeholder: "Nome do processo",
					onChange: this.nameChangeHandler
				}}
			/>,
			objective: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-process-objective",
					type: "text",
					placeholder: "Nome do objetivo",
					onChange: this.objectiveChangeHandler
				}}
			/>,
			relatedUnits: [
				{
					name: <div className="unit-multi-select">
						<ReactMultiSelectCheckboxes
							className="unit-mult-select"
							placeholderButtonLabel="Selecione uma ou mais"
							options={this.state.units}
							onChange={this.unitChangeHandler}
						/>
					</div>
				}
			],
			fileData: <div className="fpdi-tabs-nav fpdi-nav-hide-btn">
				<a onClick={this.fileLinkChangeHandler}>
					<span className="fpdi-nav-label" id="process-file-upload">
						{Messages.getEditable("label.attachFiles", "fpdi-nav-label")}
					</span>
				</a>
			</div>,
			tools: <div className="row-tools-box">
				<span
					className="mdi mdi-check btn btn-sm btn-success"
					title="Salvar"
					onClick={this.newProcess}
				/>
				<span
					className="mdi mdi-close btn btn-sm btn-danger"
					title="Cancelar"
					onClick={() =>
						this.setState({
							processes: this.state.processes.slice(1),
							newRowDisplayed: false,
						})
					}
				/>
			</div>,
		}
		const { processes } = this.state;
		processes.unshift(newRow);
        this.setState({
			selectedUnits: [],
			process: {},
			newRowDisplayed: true,
		});
    },

	nameChangeHandler(e) {
		this.setState({
			process: {
				...this.state.process,
				name: e.target.value,
			}
		})
	},

	objectiveChangeHandler(e) {
		this.setState({
			process: {
				...this.state.process,
				objective: e.target.value,
			}
		})
	},

	unitChangeHandler(value) {
		this.setState({
			selectedUnits: value,
		})
	},

	fileLinkChangeHandler() {
		const me = this;
		const title = Messages.get("label.insertAttachment");
		const msg = (
			<div>
				<p>
					{Messages.get("label.selectFile")}
				</p>
			</div>
		);
		var url = FileStore.url + "/uploadlocal";

		const onSuccess = (resp) => {
			Modal.hide();
			var file = {
				name: Modal.fileName,
				id: resp.data.id,
				description: Modal.fileName,
				fileLink: `${BACKEND_URL}file/${resp.data.id}`,
				levelInstance: {
					id: me.props.levelInstanceId
				}
			};
			me.setState({
				fileData: file,
				process: {
					...this.state.process,
					fileLink: file.fileLink,
					fileName: Modal.fileName,
				},
			});
			$('#process-file-upload').text(Modal.fileName);
		};

		const onFailure = (resp) => {
			Modal.hide();
			this.context.toastr.addAlertError(resp.message);
		};

		const formatsBlocked = "(exe*)";
		const maxSize = 2;
		const formats = "Imagens: gif, jpg, jpeg, jpg2, jp2, bmp, tiff, png, ai, psd, svg, svgz, Documentos: pdf\n";
		const formatsRegex = "gif|jpg|jpeg|jpg2|jp2|bmp|tiff|png|ai|psd|svg|svgz|pdf";

		Modal.uploadFile(title, msg, url, formatsRegex, formatsBlocked, onSuccess, onFailure, formats, maxSize);
	},

	newProcess() {
		ProcessStore.dispatch({
			action: ProcessStore.ACTION_CREATE,
			data: {
				process: _.assign(
					this.state.process,
					{
						relatedUnits: _.map(this.state.selectedUnits, unit => unit.value),
						unit: { id: this.props.unitId }
					}
				),
			},
		});
	},

	getTools(id, idx) {
		return (
			<div className="row-tools-box">
				<button
					className="row-button-icon"
					onClick={null}
				>
					<span className="mdi mdi-pencil" />
				</button>
				<button
					className="row-button-icon"
					onClick={null}
				>
					<span className="mdi mdi-delete" />
				</button>
			</div>
		);
	},

	renderDropdown() {
		return(
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<Link onClick={null}>
						<span className="mdi mdi-pencil cursorPointer" title="item 1">
							<span id="menu-levels">
								Editar Item
							</span>
						</span>
					</Link>
				</li>
				<li>
					<Link onClick={null}>
					<span className="mdi mdi-delete cursorPointer" title="item 2">
						<span id="menu-levels"> Deletar Item </span>
					</span>
					</Link>
				</li>
			</ul>
		)
	},

	render() {
		if (this.state.loading === true) {
			return <LoadingGauge/>;
		}
		const columns = [{
			Header: 'Processo',
			accessor: 'name',
		}, {
			Header: 'Objetivo',
			accessor: 'objective',
		}, {
			Header: 'Unidade relacionada',
			accessor: 'relatedUnits[0].name',
		},
		{
			Header: 'Anexo',
			accessor: 'fileData',
			Cell: props => {
				return props.value.fileName !== undefined
					? <a target="_blank" rel="noopener noreferrer" href={props.value.fileLink}>
						{props.value.fileName}
					</a>
					: props.value
			}
		},
		{
			Header: '',
			accessor: 'tools',
			sortable: false,
			width: 100
		}];

		return (
			<div className="general-table">
				<div className='table-outter-header'>
                    HISTÓRICO DE INCIDENTES
                    <Button bsStyle="info" onClick={this.insertNewRow} >Novo</Button>
                </div>
				<ReactTable
					data={this.state.processes}
					columns={columns}
					showPagination={false}
					loading={false}
					resizable={true}
					pageSize={this.state.processes.length}
					NoDataComponent={() =>
						<div className="marginLeft10">
							Nenhum processo cadastrado
						</div>
					}
				/>
			</div>
		)
	}
});
