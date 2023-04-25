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
                /*
                Whenever a connection comes through the welcome socket,
                create a thread to allow synchronized processing
                 */
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
            // Initializing input, output streams
            String input, output;
            BufferedReader inFromClient =
                    new BufferedReader(new
                            InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient =
                    new DataOutputStream(clientSocket.getOutputStream());

            // Start recording user time
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

            // Creating User Log
            String logName = name + "-Log.txt";
            File userlog = new File(logName);
            boolean fileExists = userlog.exists();
            if (userlog.createNewFile())
                System.out.println("User log created");
            else
                System.out.println("User log exists");

            FileWriter logFile = new FileWriter(userlog, true);
            if(fileExists)
                logFile.write(System.lineSeparator());
            BufferedWriter fileWrite = new BufferedWriter(logFile);
            fileWrite.write(name + " connected " + start);

            // Receiving and sending back commands
            while((input = inFromClient.readLine())!=null) {
                // Terminating
                if (input.equals("exit")){

                    // log duration calculation + writing to file
                    end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    long connectionTime = timeElapsed.toMillis();

                    if(timeElapsed.toMinutes() > 0) {
                        long minutes = timeElapsed.toMinutes();
                        long seconds = timeElapsed.minusMinutes(minutes).getSeconds();
                        fileWrite.newLine();
                        fileWrite.write(name + " disconnected, log duration: " + minutes + " minutes " + seconds + " seconds");
                    }
                    else if(timeElapsed.toSeconds() > 0) {
                        long seconds = timeElapsed.toSeconds();
                        connectionTime = timeElapsed.minusSeconds(seconds).toMillis();
                        fileWrite.newLine();
                        fileWrite.write(name + " disconnected, log duration: " + seconds + " seconds " + connectionTime + " ms");
                    }
                    else{
                        fileWrite.newLine();
                        fileWrite.write(name + " disconnected, log duration: " + connectionTime + "ms");
                    }

                    System.out.println("Closing connection: " + name);
                    break;
                }
                System.out.println(name + ": "+ input);

                // return calculation if input is a valid calculation and log the calculation request
                output = maths(input);
                if(output.matches("\\d+")) {

                }
                fileWrite.newLine();
                fileWrite.write(name + ": " + input + " Server: " + output);

                outToClient.writeBytes(output + '\n');
                System.out.println("Server: " + output);
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

    // How the math string is parsed
    public String maths(String s){
        int index = 0;

        s = s.replaceAll("\\s", "");

        for(int i = 0; i < s.length(); i++){
            // CHECK FOR OPERATION
            // Find the index of the operation, while also verifying that the first number is indeed a number
            switch (s.charAt(i)){
                case '*':
                case '/':
                case '+':
                case '-':
                    index = i;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    continue;
                default:
                    index = -1;
                    break;
            }
        }
        if(index == -1){
            return "NOT A VALID MATH CALCULATION";
        }

        // Grab first number
        String firstNum = s.substring(0, index);
        String secondNum = "";
        // Checks if the part after the operation is a number
        if(isInteger(s.substring(index+1))){
            secondNum = s.substring(index+1);

            switch (s.charAt(index)){
                case '+':
                    return Integer.parseInt(firstNum) + Integer.parseInt(secondNum) + "";
                case '-':
                    return Integer.parseInt(firstNum) - Integer.parseInt(secondNum) + "";
                case '*':
                    return Integer.parseInt(firstNum) * Integer.parseInt(secondNum) + "";
                case '/':
                    return Integer.parseInt(firstNum) / Integer.parseInt(secondNum) + "";
                default:
                    return "NOT A VALID MATH CALCULATION";
            }
        } else {
            // After the operation, if the string is not only numbers then it errors
            return "NOT A VALID MATH CALCULATION";
        }
    }


    public static boolean isInteger(String str) {
        System.out.println(str);
        boolean isInt = true;
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)){
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    continue;
                default:
                    isInt = false;
            }
        }
        return isInt;
    }
}