import React, {useContext} from 'react';
import {BuildContext} from '../OCIBuild';
import RepoIcon from '../icon/RepoIcon';

function BuildLogo(){

    const {buildStatus} = useContext(BuildContext);

    return (
        <div className={"dashboard-element-logo dashboard-element-logo-"+buildStatus} title="Public repository">
            <RepoIcon/>
        </div>);
}

export default BuildLogo;