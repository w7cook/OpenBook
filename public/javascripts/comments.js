function showDiv(div)
{
	var divstyle = new String();
	divstyle = document.getElementById(div).style.display;
	document.getElementById(div).style.display = "block";
}

function hideDiv(div)
{
	var divstyle = new String();
	divstyle = document.getElementById(div).style.display;
	document.getElementById(div).style.display = "none";
}

function limitText(limitField, limitCount, limitNum) 
{
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} 
	else {
		limitCount.value = limitNum - limitField.value.length;
	}
}

function test()
{
	alert("Hello World!");
}