//nombre del cual paquete pertence
package SoftDBManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

//manejar base de datos
//usa el patron Singleton
public class DBManager {
    //instancia unica de la clase (tipo static para que sea metodo de la clase)
    //y la conexion a la base de datos
    private static DBManager dbManager;
    private Connection dbConection;
    //archivo con propiedades
    private final String pathPropertiesFile = "recursos/DBProperties.txt";
    //datos del url de la base de datos
    private String url_db;
    private String protocol;
    private String subprotocol;
    private String hostname;
    private String portNumber;
    private String dbName;
    private String driver; //cual JDBC driver usaran
    //datos del usuario
    private String user;
    private String password;
    
    //solo puede devolver variables static
    public static DBManager getInstance() throws IOException, SQLException{
        if(DBManager.dbManager == null){
            //crear instancia
            return DBManager.createInstance();
        }
        return DBManager.dbManager;
    }
    private static DBManager createInstance() throws IOException, SQLException{
        //llama al constructor
        DBManager.dbManager = new DBManager();
        return DBManager.dbManager;
    }
    //constructor privado
    private DBManager() throws IOException, SQLException{
        //establecer las propiedades de la base de datos
        leer_propiedades_db();
        //encriptar la contraseña antes
        //realizar la conexion ya con los datos seteador con Connection
        this.dbConection = DriverManager.getConnection(this.url_db,this.user,this.password);
    }
    private void leer_propiedades_db() throws FileNotFoundException, IOException{
        //clase Properties lo maneja si recibe un stream de formato (tipo=valor)
        Properties prop = new Properties();
        prop.load(new FileInputStream(new File(this.pathPropertiesFile)));
        //setear los valores segun los tipo
        this.driver = prop.getProperty("driver");
        this.dbName = prop.getProperty("dbName");
        this.portNumber = prop.getProperty("portNumber");
        this.protocol = prop.getProperty("protocol");
        this.hostname = prop.getProperty("hostname");
        this.user = prop.getProperty("user");
        this.password = prop.getProperty("password");
        //concatenar en formato de mysql
        this.url_db = protocol + ":" + subprotocol + "//" + hostname + ":" + portNumber + "/" + dbName;
    }
    //metodo para obtener la conexion a la BD y poder enviar queries
    public Connection getConnection(){
        return this.dbConection;
    }
}
