package server.webapi.util;

public enum FilePath {

    MAIN_MENU_WEB_FILE("/pages/main_menu.html");

    private final String path;

    FilePath(String path){
        this.path = path;
    }

    public String toString(){
        return path;
    }
}
