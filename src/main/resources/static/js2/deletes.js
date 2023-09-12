const deletePaciente = document.querySelectorAll('.delete');
const deleteOdontologo = document.querySelectorAll('.deleteOdontologo');
const deleteCita = document.querySelectorAll('.deleteCita');
const confirmarCita = document.querySelector('#confirmarCita');
const confirmarPaciente = document.querySelector('#confirmarPaciente');
const confirmarOdonto = document.querySelector('#confirmarOdonto');

if(deletePaciente){
	deletePaciente.forEach(function(deletePaciente){
		deletePaciente.addEventListener('click', function(){
			var documento = this.closest('tr').querySelector('.documento');
			var numeroDocumento = documento.textContent;
			localStorage.setItem('documento', numeroDocumento);
			
		});
	});
}


if(confirmarPaciente){
	confirmarPaciente.addEventListener('click', function(){
		window.location.href = '/paciente/delete/'+localStorage.getItem('documento');
	});
	localStorage.clear();
}

if(deleteCita){
	deleteCita.forEach(function(deleteCita){
		deleteCita.addEventListener('click', function(){
			var id = this.closest('tr').querySelector('.idcita');
			var cita = id.textContent;
			localStorage.setItem('idCita', cita);
			console.log(cita);
		});
	});
}
if(confirmarCita){
	confirmarCita.addEventListener('click', function(){
		window.location.href = '/cita/delete/'+localStorage.getItem('idCita');
	});
	localStorage.clear();
}

if(deleteOdontologo){
	deleteOdontologo.forEach(function(deleteOdontologo){
		deleteOdontologo.addEventListener('click', function(){
			var documento = this.closest('tr').querySelector('.documento');
			var numeroDocumento = documento.textContent;
			localStorage.setItem('documento', numeroDocumento);
		});
	});
}

if(confirmarOdonto){
	confirmarOdonto.addEventListener('click', function(){
		window.location.href = '/odontologo/delete/'+localStorage.getItem('documento');
	});
	localStorage.clear();
}