const boton = document.getElementById("confirmarOdontograma");

boton.addEventListener("click", function() {
	html2canvas(document.querySelector("#main")).then(canvas => {

		var dataUrl = canvas.toDataURL("image/png");

		var link = document.createElement('a');
		link.href = dataUrl;
		link.download = 'screenshot.png';

		link.click();
	});
});