package cli.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Menu for commandline.
 */
public class CommandLineMenu {

    private final List<String> items = new ArrayList<>();
    private final List<Runnable> actions = new ArrayList<>();
    private final Scanner in = new Scanner(System.in);


    public void add(String item, Runnable action) {
        items.add(item);
        actions.add(action);
    }

    public void run() {
        show();
        final int action = read();
        execute(action - 1);
    }


    private void show() {
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%3d.\t%s\n", i + 1, items.get(i));
        }
    }

    private int read() {
        System.out.print("Make your choice: ");
        final int i = in.nextInt();

        return i;
    }

    private void execute(int i) {
        actions.get(i).run();
    }

}
