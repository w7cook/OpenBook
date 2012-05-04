    function ShowHide(divId) {
	    if(divId == 'RSSiFrame')
	    {
		    if(document.getElementById(divId).style.display == 'none') {
		      document.getElementById(divId).style.display='block';
		      document.getElementById('showDivRSS').style.display='block';
		      document.getElementById('hideDivRSS').style.display='none';
		
		    }
		    else {
		      document.getElementById(divId).style.display = 'none';
		      document.getElementById('showDivRSS').style.display='none';
		      document.getElementById('hideDivRSS').style.display='block';
		    }
		}
		else //if(divId == HiddenDiv)
		{
			if(document.getElementById(divId).style.display == 'none') {
		      document.getElementById(divId).style.display='block';
		      document.getElementById('showDivBday').style.display='block';
		      document.getElementById('hideDivBday').style.display='none';
		
		    }
		    else {
		      document.getElementById(divId).style.display = 'none';
		      document.getElementById('showDivBday').style.display='none';
		      document.getElementById('hideDivBday').style.display='block';
		    }
		}
  }
  
  
  var birthdayIframe = $('#HiddenDiv');
  var dlg1 = $('#modalHolder');
  var ran = false;
  
  dlg1.bind( "clickoutside", function(event){
    if(ran == true) {
      dlg1.dialog("close");
      ran = false;
    }
  });
  
  dlg1 = dlg1.dialog({
    position: { 
      my: 'right top',
      at: 'left top',
      of: birthdayIframe
    },
    width: 300,
    //height: 150,
    modal: false,
    autoOpen: false,
    resizable: false,
    closeOnEscape: true,
    draggable: false,
    show: "fade",
    hide: "drop",
    open: function(event, ui) { 
      dlg1.dialog("widget").find(".ui-dialog-titlebar").hide(); 
      ran = true; 
      $(".ui-dialog-titlebar-close").hide(); 
    }
  });  