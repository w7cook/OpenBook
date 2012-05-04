function addFormField(field,area) {
	if(!document.getElementById) return; //Prevent older browsers from getting any further.
	var field_area = document.getElementById(field);

	if(document.createElement) { //W3C Dom method.
		var li = document.createElement("li");
		var input = document.createElement("input");
		input.name = area;
		input.type = "text"; //Type of field - can be any valid input type like text,file,checkbox etc.
		li.appendChild(input);
		field_area.appendChild(li);
	} else { //Older Method
		field_area.innerHTML += "<li><input name='"+(area)+"' type='text' /></li>";
	}
}
