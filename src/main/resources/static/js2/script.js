const table = document.getElementById('tablax');
const horasDisponibles = document.querySelector('.horasDisponibles');

const agregar = document.getElementById('agregar');
const fechacita = document.getElementById('fechacita');

let cita = [];

const tableRows = table.getElementsByTagName('tr');
const tableHeader = table.getElementsByTagName('thead')[0];
const headerRow = tableHeader.getElementsByTagName('tr')[0];

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


