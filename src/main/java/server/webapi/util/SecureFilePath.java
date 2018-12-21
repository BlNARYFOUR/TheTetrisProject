package server.webapi.util;

/**
 * predefine all the paths that need to be access-secure.
 */
public enum SecureFilePath {

    MAIN_MENU("main_menu.html"),
    GAME_MODE("/chooseGamemode.html"),
    HERO("/chooseHero.html"),
    GAME_MULTI("/tetrisMultiplayerField.html");

    private final String path;

    SecureFilePath(final String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
