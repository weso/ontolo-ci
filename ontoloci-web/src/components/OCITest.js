import React , {useState} from 'react';
import '../css/testcase.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import ClockIcon from './icon/CalendarIcon';
import ResultsTable from './test/ResultsTable'


function OCITest(props){

    const { status,
            testName,
            expected,
            produced,
            expected_output,
            produced_output,
            executionTime} = props;

    const [showResults, setShowResults] = useState(false);

    const toggle = () => setShowResults(!showResults);

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
    <ResultsTable   status={status}
                    showResults={showResults}
                    expected={expected}
                    produced={produced}
                    produced_output={produced_output}/>
</div>) 
}

export default OCITest;
