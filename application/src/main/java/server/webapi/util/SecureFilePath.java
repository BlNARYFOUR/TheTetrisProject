package server.webapi.util;

public enum SecureFilePath {

    MAIN_MENU("/pages/main_menu.html"),
    GAME("/pages/game.html"),
    GAME_MODE("/pages/chooseGamemode.html"),
    HERO("/pages/chooseHero.html");

    private final String path;

    SecureFilePath(String path){
        this.path = path;
    }

    public String toString(){
        return path;
    }
}
