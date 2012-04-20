$(function() {

   /*
* Autocomplete for populating an input field with a value from the server.
*/
   $('input.autocomplete').each( function() {
      var $input = $(this);
      var serverUrl = $input.data('url');
      $(this).autocomplete({ source:serverUrl });
   });
});