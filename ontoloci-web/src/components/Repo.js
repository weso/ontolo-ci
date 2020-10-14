import React from 'react';
import { Link } from "react-router-dom";

function Repo(props){

    const getPassClass = ()=>{
        if(props.pass)
            return 'passed';
        else
            return 'failed'
    }


    return (
    <div className="dashboard-element">
        <div className={"dashboard-element-logo dashboard-element-logo-"+getPassClass()}
        title="Public repository">
            <svg    xmlns="http://www.w3.org/2000/svg" 
                    viewBox="0 0 16 16" 
                    className="icon icon-dashboard-element public" 
                    alt="Public repository">
                        <g fill="none" 
                          stroke="#9ea3a8" 
                          stroke-linecap="round"
                          stroke-linejoin="round" 
                          stroke-miterlimit="10">
                              <path d="M7.089 13.343h6.434L13.524 1H2.176v12.343H4.46M2.176 10.543h11.348"></path><path d="M4.46 12.239v2.756l1.394-1.285 1.235 1.236v-2.707zM4.199 1v9.543M5.635 8.737h.63M5.635 6.782h.63M5.635 4.828h.63M5.635 2.873h.63"/>
                    </g>
            </svg>
        </div>
        <div className="dashboard-element-status">
            <svg xmlns="http://www.w3.org/2000/svg" className={"icon-check-"+getPassClass()} height="24" viewBox="0 0 24 24" width="24"><path d="M0 0h24v24H0z" fill="none"/><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>
        </div>
        <div className="dashboard-element-info">
            <a className={getPassClass()}>{props.owner}</a>
            <Link to={`/tests/${props.repo}`} className={getPassClass()}>
               {props.repo}
            </Link>
        </div>

        <div className="dashboard-element-info">
            <a className="commit-name">{props.commitName}</a>
            <div className="logo-container">
                <div className="logo">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 17 17" className="icon"><circle cx="4.94" cy="2.83" r="1.83" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><circle cx="11.78" cy="5.15" r="1.83" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><circle cx="4.98" cy="14.17" r="1.83" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><path d="M11.78 6.99s.09 2.68-1.9 3.38c-1.76.62-2.92-.04-4.93 1.97V4.66" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></path></svg>
                </div>
                <a className="branch-name">{props.branchName}</a>
            </div>
        </div>

        <div className="dashboard-element-info github-metadata">
            <div className="logo-container">
                <div className="logo">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 17 17" className="icon"><circle cx="8.51" cy="8.5" r="3.5" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><path d="M16.5 8.5h-4.49m-7 0H.5" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></path></svg>
                </div>
                <a href="#" className={getPassClass()+" pr-id"}>{props.prId}</a>
            </div>
            <div className="logo-container">
                <div className="logo">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 17 17" className="icon"><circle cx="8.51" cy="8.5" r="3.5" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><path d="M16.5 8.5h-4.49m-7 0H.5" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></path></svg>
                </div>
                <a className="commit-id">{props.commitId}</a>
            </div>
        </div>

        <div className="dashboard-element-info ci-metadata">
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
        
   
        
    </div>);
}

export default Repo;