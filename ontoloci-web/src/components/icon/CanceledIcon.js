import React from 'react';
import Tooltip from '@material-ui/core/Tooltip';
import { makeStyles } from '@material-ui/core/styles';

function CanceledIcon(props){

    const {checkTitle} = props;

    const useStylesBootstrap = makeStyles((theme) => ({
        arrow: {
          color: theme.palette.common.black,
        },
        tooltip: {
          backgroundColor: theme.palette.common.black,
        },
      }));

    function BootstrapTooltip(props) {
        const classes = useStylesBootstrap();
        return <Tooltip arrow classes={classes} {...props} />;
    }

    return (
        <BootstrapTooltip title={checkTitle}>
            <svg    className="icon-check-cancelled" 
                    xmlns="http://www.w3.org/2000/svg" 
                    height="24" 
                    viewBox="0 0 24 24" 
                    width="24">
                        <path d="M0 0h24v24H0V0z" fill="none"/>
                        <path d="M11 15h2v2h-2zm0-8h2v6h-2zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z"/>
            </svg>
        </BootstrapTooltip>
    );
}

export default CanceledIcon;

