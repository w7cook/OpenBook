function friend(userId) {
  $.ajax({
    url: '/users/' + userId + '/friends',
    type: 'POST',
    success: function(data, textStatus, jqXHR) {
      $('#not-friends').hide();
      $('#friend-request').hide();
      if(data === "friends")
        $('#friends').fadeIn();
      else
        $('#pending-friends').fadeIn();
      
    },
    error: function(jqXHR, textStatus, errorType) {
      alert(errorType);
    }
  });
}

function unfriend(userId) {
  $.ajax({
    url: '/users/' + userId + '/friends',
    type: 'DELETE',
    success: function(data, textStatus, jqXHR) {
      $('#friend-request').hide();
      $('#friends').hide();
      $('#pending-friends').hide();
      $('#not-friends').fadeIn();
    },
    error: function(jqXHR, textStatus, errorType) {
      alert(errorType);
    }
  });
}