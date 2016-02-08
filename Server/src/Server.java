import java.io.*;
import java.net.*;

public class Server {

    private static final int PORT = 9000;
    private ServerSocket serverSocket;
    Socket socket;

    //DataInputStream inputFromClient;
    DataOutputStream outputToClient;
    PrintWriter out;
    BufferedReader in;

    public static void main(String[] args) {
        int port = PORT;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        new Server(port);
    }

    public Server(int port) {
        //PrintWriter outToClient1;
        //BufferedReader inPutFromClient1;

        // create a server socket
        try {
            serverSocket = new ServerSocket(port);

            System.out.println("Creating Server...");
        } catch (IOException e) {
            System.err.println("Error in creation of the server socket");;
            System.exit(0);
        }

        while (true) {
            try {
                // listen for a connection
                System.out.println("Waiting for connection...");
                socket = serverSocket.accept();
                System.out.println("Client Connected");
                outputToClient = new DataOutputStream(socket.getOutputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Connection succssed");

                String test = in.readLine();
                System.out.println("Client connected on IP: " + test);

                Protocol p = new Protocol();

                while (true) {

                    String typo = in.readLine();
                    System.out.println("Client: " + typo);
                    String syntax = p.checkForCommands(typo);
                    if (typo.equals("List") || typo.equals("apa")) {
                        //if (!syntax.equals("")) {
                        System.out.println("Server: " + syntax);

                        //}
                        out.println(syntax);
                        out.flush();
                    } else if (typo.charAt(0) == 'd'&& typo.charAt(1) == 'l') {
                        
                        dlFile(syntax);
                    }

                }

            } catch (IOException e) {
                System.out.println("Client failed to connect!");
            }
        }
    }

    private void dlFile(String path) {
        FileInputStream fIS = null;
        BufferedInputStream bIS = null;
        OutputStream os = null;
        try {
            File dlfile = new File(path);
            byte[] filebyte = new byte[(int) dlfile.length()];
            fIS = new FileInputStream(dlfile);
            bIS = new BufferedInputStream(fIS);
            bIS.read(filebyte, 0, filebyte.length);
            os = socket.getOutputStream();
            System.out.println("skickar fil");
            out.println(filebyte.length);
            out.println(dlfile.getName());
            os.write(filebyte, 0, filebyte.length);
            os.flush();
            out.flush();
            System.out.println("done");
        } catch (IOException ioe) {

        } finally {
//            try{
//                System.out.println("");
//            }catch(IOException ioe){
//                
//            }
        }

    }

    private void shutDownHook(Socket socket) {
        System.out.println("Applying safe exit hook");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    socket.close();
                    serverSocket.close();
                    System.out.println("Connection Closed.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        ));
    }

}