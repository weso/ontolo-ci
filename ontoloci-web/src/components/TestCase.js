import React , {useState} from 'react';
import { Collapse, Button, CardBody, Card } from 'reactstrap';
import { LazyLog } from 'react-lazylog';
import '../css/testcase.css'
import 'bootstrap/dist/css/bootstrap.min.css';

function TestCase(props){

    const [isOpen, setIsOpen] = useState(false);

    const toggle = () => setIsOpen(!isOpen);

    const getPassClass = ()=>{
        if(props.pass)
            return 'passed';
        else
            return 'failed'
    }


    return (
    <div className="test-element-container">
        <div className="test-element">
            <div className="test-element-status">
                <svg xmlns="http://www.w3.org/2000/svg" className={"icon-check-"+getPassClass()} height="24" viewBox="0 0 24 24" width="24"><path d="M0 0h24v24H0z" fill="none"/><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>
            </div>
    
            <div className="test-element-name">
                <h3>TEST NAME</h3>
                <a>{props.testName}</a>
            </div>

            <div className="test-element-data">
                <h3>DATA</h3>
                <a>{props.data}</a>
            </div>

            <div className="test-element-shape">
                <h3>SHAPE</h3>
                <a>{props.shape}</a>
            </div>

            <div className="test-element-validation-result">
                <h3>STATUS</h3>
                <a>{props.status}</a>
            </div>

            <div className="dashboard-element-info github-metadata">
                <div className="logo-container">
                    <div className="logo">
                    <svg xmlns="http://www.w3.org/2000/svg" className="icon icon-color" height="24" viewBox="0 0 24 24" width="24"><path d="M0 0h24v24H0z" fill="none"/><path d="M15 1H9v2h6V1zm-4 13h2V8h-2v6zm8.03-6.61l1.42-1.42c-.43-.51-.9-.99-1.41-1.41l-1.42 1.42C16.07 4.74 14.12 4 12 4c-4.97 0-9 4.03-9 9s4.02 9 9 9 9-4.03 9-9c0-2.12-.74-4.07-1.97-5.61zM12 20c-3.87 0-7-3.13-7-7s3.13-7 7-7 7 3.13 7 7-3.13 7-7 7z"/></svg>
                    </div>
                    <a className="pr-id">{props.executionTime}</a>
                </div>
                <div className="logo-container">
                    <div className="logo">
                    <svg xmlns="http://www.w3.org/2000/svg" className="icon icon-color" viewBox="0 0 24 24" fill="black" width="18px" height="18px"><path d="M0 0h24v24H0z" fill="none"/><path d="M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11zM7 10h5v5H7z"/></svg>
                    </div>
                    <a className="commit-id">{props.date}</a>
                </div>
            </div>

            
            <svg onClick={toggle} className="expand-more-btn" xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 0 24 24" width="24"><path d="M0 0h24v24H0z" fill="none"/><path d="M16.59 8.59L12 13.17 7.41 8.59 6 10l6 6 6-6z"/></svg>

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

export default TestCase;

/**
 * <code>
{
    "ResultShapeMap: [ \n"+
    "{\n"+
    "'node' : '<http://purl.org/hercules/asio/modules#ES_SUBJECT_AREA_LEVEL_2_PIN>',\n"+
    "'shape' : '<http://purl.org/hercules/asio/core#ResearchFieldShape>',\n"+
    "'status' : 'conformant',\n"+
    "'appInfo' : 'Shaclex',\n"+
    "'reason' : '<http://purl.org/hercules/asio/modules#ES_SUBJECT_AREA_LEVEL_2_PIN> passes OR \n"+
    "}\n"+
    "{\n"+
    "'node' : '<http://purl.org/hercules/asio/modules#ES_SUBJECT_AREA_LEVEL_2_PIN>',\n"+
    " 'shape' : '<http://purl.org/hercules/asio/core#ESSubjectAreas3rdListShape>',\n"+
    " 'status' : 'conformant',\n"+
    " 'appInfo' : 'Shaclex',\n"+
    " 'reason' : ''\n"+
    "}\n"
}
</code>
 */