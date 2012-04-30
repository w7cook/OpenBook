function submitCategory(id) {
	if ($('#category-name').val() == ''
			|| $('#category-description').val() == '') {
		if ($('#category-name').val() == '') {

		}
		if ($('#category-description').val() == '') {

		}
	} else {

		var categoryData = {
			name : $('#category-name').val(),
			description : $('#category-description').val()
		};

		$.ajax({
			url : '/threads/newCategory',
			type : 'POST',
			data : categoryData,
			success : function(data, textStatus, jqXHR) {
				$('#msg-sent-alert').slideDown(300).delay(8000).slideUp(300);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				$('#msg-failed-alert').slideDown(300).delay(8000).slideUp(300);
			},
			'complete' : function(jqXHR, textStatus) {
				$('#category-name').val('');
				$('#category-description').val('');
			}
		});

		var newCategory = "" + "<div class='row-fluid category' id='cat-" + id
				+ "'>" + "<div class='span8 cat-name'>"
				+ "<h4><a href='threads/" + id + "'>"
				+ $('#category-name').val() + "</a></h4>"
				+ "<span class='categoryDescription'> - "
				+ $('#category-description').val() + "</span>" + "</div>"
				+ "<div class='span4 cat-latest cat-empty'>None</div>"
				+ "</div>";

		var $fi = $('.category');
		var $el = $fi.eq($fi.length - 1);

		$el.after(newCategory);

		var newDelete = "<div id='deleteCat-" + id
				+ "' class='deleteCat' ><a href='#' "
				+ "onclick='deleteCategory(" + id + ")' data-dismiss='modal'>"
				+ "<i class='icon-remove'></i></a> "
				+ $('#category-name').val() + "</div>";

		var $fi2 = $('.deleteCat');
		var $el2 = $fi2.eq($fi2.length - 1);

		$el2.after(newDelete);

		var newButton = "<button class='btn btn-success' data-dismiss='modal' "
				+ "id='createCat' onclick='submitCategory(" + ++id + ")'>"
				+ "Create category</button>"

		$('#createCat').replaceWith(newButton);

		$('#numCats').html(id);

		if ($('.cat-name').length >= 8) {
			$('#newItem').remove();
		}
	}
}

function deleteCategory(id) {
	var deleteData = {
		categoryId : id,
	};

	$.ajax({
		url : '/threads/deleteCategory',
		type : 'POST',
		data : deleteData,
		success : function(data, textStatus, jqXHR) {
			$('#msg-sent-alert').slideDown(300).delay(8000).slideUp(300);

		},
		error : function(jqXHR, textStatus, errorThrown) {
			$('#msg-failed-alert').slideDown(300).delay(8000).slideUp(300);
		},
		'complete' : function(jqXHR, textStatus) {
			$('div#cat-' + id).slideUp(300);
			$('div#deleteCat-' + id).remove();
		}
	});

	if ($('.cat-name').length == 8) {
		$('#delItem').before(
				"<a class='btn btn-primary' data-toggle='modal' "
						+ "href='#create-category' id='newItem'>Create new "
						+ "category</a> ");
	}
}

function submitThread(catId) {
	if ($('#thread-title').val() == '' || $('#thread-content').val() == '') {

	} else {
		var threadData = {
			categoryId : catId,
			title : $('#thread-title').val(),
			content : $('#thread-content').val()
		};

		$.ajax({
			url : '/threads/newThread',
			type : 'POST',
			data : threadData,
			success : function(response) {
				window.location = "/threads/" + catId + "/" + response
			},
			error : function(jqXHR, textStatus, errorThrown) {
				$('#msg-failed-alert').slideDown(300).delay(8000).slideUp(300);
			},
			'complete' : function(jqXHR, textStatus) {
				$('#thread-title').val('');
				$('#thread-content').val('');
			}
		});
	}
}

function deleteThread(id) {
	var deleteData = {
		threadId : id,
	};

	$.ajax({
		url : '/threads/deleteThread',
		type : 'POST',
		data : deleteData,
		success : function(data, textStatus, jqXHR) {
			$('#msg-sent-alert').slideDown(300).delay(8000).slideUp(300);

		},
		error : function(jqXHR, textStatus, errorThrown) {
			$('#msg-failed-alert').slideDown(300).delay(8000).slideUp(300);
		},
		'complete' : function(jqXHR, textStatus) {
			$('#cat-' + id).slideUp(300);
		}
	});
}
