import React , {useState} from 'react';
import { Collapse} from 'reactstrap';
import { LazyLog } from 'react-lazylog';
import '../css/testcase.css'
import 'bootstrap/dist/css/bootstrap.min.css';

function OCITest(props){

    const [isOpen, setIsOpen] = useState(false);

    const toggle = () => setIsOpen(!isOpen);

    return (
    <div className="test-element-container">
        <div className={"test-element" + " test-element-"+props.status}>
            <div className="test-element-field test-element-name">
                <h3 className="field-title">TEST NAME</h3>
                <span>{ ((props.testName).length > 30) ? 
                (((props.testName).substring(0,30-3)) + '...') : 
                props.testName }
                </span>
            </div>

            <div className="test-element-field test-element-data">
                <h3 className="field-title">DATA NODE</h3>
                <span>{props.dataNode}</span>
            </div>

            <div className="test-element-field test-element-shape">
                <h3 className="field-title">SHAPE</h3>
                <span>{props.shape}</span>
            </div>

            <div className="test-element-field test-element-validation-result">
                <h3 className="field-title">STATUS</h3>
                <span>{props.validationStatus}</span>
            </div>

            <div className="test-element-field test-element-validation-result">
                <h3 className="field-title">EXPECTED STATUS</h3>
                <span>{props.expectedValidationStatus}</span>
            </div>

            <div className="test-element-field ">
                <div className="dashboard-element-info github-metadata">
                    <div className="logo-container">
                        <div className="logo">
                            <svg xmlns="http://www.w3.org/2000/svg" className="icon icon-color" height="24" viewBox="0 0 24 24" width="24"><path d="M0 0h24v24H0z" fill="none"/><path d="M15 1H9v2h6V1zm-4 13h2V8h-2v6zm8.03-6.61l1.42-1.42c-.43-.51-.9-.99-1.41-1.41l-1.42 1.42C16.07 4.74 14.12 4 12 4c-4.97 0-9 4.03-9 9s4.02 9 9 9 9-4.03 9-9c0-2.12-.74-4.07-1.97-5.61zM12 20c-3.87 0-7-3.13-7-7s3.13-7 7-7 7 3.13 7 7-3.13 7-7 7z"/></svg>
                        </div>
                        <span >{props.executionTime}</span>
                    </div>

                    <svg onClick={toggle} className="expand-more-btn" xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 0 24 24" width="24"><path d="M0 0h24v24H0z" fill="none"/><path d="M16.59 8.59L12 13.17 7.41 8.59 6 10l6 6 6-6z"/></svg>
                </div>
            </div>

            <div>
        </div>
    </div>
    <Collapse isOpen={isOpen} >
        <div className="test-element-log">
            <LazyLog enableSearch url="http://localhost:3000/examplelog.log" />
        </div>
    </Collapse>
</div>) 
}

export default OCITest;
