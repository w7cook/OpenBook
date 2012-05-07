// http://code.google.com/apis/maps/documentation/javascript/examples/places-autocomplete.html
google.maps.event.addDomListener(window, 'load', initialize);
function initialize() {

  var input = document.getElementById('searchTextField');
  var autocomplete = new google.maps.places.Autocomplete(input);
  
  autocomplete.bindTo('bounds', map);

  google.maps.event.addListener(autocomplete, 'place_changed', function() {
    var place = autocomplete.getPlace(); 
  });
navigator.geolocation.getCurrentPosition(setCurrentLocation);
}
google.maps.event.addDomListener(window, 'load', initialize);


