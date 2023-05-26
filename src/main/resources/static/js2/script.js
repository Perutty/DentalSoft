const deletePaciente = document.querySelectorAll('.delete');
const deleteOdontologo = document.querySelectorAll('.deleteOdontologo');
const confirmar = document.querySelector('#confirmar');
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

if(confirmar){
	confirmar.addEventListener('click', function(){
		window.location.href = '/paciente/delete/'+localStorage.getItem('documento');
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

if(confirmar){
	confirmar.addEventListener('click', function(){
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