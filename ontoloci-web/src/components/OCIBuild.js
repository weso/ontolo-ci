import React from 'react';
import { Link } from "react-router-dom";
import BuildLogo from './build/BuildLogo';
import BuildStatus from './build/BuildStatus';
import BuildAuthor from './build/BuildAuthor';
import BuildCommit from './build/BuildCommit';
import BuildCommitMeta from './build/BuildCommitMeta';
import BuildTime from './build/BuildTime';

export const BuildContext = React.createContext();

function OCIBuild(props){

    const {build} = props;
    const SUCCESS_BUILD = "success";
    const FAILURE_BUILD = "failure";
    const CANCELLED_BUILD = "cancelled";

    const getBuildStatus = function(){
        return build.status.toLowerCase();
    }

    const getBuildPath = function(){
        let link = `/tests/${build.id}`;
        let noLink = '/';
        return getBuildStatus() === CANCELLED_BUILD ? noLink :link;
    }

    const getBuildClass = function(){
        let dashboard = 'dashboard-element ';
        let disabled = dashboard + 'disabled-build';
        let enabled = dashboard +'dashboard-element-'+getBuildStatus();
        return getBuildStatus() === CANCELLED_BUILD ? disabled :enabled;
    }


    console.log(build)
    return (

    <Link to={getBuildPath()} className={getBuildClass()}>
        <BuildContext.Provider
            value={{
                SUCCESS_BUILD:SUCCESS_BUILD,
                FAILURE_BUILD:FAILURE_BUILD,
                CANCELLED_BUILD:CANCELLED_BUILD,
                build:build,                
                buildStatus:getBuildStatus(),
                checkTitle:build.metadata.checkTitle,
                owner:build.metadata.owner,
                repo:build.metadata.repo,
                branchName:build.metadata.branch,
                commitName:build.metadata.commitName,
                commitId:build.metadata.commitId,
                prNumber:build.metadata.prNumber,
                executionTime:build.metadata.execution_time,
                date:build.metadata.execution_date
            }}>
                <BuildLogo/>
                <BuildStatus/>
                <BuildAuthor/>
                <BuildCommit/>
                <BuildCommitMeta/>
                <BuildTime/>
        </BuildContext.Provider>
    </Link>);
}

export default OCIBuild;