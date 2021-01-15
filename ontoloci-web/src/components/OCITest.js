import React , {useState} from 'react';
import { Collapse} from 'reactstrap';
import '../css/testcase.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import ClockIcon from './icon/CalendarIcon';

function OCITest(props){

    const {
        status,
        testName,
        expectedSM_in,
        producedSM_in,
        expectedSM_out,
        producedSM_out,
        executionTime} = props;

    const [isOpen, setIsOpen] = useState(false);


    const getTableContent = function(){
        let pasrsedProduced = JSON.parse(producedSM_out);
        console.log(pasrsedProduced)
        return Object.keys(pasrsedProduced).map((p,index)=>{
            let result = pasrsedProduced[p];
            return (<tr className={status}>
                <td>{result.node}</td>
                <td>{result.shape}</td>
                <td>{result.status}</td>
                <td>confortmant</td>
        </tr>)
        })
    }
   
    

    const toggle = () => setIsOpen(!isOpen);

    return (
    <div className="test-element-container">
        <div className={"test-element" + " test-element-"+status}>
            <div className="test-element-field test-element-name">
                <h3 className="field-title">TEST NAME</h3>
                <span>{testName.charAt(0).toUpperCase() + testName.slice(1)}
                </span>
            </div>

            <div className="test-element-field ">
                <div className="dashboard-element-info github-metadata">
                    <div className="logo-container">
                        <div className="logo">
                            <ClockIcon/>
                        </div>
                        <span >{executionTime}</span>
                    </div>

                    <svg onClick={toggle} className="expand-more-btn" xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 0 24 24" width="24"><path d="M0 0h24v24H0z" fill="none"/><path d="M16.59 8.59L12 13.17 7.41 8.59 6 10l6 6 6-6z"/></svg>
                </div>
            </div>

            <div>
        </div>
    </div>
    <Collapse isOpen={isOpen} >
        <div className="test-element-log">
        <table id="customers">
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
    </Collapse>
</div>) 
}

export default OCITest;

/*<LazyLog enableSearch url="http://localhost:3000/examplelog.log" />*/

/**
 * import { Button,Accordion  } from 'react-bootstrap';
import { LazyLog } from 'react-lazylog';
 */

/**
 *  <span>{ ((testName).length > 30) ? 
                (((testName).substring(0,30-3)) + '...') : 
                testName }
                </span>
 */

 /**
  *  <div className="test-element-field test-element-data">
                <h3 className="field-title">DATA NODE</h3>
                <span>{dataNode}</span>
            </div>

            <div className="test-element-field test-element-shape">
                <h3 className="field-title">SHAPE</h3>
                <span>{shape}</span>
            </div>

            <div className="test-element-field test-element-validation-result">
                <h3 className="field-title">STATUS</h3>
                <span>{validationStatus}</span>
            </div>

            <div className="test-element-field test-element-validation-result">
                <h3 className="field-title">EXPECTED STATUS</h3>
                <span>{expectedValidationStatus}</span>
            </div>

  */