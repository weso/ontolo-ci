function getFormatedDate(miliseconds) {
    var days, hours, minutes,  total_hours, total_minutes, total_seconds;
    
    total_seconds = parseInt(Math.floor(miliseconds / 1000));
    total_minutes = parseInt(Math.floor(total_seconds / 60));
    total_hours = parseInt(Math.floor(total_minutes / 60));

    days = parseInt(Math.floor(total_hours / 24));
    //seconds = parseInt(total_seconds % 60);
    minutes = parseInt(total_minutes % 60);
    hours = parseInt(total_hours % 24);
    

    if(days>0){
      return days +" days ago";
    }

    if(hours>0){
      if(hours>1)
        return hours +" hours ago";
      return hours +" hour ago";
    }

    if(minutes>0){
      return minutes +" minutes ago";
    }

    return "A few seconds ago";
};

export function getDate(date){
    let executionDate = new Date(parseInt(date));
    let currentDate = new Date();
    let resta = currentDate.getTime() - executionDate.getTime();
    return getFormatedDate(Math.round(resta));
}



