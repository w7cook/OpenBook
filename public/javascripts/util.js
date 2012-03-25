/**
 *Util.js
 * 
 * Author: Nathan Petryk (matrix10657)
 * 
 * This script parses the DOM and adds interactive functionality
 *
*/
$(document).ready(function() {
 
 	/* Show & Hide Functionality---
 		example:
 			<div class="hideable">
 				<img class="triggerhide"/>
 				<img class="triggershow showfirst"/>
 				
 				<div class="post-teaser tohideopp">
 				
 					Here is a few words about the post
 				</div>
 				<div class="post-content tohide hide">
 				
 					Here is all of the post content
 				</div>
 			</div> 
 	
 		In this example, we denote a 'domain' for the hide triggers(.triggerhide, .triggershow) and hide content (.tohide, tohideopp)
 		with the class .hideable. All of the following classes must reside beneath this element.
 		.triggerhide :: when clicked on, will hide elements w/ classes .tohide, .triggerhide and show .tohideopp, .triggershow
 		.triggershow :: opposite of .triggerhide
 		.tohide      :: this is an element that will be toggled between hidden and visible
 		.tohideopp   :: this is an (optional) element that will be visible when tohide is hidden, and hidden when tohide is visibile 
 		.showfirst   :: belongs on a .triggerhide or .triggershow class. If a trigger has this, it will be visible when the page is rendered
 		.hide		 :: will hide any element with this class. Can be put on .tohide or .tohideopp or the triggers to explicitly hide them
 						when the page is rendered
 	*/
 	
 	// Binds click handlers for the .triggerhide class.
	$(".triggerhide").click(function(){
		var hideable = $(this).closest('.hideable'); 		// Get the .hideable domain
		var elem = relevantToHide($(this));					// find the .tohide element in it
		var opp  = relevantToHideOpp($(this));				// find the .tohideopp element in it
		
    	if( elem.is(":visible") == true ) {					// if the tohide is visible
			elem.hide(300);									//  hide it
		    opp.show();										//  show the .tohideopp
		    
		    hideable.find('.triggershow:first').show();		//  unhide the .triggershow
		    hideable.find('.triggerhide:first').hide(300);		//  hide the .triggerhide
		} 
  	});

	// Binds click handlers for the .triggershow class.
	// see the .click for the .triggerhide above for documentation
  	$(".triggershow").click(function(){
  		var hideable = $(this).closest('.hideable');
    	var elem = relevantToHide($(this));
		var opp = relevantToHideOpp($(this));
		
    	if( elem.is(":visible") == false ) {
			elem.show();
      		opp.hide(300);
      		
      		hideable.find('.triggerhide:first').show();
      		hideable.find('.triggershow:first').hide(300);
		}
	});

	// All elements with the .hide class will be hidden
	// This can be moved to a stylesheet.
	$(".hide").each(function() {
		$(this).hide(300);
	});
	
	
	// Preprocess .triggerhide and .triggershow elements
	$(".triggerhide, .triggershow").each(function() {
		var elem = relevantToHide($(this));						// get the element to hide
		var tagName = $(this).get(0).tagName.toLowerCase()		// get this trigger's tagName
		
		if (tagName === "img") {								// if it is an image, we want to use little expand and collapse buttons
			if( $(this).hasClass('triggerhide') ) {				//  if its a .triggerhide
				$(this).attr("src","/public/images/col.gif");	//   use collapse
			} else { 											//  else ..
				$(this).attr("src","/public/images/exp.gif");
			}
		}
		
	    if ($(this).hasClass('showfirst')) {					// if this trigger has showfirst on it
	    	$(this).show();										//  we want to display it initially
	    } else {	
	    	$(this).hide(300);
	    }
	});	  
});

// find a .tohide in the .hideable domain of the given element, elem
function relevantToHide(elem) {
	return elem.closest(".hideable").find(".tohide:first");
}

// find a .tohideopp in the .hideable domain of the given element, elem
function relevantToHideOpp(elem) {
  return elem.closest(".hideable").find(".tohideopp:first");
}
