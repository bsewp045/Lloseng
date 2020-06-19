// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import ocsf.server.*;
import common.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer
{
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Instance variables************************************************
  /**
  *The interface type variable
  */
  ChatIF serverUI;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  {

    super(port);
    serverUI = new ServerConsole(port, this);
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {

    String message = (String) msg;
    if( message.length() >= 8 && (message.substring(0,6).equals("#login")) ){
      if(client.getInfo(client.toString()) == null){
          client.setInfo(client.toString(), msg);
          System.out.println("Message received: " + msg + " from " + client);
      }else{
        try{
          client.sendToClient("login command can't be issued twice");
        }catch(Exception e){

        }

      }

    }else{
      if(client.getInfo(client.toString()) == null){
        System.out.println("No login command sent to server");
        try{
          client.close();
        }catch (Exception e){

        }

      }else{
        System.out.println("Message received: " + msg + " from " + client);
        Object mes =  (Object) ((String)client.getInfo(client.toString()) + " " + (String) msg);
        this.sendToAllClients(mes);
      }

    }

  }

  public void handleMessageFromServerUI(Object msg){
    serverUI.display((String) msg);
    Object message = (Object) ("SERVER MSG> " + (String) msg);
    this.sendToAllClients(message);
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  protected void clientConnected(ConnectionToClient client) {
      System.out.println("A new client is attempting to connect to the server ");
  }

  synchronized protected void clientDisconnected(
    ConnectionToClient client) {
      System.out.println("Client is disconnected");
  }

  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
      try{
        client.close();
      } catch(Exception e){

      }

  }



}
//End of EchoServer class
