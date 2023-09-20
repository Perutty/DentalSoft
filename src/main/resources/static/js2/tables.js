$(document).ready(function() {
    $('#tablax').DataTable({
        paging: true,
        searching: false,
        ordering: false,
        info: true,
        lengthMenu: [5, 10, 20],
        "oLanguage": {
            "oPaginate": {
                "sPrevious": "Anterior",
                "sNext": "Siguiente"
            }
        }
    });
});
