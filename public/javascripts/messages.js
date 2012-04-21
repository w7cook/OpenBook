function submitMessage() {
  var messageData = {
    username : $('#message-recipient').val(),
    subject : $('#message-title').val(),
    content : $('#message-content').val()
  };

  $.ajax({
    url:'/messages', 
    type: 'POST',
    data: messageData,
    success: function(data, textStatus, jqXHR) {
      $('#msg-sent-alert').slideDown(300).delay(8000).slideUp(300);
    },
    error: function(jqXHR, textStatus, errorThrown) {
      $('#msg-failed-alert').slideDown(300).delay(8000).slideUp(300);
    },
    'complete': function (jqXHR, textStatus) {
      $('#message-recipient').val('');
      $('#message-title').val('');
      $('#message-content').val('');
    }
  });
}

