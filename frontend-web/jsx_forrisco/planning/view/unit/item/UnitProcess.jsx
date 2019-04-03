import React from "react";
import _ from "underscore";
import ReactTable from 'react-table';
import { Button } from 'react-bootstrap';
import ReactMultiSelectCheckboxes from 'react-multiselect-checkboxes';
import $ from "jquery";

import ProcessStore from "forpdi/jsx_forrisco/planning/store/Process.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import FileStore from "forpdi/jsx/core/store/File.jsx"
import Messages from "@/core/util/Messages";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx"
import { MED_PAGE_SIZE } from "forpdi/jsx/core/util/const.js"

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		roles: React.PropTypes.object.isRequired,
	},

	getInitialState() {
		return {
			processes: [],
			processesTotal: null,
			unit: null,
			units: [],
			process: null,
			selectedUnits: [],
			fileData: null,
			newRowDisplayed: false,
			updateRowDisplayed: false,
			loading: true,
		}
	},

	componentDidMount() {
		ProcessStore.on('processListedByUnit', response => {
			const processes = response.data;
			_.forEach(processes, (value, idx) => {
				_.assign(
					value,
					{ tools: value.unitCreator.id == this.props.unitId && this.isPermissionedUser() ? this.getTools(idx) : null},
					{
						fileData: {
							fileName: value.file? value.file.name : null,
							fileLink: value.file? value.fileLink : null,
						}
					}
				)
			});

			this.setState({
				processes,
				processesTotal: response.total,
				newRowDisplayed: false,
				updateRowDisplayed: false,
				loading: false,
			});


		}, this);
		ProcessStore.on('processCreated', response => {
			if (response.success) {
				this.getProcesses(this.props.unitId);
			} else {
				this.context.toastr.addAlertError(response.responseJSON.message);
			}
		}, this);
		ProcessStore.on('processDeleted', response => {
			if (response.success) {
				this.getProcesses(this.props.unitId);
			} else {
				this.context.toastr.addAlertError(response.responseJSON.message);
			}
		}, this);
		ProcessStore.on('processUpdated', response => {
			if (response.success) {
				this.getProcesses(this.props.unitId);
			} else {
				this.context.toastr.addAlertError(response.responseJSON.message);
			}
		}, this);
		UnitStore.on('allunitsbyplan', response => {


			const unit = _.find(response.data, unit => (
				unit.id === parseInt(this.props.unitId)
			));

			const units= _.filter(response.data, unit =>{
				return unit.id != this.props.unitId
			})

			this.setState({
				units: _.map(units, unit => ({
					label: unit.name,
					value: unit.id,
					data: unit,
				})),
				unit,
			});
		}, this);
		this.refreshComponent(this.props.unitId, this.props.planRiskId);
	},

	componentWillReceiveProps(newProps) {
		if(newProps.unitId !== this.props.unitId) {
			this.refreshComponent(newProps.unitId, newProps.planRiskId)
		}
	},

	componentWillUnmount() {
		ProcessStore.off(null, null, this);
		UnitStore.off(null, null, this);
	},

	isPermissionedUser() {
		return (this.context.roles.MANAGER ||
			_.contains(this.context.permissions, PermissionsTypes.FORRISCO_MANAGE_PROCESS_PERMISSION)
		);
	},

	refreshComponent(unitId, planRiskId) {
		this.getProcesses(unitId);
		UnitStore.dispatch({
			action: UnitStore.ACTION_FIND_ALL_BY_PLAN,
			data: planRiskId,
		});
	},

	getProcesses(unitId, page = 1, pageSize = MED_PAGE_SIZE) {
		ProcessStore.dispatch({
			action: ProcessStore.ACTION_LIST_BY_UNIT,
			data: {
				id: unitId,
				page,
				pageSize,
			},
		});
		this.refs['unit-pagination'] && this.refs['unit-pagination'].setState({
			page,
			pageSize,
		});
	},

	pageChange(page, pageSize) {
		this.getProcesses(this.props.unitId, page, pageSize);
	},

	insertNewRow() {
		if (this.state.newRowDisplayed || this.state.updateRowDisplayed) {
			return;
		}
	    const newRow = {
			name: (
				<VerticalInput
					className="padding7"
					fieldDef={{
						name: "new-process-name",
						type: "textarea",
						rows: 3,
						onChange: this.nameChangeHandler,
						required: true,
					}}
				/>
			),
			objective: (
				<VerticalInput
					className="padding7"
					fieldDef={{
						name: "new-process-objective",
						type: "textarea",
						rows: 3,
						onChange: this.objectiveChangeHandler,
						required: true,
					}}
				/>
			),
			unitCreator: {
				name: this.state.unit.name,
			},
			relatedUnits: [
				{
					name: (
						<div className="unit-multi-select">
							<ReactMultiSelectCheckboxes
								className="unit-mult-select"
								placeholderButtonLabel="Selecione uma ou mais"
								options={this.state.units}
							/>
						</div>
					),
				}
			],
			fileData: (
				<div className="fpdi-tabs-nav fpdi-nav-hide-btn">
					<a onClick={this.fileLinkChangeHandler}>
						<span className="fpdi-nav-label" id="process-file-upload">
							{/* <span style={{ 'display': 'inline', 'color': 'red' }}>*</span> */}
							{Messages.getEditable("label.attachFiles", "fpdi-nav-label")}
						</span>
					</a>
				</div>
			),
			tools: (
				<div className="row-tools-box">
					<button className="btn btn-sm btn-success mdi mdi-check" title="Salvar" onClick={this.newProcess}>
					</button>
					<button
						className="btn btn-sm btn-danger mdi mdi-close "
						title="Cancelar"
						onClick={() =>
							this.setState({
								processes: this.state.processes.slice(1),
								newRowDisplayed: false,
							})
						}
					/>
				</div>
			),
		}
		const { processes } = this.state;
		processes.unshift(newRow);
        this.setState({
			selectedUnits: [],
			process: {},
			newRowDisplayed: true,
		});
    },

	enableUpdateMode(idx) {

		if (this.state.newRowDisplayed || this.state.updateRowDisplayed) {
			return;
		}
		const { processes } = this.state;
		const process = processes[idx];
		const selectedUnits = _.map(process.relatedUnits, unit => ({
			label: unit.name,
			value: unit.id,
			data: unit
		}));

	    processes[idx] = {
			name: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-process-name",
					type: "textarea",
					rows: 3,
					value: process.name,
					onChange: this.nameChangeHandler,
					required: true,
				}}
			/>,
			objective: <VerticalInput
				className="padding7"
				fieldDef={{
					name: "new-process-objective",
					type: "textarea",
					rows: 3,
					objective: process.objective,
					value: process.objective,
					onChange: this.objectiveChangeHandler,
					required: true,
				}}
			/>,
			relatedUnits: [
				{
					name: <div className="unit-multi-select">
						<ReactMultiSelectCheckboxes
							className="unit-mult-select"
							placeholderButtonLabel="Selecione uma ou mais"
							options={this.state.units}
							defaultValue={selectedUnits}
							onChange={this.unitChangeHandler}
						/>
					</div>
				}
			],
			unitCreator: {
				name: process.unitCreator.name,
			},
			fileData: <div className="fpdi-tabs-nav fpdi-nav-hide-btn">
				<a onClick={this.fileLinkChangeHandler}>
					<span className="fpdi-nav-label" id="process-file-upload">
						{process.fileData.fileName ? process.fileData.fileName : Messages.get("label.attachFiles")}
					</span>
				</a>
			</div>,
			tools: <div className="row-tools-box">
				<span
					className="mdi mdi-check btn btn-sm btn-success"
					title="Salvar"
					onClick={this.updateProcess}
				/>
				<span
					className="mdi mdi-close btn btn-sm btn-danger"
					title="Cancelar"
					onClick={() => {
						const { processes } = this.state;
						processes[idx] = process;
						this.setState({
							processes,
							updateRowDisplayed: false,
						})
					}}
				/>
			</div>,
		}
        this.setState({
			selectedUnits,
			processes,
			process,
			updateRowDisplayed: true,
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

	unitChangeHandler(values) {
		this.state.selectedUnits=values
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
		const url = `${FileStore.url}/uploadlocal`;

		const onSuccess = (resp) => {
			Modal.hide();
			const fileData = {
				name: Modal.fileName,
				id: resp.data.id,
				description: Modal.fileName,
				fileLink: `${BACKEND_URL}file/${resp.data.id}`,
				levelInstance: {
					id: me.props.levelInstanceId
				}
			};

			me.setState({
				fileData,
				process: {
					...this.state.process,
					fileLink: fileData.fileLink,
					file: {name: fileData.name, id:  fileData.id}
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

		Modal.uploadFile(
			title,
			msg,
			url,
			formatsRegex,
			formatsBlocked,
			onSuccess,
			onFailure,
			formats,
			maxSize,
		);
	},

	newProcess() {
		const { process } = this.state;

		if (!process.name || !process.objective){ // || !process.file) {
			this.context.toastr.addAlertError("Para confirmar a ação preencha todos os campos obrigatórios.");
		} else {
			ProcessStore.dispatch({
				action: ProcessStore.ACTION_CREATE,
				data: {
					process: _.assign(
						process,
						{
							relatedUnits: _.map(this.state.selectedUnits, value => value.data),
							unit: { id: this.props.unitId }
						}
					),
				},
			});
		}
	},

	deleteProcess(idx) {
		const process = this.state.processes[idx];

		if(this.props.unitId !=process.unitCreator.id){
			this.context.toastr.addAlertError(Messages.get("notification.process.deleteError"));
			return
		}

		ProcessStore.dispatch({
			action: ProcessStore.ACTION_DELETE,
			data: {
				processId: process.id,
			},
		});
	},

	updateProcess() {
		const { process } = this.state;

		if (!process.name || !process.objective){ // || !process.file) {
			this.context.toastr.addAlertError(Messages.get("label.msg.errorsForm"));
		} else {
			ProcessStore.dispatch({
				action: ProcessStore.ACTION_UPDATE,
				data: {
					process: {
						...this.state.process,
						relatedUnits: _.map(this.state.selectedUnits, value => value.data),
						unit: { id: this.props.unitId },
						tools: undefined,
						fileData: undefined,
					},
				},
			});
		}
	},

	getTools(idx) {
		return (
			<div className="row-tools-box">
				<button
					className="row-button-icon"
					onClick={() => this.enableUpdateMode(idx)}
				>
					<span className="mdi mdi-pencil" />
				</button>
				<button
					className="row-button-icon"
					onClick={() => this.deleteProcess(idx)}
				>
					<span className="mdi mdi-delete" />
				</button>
			</div>
		);
	},

	render() {

		if (this.state.loading === true) {
			return <LoadingGauge/>;
		}

		const columns = [{
			Header: 'Processo',
			accessor: 'name',
		},
		{
			Header: 'Objetivo',
			accessor: 'objective',
		},
		{
			Header: 'Unidade responsável',
			accessor: 'unitCreator.name',
		},
		{
			Header: 'Unidade(s) relacionada(s)',
			Cell: props => <span className=''>{
				props.original.relatedUnits.map( (unit, idx)=>{
					if(this.props.unitId != unit.id){
						return <p>{unit.name}</p>
					}

				})
			}</span>
		},
		{
			Header: 'Anexo',
			accessor: 'fileData',
			Cell: props => {
				return props.value.fileName !== undefined
					?
					<a target="_blank" rel="noopener noreferrer" href={props.value.fileLink}>
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

		if(this.state.updateRowDisplayed){
			columns[2].Cell=null
		}

		return (
			<div className="general-table">
				<div className='table-outter-header'>
    			{this.props.isSubunit ? 'PROCESSOS DA SUBUNIDADE' : 'PROCESSOS DA UNIDADE'}
					{
						this.isPermissionedUser() && <Button bsStyle="info" onClick={this.insertNewRow}>Novo</Button>
					}
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
				<TablePagination
					ref="unit-pagination"
					defaultPageSize={MED_PAGE_SIZE}
					total={this.state.processesTotal}
					onChangePage={this.pageChange}
					tableName={"unit-process-table"}
				/>
			</div>
		)
	}
});
