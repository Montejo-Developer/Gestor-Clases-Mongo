
package examenmarzo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Montejeitor
 */

public class ExamenMarzo {

    static Scanner scan = new Scanner(System.in);
    
    public static void main(String[] args) throws SQLException, 
            ClassNotFoundException {
        
        boolean salir = false;
        
        do {
            
            mostrarMenu();
            String opcion = scan.nextLine();
            
            switch (opcion) {
                
                case "1":
                    anyadirExamen();
                    break;
                    
                case "2":
                    System.out.println("1- Buscar asignatura?");
                    System.out.println("2- Buscar Texto?");
                    String buscar = scan.nextLine();
                    
                    if (buscar.equals("1")) {
                        buscarAsignatura();
                    }
                    
                    else if (buscar.equals("2")) {
                        buscarTexto();
                    }
                    
                    else {
                        System.out.println("Opcion no valida");
                    }
                    
                    break;
                    
                case "3":
                    modificarExamen();
                    break;
                    
                case "4":
                    //TODO
                    break;
                    
                case "5":
                    anyadirAlumnos();
                    break;
                    
                case "6":
                    anyadirNotas();
                    break;
                    
                case "7":
                    consultarNotas();
                    break;
                    
                case "8":
                    consultarResultados();
                    break;
                    
                case "9":
                    //TODO
                    break;
                    
                case "10":
                    exportarMongo();
                    break;
                    
                case "s":
                    salir = true;
                    break;
                    
                case "S":
                    salir = true;
                    break;
                    
                    
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
            
        } while (!salir);
        
    }
    
    public static void mostrarMenu() {
        System.out.println("1- Añadir examen");
        System.out.println("2- Buscar en los examenes");
        System.out.println("3- Modificar examen");
        System.out.println("4- Buscar errores en los examenes");
        System.out.println("5- Añadir alumno");
        System.out.println("6- Añadir nota");
        System.out.println("7- Consultar notas de un alumno");
        System.out.println("8- Consultar resultados de un examen");
        System.out.println("9- Buscar errores en los alumnos");
        System.out.println("10- Exportar a MongoDB");
        System.out.println("S- Salir");
    }
    
    public static void anyadirExamen() {
        
        System.out.print("Introduzca el código del examen: ");
        int id = Integer.parseInt(scan.nextLine());
        
        System.out.print("Introduzca la fecha: ");
        String fechaTexto = scan.nextLine();
        DateFormat fecha = null;
        
        try {
            fecha.parse(fechaTexto);
            
        } catch (ParseException ex) {
            Logger.getLogger(ExamenMarzo.
                    class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.print("Introduzca la asignatura: ");
        String asignatura = scan.nextLine();
        
        String texto = "";
        do {
            
            System.out.println("Introduzca el texto del examen");
            texto = scan.nextLine();
        
        } while (texto.equals(""));      
        
        Session sesion =
        NewHibernateUtil.getSessionFactory().openSession();
        Transaction trans = sesion.beginTransaction();
        
        Examen examen = new Examen(id, fecha, asignatura, texto);
        sesion.save(examen);
        trans.commit();
        sesion.close();

    }
    
    public static void buscarAsignatura () {
        
        Session sesion =
            NewHibernateUtil.getSessionFactory().openSession();
        
        System.out.println("Dime la asignatra a buscar:");
        String asignatura = scan.nextLine();
        
        Query consulta =
            sesion.createQuery
        ("select asignatura from examen where asignatura like '" + asignatura 
        + "'");
        
        List resultados = consulta.list();
        
        for( Object resultado : resultados)
        {
            String titulo = (String) resultado;
            System.out.println ( titulo );
        }
        
        sesion.close(); 
    }
    
    public static void buscarTexto () {
        
        Session sesion =
            NewHibernateUtil.getSessionFactory().openSession();
        
        System.out.println("Dime el texto a buscar:");
        String texto = scan.nextLine();
        
        Query consulta =
            sesion.createQuery
        ("select texto from examen where texto like '%" + texto 
        + "%'");
        
        List resultados = consulta.list();
        
        for( Object resultado : resultados)
        {
            String titulo = (String) resultado;
            System.out.println ( titulo );
        }
        
        sesion.close(); 
    }
    
    public static void modificarExamen () {
        
        System.out.println("Dime el id a buscar");
        int idBuscado = scan.nextInt();
        
        Session sesion =
            NewHibernateUtil.getSessionFactory().openSession();
        
        Query consulta = sesion.createQuery(
            "FROM examen WHERE clave="+idBuscado);
        
        List resultados = consulta.list();
        
        Examen examenAModificar = (Examen) resultados.get(0);
        
        Transaction trans = sesion.beginTransaction();
        
        System.out.println("dime la nueva fecha:");
        String fechaTexto = scan.nextLine();
        DateFormat fecha = null;
        
        try {
            fecha.parse(fechaTexto);
            
        } catch (ParseException ex) {
            Logger.getLogger(ExamenMarzo.
                    class.getName()).log(Level.SEVERE, null, ex);
        }
        
        examenAModificar.setFecha(fecha);
        
         System.out.print("Introduzca la nueva asignatura: ");
        String asignatura = scan.nextLine();
        
        examenAModificar.setAsignatura(asignatura);
        
        String texto = "";
        do {
            
            System.out.println("Introduzca el nuevo texto del examen");
            texto = scan.nextLine();
        
        } while (texto.equalsIgnoreCase(""));      
        
        examenAModificar.setTexto(texto);
        
        sesion.update(examenAModificar);
        
        trans.commit();
        sesion.close();
    }
    
    public static void anyadirAlumnos()  throws
        ClassNotFoundException, SQLException {
        
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/Examenes";
        String usr = "postgres";
        String pass = "03059420";
        Connection con = DriverManager.getConnection(url, usr, pass);
        
        Statement statement = con.createStatement();
        
        System.out.println("Dime el codigo del alumno");
        int id = scan.nextInt();
        
        String nombre = "";
        do {
            
            System.out.println("Introduzca el nombre del alumno");
            nombre = scan.nextLine();
        
        } while (nombre.equals(""));   
        
        String apellidos = "";
        do {
            
            System.out.println("Introduzca el apellido del alumno");
            apellidos = scan.nextLine();
        
        } while (apellidos.equals(""));   
        
        String sentenciaSQL = "INSERT INTO alumno VALUES "+
            "('" + id + "','" + nombre + "','" + apellidos +"');";
        
        int cantidad = statement.executeUpdate(sentenciaSQL);
        
        System.out.println("Datos insertados: " + cantidad);
        
        con.close();
    }
    
    public static void anyadirNotas()  throws
        ClassNotFoundException, SQLException {
        
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/Examenes";
        String usr = "postgres";
        String pass = "03059420";
        Connection con = DriverManager.getConnection(url, usr, pass);
        
        Statement statement = con.createStatement();
        
        boolean notaCorrecta = false;    
        double nota;
        
        do {
            
            System.out.println("Dime la nota del examen");
            nota = scan.nextDouble();
            
            if((nota <= 10) && (nota >= 0))
                notaCorrecta =true;
            
        } while (!notaCorrecta);
        
        System.out.println("Dime el codigo del alumno");
        int idAlumno = scan.nextInt();
        
        System.out.println("Dime el codigo del examen");
        int idExamen = scan.nextInt();
        
        String sentenciaSQL = "INSERT INTO nota VALUES "+
            "('" + nota + "','" + idAlumno + "','" + idExamen +"');";
        
        int cantidad = statement.executeUpdate(sentenciaSQL);
        
        System.out.println("Datos insertados: " + cantidad);
        
        con.close();
    }
    
    public static void consultarNotas ()  throws
        ClassNotFoundException, SQLException {
        
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/Examenes";
        String usr = "postgres";
        String pass = "03059420";
        
        Connection con = DriverManager.getConnection(url, usr, pass);
        Statement statement = con.createStatement();
        
        System.out.println("Dime el codigo del alumno:");
        int codigo = scan.nextInt();
        
        String sentenciaSQL = "SELECT examen.fecha, examen.asignatura," +
                "nota.nota FROM examen, nota WHERE nota.codAlumno LIKE '" +
                codigo + "'";
        
        ResultSet rs = statement.executeQuery(sentenciaSQL);
        
        System.out.println("Fecha" + "\t" + "Asignatura" + "\t" + "Nota");
        System.out.println("-----------------------------------------");
        while (rs.next()) {
        System.out.println(rs.getString(1) + "\t " +
            rs.getString(2) + "\t" + rs.getString(3));
        }
        
        rs.close();
        con.close();
    
    }
    
    public static void consultarResultados ()  throws
        ClassNotFoundException, SQLException {
        
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/Examenes";
        String usr = "postgres";
        String pass = "03059420";
        
        Connection con = DriverManager.getConnection(url, usr, pass);
        Statement statement = con.createStatement();
        
        System.out.println("Dime el codigo del examen:");
        int codigo = scan.nextInt();
        
        String sentenciaSQL = "SELECT alumno.nombre, alumno.apellidos," +
                "nota.nota FROM alumno, nota WHERE nota.codExamen LIKE '" +
                codigo + "'";
        
        ResultSet rs = statement.executeQuery(sentenciaSQL);
        
        System.out.println("Nombre" + "\t" + "Apellidos" + "\t" + "Nota");
        System.out.println("-----------------------------------------");
        while (rs.next()) {
        System.out.println(rs.getString(1) + "\t " +
            rs.getString(2) + "\t" + rs.getString(3));
        }
        
        rs.close();
        con.close();
    
    }
    
    public static void exportarMongo ()  throws
        ClassNotFoundException, SQLException {
       
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/Examenes";
        String usr = "postgres";
        String pass = "03059420";
        Connection con = DriverManager.getConnection(url, usr, pass);

        Statement statement = con.createStatement();
        String sentenciaSQL = "SELECT alumno.nombre, alumno.apellidos," +
                "examen.fecha, examen.asignatura, nota.nota" +  ""
                + "FROM alumno,nota,examen";
        
        ResultSet rs = statement.executeQuery(sentenciaSQL);
        
        try {
            MongoClient cliente = new MongoClient();
            MongoDatabase db = cliente.getDatabase("Examenes");
            
            MongoCollection<Document> coleccion_datos = 
                    db.getCollection("DatoExportado");
            
            while (rs.next()) {
                
                DatoExportado dato = new DatoExportado(rs.getString(1),
                rs.getString(2),rs.getDate(3),rs.getString(4),rs.getDouble(5));
                
                DatoExportado.guardar(db, dato);
            }
                    
            System.out.println("Desconectando de MongoDB.");
            cliente.close();
                    
        }catch (MongoCommandException e) { 
            System.err.println(e.getErrorMessage());
        }
        
        rs.close();
        con.close();

    }
}
