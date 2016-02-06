function loadTeam(table, json) {
    
    // load the json received into the datatable
    $(table).dataTable( {
        "ajax": {
            "url" : json,
            "dataSrc": function ( json ) {
      for ( var i=0, ien=json.data.length ; i<ien ; i++ ) {
          
    	  // Insert link for team
    	  json.data[i].name = '<a href="/team.html?query='+json.data[i].name+'">'+json.data[i].name+'</a>';
    	  
          // Parse games (multivalues) into string
          if(typeof(json.data[i].games) == "undefined"){
              json.data[i].games = "None";
          }
          else{
              var games = json.data[i].games[0].name;
              if(json.data[i].games.length > 1){
                for (var j = 1; j < json.data[i].games.length; j++){
                  games = games + ", " +  json.data[i].games[j].name;
                }
              }
              json.data[i].games = games;
          }
          
          // Parse competitions (multivalues) into string
          if(typeof(json.data[i].competitions) == "undefined"){
              json.data[i].competitions = "None";
          }
          else{
              var comps = json.data[i].competitions[0].name;
              if(json.data[i].competitions.length > 1){
                for (var j = 1; j < json.data[i].competitions.length; j++){
                  comps = comps + ", " +  json.data[i].competitions[j].name;
                }
              }
              json.data[i].competitions = comps;
          }
      }
      return json.data;
    }
        },
        "columns": [
            { "data": "name" },
            { "data": "size" },
            { "data": "won" },
            { "data": "games" },
            { "data": "competitions"}
        ]
        
    } );
    
}