function submitCategory(id)
{
  if ($('#category-name').val() == '' || $('#category-description').val() == '')
  {
    if ($('#category-name').val() == '')
    {

    }
    if ($('#category-description').val() == '')
    {

    }
  }
  else
  {

    var categoryData = {
      name : $('#category-name').val(),
      description : $('#category-description').val()
    };

    $.ajax({
      url : '/threads/newCategory',
      type : 'POST',
      data : categoryData,
      success : function(data, textStatus, jqXHR)
      {
        $('#msg-sent-alert').slideDown(300).delay(8000).slideUp(300);
      },
      error : function(jqXHR, textStatus, errorThrown)
      {
        $('#msg-failed-alert').slideDown(300).delay(8000).slideUp(300);
      },
      'complete' : function(jqXHR, textStatus)
      {
        $('#category-name').val('');
        $('#category-description').val('');
      }
    });

    var newCategory = "" + "<div class='row-fluid category' id='cat-" + id
        + "'>" + "<div class='span8 cat-name'>" + "<h4><a href='threads/" + id
        + "'>" + $('#category-name').val() + "</a></h4>"
        + "<span class='categoryDescription'> - "
        + $('#category-description').val() + "</span>" + "</div>"
        + "<div class='span4 cat-latest cat-empty'>None</div>" + "</div>";

    var $fi = $('.category');
    var $el = $fi.eq($fi.length - 1);

    $el.after(newCategory);

    var newDelete = "<div id='deleteCat-" + id
        + "' class='deleteCat' ><a href='#' " + "onclick='deleteCategory(" + id
        + ")' data-dismiss='modal'>" + "<i class='icon-remove'></i></a> "
        + $('#category-name').val() + "</div>";

    var $fi2 = $('.deleteCat');
    var $el2 = $fi2.eq($fi2.length - 1);

    $el2.after(newDelete);

    var newButton = "<button class='btn btn-success' data-dismiss='modal' "
        + "id='createCat' onclick='submitCategory(" + ++id + ")'>"
        + "Create category</button>"

    $('#createCat').replaceWith(newButton);

    $('#numCats').html(id);

    if ($('.cat-name').length >= 8)
    {
      $('#newItem').remove();
    }
  }
}

function deleteCategory(id)
{
  var deleteData = {
    categoryId : id,
  };

  $.ajax({
    url : '/threads/deleteCategory',
    type : 'POST',
    data : deleteData,
    'complete' : function(jqXHR, textStatus)
    {
      $('div#cat-' + id).slideUp(300);
      $('div#deleteCat-' + id).remove();
    }
  });

  if ($('.cat-name').length == 8)
  {
    $('#delItem').before(
        "<a class='btn btn-primary' data-toggle='modal' "
            + "href='#create-category' id='newItem'>Create new "
            + "category</a> ");
  }
}

function submitThread(catId)
{
  if ($('#thread-title').val() == '' || $('#thread-content').val() == '')
  {

  }
  else
  {
    var threadData = {
      categoryId : catId,
      title : $('#thread-title').val(),
      content : $('#thread-content').val()
    };

    $.ajax({
      url : '/threads/newThread',
      type : 'POST',
      data : threadData,
      success : function(response)
      {
        window.location = "/threads/" + catId + "/" + response
      },
      error : function(jqXHR, textStatus, errorThrown)
      {
        $('#msg-failed-alert').slideDown(300).delay(8000).slideUp(300);
      },
      'complete' : function(jqXHR, textStatus)
      {
        $('#thread-title').val('');
        $('#thread-content').val('');
      }
    });
  }
}

function deleteThread(id)
{
  var deleteData = {
    threadId : id,
  };

  $.ajax({
    url : '/threads/deleteThread',
    type : 'POST',
    data : deleteData,
    'complete' : function(jqXHR, textStatus)
    {
      $('#cat-' + id).slideUp(300);
    }
  });
}

function deleteComment(id, event)
{
  event.preventDefault();
  
  var deleteData = {
    commentId : id,
  };
  
  $.ajax({
    url : '/comments/' + id,
    type : 'DELETE',
    data : deleteData,
    'complete' : function(jqXHR, textStatus)
    {
      $('#thd-' + id).slideUp(300);
    }
  });  
}

function postComment(thdId, event)
{
  event.preventDefault();
  
  if ($('textarea#threadReply').val() == '')
  {

  }
  else
  {
    var commentData = {
        commentContent : $('textarea#threadReply').val(),
        statusId : thdId
    };

    $.ajax({
      url : '/FThread/' + thdId + "/comments/",
      type : 'POST',
      data : commentData,
      'complete' : function(jqXHR, textStatus, response)
      {
        $('textarea#threadReply').val('');
        
        var currentTime = new Date();
        var month = currentTime.getMonth() + 1;
        var day = currentTime.getDate();
        var year = currentTime.getYear().toString().slice(1);
        var hours = currentTime.getHours();
        var minutes = currentTime.getMinutes();
        if (minutes < 10)
        {
            minutes = "0" + minutes
        }
        if (hours == 0)
        {
            hours = 12;
            var ampm = "AM";
        }
        else if(hours > 11)
        {
            var ampm = "PM";
            hours = hours - 12;
        } 
        else 
        {
            var ampm = "AM";
        }
        
        var $fi = $('.category');
        var $el = $fi.eq($fi.length-2);
        
        var username = $('#userinfo').data('username');
        var userId = $('#userinfo').data('userid');
        
        var newId = response.substr
        
        var newComment = "" +
        "<div class='row-fluid category' id='thd-${comment.id}'>    " +
        "<div class='authorDetails'>" +
        "<a href='#' onclick='deleteComment(${comment.id}, event)'>" +
        "<i class='icon-remove'></i></a>" +
        "<a href='/?id=" + userId + "'>" +
        "<img src='/photos/120x120/" + userId + "' class='postAvatar'></a>" +
        "<h4><a href='/?id=${comment.owner.id}'>" + username + "</a></h4>" +
        "<span class='postDate'>" + month + "-" + day + "-" + year + ", " +
        hours + ":" + minutes + " " + ampm +
        "</span></div><div class='postContent'>" + commentData.commentContent +
        "</div></div>";
        
        
        $el.after(newComment);
        
        
      }
    });
  }
}
