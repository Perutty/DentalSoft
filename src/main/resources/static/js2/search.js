const searchInput = document.getElementById('search-input');

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