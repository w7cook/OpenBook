var geocoder;
var map;

function codeAddress() {
  var address = document.getElementById("searchTextField").value;
  geocoder.geocode( { 'address': address}, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
      map.setCenter(results[0].geometry.location);
      var marker = new google.maps.Marker({
        map: map, 
        position: results[0].geometry.location
      });
    } else {
      alert("Geocode was not successful for the following reason: " + status);
    }
  });
}

function initialize() {
  geocoder = new google.maps.Geocoder();
  var mapOptions = {
    center: new google.maps.LatLng(30.2733,-97.7421),
    zoom: 13,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  map = new google.maps.Map(document.getElementById('map_canvas'),
                            mapOptions);
  codeAddress()
}
google.maps.event.addDomListener(window, 'load', initialize);