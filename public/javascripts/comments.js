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

function test()
{
	alert("Hello World!");
}