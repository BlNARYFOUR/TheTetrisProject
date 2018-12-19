package server;

/**
 * Easy to use server config class.
 */
public final class Config {
    public static final String STATIC_FILE_URL = "/static";
    private static final int GROUP_NUM = 16;
    private static final String TETRIS = "/tetris-" + GROUP_NUM;
    public static final int WEB_PORT = 8000 + GROUP_NUM;
    public static final int DB_WEBCLIENT_PORT = 9000 + GROUP_NUM;
    public static final String SOCKET_URL = TETRIS + "/socket/";
    public static final String DB_CONN_STR = "jdbc:h2:~" + TETRIS;
    public static final String REST_ENDPOINT = TETRIS + "/api/";

    private Config() {

    }
}
