// Javascript file for the e-sports ontology website

$(document).ready( function () {
    // Datatables
    //$('#table_landing').DataTable();
    //$('#table_teams').DataTable();
    //loadTable('#table_teams', "json/team.json");
} );

function getQueryVariable(variable)
{
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}