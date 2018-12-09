import domain.User;
import domain.game.Player;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class PlayerTest {
    @Test
    public void testNextBlockFall() {
        User user = new User();
        Player player = new Player(0, user, "1234", "<EMPTY>");

        System.out.println(player.getFallingBlock());
        System.out.println(player.getFallingBlock().getX() + " " + player.getFallingBlock().getY());

        System.out.println();

        boolean stop = false;
        String data = "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n"
                + "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n"
                + "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n"
                + "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n"
                + "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n"
                + "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n"
                + "Stop\r\n";
        InputStream stdin = System.in;

        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            Scanner scanner = new Scanner(System.in);

            do {
                System.out.println(player.playingFieldWithFallingBlock());

                String input = scanner.nextLine();
                if (input.toLowerCase().equals("stop")) {
                    stop = true;
                } else {
                    player.nextBlockFall();
                }
            } while (!stop);
        } finally {
            System.setIn(stdin);
        }
    }
}
