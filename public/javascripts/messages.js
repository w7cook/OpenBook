function submitMessage() {
  var messageData = {
    username : $('#message-recipient').val(),
    subject : $('#message-title').val(),
    content : $('#message-content').val()
  };

  $.post('/messages', messageData, function(data, textStatus, jqXHR) {     
    $('#message-recipient').val(''),
    $('#message-title').val(''),
    $('#message-content').val('')
    
    $('#msg-sent-alert').slideDown(300).delay(8000).slideUp(300);
  });
}