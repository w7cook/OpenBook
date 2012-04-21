function like(id, userId, likeTxt, unlikeTxt) {
  var likes;

  $.ajax({
    dataType: 'json',
    async: false,
    type: 'GET',
    url: '/status/' + id + '/likes',
    success: function(data, textStatus, jqXHR) {
      likes = data;
    },
    error: function(jqXHR, textStatus, errorThrown) {
      alert(errorThrown);
    }
  });

  var numlikes = likes.length;
  var bttn = $('#likeButton' + id);
  
  if(likes.indexOf('/users/' + userId) === -1) {
    $.ajax({
      url: "/status/" + id + "/likes",
      type: 'POST',
      success: function(data, textStatus, jqXHR) {
        alert('liking');
        numlikes++;
        var likeText = 'Like';
        if (numlikes > 1)
          likeText += 's';
        
        $('#numLikesPost'+id).val(numlikes + ' ' + likeText);
        $('#likeButton' + id).val(unlikeTxt);
      },
      error: function(jqXHR, textStatus, errorThrown) {
        alert(errorThrown);
      }  
    });
  }
  else {
    $.ajax({
      url: "/status/" + id + "/likes",
      type: 'DELETE',
      success: function(data, textStatus, jqXHR) {
        alert('unliking');
        numlikes--;
        var likeText = 'Like';
        if (numlikes > 1)
          likeText += 's';
        
        $('#numLikesPost'+id).val(numlikes + ' ' + likeText);
        $('#likeButton' + id).val(likeTxt);
      },
      error: function(jqXHR, textStatus, errorThrown) {
        alert(errorThrown);
      }
      
    });
  }
}