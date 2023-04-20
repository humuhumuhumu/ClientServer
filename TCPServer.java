import java.io.*;
import java.net.*;
import java.time.*;

class TCPServer {

    public static void main(String[] argv) throws Exception
    {
        System.out.println("Server started");

        BufferedReader inFromServer =
                new BufferedReader(new InputStreamReader(System.in));

        ServerSocket welcomeSocket = new ServerSocket(1234);

        while(true) {
            try {
                Socket connectionSocket = welcomeSocket.accept();

                Thread t = new Thread(new serverThread(connectionSocket));
                t.start();

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
            // initializing input output streams
            String input, output;
            BufferedReader inFromClient =
                    new BufferedReader(new
                            InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient =
                    new DataOutputStream(clientSocket.getOutputStream());

            Instant start, end;
            start = Instant.now();

            // Ack connection
            outToClient.writeBytes("1" + '\n');

            // read Name
            String name = "";
            if ((input = inFromClient.readLine())!= null){
                name = input;
                System.out.println(name + " has entered");
            }

            // creating user log
            String logName = name + "-Log.txt";
            File userlog = new File(logName);
            if (userlog.createNewFile())
                System.out.println("User log created");
            else
                System.out.println("User log exists");

            FileWriter logFile = new FileWriter(userlog);
            BufferedWriter fileWrite = new BufferedWriter(logFile);
            fileWrite.write(name + " connected " + start);

            // receiving and sending back commands
            while((input = inFromClient.readLine())!=null) {
                //terminating
                if (input.equals("exit")){
                    end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    fileWrite.newLine();
                    fileWrite.write(name + " disconnected, log duration: " + timeElapsed.toMillis() + "ms");
                    System.out.println("Closing a connection");
                    break;
                }
                System.out.println(name + ": "+ input);

                // do stuff here

                output = input + " test";

                outToClient.writeBytes(output + '\n');
            }

            fileWrite.flush();
            fileWrite.close();
            inFromClient.close();
            outToClient.close();
            clientSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}