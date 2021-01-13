import React, {useContext} from 'react';
import {BuildContext} from '../OCIBuild';
import CommitIcon from '../icon/CommitIcon';

function BuildCommitMeta(){

    const {prNumber,commitId} = useContext(BuildContext);

    return (
        <div className="dashboard-element-info build-metadata">
            {prNumber!=='none' ?
                <div className="logo-container">
                    <div className="logo">
                        <CommitIcon/>
                    </div>
                    <span href="#" className=" pr-id">{"#"+prNumber}</span>
                </div> 
                :  
                <div />
            }
            
            <div className="logo-container">
                <div className="logo">
                    <CommitIcon/>
                </div>
                <span className="commit-id">{commitId}</span>
            </div>
        </div>
    );
}

export default BuildCommitMeta;