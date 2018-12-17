package server.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.dailyStreakRepository.DailyRepository;
import data.loggedInRepository.LoggedInRepository;
import data.Repositories;
import data.loginRepository.LoginRepository;
import domain.User;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import org.pmw.tinylog.Logger;
import server.webapi.util.SecureFilePath;
import util.Hash;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * all the rout handlers are initialized here.
 */
class Routes {
    private static final String INFO_COOKIE = "info";
    private static final String STATIC_REF = "/static";
    private static final String MAIN_REF = "/static/pages/main_menu.html";
    private static final String LOCATION = "location";
    private static final String SESSION_COOKIE = "vertx-web.session";
    private static final String INDEX_REF = "webroot/index.html";
    private static final String SPACE = " ";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";

    private static ObjectMapper objectMapper = new ObjectMapper();
    private LoginRepository loginRepo = Repositories.getInstance().getLoginRepository();
    private LoggedInRepository loggedInRepo = Repositories.getInstance().getLoggedInRepository();
    private DailyRepository repo = Repositories.getInstance().getDailyRepository();

    void rootHandler(RoutingContext routingContext) {
        final HttpServerResponse response = routingContext.response();
        response.setChunked(true);
        response
                .putHeader("content-type", "text/html")
                .write("<h1>Wrong page amigo...</h1>"
                        + "<img src=static/images/facepalm.jpg>"
                        + "<p>Goto <a href=static>here</a> instead</p>")
                .end();
    }

    private User getUserFromBody(String body) {
        final String[] params = body.split("&");
        final String equals = "=";
        final String username = params[0].split(equals)[1];
        final String password = params[1].split(equals)[1];

        return new User(username, password);
    }

    private void sendRef(RoutingContext routingContext, String ref) {
        final HttpServerResponse response = routingContext.response();
        response.setChunked(true);
        response.sendFile(ref);
    }

    private String tryLogin(RoutingContext routingContext) throws UnsupportedEncodingException {
        final String body = routingContext.getBodyAsString();
        String infoBuf = "";
        User user = getUserFromBody(body);

        final Session session = routingContext.session();
        session.put(USERNAME, user.getUsername());
        session.put(PASSWORD, Hash.md5(user.getPassword()));

        //System.out.println("L " + session.get(USERNAME) + SPACE + session.get(PASSWORD));

        user = loginRepo.authenticateUser(session.get(USERNAME), session.get(PASSWORD), false);

        //System.out.println(user + " : " + loggedInRepo.getLoggedUser(session.id()));

        if (loggedInRepo.isUserLogged(user) || user == null) {
            if (loggedInRepo.isUserLogged(user)) {
                Logger.warn("User already logged in: " + Objects.requireNonNull(user).getUsername());
                infoBuf = "User '" + Objects.requireNonNull(user).getUsername() + "' has already logged in.";
            } else if (session.get(USERNAME) != null) {
                infoBuf = "User or password are incorrect.";
            }

            cookieHandler(INFO_COOKIE, infoBuf, routingContext);

            sendRef(routingContext, INDEX_REF);
        } else {
            routingContext.getCookie(SESSION_COOKIE).setMaxAge(LoggedInRepository.EXPIRATION_TIME);

            loggedInRepo.addLoggedUser(session.id(), user);
            //System.out.println(loggedInRepo.getLoggedUser(session.id()).getLoginDate());

            cookieHandler(INFO_COOKIE, infoBuf, routingContext);

            final HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.headers().add(LOCATION, MAIN_REF);
            response.setStatusCode(302).end();
        }

        return infoBuf;
    }

    public void loginHandler(RoutingContext routingContext) {
        synchronized (this) {
            String info = "";

            try {
                info = tryLogin(routingContext);
            } catch (Exception ex) {
                info = "Something went wrong.";
                try {
                    cookieHandler(INFO_COOKIE, info, routingContext);
                } catch (UnsupportedEncodingException e) {
                    Logger.warn("Unable to send info cookie", e);
                }

                sendRef(routingContext, INDEX_REF);
            }
        }
    }

