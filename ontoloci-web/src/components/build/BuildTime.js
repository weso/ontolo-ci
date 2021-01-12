import React, {useContext} from 'react';
import {BuildContext} from '../OCIBuild';
import ClockIcon from '../icon/ClockIcon';
import CalendarIcon from '../icon/CalendarIcon';
import {getDate} from '../../utils/dateUtils';

function BuildMetadata(){

    const {executionTime,date} = useContext(BuildContext);

    return (
        <div className="dashboard-element-info ci-metadata">
            <div className="logo-container">
                <div className="logo">
                    <ClockIcon/>
                </div>
                <span>{executionTime}</span>
            </div>
            <div className="logo-container">
                <div className="logo">
                    <CalendarIcon/>
                </div>
                <span>{getDate(date)}</span>
            </div>
        </div>
    );
}

export default BuildMetadata;