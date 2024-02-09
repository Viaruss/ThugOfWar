import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final int SERVER_PORT = 12321;
    private static int team = 0;
    public static int currentScore = 0, lastScore = 0;


    public static void main(String[] args) throws InterruptedException {

        try {
            Socket socket = new Socket("localhost", SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            Interface anInterface = new Interface(currentScore, writer);

            System.out.println("Server: " + reader.readLine());
            currentScore = Integer.parseInt(reader.readLine());
            System.out.println("Server: Current score: " + currentScore);

            while(true) {
                if (reader.ready()) {
                    String temp = reader.readLine();
                    if (temp != "") {
                        int mess = Integer.parseInt(temp);
                        if (mess == 444) break;
                        else currentScore = mess;
                        if (mess == 20) {
                            int closeGame = anInterface.endGame("RIGHT TEAM WON!", team == 1 ? "CONGRATULATIONS!" : "GOOD LUCK NEXT TIME!");
                            if (closeGame == 0) writer.println(444);
                        } else if (mess == -20) {
                            int closeGame = anInterface.endGame("LEFT TEAM WON!", team == -1 ? "CONGRATULATIONS!" : "GOOD LUCK NEXT TIME!");
                            if (closeGame == 0) writer.println(444);
                        }
                    }
                }
                if (lastScore != currentScore) {
                        anInterface.setScore(currentScore);
                    lastScore = currentScore;
                }
            }
            socket.close();
            System.out.println("closing");
            anInterface.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}