function deleteThing(id, type) {
  $.ajax({
    url: '/' + type + 's/' + id,
    type: 'DELETE',
    success: function() {
      $('#' + type + id).slideUp();
    }
  });
}

function like(id, type, userId, likeTxt, unlikeTxt) {
  var likes;  //array of users who like the thing
  
  var url = '/' + type + 's/' + id + '/likes';
  $.ajax({
    dataType: 'json',
    async: false,
    type: 'GET',
    url: url,
    success: function(data, textStatus, jqXHR) {
      likes = data;
    },
    error: function(jqXHR, textStatus, errorThrown) {
      alert(errorThrown);
    }
  });
  
  if (likes !== undefined) {
    var numlikes = likes.length;
    var numLikesSpan = $('#' + type + id + 'likes');
    
    if(likes.indexOf('/users/' + userId) === -1) {
      $.ajax({
        url: url,
        type: 'POST',
        dataType: 'json',
        success: function(data, textStatus, jqXHR) {
          numlikes = data.length;
          var likeSpanText = (numlikes === 1) ? 'Like' : 'Likes';
          numLikesSpan.text(numlikes + ' ' + likeSpanText);
          $('#like' + type + id).text(unlikeTxt);
        },
        error: function(jqXHR, textStatus, errorThrown) {
          alert(errorThrown);
        }  
      });
    }
    else {
      $.ajax({
        url: url,
        type: 'DELETE',
        dataType: 'json',
        success: function(data, textStatus, jqXHR) {
          numlikes = data.length;
          var likeSpanText = (numlikes === 1) ? 'Like' : 'Likes';
          numLikesSpan.text(numlikes + ' ' + likeSpanText);
          $('#like' + type + id).text(likeTxt);
        },
        error: function(jqXHR, textStatus, errorThrown) {
          alert(errorThrown);
        }
      });
    }
  }
}

function submit(textboxid, listid, url) {
  var textbox = $('#' + textboxid);
  var list = $('#' +listid);
  var nonws = /\S/;
  var content = textbox.val();
  if(textbox === undefined || list === undefined || !nonws.test(content)) {
    textbox.val('');
    textbox.focus();
  }

  else {
    $.ajax({
      url: url,
      type: 'POST',
      data: {'content': content},
      success: function (data, textStatus, jqXHR) {
        var newPost = $(data).hide();
        list.prepend(newPost);
        newPost.slideDown();
      },
      error: function (jqXHR, textStatus, errorThrown) {
        alert(errorThrown);
      },
      'complete' : function (jqXHR, textStatus) {
        textbox.val('');
      }
    });
  }
}