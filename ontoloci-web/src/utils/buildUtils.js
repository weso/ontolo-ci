export function sortBuilds(unsortedBuilds){
    return unsortedBuilds.sort((build1,build2)=>{
      let date1 = build1.metadata.execution_date;
      let date2 = build2.metadata.execution_date;

      if(date1 > date2 )
        return -1;
    
      if(date1< date2 )
        return 1;
    
      return 0;
    })
}

