function like(id, userId, like-btn, unlike-btn) {
  alert('liking');
  /*
  var likes;
  $.get("/status/${_item.id}/likes",
        function (data, textStatus, jqXHR) {
          likes = data;
        },
        "json");
  var numlikes = likes.length();
  
  
  var bttn = $('#likeButton' + id);
  
  if(likes.indexOf('/users/' + userId) === -1) {
    $.post("/status/${_item.id}/likes", {}, function(data) {
      numlikes++;
      var likeText = 'Like';
      if (numlikes > 1)
        likeText += 's';
      
      $('#numLikesPost'+id).val(numlikes + ' ' + likeText);
      $('#likeButton' + id).val(unlike-btn);
    });        drawLikesButton(id, like-btn);
  }
  else {
    $.ajax({
      url: "/status/${_item.id}/likes",
      type: 'DELETE',
      'complete': function(data) {
        numlikes--;
        var likeText = 'Like';
        if (numlikes > 1)
          likeText += 's';
        
        $('#numLikesPost'+id).val(numlikes + ' ' + likeText);
        $('#likeButton' + id).val(like-btn);
      }
    });
  }
  */
}