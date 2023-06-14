const deletePaciente = document.querySelectorAll('.delete');
const deleteOdontologo = document.querySelectorAll('.deleteOdontologo');
const deleteCita = document.querySelectorAll('.deleteCita');
const confirmarCita = document.querySelector('#confirmarCita');
const confirmar = document.querySelector('#confirmar');
const confirmarPaciente = document.querySelector('#confirmarPaciente');
const confirmarOdonto = document.querySelector('#confirmarOdonto');
const searchInput = document.getElementById('search-input');
const table = document.getElementById('tablax');
const horasDisponibles = document.querySelector('.horasDisponibles');

const agregar = document.getElementById('agregar');
const fechacita = document.getElementById('fechacita');

let cita = [];

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

function togglePassword() {
    var passwordInput = document.getElementById("password");
    if (passwordInput.type === "password") {
      passwordInput.type = "text";
    } else {
      passwordInput.type = "password";
    }
  }
  
if(agregar){
	agregar.addEventListener('click', function(){
		const fechascitas = document.querySelectorAll('td.fecha');
		const horascitas = document.querySelectorAll('td.hora');
		
		fechascitas.forEach((f, index)=>{
				var fecha = f.textContent;
				var hora = horascitas[index].textContent;
				const fechahora = [fecha, hora];
				cita.push(fechahora);
			});
		});
}

if (fechacita) {
  fechacita.addEventListener('change', function() {
    fechaInput = fechacita.value;
    const selectHora = document.querySelector('select[name="hora"]');
    const fechaX = fechaInput.split("-");
    const fechaFormateada = `${fechaX[2]}/${fechaX[1]}/${fechaX[0]}`;
    
    selectHora.querySelectorAll('option').forEach(opcion => {
      opcion.disabled = false;
      opcion.style.color = "green"; // Habilitar todas las opciones
    });
    
    for (let i = 0; i < cita.length; i++) {
      const citaFecha = cita[i][0];
      const citaHora = cita[i][1];
      
      if (fechaFormateada === citaFecha) {
        const opcionHora = selectHora.querySelector(`option[value="${citaHora}"]`);
        if (opcionHora) {
          opcionHora.disabled = true;
          opcionHora.style.color = "red"; // Deshabilitar opción ocupada
        }
      }
    }
  });
}


