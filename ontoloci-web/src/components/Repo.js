import React from 'react';

function Repo(props){

    const getPassClass = ()=>{
        if(props.pass)
            return 'passed';
        else
            return 'failed'
    }


    return (
    <div className="repo">
        <div className={"repo-logo repo-logo-"+getPassClass()}>
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" className="icon icon-repo public" alt="Public repository"><g fill="none" stroke="#9ea3a8" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"><path d="M7.089 13.343h6.434L13.524 1H2.176v12.343H4.46M2.176 10.543h11.348"></path><path d="M4.46 12.239v2.756l1.394-1.285 1.235 1.236v-2.707zM4.199 1v9.543M5.635 8.737h.63M5.635 6.782h.63M5.635 4.828h.63M5.635 2.873h.63"></path></g></svg>
        </div>
        <div className="repo-status">
            <svg xmlns="http://www.w3.org/2000/svg" className={"icon-check-"+getPassClass()} height="24" viewBox="0 0 24 24" width="24"><path d="M0 0h24v24H0z" fill="none"/><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>
        </div>
        <div className="repo-info">
            <a className={"repo-"+getPassClass()}>{props.owner}</a>
            <a className={"repo-"+getPassClass()}>{props.repo}</a>
        </div>

        <div className="repo-info">

            <a className="commit-name">{props.commitName}</a>
            <div className="branch-container">
                <div className="branch-logo">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 17 17" className="icon"><circle cx="4.94" cy="2.83" r="1.83" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><circle cx="11.78" cy="5.15" r="1.83" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><circle cx="4.98" cy="14.17" r="1.83" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></circle><path d="M11.78 6.99s.09 2.68-1.9 3.38c-1.76.62-2.92-.04-4.93 1.97V4.66" fill="none" stroke="#9d9d9d" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10"></path></svg>
                </div>
                <a className="branch-name">{props.branchName}</a>
            </div>
        </div>

        <div className="repo-info github-metadata">
        
            <a className="commit-name">{props.commitName}</a>
            <a className="branch-name">{props.branchName}</a>
        </div>

        <div className="repo-info ci-metadata">
            <a className="commit-name">{props.commitName}</a>
            <a className="branch-name">{props.branchName}</a>
        </div>
        
    </div>);
}

export default Repo;