function loadCompetitions(table, json) {
    
    // load the json received into the datatable
    $(table).dataTable( {
        "ajax": {
            "url" : json,
            "dataSrc": function ( json ) {
    	      	json = json.competitions;
    	    	
    	    	json = JSON.parse(json);
    	    	
      for ( var i=0, ien=json.data.length ; i<ien ; i++ ) {
    	  // Insert link for team
    	  json.data[i].name = '<a href="/competition.html?query='+json.data[i].name+'">'+json.data[i].name+'</a>';
    	  
//          // Parse competitions (multivalues) into string
//          if(typeof(json.data[i].games) == "undefined"){
//              json.data[i].games = "None";
//          }
//          else{
//              var games = json.data[i].games[0].name;
//              if(json.data[i].games.length > 1){
//                for (var j = 1; j < json.data[i].games.length; j++){
//                  games = games + ", " +  json.data[i].games[j].name;
//                }
//              }
//              json.data[i].games = games;
//          }
//          
//          // Parse winners (multivalues) into string
//          if(typeof(json.data[i].winners) == "undefined"){
//              json.data[i].winners = "None";
//          }
//          else{
//              var winners = '<a href="/team.html?query='+json.data[i].winners[0].name+'">'+json.data[i].winners[0].name+'</a>' + " (" + json.data[i].winners[0].year + ") ";
//              if(json.data[i].winners.length > 1){
//                for (var j = 1; j < json.data[i].winners.length; j++){
//                  winners = winners + ", " +  '<a href="/team.html?query='+json.data[i].winners[0].name+'">'+json.data[i].winners[0].name+'</a>' + " (" + json.data[i].winners[0].year + ") ";
//                }
//              }
//              json.data[i].winners = winners;
//          }
//          
//          // Parse cities (multivalues) into string
//          if(typeof(json.data[i].cities) == "undefined"){
//              json.data[i].cities = "None";
//          }
//          else{
//              var cities = json.data[i].cities[0].name + " (" + json.data[i].cities[0].year + ") ";
//              if(json.data[i].cities.length > 1){
//                for (var j = 1; j < json.data[i].cities.length; j++){
//                  cities = cities + ", " +  json.data[i].cities[j].name + " (" + json.data[i].cities[0].year + ") ";
//                }
//              }
//              json.data[i].cities = cities;
//          }
      }
      return json.data;
    }
        },
        "columns": [
            { "data": "name" },
            { "data": "events" },
            { "data": "type" },
            { "data": "games" },
            { "data": "winners"},
            { "data": "cities"}
        ]
        
    } );
    
}