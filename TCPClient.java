import java.io.*;
import java.net.*;
class TCPClient {

    public static void main(String[] arg) throws Exception
    {
        System.out.println("Client Started");
        String sentence;
        String modifiedSentence;

        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket("127.0.0.1", 6789);

        DataOutputStream outToServer =
                new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer =
                new BufferedReader(new
                        InputStreamReader(clientSocket.getInputStream()));

        while(true) {
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
}

        