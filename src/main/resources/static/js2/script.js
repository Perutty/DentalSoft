const deletePaciente = document.querySelectorAll('.delete');
const confirmar = document.querySelector('#confirmar');


if(deletePaciente){
	deletePaciente.forEach(function(deletePaciente){
		deletePaciente.addEventListener('click', function(){
			var documento = this.closest('tr').querySelector('.documento');
			var numeroDocumento = documento.textContent;
			localStorage.setItem('documento', numeroDocumento);
		});
	});
}

if(confirmar){
	confirmar.addEventListener('click', function(){
		window.location.href = '/paciente/delete/'+localStorage.getItem('documento');
	});
	localStorage.clear();
}