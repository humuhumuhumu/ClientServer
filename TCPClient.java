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

        System.out.print("Enter Port: ");

        String p = inFromUser.readLine();
        int port = 6789;
        if(isInteger(p)){
            port = Integer.parseInt(p);
        }else{
            System.out.println("Use a integer not a string pls");
            exit(-1);
        }

        Socket clientSocket = new Socket("127.0.0.1", port);

        DataOutputStream outToServer =
                new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer =
                new BufferedReader(new
                        InputStreamReader(clientSocket.getInputStream()));


        // CHECKING FOR CONNECTION
        if(inFromServer.readLine().equals("1")){
            System.out.println("Successfully Connected");
        }else{
            System.out.println("L");
            System.exit(-1);
        }

        // SEND NAME IG
        System.out.print("Enter your name: ");
        outToServer.writeBytes(inFromUser.readLine() + '\n');

        while(true) {
            System.out.print("> ");
            sentence = inFromUser.readLine().toLowerCase();
            if(sentence.equals("exit")){
                break;
            }
            outToServer.writeBytes(sentence + '\n');

            modifiedSentence = inFromServer.readLine();

            System.out.println("FROM SERVER: " + modifiedSentence);

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

        