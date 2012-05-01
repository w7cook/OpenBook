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

function submitComment(id) {
  var nonws = /\S/;
  var textbox = $('#commentContent' + id);
  var content = textbox.val();
  if(!nonws.test(content)) {
    textbox.val('');
    textbox.focus();
  }
  else {
    $.ajax({
      url: '/posts/' + id + '/comments',
      type: 'POST',
      data: {'commentContent': content},
      success: function (data, textStatus, jqXHR) {
        newComment = $(data).hide();
        $('#commentsList' + id).prepend(newComment);
        newComment.slideDown();
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

//TODO: merge with submitComment
function submitPost() {
  var nonws = /\S/;
  var textbox = $('#postContent');
  var content = textbox.val();
  if(!nonws.test(content)) {
    textbox.val('');
    textbox.focus();
  }
  else {
    $.ajax({
      url: '/posts',
      type: 'POST',
      data: {'postContent': content},
      success: function (data, textStatus, jqXHR) {
        var newPost = $(data).hide();
        $('#postsList').prepend(newPost);
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