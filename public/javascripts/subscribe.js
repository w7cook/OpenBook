function subscribe(userId) {
  $.ajax({
    url: '/users/' + userId + '/subscribe',
    type: 'POST',
    success: function(data, textStatus, jqXHR) {
      $('#subscribedTo').hide();
      $('#subscribed').fadeIn();
    },
    error: function(jqXHR, textStatus, errorType) {
      alert(errorType);
    }
  });
}

function unsubscribe(userId) {
  $.ajax({
    url: '/users/' + userId + '/subscribe',
    type: 'DELETE',
    success: function(data, textStatus, jqXHR) {
      $('#subscribed').hide();
      $('#subscribedTo').fadeIn();
    },
    error: function(jqXHR, textStatus, errorType) {
      alert(errorType);
    }
  });
}