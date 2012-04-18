function submitPost() {
  postData = {postContent : $("textarea#postContent").val()};
  $.post('/posts', postData, function(data, textStatus, jqXHR) {
    newPost = $(data).hide();
    $('ul#post-list').prepend(newPost);
    newPost.slideDown();
    $('textarea#postContent').val('');
  });
}