package co.empresa.dentalsoft.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import co.empresa.dentalsoft.model.Cita;
import co.empresa.dentalsoft.model.Evolucion;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class HistoriaClinicaExport {
	
	private List<Evolucion> evoluciones;
	private List<Cita> citas;
	private String nombre;

	public HistoriaClinicaExport(List<Evolucion> evoluciones, List<Cita> citas, String nombre) {
		super();
		this.evoluciones = evoluciones;
		this.citas = citas;
		this.nombre = nombre;
	}
	
	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(documento, response.getOutputStream());
		writer.setPageEvent(new EncabezadoEvento());
		
		Date fechaActual = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);

        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);
        int segundos = calendar.get(Calendar.SECOND);
		
		documento.open();
		
		Chunk saltolinea = new Chunk("\n");
		Chunk espacio = new Chunk("																	");
		Font fuente = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
		Font fuente2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11);
		Font fuente3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC);
		Paragraph titulo = new Paragraph("HISTORIA CLÍNICA DEL PACIENTE ",fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		Paragraph nombrePaciente = new Paragraph(nombre.toUpperCase(), fuente);
		nombrePaciente.setAlignment(Paragraph.ALIGN_CENTER);
		
		documento.add(titulo);
		documento.add(nombrePaciente);
		documento.add(saltolinea);
		documento.add(saltolinea);
		Paragraph fechageneracion = new Paragraph("Generado el día "+dia+" del mes "+mes+" del año "+año+" a las "+h+":"+minutos+":"+segundos, fuente3);
		fechageneracion.setAlignment(Paragraph.ALIGN_RIGHT);
		documento.add(fechageneracion);
		documento.add(saltolinea);
		documento.add(saltolinea);
		for (Evolucion evolucion : evoluciones) {
			for(Cita cita : citas) {
				if(evolucion.getCita_id().equals(cita.getId())) {
				SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
				String fechaFormateada = formatoFecha.format(cita.getFecha());
				Chunk motivo = new Chunk("Motivo cita:	 ", fuente);
				Chunk tituloEvolucion = new Chunk(cita.getTratamiento_cod(),fuente2);
				Chunk fecha = new Chunk("Fecha: 	", fuente);
				Chunk hora = new Chunk("Hora: 	", fuente);
				Chunk fechainfo = new Chunk(fechaFormateada,fuente2);
				Chunk horainfo = new Chunk(cita.getHora(),fuente2);
				Chunk odontologo = new Chunk("Odontólogo:	 ", fuente);
				Chunk odontologoinfo = new Chunk("DR. "+cita.getOdontologo_doc(),fuente2);
				documento.add(motivo);
				documento.add(tituloEvolucion);
				documento.add(espacio);
				documento.add(fecha);
				documento.add(fechainfo);
				documento.add(espacio);
				documento.add(hora);
				documento.add(horainfo);
				documento.add(saltolinea);
				documento.add(odontologo);
				documento.add(odontologoinfo);
				documento.add(saltolinea);
				Chunk descripcion = new Chunk("Descripción: ", fuente);
				Chunk descripcioninfo = new Chunk(evolucion.getDescripcion(),fuente2);
				documento.add(descripcion);
				documento.add(descripcioninfo);
			}
		   }
			
			documento.add(saltolinea);
			documento.add(saltolinea);
			documento.add(saltolinea);
		}
		documento.close();
	}
	
	class EncabezadoEvento extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document documento) {
            try {
                Image imagen = Image.getInstance("https://i.imgur.com/ejtLfAQ.png");
                float paginaAncho = documento.getPageSize().getWidth();
                float paginaAlto = documento.getPageSize().getHeight();
                float imagenAncho = imagen.getScaledWidth();
                float imagenAlto = imagen.getScaledHeight();

                float x = (paginaAncho - imagenAncho) / 2;
                float y = paginaAlto - 5 - (imagenAlto / 2);

                imagen.setAbsolutePosition(x, y);
                imagen.scaleAbsolute(110, 50);
                PdfContentByte cb = writer.getDirectContent();
                cb.addImage(imagen);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
}
