const deletePaciente = document.querySelectorAll('.delete');
const newEvolution = document.querySelectorAll('.newEvolution');
const deleteOdontologo = document.querySelectorAll('.deleteOdontologo');
const deleteCita = document.querySelectorAll('.deleteCita');
const confirmarCita = document.querySelector('#confirmarCita');
const confirmar = document.querySelector('#confirmar');
const confirmarPaciente = document.querySelector('#confirmarPaciente');
const confirmarOdonto = document.querySelector('#confirmarOdonto');
const searchInput = document.getElementById('search-input');
const table = document.getElementById('tablax');

const tableRows = table.getElementsByTagName('tr');
const tableHeader = table.getElementsByTagName('thead')[0];
const headerRow = tableHeader.getElementsByTagName('tr')[0];

searchInput.addEventListener('keyup', function() {
  const searchValue = this.value.toLowerCase();
  const searchResults = [];
  searchResults.push(headerRow);
  
  for (let i = 0; i < tableRows.length; i++) {
    const rowCells = tableRows[i].getElementsByTagName("td");
    let found = false;
    for (let j = 0; j < rowCells.length; j++) {
      const cellText = rowCells[j].textContent.toLowerCase();
      if (cellText.indexOf(searchValue) > -1) {
        found = true;
        break;
      }
    }
    if (found) {
      searchResults.push(tableRows[i]);
      }
    }
    for (let i = 0; i < tableRows.length; i++) {
    if (searchResults.indexOf(tableRows[i]) > -1) {
      tableRows[i].style.display = "";
    } else {
      tableRows[i].style.display = "none";
    }
  }
});


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
			var id = this.closest('tr').querySelector('.id');
			var idCita = id.textContent;
			localStorage.setItem('idCita', idCita);
		});
	});
}
if(confirmarCita){
	confirmarCita.addEventListener('click', function(){
		window.location.href = '/cita/delete/'+localStorage.getItem('idCita');
	});
	localStorage.clear();
}

if(confirmar){
	confirmar.addEventListener('click', function(){
		window.location.href = '/admin/delete/'+localStorage.getItem('idCita');
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

if(newEvolution){
	newEvolution.forEach(function(newEvolution){
		newEvolution.addEventListener('click', function(){
			var idCita = this.closest('tr').querySelector('.id');
			var numeroDocumento = documento.textContent;
			localStorage.setItem('documento', numeroDocumento);
		});
	});
}

function togglePassword() {
    var passwordInput = document.getElementById("password");
    if (passwordInput.type === "password") {
      passwordInput.type = "text";
    } else {
      passwordInput.type = "password";
    }
  }