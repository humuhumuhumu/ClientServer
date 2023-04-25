import java.io.*;
import java.net.*;

import static java.lang.System.exit;

class TCPClient {

    public static void main(String[] arg) throws Exception
    {
        System.out.println("Client Started");
        String sentence;
        String modifiedSentence;

        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        // Name and Port input from client
        System.out.print("Enter your name: ");
        String name = inFromUser.readLine();
        System.out.print("Enter Port: ");
        String p = inFromUser.readLine();

        int port = 6789; // Number is used as a place holder until the user enters the port
        if(isInteger(p)){
            port = Integer.parseInt(p);
        }else{
            System.out.println("Use a integer not a string pls");
            exit(-1);
        }

        // Server that you are connecting to is 127.0.0.1 or the localhost
        Socket clientSocket = new Socket("127.0.0.1", port);

        // Initialize input and output streams.
        DataOutputStream outToServer =
                new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer =
                new BufferedReader(new
                        InputStreamReader(clientSocket.getInputStream()));


        // CHECKING FOR CONNECTION
        if(inFromServer.readLine().equals("1")){
            System.out.println("Successfully Connected");
            outToServer.writeBytes(name + '\n'); //Send name upon connection
        } else{
            System.out.println("L");
            System.exit(-1);
        }

        // Sending and receiving commands, until the client decides to exit the connection.
        while(true) {
            System.out.print("> ");
            sentence = inFromUser.readLine().toLowerCase();
            // EXIT
            if(sentence.equals("exit")){
                outToServer.writeBytes(sentence + '\n');
                break;
            }

            // SENDS TO SERVER THE COMMAND
            outToServer.writeBytes(sentence + '\n');

            // RESULT
            modifiedSentence = inFromServer.readLine();

            System.out.println(modifiedSentence);

        }
        clientSocket.close();

    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}