const notificar = document.querySelectorAll('.notify');
const confirmarNotificacion = document.querySelector('#confirmarNotificacion');



if(notificar){
	notificar.forEach(function(notificar){
		notificar.addEventListener('click', function(){
			var fecha = this.closest('tr').querySelector('.fecha');
			var hora = this.closest('tr').querySelector('.hora');
			var documento = this.closest('tr').querySelector('.documento');
			var doctor = this.closest('tr').querySelector('.doctor');
			
			var numeroDocumento = documento.textContent;
			var horaContent = hora.textContent;
			var fechaDef = fecha.textContent;
			var odontologo = doctor.textContent;
			
			var partesFecha = fechaDef.split("/");
			var dia = partesFecha[0];
			var mes = partesFecha[1] - 1; // Los meses en JavaScript son 0-indexados (0 para enero, 11 para diciembre)
			var anio = partesFecha[2];
			
			// Crea un objeto Date con la fecha
			var fechaString = new Date(anio, mes, dia);
			
			// Obtiene el día de la semana como un número (0 para domingo, 1 para lunes, ..., 6 para sábado)
			var numeroDia = fechaString.getDay();
			var numeroMes = fechaString.getMonth();
			var diasSemana = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"];
			var nombreDia = diasSemana[numeroDia];
			var mesesDelAno = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
			var nombreMes = mesesDelAno[numeroMes];
			
			var fechaCompleta = nombreDia + " "+dia+" de "+nombreMes+" del año "+anio;
			var horaRecortada = horaContent.split(' - ')[0];
			var amPm = horaContent.split(' ')[3];
			var horaDef = horaRecortada + " "+amPm;
			
			localStorage.setItem('fecha', fechaCompleta);
			localStorage.setItem('documento', numeroDocumento);
			localStorage.setItem('hora', horaDef);
			localStorage.setItem('doctor', odontologo);
			console.log(fechaCompleta,numeroDocumento,horaDef);
			
		});
	});
}

if(confirmarNotificacion){
	confirmarNotificacion.addEventListener('click', function(){
		window.location.href = '/admin/enviarcorreo/'+localStorage.getItem('documento')+'/'+localStorage.getItem('fecha')+'/'+localStorage.getItem('hora')+'/'+localStorage.getItem('doctor');
	});
	localStorage.clear();
}

