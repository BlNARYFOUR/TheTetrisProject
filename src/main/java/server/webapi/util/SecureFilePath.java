package server.webapi.util;

/**
 * predefine all the paths that need to be access-secure.
 */
public enum SecureFilePath {

    MAIN_MENU("/pages/main_menu.html"),
    GAME_MODE("/pages/chooseGamemode.html"),
    HERO("/pages/chooseHero.html"),
    GAME_MULTI("/pages/tetrisMultiplayerField.html");

    private final String path;

    SecureFilePath(final String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
