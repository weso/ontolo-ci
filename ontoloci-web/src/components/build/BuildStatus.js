import React, {useContext} from 'react';
import {BuildContext} from '../OCIBuild';
import SuccessIcon from '../icon/SuccessIcon';
import FailureIcon from '../icon/FailureIcon';
import CanceledIcon from '../icon/CanceledIcon';

function BuildStauts(){

    const {buildStatus,checkTitle,SUCCESS_BUILD,FAILURE_BUILD} = useContext(BuildContext);

    const getSvgStatus = function(){
        if(buildStatus===SUCCESS_BUILD)
            return <SuccessIcon/>
        if(buildStatus===FAILURE_BUILD)
            return <FailureIcon/>

        return <CanceledIcon checkTitle={checkTitle}/>
    }

    return (<div className="dashboard-element-status">
                {getSvgStatus()}
            </div>);
}

export default BuildStauts;