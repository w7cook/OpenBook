function deleteThing(id, type) {
  $.ajax({
    url: '/' + type + 's/' + id,
    type: 'DELETE',
    success: function() {
      $('#' + type + id).slideUp();
    }
  });
}

function like(url, metaId) {
  var meta = $('#' + metaId);
  $.ajax({
    url: url,
    type: 'POST',
    success: function(data, textStatus, jqXHR) {
      meta.find('.like-btn').hide();
      meta.find('.unlike-btn').fadeIn();
      if(textStatus === 'success') {
        var numLikes = parseInt(meta.find('.num-likes').text());
        numLikes++;
        var likesText = (numLikes === 1) ? 'Like.' : 'Likes.';
        meta.find('.num-likes').text(numLikes)
        meta.find('.num-likes-text').text(likesText);
      }
    },
    error: function (jqXHR, textStatus, errorType) {
      alert('error: ' + errorType);
    }
  });
}

function unlike(url, metaId) {
  var meta = $('#' + metaId);
  $.ajax({
    url: url,
    type: 'DELETE',
    success: function(data, textStatus, jqXHR) {
      meta.find('.unlike-btn').hide();
      meta.find('.like-btn').fadeIn();
      if(textStatus === 'success') {
        var numLikes = parseInt(meta.find('.num-likes').text());
        numLikes--;
        var likesText = (numLikes === 1) ? 'Like.' : 'Likes.';
        meta.find('.num-likes').text(numLikes)
        meta.find('.num-likes-text').text(likesText);
      }
    },
    error: function (jqXHR, textStatus, errorType) {
      alert('error: ' + errorType);
    }
  });
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