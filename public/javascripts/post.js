function deleteThing(id, type) {
  $.ajax({
    url: '/' + type + 's/' + id,
    type: 'DELETE',
    success: function() {
      $('div#' + type + id).slideUp();
    }
  });
}

function like(id, type, userId, likeTxt, unlikeTxt) {
  var likes;  //array of users who like the thing
  
  var url = '/status/' + id + '/likes';
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
        success: function(data, textStatus, jqXHR) {
          numlikes++;
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
        success: function(data, textStatus, jqXHR) {
          numlikes--;
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
  nonws = /\S/;
  textbox = $('#commentContent' + id)
  content = textbox.val();
  if(!nonws.test(content)) {
    textbox.val('');
    textbox.focus();
  }
  else {
    $.ajax({
      url: 'status/' + id + '/comments',
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
  nonws = /\S/;
  textbox = $('#postContent');
  content = textbox.val();
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
        newPost = $(data).hide();
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