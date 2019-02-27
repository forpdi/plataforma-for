import React, { Component } from 'react';
import ReactTable from 'react-table';
import "react-table/react-table.css";
import _ from 'underscore';
import { Button } from 'react-bootstrap';

//esse componente recebe uma lista de nomes que sao os labels das colunas das tabelas;
//recebe tambem uma lista de objetos "tableItemsList",
//onde cada objeto eh uma lista com os valores de cada linha da tabela

class GeneralTable extends Component {

  constructor(props) {
    super(props);
    this.state = {
      data: [{
        resume: 'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Blanditiis dolorum, unde repellat quos modi expedita nobis eos doloribus obcaecati iste excepturi tempora quidem quo impedit earum? Doloribus eveniet sunt repudiandae.',
        probability: 'Alta',
        impact: 'Baixo',
        responsible: 'Gustavo Melo',
        dateTime: '11/07/2018 14:10',
        tools: this.renderRowTools()
      }, {
        resume: 'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Blanditiis dolorum, unde repellat quos modi expedita nobis eos doloribus obcaecati iste excepturi tempora quidem quo impedit earum? Doloribus eveniet sunt repudiandae.',
        probability: 'Baixa',
        impact: 'Medio',
        responsible: 'Camila Dias Magalhaes',
        dateTime: '11/07/2018 14:10',
        tools: this.renderRowTools()
      }, {
        resume: 'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Blanditiis dolorum, unde repellat quos modi expedita nobis eos doloribus obcaecati iste excepturi tempora quidem quo impedit earum? Doloribus eveniet sunt repudiandae.',
        probability: 'Media',
        impact: 'Alto',
        responsible: 'Gustavo Melo',
        dateTime: '11/07/2018 14:10',
        tools: this.renderRowTools()
      }, {
        resume: 'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Blanditiis dolorum, unde repellat quos modi expedita nobis eos doloribus obcaecati iste excepturi tempora quidem quo impedit earum? Doloribus eveniet sunt repudiandae.',
        probability: 'Alta',
        impact: 'Baixo',
        responsible: 'Camila Dias Magalhaes',
        dateTime: '11/07/2018 14:10',
        tools: this.renderRowTools()
      }],
    }
    this.insertNewRow = this.insertNewRow.bind(this);
  }

  renderRowTools() {
    return(
      <div className="row-tools-box">
        <button className="row-button-icon">
          <span className="mdi mdi-pencil" />
        </button>
        <button className="row-button-icon">
          <span className="mdi mdi-delete" />
        </button>
      </div>
    );
  }

    insertNewRow(data) {
        const newRow = {resume: this.renderInput(), probability: this.renderInput(), impact: this.renderInput(), responsible: this.renderInput(), dateTime: '', tools: <div className="row-tools-box"><button className="row-button-icon"><span className="mdi mdi-check" /></button><button className="row-button-icon"><span className="mdi mdi-delete" /></button></div>}
        data.unshift(newRow);
        this.setState({ data: data });
    }
    renderInput() {
        return (
            <input type="text"></input>
        );
    }

    render() {

          const columns = [{
            Header: 'Parecer',
            accessor: 'resume',
            minWidth: 200
          }, {
            Header: 'Probabilidade',
            accessor: 'probability',
          }, {
            Header: 'Impacto',
            accessor: 'impact',
          }, {
            Header: 'Responsavel',
            accessor: 'responsible',
          }, {
            Header: 'Data e horario',
            accessor: 'dateTime',
          }, {
            Header: '',
            accessor: 'tools',
            width: 100
          }]
        return(
            <div className='general-table'>
                <div className='table-outter-header'>
                    HISTORICO DE MONITORAMENTOS
                    <Button bsStyle="info" onClick={() => this.insertNewRow(this.state.data)} >Novo</Button>
                </div>
                <ReactTable data={this.state.data} columns={columns} showPagination={false} loading={false} resizable={true} pageSize={this.state.data.length}/>
            </div>
        );
    }

}

export default GeneralTable;