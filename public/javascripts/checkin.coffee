initialize = ->
  mapOptions =
    center: new google.maps.LatLng(30.2733,-97.7421)
    zoom: 13
    mapTypeId:  google.maps.MapTypeId.ROADMAP
  nil

map = new google.maps.Map document.getElementById('map_canvas'), mapOptions
input = document.getElementById 'searchTextField'
autocomplete = new google.maps.places.Autocomplete input

autocomplete.bindTo 'bounds', map

infowindow = new google.maps.InfoWindow()

marker = new google.maps.Marker {map: map}

google.maps.event.addListener autocomplete, 'place_changed', () ->
  infowindow.close()
  place = autocomplete.getPlace()
  if place.geometry.viewport
    map.fitBounds place.geometry.viewport

  else
    map.setCenter place.geometry.location
    map.setZoom 17  # Why 17? Because it looks good.

  image = new google.maps.MarkerImage(
    place.icon,
    new google.maps.Size(71, 71),
    new google.maps.Point(0, 0),
    new google.maps.Point(17, 34),
    new google.maps.Size(35, 35))
  marker.setIcon image
  marker.setPosition place.geometry.location

  address = ''
  if place.address_components
    address = [(place.address_components[0] &&
      place.address_components[0].short_name || ''),
      (place.address_components[1] and
      place.address_components[1].short_name or ''),
      (place.address_components[2] and
      place.address_components[2].short_name or '')
    ].join ' '

  html = "<a href=checkin/at?location=#{place.geometry.location}&name=#{place.name}&address=#{place.formatted_address}>Checkin Here</a><br>
  <strong>#{place.name}</strong><br>#{address}"
  infowindow.setContent html
  infowindow.open map, marker
  nil

google.maps.event.addDomListener window, 'load', initialize