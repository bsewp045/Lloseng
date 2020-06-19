// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF
{
  //Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Instance variables **********************************************

  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String loginId)
  {
    try
    {
      client= new ChatClient(host, port, this, loginId );
      String message = "#login " + loginId ;
      client.handleMessageFromClientUI(message);
      System.out.println(loginId + " has logged on");
    }
    catch(IOException exception)
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }


  //Instance methods ************************************************

  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the client's message handler.
   */
  public void accept()
  {
    try
    {

      BufferedReader fromConsole =
        new BufferedReader(new InputStreamReader(System.in));
      String message;


      while (true)
      {
        message = fromConsole.readLine();
        if(message.equals("#quit")){
          client.quit();
        }
        if(message.equals("#logoff")){
          try{

            client.closeConnection();
          }catch(Exception e){

          }

        }else if(message.length() >= 10 && (message.substring(0,8)).equals("#sethost")){
          if(! client.isConnected()){
            String msgg = message.substring(9);
            client.setHost(msgg);
            System.out.println("Host set to : " + client.getHost());
          }else{
            System.out.println("Client must be logged off to set host");
          }
        }else if(message.length() >= 10 && (message.substring(0,8)).equals("#setport") ){

          if(! client.isConnected()){
            int num = Integer.parseInt(message.substring(9));
            client.setPort(num);
            System.out.println("Port set to : " + client.getPort());
          }else{
            System.out.println("Client must be logged off to set port");
          }
        }else if(message.length() == 6 && message.equals("#login")){
          if( !client.isConnected()){
            try{
              client.openConnection();
              String msg = "#login " + client.getLoginId();
              client.handleMessageFromClientUI( msg);
              System.out.println(client.getLoginId() + " has logged on");
            }catch(Exception e){

            }
          }else{
            System.out.println("Client is already logged in");
          }
        }else if(message.equals("#gethost")){
          System.out.println(client.getHost());
        }else if(message.equals("#getport")){
          System.out.println(client.getPort());
        }else{
          client.handleMessageFromClientUI(message);
        }
      }
    }
    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message)
  {
    System.out.println("> " + message);
  }


  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args)
  {
    String host = "";
    int port = 0;  //The port number
    String loginId = "";

    try{
      if(args.length == 0){
        System.out.println("No login ID specified. Conenction aborted.");
      }else if(args.length == 3){
        loginId = args[0];
        host = args[1];
        port = Integer.parseInt(args[2]);
      }else {
        loginId = args[0];
        host = "localhost";
        port = Integer.parseInt(args[1]);
      }
    }catch(Exception e){
      loginId = args[0];
      host = "localhost";
      port = DEFAULT_PORT;

    }


    ClientConsole chat= new ClientConsole(host, port, loginId);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
