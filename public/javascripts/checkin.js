// http://code.google.com/apis/maps/documentation/javascript/examples/places-autocomplete.html

var map;

function setCurrentLocation(p) {
  var currentLocation = 
    new google.maps.LatLng(p.coords.latitude, p.coords.longitude);
  console.log(p.coords.longitude+", "+p.coords.latitude);
  map.setCenter(currentLocation);
  map.setZoom(13);
}

function initialize() {
  var mapOptions = {
    center: new google.maps.LatLng(30.2733,-97.7421),
    zoom: 13,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  map = new google.maps.Map(document.getElementById('map_canvas'),
                            mapOptions);
  
  var input = document.getElementById('searchTextField');
  var autocomplete = new google.maps.places.Autocomplete(input);
  
  autocomplete.bindTo('bounds', map);
  
  var infowindow = new google.maps.InfoWindow();
  var marker = new google.maps.Marker({
    map: map
  });
  
  google.maps.event.addListener(autocomplete, 'place_changed', function() {
    infowindow.close();
    var place = autocomplete.getPlace();
    if (place.geometry.viewport) {
      map.fitBounds(place.geometry.viewport);
    } else {
      map.setCenter(place.geometry.location);
      map.setZoom(17);  // Why 17? Because it looks good.
    }
    
    var image = new google.maps.MarkerImage(
      place.icon,
      new google.maps.Size(71, 71),
      new google.maps.Point(0, 0),
      new google.maps.Point(17, 34),
      new google.maps.Size(35, 35));
    marker.setIcon(image);
    marker.setPosition(place.geometry.location);
    
    var address = '';
    if (place.address_components) {
      address = [(place.address_components[0] &&
                  place.address_components[0].short_name || ''),
                 (place.address_components[1] &&
                  place.address_components[1].short_name || ''),
                 (place.address_components[2] &&
                  place.address_components[2].short_name || '')
                ].join(' ');
    }
    
    infowindow.setContent('<div><a href="checkins/at?location='+
                          place.geometry.location+'&name='+place.name+
                          '&address='+place.formatted_address+
                          '">Checkin Here</a><br>'+
                          '<strong>' + place.name + '</strong><br>' + address);
    infowindow.open(map, marker);
  });
  navigator.geolocation.getCurrentPosition(setCurrentLocation);
}
google.maps.event.addDomListener(window, 'load', initialize);
