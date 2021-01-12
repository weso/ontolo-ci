import React, {useContext} from 'react';
import {BuildContext} from '../OCIBuild';
import BranchIcon from '../icon/BranchIcon';

function BuildCommit(){

    const {commitName,branchName} = useContext(BuildContext);

    return (
        <div className="dashboard-element-info">
            <span className="commit-name">{commitName}</span>
            <div className="logo-container">
                <div className="logo">
                    <BranchIcon/>
                </div>
                <span className="branch-name">{branchName}</span>
            </div>
        </div>);
}

export default BuildCommit;