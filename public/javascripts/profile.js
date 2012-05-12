$(document).ready(function()
{
    $(".defaultText").focus(function(srcc)
    {
        if ($(this).val() == $(this)[0].title)
        {
            $(this).removeClass("defaultTextActive");
            $(this).val("");
        }
    });
    
    $(".defaultText").blur(function()
    {
        if ($(this).val() == "")
        {
            $(this).addClass("defaultTextActive");
            $(this).val($(this)[0].title);
        }
    });
    
    $(".defaultText").blur();        
});

  $(document).ready(function(){
    $('[name="cancel"]').click(function(){
      $(this).parent().toggle();
      $(this).parent().parent().siblings('.Data').toggle(); });
 
    $('.EditButton').click(function(){
      $(this).siblings('.Data').toggle();
      $(this).siblings('.editData').toggle(); });
  });
  
  $(function() {
	$( "#birthdaypicker" ).datepicker({
		changeMonth: true,
		changeYear: true,
		yearRange: "-100:+0"
	});
  });
  
  $(function() {
	$( "#anniversarypicker" ).datepicker({
		changeMonth: true,
		changeYear: true,
		yearRange: "-100:+0"
	});
  });