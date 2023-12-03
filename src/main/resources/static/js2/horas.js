const odontologo = document.getElementById('odontologo');
const fechacita = document.getElementById('fechacita');
const selectHora = document.querySelector('select[name="hora"]');
const selectDoc = document.querySelector('select[name="odontologo_doc"]');
var hora;
var horasArray;

odontologo.addEventListener('change', function(){
	if(localStorage.getItem('fechas')!==null){
		horas();
	}
});

fechacita.addEventListener('change', function() {
	horas();	
});

function horas () {
	var fechaSeleccionada = fechacita.value;
	localStorage.setItem('fechas',fechaSeleccionada);
	
	fetch('http://localhost:8080/cita/horasocupadas/' + fechaSeleccionada + '/' + odontologo.value)
		.then(response => response.text())
		.then(data => {
		if(data === null){
			console.log(data);
			fetch('https://dentalsoft-production.up.railway.app/cita/horasocupadas/' + fechaSeleccionada + '/' + odontologo.value)
		.then(response => response.text())
		.then(data => {
			var dataX = data.slice(1, -1);
			horasArray = dataX.split(',');
			var opcionHora = selectHora.querySelectorAll('.horacita');

			if (data.length === 0 || data === '[]') {
				opcionHora.forEach(function(opcion) {
					opcion.disabled = false;
					opcion.style.color = "green";
				})
			}else {
				opcionHora.forEach(function(opcion) {
					for (let i = 0; i < horasArray.length; i++) {

						if (opcion.textContent.trim() === horasArray[i].trim()) {
							opcion.disabled = true;
							opcion.style.color = "red";
							break; // Deshabilitar opción ocupada
						} else {
							opcion.disabled = false;
							opcion.style.color = "green"; // Habilitar todas las opciones
						}
					}
				})
			}
		})
		.catch(error => console.error('Error', error));
		}
		console.log(data);
			var dataX = data.slice(1, -1);
			horasArray = dataX.split(',');
			var opcionHora = selectHora.querySelectorAll('.horacita');

			if (data.length === 0 || data === '[]') {
				opcionHora.forEach(function(opcion) {
					opcion.disabled = false;
					opcion.style.color = "green";
				})
			}else {
				opcionHora.forEach(function(opcion) {
					for (let i = 0; i < horasArray.length; i++) {

						if (opcion.textContent.trim() === horasArray[i].trim()) {
							opcion.disabled = true;
							opcion.style.color = "red";
							break; // Deshabilitar opción ocupada
						} else {
							opcion.disabled = false;
							opcion.style.color = "green"; // Habilitar todas las opciones
						}
					}
				})
			}
		})
		.catch(error => console.error('Error', error));
		
		
}