    private void cookieHandler(final String key, final String value, final RoutingContext routingContext)
            throws UnsupportedEncodingException {
        final String valueEnc = URLEncoder.encode(value, "UTF-8");
        routingContext.addCookie(Cookie.cookie(key, valueEnc));
    }

    public void registerHandler(final RoutingContext routingContext) {
        synchronized (this) {
            try {
                final String body = routingContext.getBodyAsString();
                final User user = getUserFromBody(body);
                //user.setPassword(Hash.md5(user.getPassword()));

                final Session session = routingContext.session();
                session.put(USERNAME, user.getUsername());
                session.put(PASSWORD, Hash.md5(user.getPassword()));

                //System.out.println("1 " + session.get(USERNAME) + SPACE + session.get(PASSWORD));

                loginRepo.addUser(user);

                routingContext.getCookie(SESSION_COOKIE).setMaxAge(LoggedInRepository.EXPIRATION_TIME);

                loggedInRepo.addLoggedUser(session.id(), user);

                final HttpServerResponse response = routingContext.response();
                response.setChunked(true);
                response.headers().add(LOCATION, MAIN_REF);
                response.setStatusCode(302).end();
            } catch (Exception ex) {
                final HttpServerResponse response = routingContext.response();
                response.setChunked(true);
                response.sendFile("webroot/pages/register.html");
            }
        }
    }

    public void secureHandler(final RoutingContext routingContext, final SecureFilePath filePath) {
        final Session session = routingContext.session();
        final HttpServerResponse response = routingContext.response();
        response.setChunked(true);

        try {
            //System.out.println(session.id());
            final User user = loginRepo.authenticateUser(session.get(USERNAME), session.get(PASSWORD), false);
            if (!loggedInRepo.isUserLogged(session.id(), user) || user == null) {
                response.headers().add(LOCATION, STATIC_REF);
                response.setStatusCode(302).end();
            } else {
                response.sendFile("webroot" + filePath);
            }
        } catch (Exception ex) {
            response.headers().add(LOCATION, STATIC_REF);
            response.setStatusCode(302).end();
        }
    }

    public void rerouteHandler(final RoutingContext routingContext) {
        final Session session = routingContext.session();
        final HttpServerResponse response = routingContext.response();
        response.setChunked(true);

        try {
            final User user = loginRepo.authenticateUser(session.get(USERNAME), session.get(PASSWORD), false);

            if (!loggedInRepo.isUserLogged(session.id(), user) || user == null) {
                response.headers().add(LOCATION, STATIC_REF);
            } else {
                response.headers().add(LOCATION, STATIC_REF + SecureFilePath.MAIN_MENU);
            }
        } catch (Exception ex) {
            response.headers().add(LOCATION, STATIC_REF);
        }

        response.setStatusCode(302).end();
    }

    public void rerouteWebrootHandler(final RoutingContext routingContext) {
        final Session session = routingContext.session();
        final HttpServerResponse response = routingContext.response();
        response.setChunked(true);

        try {
            final User user = loginRepo.authenticateUser(session.get(USERNAME), session.get(PASSWORD), false);

            if (!loggedInRepo.isUserLogged(session.id(), user) || user == null) {
                response.sendFile(INDEX_REF);
            } else {
                response.headers().add(LOCATION, STATIC_REF + SecureFilePath.MAIN_MENU);
                response.setStatusCode(302).end();
            }
        } catch (Exception ex) {
            response.sendFile(INDEX_REF);
        }
    }

    public void logoutHandler(final RoutingContext routingContext) {
        final Session session = routingContext.session();
        loggedInRepo.deleteLoggedUser(session.id());
        session.destroy();

        final HttpServerResponse response = routingContext.response();
        response.setChunked(true);
        response.headers().add(LOCATION, STATIC_REF);
        response.setStatusCode(302).end();
    }
}


