import React,{useState}  from 'react';
import {Collapse} from 'reactstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import JSONPretty from 'react-json-pretty';
import Button from 'react-bootstrap/Button';


function ResultsTable(props){

    const {showResults,status,expected,produced,produced_output} = props;
    const [showSM,setShowSM] = useState(false);

    const toggle = () => setShowSM(!showSM);

    const getTableContent = function(){
        console.log({expected:expected})
        let parsedExpected = JSON.parse(expected);
        let parsedProduced = JSON.parse(produced);
        //console.log({parsedProduced:parsedProduced,parsedExpected:parsedExpected})
        return Object.keys(parsedProduced).map((p,index)=>{
            let result = parsedProduced;
            let expectedIndex =Object.keys(parsedExpected)[index];
            let rowStatus = result[p].status == parsedExpected[expectedIndex].status ? "success" : "failure";
            return (<tr className={rowStatus} key={result[p].node}>
                <td>{result[p].node}</td>
                <td>{result[p].shape}</td>
                <td>{result[p].status}</td>
                <td>{parsedExpected[expectedIndex].status}</td>
        </tr>)
        })
    }
   

    return (
    
    <Collapse isOpen={showResults} className="test-results">
        <div className="test-element-log">
            <table className="results-table">
                <thead>
                    <tr>
                        <th className={status} scope="col">Data Node</th>
                        <th className={status} scope="col">Shape</th>
                        <th className={status} scope="col">Status</th>
                        <th className={status} scope="col">Expected Status</th>
                    </tr>
                </thead>
                <tbody>
                    {    
                        getTableContent()
                    }
                </tbody>
            </table>
        </div>
        <Button  variant="link" onClick={toggle}>Shape Map</Button>
        <Collapse isOpen={showSM}>
            <JSONPretty id="json-pretty" data={produced_output}></JSONPretty>
        </Collapse>
    </Collapse>) 
}

export default ResultsTable;

/*<LazyLog enableSearch url="http://localhost:3000/examplelog.log" />*/

/**
 * import { Button,Accordion  } from 'react-bootstrap';
import { LazyLog } from 'react-lazylog';
 */

