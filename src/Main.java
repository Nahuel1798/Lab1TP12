import java.sql.*;
import java.time.LocalDate;
import java.time.Month;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Nahue
 */
public class Main {

    public static void main(String[] args) {
        Connection miConexion;
        try {
            String CLASSPATH = "com.mysql.cj.jdbc.Driver";
            String URL = "jdbc:mysql://localhost:3308/tp12";
            String USUARIO = "root";
            String PASSWORD = "";
            // 1. Cargar el driver
            Class.forName(CLASSPATH);
            // 2. Establecer conexion
            miConexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("No se pudo encontrar el driver");
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            System.out.println("[SQL EXCEPTION " + e.getErrorCode() + "] " + e.getMessage());
            e.printStackTrace();
            return;
        }
        //ingreso empleados
        Empleado empleados[] = null;
        try {
            Empleado empleado1 = new Empleado(1, 11000111, "Nahuel", "Lucero", 2, true);
            Empleado empleado2 = new Empleado(2, 37666666, "Leonel", "Nievas", 3, true);
            Empleado empleado3 = new Empleado(3, 40000444, "Nahuel", "Ochoa", 4, true);
            empleados = new Empleado[]{empleado1, empleado2, empleado3};
            
            String insert="INSERT INTO empleado(`id_empleado`, `dni`, `apellido`, `nombre`, `acceso`, `estado`) VALUES (?,?,?,?,?,?)";
            for(int i=0;i<empleados.length;i++){
                PreparedStatement ps = miConexion.prepareStatement(insert);
                ps.setInt(1, empleados[i].getId_empleado());
                ps.setLong(2, empleados[i].getDni());
                ps.setString(3, empleados[i].getApellido());
                ps.setString(4, empleados[i].getNombre());
                ps.setInt(5, empleados[i].getAcceso());
                ps.setBoolean(6, empleados[i].isEstado());
                int filas=ps.executeUpdate();
                if(filas>0){
                    System.out.println("Se ingreso el empleado");
                }
            }
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if (errorCode != 1062) { // Ignorar empleado repetidos
                System.out.println("[Error " + errorCode + "] " + e.getMessage());
                e.printStackTrace();
            } else {
                System.out.println("[Empleado repetido] " + e.getMessage());
            }
        }
        //ingreso herrramientas
        Herramienta herramientas[]=null;
        try {
            Herramienta herramienta1 = new Herramienta(1, "Martillo", "carpintero", 7, true);
            Herramienta herramienta2 = new Herramienta(2, "Tenezas", "armador", 11, true);
            herramientas = new Herramienta[]{herramienta1,herramienta2};
            
            String insert="INSERT INTO `herramienta`(`id_herramienta`, `nombre`, `descripcion`, `stock`, `estado`) VALUES (?,?,?,?,?)";
            for(int i=0;i<herramientas.length;i++){
                PreparedStatement ps = miConexion.prepareStatement(insert);
                ps.setInt(1,herramientas[i].getId_herramienta());
                ps.setString(2, herramientas[i].getNombre());
                ps.setString(3, herramientas[i].getDescripcion());
                ps.setInt(4, herramientas[i].getStock());
                ps.setBoolean(5, herramientas[i].isEstado());
                int filas=ps.executeUpdate();
                if(filas>0){
                    System.out.println("Se ingreso la herramienta");
                }
            }
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if (errorCode != 1062) { // Ignorar herramienta repetidas
                System.out.println("[Error " + errorCode + "] " + e.getMessage());
                e.printStackTrace();
            } else {
                System.out.println("[Herramienta repetida] " + e.getMessage());
            }
        }
        //listar todas las herramientas con stock superior a 10
        try{
            String lista="SELECT * FROM herramienta WHERE id_herramienta IN(SELECT id_herramienta FROM herramienta WHERE stock >8)";
            PreparedStatement ps = miConexion.prepareStatement(lista);
            ResultSet res = ps.executeQuery();
            while(res.next()){
                int id_herramienta=res.getInt("id_herramienta");
                String nombre=res.getString("nombre");
                String descripcion=res.getString("descripcion");
                int stock=res.getInt("stock");
                boolean estado=res.getBoolean("estado");
                System.out.println("Las herramientas que poseen stock mayor a 10 son: ");
                System.out.println("id_herramienta: " + id_herramienta);
                System.out.println("Nombre: " + nombre);
                System.out.println("Descripcion: " + descripcion);
                System.out.println("Stock: " + stock);
                System.out.println("Estado: " + estado);
            }
        } catch (SQLException e) {
            System.out.println("[Error " + e.getErrorCode() + "] " + e.getMessage());
            e.printStackTrace();
            return;
        }
        //Dar de baja el primer empleado ingresado a la base de datos
        try{
            String delete="DELETE FROM empleado WHERE id_empleado = (SELECT MIN(id_empleado) FROM empleado);";
            PreparedStatement ps = miConexion.prepareStatement(delete);
            int filas = ps.executeUpdate();
            delete = "SELECT * FROM empleado";
            ps = miConexion.prepareStatement(delete);
            ResultSet res = ps.executeQuery();
            while(res.next()){
                int id_empleado=res.getInt("id_empleado");
                int dni=res.getInt("dni");
                String nombre=res.getString("nombre");
                String apellido=res.getString("apellido");
                int acceso=res.getInt("acceso");
                boolean estado=res.getBoolean("estado");
                System.out.println("Los empleados que quedan son:");
                System.out.println("Id: " + id_empleado);
                System.out.println("Dni"+ dni);
                System.out.println("Nombre: " + nombre);
                System.out.println("Apellido: " + apellido);
                System.out.println("Acceso: " + acceso);
                System.out.println("Estado: " + estado);
                
            }
        } catch (SQLException e) {
            System.out.println("[Error " + e.getErrorCode() + "] " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }
    
}
