function submitPost() {
  var postData = {postContent : $("textarea#postContent").val()};
  var nonws = /\S/;
  if (!nonws.test(postData.postContent)) {
    $("textarea#postContent").val('');
    $("textarea#postContent").focus();
  }
  else {
    $.post('/posts', postData, function(data, textStatus, jqXHR) {
      newPost = $(data).hide();
      $('ul#post-list').prepend(newPost);
      newPost.slideDown();
      $('textarea#postContent').val('');
    });
  }
}

function deletePost(id) {
  $.ajax({
    url:  '/posts/' + id,
    type: 'DELETE',
    'complete': function(result) {$('#post-' + id).slideUp();}
  });
}

function submitComment(postId) {
  var input = $("textarea#commentContent" + postId);
  var commentData = {
    commentContent : input.val(),
    statusId: postId
  };
  var nonws = /\S/;
  if (!nonws.test(commentData.commentContent)) {
    input.val('');
    input.focus();
  }
  else {
    $.post('/status/' + postId + '/comments', commentData, function (data, textStatus, jqXHR) {
      alert('created comment');
      comment = $(data).hide();
      $('ul#commentlist-' + postId).prepend(comment);
      comment.slideDown();
      input.val('');      
    });
    input.val('TODO: submit comment');
  }
  input.focus();
}