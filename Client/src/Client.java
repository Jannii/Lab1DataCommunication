
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private static final int PORT = 9000;
    private static String SERVER = "Localhost";
    private static int tries = 0;
    private PrintWriter outPutToServer1;
    private BufferedReader inPutFromServer1;
    private Socket socket;
    private static String dlFolder;

    public static void main(String[] args) {
        try {
            setDlFolder(new File(".").getCanonicalPath());
        } catch (IOException ioe) {

        }

        Scanner serverIP = new Scanner(System.in);
        System.out.println("Current server are " + SERVER + " for other server print server ip number or just press enter for current!");
        String newIp = serverIP.nextLine();
        if (!"".equals(newIp)) {
            SERVER = newIp;
        }

        String server = SERVER;
        int port = PORT;

        if (args.length >= 1) {
            server = args[0];
        }
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }

        Client t = new Client();
        t.clientConnect(server, port);
    }

    public void checkForCommand(String server, int port, PrintWriter out, BufferedReader in) {

        try {

            String syntax = "";
            while (true) {

                Scanner sc = new Scanner(System.in);
                System.out.println(">>>>>");
                String typo = sc.nextLine();
                out.println(typo);
                out.flush();

                syntax = in.readLine();

                if (typo.equals("List") || typo.equals("dirr")) {

                    String[] splitRes = syntax.split(",");
                    for (int i = 0; i < splitRes.length; i++) {
                        System.out.println(splitRes[i]);
                    }
                }
                if (typo.charAt(0) == 'd' && typo.charAt(1) == 'l') {

                    System.err.println(syntax);

                    if (syntax.equals("gone")) {
                        System.out.println("File doesent exist");
                    } else {
                        System.out.println("Attempting to download");
                        getFile();
                    }
                }
            }

        } catch (IOException e) {
            retryException(server, port);
        }

    }

    public void clientConnect(String server, int port) {

        BufferedReader in = null;
        PrintWriter out = null;
        DataInputStream DIS = null;

        try {
            tries++;
            System.out.println("Attempting to connect to " + SERVER + ":" + PORT);
            socket = new Socket(server, port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            DIS = new DataInputStream(socket.getInputStream());

            System.out.println("Your Ip: " + socket.getInetAddress());

            out.println(socket.getInetAddress());
            out.flush();

            System.out.println("Connecion succeed");
        } catch (IOException ex) {
            retryException(server, port);
        }

        checkForCommand(server, port, out, in);

    }

    public void getFile() {

        InputStream is = null;
        BufferedOutputStream bOS = null;
        FileOutputStream fOS = null;
        BufferedReader in = null;
        int byteRead;
        int current = 0;
        try {

            is = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String FILE_SIZE = in.readLine();
            System.out.println("FILE_SIZE: " + FILE_SIZE);
            String FILE_NAME = in.readLine();
            System.out.println("FILE_NAME: " + FILE_NAME);
            fOS = new FileOutputStream(dlFolder + "\\" + FILE_NAME);
            bOS = new BufferedOutputStream(fOS);
            byte[] fileByte = new byte[Integer.parseInt(FILE_SIZE)];
            System.out.println(fileByte.length);
            System.out.println(dlFolder + "\\" + FILE_NAME);
            byteRead = is.read(fileByte, 0, fileByte.length);
            System.out.println("bytreRead: " + byteRead);
            current = byteRead;

            System.out.println("Current " + current);
            do {
                System.out.println("do while");
                byteRead = is.read(fileByte, current, (fileByte.length - current));
                System.out.println("byteRead: " + byteRead);
                if (byteRead >= -1) {
                    current += byteRead;
                }
            } while (Integer.parseInt(FILE_SIZE) > current);
            bOS.write(fileByte, 0, current);
            bOS.flush();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                System.out.println("close");
                if (fOS != null) {
                    fOS.close();
                }
                if (bOS != null) {
                    bOS.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void setDlFolder(String folder) {
        try {
            dlFolder = folder;
        } catch (Exception ioe) {

        }
    }

    public void retryException(String server, int port) {

        System.out.println("Failed to connect");
        System.out.println("Attempting to reconnect... " + tries);
        if (tries < 5) {
            clientConnect(server, port);
        } else {
            System.out.println("Failed.");
            System.exit(0);
        }
    }

    private void shutDownHook(Socket socket, DataInputStream inputFromServer, DataOutputStream outputToServer) {
        System.out.println("Applying safe exit hook");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    socket.close();
                    inputFromServer.close();
                    outputToServer.close();
                    socket.close();
                    System.out.println("Connection Closed.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        ));
    }

}
