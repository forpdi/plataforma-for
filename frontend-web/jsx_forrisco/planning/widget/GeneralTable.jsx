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
        name: 'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Blanditiis dolorum, unde repellat quos modi expedita nobis eos doloribus obcaecati iste excepturi tempora quidem quo impedit earum? Doloribus eveniet sunt repudiandae.',
        age: 26,
      }, {
        name: 'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Blanditiis dolorum, unde repellat quos modi expedita nobis eos doloribus obcaecati iste excepturi tempora quidem quo impedit earum? Doloribus eveniet sunt repudiandae.',
        age: 20,  
      }, {
        name: 'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Blanditiis dolorum, unde repellat quos modi expedita nobis eos doloribus obcaecati iste excepturi tempora quidem quo impedit earum? Doloribus eveniet sunt repudiandae.',
        age: 22,
      }, {
        name: 'Lorem ipsum dolor, sit amet consectetur adipisicing elit. Blanditiis dolorum, unde repellat quos modi expedita nobis eos doloribus obcaecati iste excepturi tempora quidem quo impedit earum? Doloribus eveniet sunt repudiandae.',
        age: 36,
      }],
    }
    this.insertNewRow = this.insertNewRow.bind(this);
  }

    insertNewRow(data) {
        const newRow = {name: this.renderInput(), age: this.renderInput()}
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
            Header: 'Name',
            accessor: 'name', // String-based value accessors!
          }, {
            Header: 'Age',
            accessor: 'age',
            // Cell: props => <span className='number'>{props.value}</span> // Custom cell components!
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