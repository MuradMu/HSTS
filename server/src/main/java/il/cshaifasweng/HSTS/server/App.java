package il.cshaifasweng.HSTS.server;

import org.hibernate.Session;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    private static SimpleServer server;
    private static Session session;
    public static void main( String[] args ) throws IOException
    {
        try{
            session = ConnectToDatabase.initializeDatabase();
            server = new SimpleServer(3000, session);
            server.listen();
            System.out.print("\nListening \n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
