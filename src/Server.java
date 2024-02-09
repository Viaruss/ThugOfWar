import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 12321;
    public static ArrayList<PrintWriter> writerList = new ArrayList();
    private static int connectedPlayers = 0;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Running tug of war server on port: " + PORT);
            ScoreHandler scoreHandler = new ScoreHandler();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                connectedPlayers += 1;
                System.out.println("Client connected on: " + clientSocket + "\nThere are currently " + connectedPlayers + " players connected");

                writerList.add(new PrintWriter(clientSocket.getOutputStream(), true));
                Thread client = new Thread(new ClientHandler(clientSocket, scoreHandler, writerList.get(writerList.size()-1)));
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void scoreUpdate(int score){
        for (PrintWriter writers : writerList){
            writers.println(score);
        }
    }

   private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter writer;
        private BufferedReader reader;
        private ScoreHandler scoreHandler;

        public ClientHandler(Socket cs, ScoreHandler sh, PrintWriter writ) {
            this.clientSocket = cs;
            this.scoreHandler = sh;
            this.writer = writ;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer.println("Successfully connected to the tug of war server");
                writer.println(scoreHandler.score);
                while(true){
                    int log = 0;
                    if (reader.ready()){
                        log = Integer.parseInt(reader.readLine());
                    }
                    if (log == 444){
                        break;
                    } else if(log == -1 || log == 1){ //-1 - left, 1 - right
                        int score = scoreHandler.addScore(log);
                        System.out.println(score);
                        scoreUpdate(score);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                writer.println(444);
                writerList.remove(writer);
                writer.close();
                connectedPlayers -= 1;
                System.out.println("Client disconnected on: " + clientSocket + "\nThere are currently " + connectedPlayers + " players connected");
                try{
                    reader.close();
                } catch (IOException ignored){}
            }
            if (connectedPlayers < 1)
                if (scoreHandler.score <= -20 || scoreHandler.score >= 20) {
                    System.out.println("Game finished, all players disconnected, closing down the server...");
                    System.exit(0);
                }
        }
    }

    private static class ScoreHandler {
        private int score = 0;
        synchronized public int addScore (int team) {
            score += team;
            return score;
        }
    }
}