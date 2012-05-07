
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
      $("button#AboutMeEditButton").click(function(){$("div#EditDataAboutMe").toggle(); 
                        $("div#AboutMeData").toggle(); return false;});
    $("button#InformationEditButton").click(function(){$("div#EditDataInformation").toggle(); 
                        $("div#InformationData").toggle(); return false;}); 
    $("button#ContactInfoEditButton").click(function(){$("div#EditDataContactInfo").toggle(); 
                        $("div#ContactInfoData").toggle(); return false;}); 
    $("button#WorkEduEditButton").click(function(){$("div#EditDataWorkEdu").toggle(); 
                        $("div#WorkEduData").toggle(); return false;});
    $("button#LivingEditButton").click(function(){$("div#EditDataLiving").toggle(); 
                        $("div#LivingData").toggle(); return false;});  
    $("button#QuoteEditButton").click(function(){$("div#EditDataQuote").toggle(); 
                        $("div#QuoteData").toggle(); return false;});                         
  });
  
  $(function() {
	$( "#datepicker" ).datepicker({
		changeMonth: true,
		changeYear: true,
		yearRange: "-100:+0"
	});
  });