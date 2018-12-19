package cli.util;

import org.pmw.tinylog.Logger;

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


    public void add(final String item, final Runnable action) {
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
            Logger.info(String.format("%3d.\t%s", i + 1, items.get(i)));
        }
    }

    private int read() {
        Logger.info("Make your choice: ");
        return in.nextInt();
    }

    private void execute(final int i) {
        actions.get(i).run();
    }

}
