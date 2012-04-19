function submitPost() {
  postData = {postContent : $("textarea#postContent").val()};
  if (postData.postContent !== '') {
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