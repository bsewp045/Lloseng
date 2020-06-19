import java.io.*;
import common.*;
import ocsf.server.*;
/**
*This class creates the UI for a server end-user.
*It implements the chat interface in order to activate the dispplay method
*/
public class ServerConsole implements ChatIF{

    /**
     * The default port to connect on.
     */
    final public static int DEFAULT_PORT = 5555;




    private EchoServer server; // The server to which the consoel communicates
    private int port;// The port number on which the server is listening


    //Constructors ****************************************************

    /**
     * Constructs an instance of the ServerConsole UI.
     *
     * @param port The port to connect on.
     *@param server The server to connect to.
     */
    public ServerConsole(int port, EchoServer server)
    {
        this.server = server;
        this.port = port;
    }


    public void accept() {
      try
      {
        BufferedReader fromConsole =
          new BufferedReader(new InputStreamReader(System.in));
        String message = "";


        while (true)
        {
          message = fromConsole.readLine();
          if(message.equals("#quit")){
            server.close();
            System.out.println("The server quit");

          }else if(message.equals("#stop")){
            server.stopListening();
          }else if(message.equals("#close")){
            server.stopListening();
            Thread[] connections = server.getClientConnections();
            for(int i = 0; i <= connections.length; i++){
              ((ConnectionToClient) connections[i]).close();
            }

          }else if(message.equals("#getport")){
            System.out.println(server.getPort());
          }else if( message.length() >= 10 && (message.substring(0,8)).equals("#setport")){
            if(!server.isListening()){
              int port = Integer.parseInt(message.substring(9));
              server.setPort(port);
            }else{
              System.out.println("To set the port, first close the server");
            }
          }else if(message.equals("#start")){
            if( !server.isListening()){
              server.listen();
            }else{
              System.out.println("Server is already listening.");
            }
          }else{
            server.handleMessageFromServerUI(message);
          }
        }
        }catch (Exception ex){


        System.out.println
          ("Unexpected error while reading from console!");
      }

    }


    public void display(String message)
    {
      System.out.println("SERVER MSG> " + message);
    }

    public static void main(String[] args)
    {
      int port = 0; //Port to listen on

      try
      {
        port = Integer.parseInt(args[0]); //Get port from command line
      }
      catch(Throwable t)
      {
        port = DEFAULT_PORT; //Set port to 5555
      }


      EchoServer sv = new EchoServer(port);
      ServerConsole serverUI = new ServerConsole(port, sv);

      try
      {

        sv.listen(); //Start listening for connections
        serverUI.accept();
      }
      catch (Exception ex)
      {
        System.out.println(ex);
        System.out.println("ERROR - Could not listen for clients!");
      }
    }



  }
