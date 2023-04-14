import java.io.*;
import java.net.*;

class TCPServer {

    public static void main(String[] argv) throws Exception
    {
        System.out.println("Server started");

        BufferedReader inFromServer =
                new BufferedReader(new InputStreamReader(System.in));

        ServerSocket welcomeSocket = new ServerSocket(6789);

        while(true) {
            try {
                Socket connectionSocket = welcomeSocket.accept();
                new Thread(new serverThread(connectionSocket)).start();
            }catch (Exception e){
                e.printStackTrace();
            }
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
            // initializeing input output streams
            String input, output;
            BufferedReader inFromClient =
                    new BufferedReader(new
                            InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient =
                    new DataOutputStream(clientSocket.getOutputStream());

            // receiving and sending back commands
            while((input = inFromClient.readLine())!=null) {
                //terminating
                if (input.equals("exit")){
                    System.out.println("Closing a connection");
                    break;
                }
                System.out.println("input is : " + input);

                // do stuff here

                output = input + "test";

                outToClient.writeBytes(output + '\n');
            }


         inFromClient.close();
         outToClient.close();
         clientSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}

           