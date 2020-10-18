package examenmarzo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Date;
import java.util.Objects;
import org.bson.Document;

/**
 *
 * @author Montejeitor
 */
public class DatoExportado {
    
        
    private String nombreAlumno;
    private String apellidosAlumno;
    private Date fechaExamen;
    private String asignatura;
    private double nota;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.nombreAlumno);
        hash = 79 * hash + Objects.hashCode(this.apellidosAlumno);
        hash = 79 * hash + Objects.hashCode(this.fechaExamen);
        hash = 79 * hash + Objects.hashCode(this.asignatura);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.nota) ^ (Double.doubleToLongBits(this.nota) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DatoExportado other = (DatoExportado) obj;
        if (this.nota != other.nota) {
            return false;
        }
        if (!Objects.equals(this.nombreAlumno, other.nombreAlumno)) {
            return false;
        }
        if (!Objects.equals(this.apellidosAlumno, other.apellidosAlumno)) {
            return false;
        }
        if (!Objects.equals(this.asignatura, other.asignatura)) {
            return false;
        }
        if (!Objects.equals(this.fechaExamen, other.fechaExamen)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatoExportado{" + "nombreAlumno=" + nombreAlumno + 
                ", apellidosAlumno=" + apellidosAlumno + ", fechaExamen=" 
                + fechaExamen + ", asignatura=" + asignatura + ", nota=" +
                nota + '}';
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getApellidosAlumno() {
        return apellidosAlumno;
    }

    public void setApellidosAlumno(String apellidosAlumno) {
        this.apellidosAlumno = apellidosAlumno;
    }

    public Date getFechaExamen() {
        return fechaExamen;
    }

    public void setFechaExamen(Date fechaExamen) {
        this.fechaExamen = fechaExamen;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public DatoExportado() {
    }

    public DatoExportado(String nombreAlumno, String apellidosAlumno, 
            Date fechaExamen, String asignatura, double nota) {
        this.nombreAlumno = nombreAlumno;
        this.apellidosAlumno = apellidosAlumno;
        this.fechaExamen = fechaExamen;
        this.asignatura = asignatura;
        this.nota = nota;
    }
    
    public static void guardar (MongoDatabase basedatos, DatoExportado dato) {
        
        MongoCollection<Document> coleccion = 
                basedatos.getCollection("DatoExportado");

        String nombreAlumno1 = dato.nombreAlumno;
        String apellidosAlumno1 = dato.apellidosAlumno;
        Date fechaExamen1 = dato.fechaExamen;
        String asignatura1 = dato.asignatura;
        double nota1 = dato.nota;
        
        Document Datos = new Document();

        Datos.append("nombreAlumno", nombreAlumno1);
        Datos.append("apellidosAlumno", apellidosAlumno1);
        Datos.append("fechaExamen", fechaExamen1);
        Datos.append("asignatura", asignatura1);
        Datos.append("nota", nota1);
        
        coleccion.insertOne(Datos);
    }

}
