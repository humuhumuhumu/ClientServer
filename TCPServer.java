import java.io.*;
import java.net.*;

class TCPServer {

    public static void main(String argv[]) throws Exception
    {
        int port;

        //System.out.println("Input the port that you want the server to use: ");

        BufferedReader inFromServer =
                new BufferedReader(new InputStreamReader(System.in));

        //port = Integer.valueOf(inFromServer.readLine());
        //ServerSocket welcomeSocket = new ServerSocket(port);


        ServerSocket welcomeSocket = new ServerSocket(6789);

        while(true) {

            Socket connectionSocket = null;

            try{
                connectionSocket = welcomeSocket.accept();
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("very poggers");
            }
            System.out.println("try to push connection tothread");
            new Thread(new serverThread(connectionSocket)).start();
            System.out.println("successfully pushed it to thread");
        }
    }
}

class serverThread extends Thread{
    private Socket clientSocket;
    public serverThread(Socket clientSocket){
        super("serverThread");
        this.clientSocket = clientSocket;
    }

    public void run(){
        try{
            System.out.println("Threads???/");
            // initializeing input output streams
            String input, output;
            BufferedReader inFromClient =
                    new BufferedReader(new
                            InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream  outToClient =
                    new DataOutputStream(clientSocket.getOutputStream());

            // authentication or something


            // receiving and sending back commands
            while(true) {
                input = inFromClient.readLine();
                System.out.println("help");
                //terminating
                if (input.equals("exit")){
                    System.out.println("Closing a connection");
                    break;
                }
                System.out.println("inb4 writing");

                // do stuff here

                outToClient.writeBytes(input + "test");

                System.out.println("done writing");
            }


         inFromClient.close();
         outToClient.close();
         clientSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}

           