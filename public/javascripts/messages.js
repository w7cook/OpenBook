function submitMessage() {

  var messageData = {
    recipientUsername : $('#message-recipient').val(),
    title : $('#message-title').val(),
    content : $('#message-content').val()
  };

  $.post('/messages', messageData, function(data, textStatus, jqXHR) { 
    alert('sending');
    newMessage = $(data).hide();
    $('ul#message-list').prepend(newPost);
    newMessage.slideDown();
  });
}