import java.io.*;
import java.net.*;

class TCPServer {

    public static void main(String argv[]) throws Exception
    {
        System.out.println("Server started");

        int port;

        //System.out.println("Input the port that you want the server to use: ");

        BufferedReader inFromServer =
                new BufferedReader(new InputStreamReader(System.in));

        //port = Integer.valueOf(inFromServer.readLine());
        //ServerSocket welcomeSocket = new ServerSocket(port);


        ServerSocket welcomeSocket = new ServerSocket(6789);

        while(true) {

            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("try to push connection tothread");
            new Thread(new serverThread(connectionSocket)).start();
            System.out.println("successfully pushed it to thread");
        }
    }
}

class serverThread extends Thread{
    private Socket clientSocket;
    public serverThread(Socket socket){
        this.clientSocket = socket;
    }

    public void run(){
        try{
            System.out.println("Threads???/");
            // initializeing input output streams
            String input, output;
            BufferedReader inFromClient =
                    new BufferedReader(new
                            InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient =
                    new DataOutputStream(clientSocket.getOutputStream());

            // receiving and sending back commands
            while((input = inFromClient.readLine())!=null) {
                System.out.println("help");
                //terminating
                if (input.equals("exit")){
                    System.out.println("Closing a connection");
                    break;
                }
                System.out.println("input is : " + input);

                // do stuff here

                output = input + "test";

                outToClient.writeBytes("hi" + '\n');

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

           