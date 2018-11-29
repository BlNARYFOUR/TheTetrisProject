package server.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.TetrisRepository;
import data.dailyStreakRepository.DailyRepository;
import data.loggedInRepository.LoggedInRepository;
import data.Repositories;
import data.loginRepository.LoginRepository;
import domain.User;
import domain.dailyStreak.DailyStreakRewards;
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

class Routes {
    private static final String INFO_COOKIE = "info";

    private static ObjectMapper objectMapper = new ObjectMapper();
    private DailyRepository repo = Repositories.getInstance().getDailyReposistory();
    private LoginRepository loginRepo = Repositories.getInstance().getLoginRepository();
    private LoggedInRepository loggedInRepo = Repositories.getInstance().getLoggedInRepository();


    void rootHandler(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.setChunked(true);
        response
                .putHeader("content-type", "text/html")
                .write("<h1>Wrong page amigo...</h1><img src=static/images/facepalm.jpg><p>Goto <a href=static>here</a> instead</p>")
                .end();
    }

    private User getUserFromBody(String body) {
        String[] params = body.split("&");
        String username = params[0].split("=")[1];
        String password = params[1].split("=")[1];

        return new User(username, password);
    }

    synchronized void loginHandler(RoutingContext routingContext) {
        String info = "";

        try {
            String body = routingContext.getBodyAsString();
            User user = getUserFromBody(body);

            Session session = routingContext.session();
            session.put("username", user.getUsername());
            session.put("password", Hash.md5(user.getPassword()));

            System.out.println("L " + session.get("username") + " " + session.get("password"));

            user = loginRepo.authenticateUser(session.get("username"), session.get("password"), false);

            System.out.println(user + " : " + loggedInRepo.getLoggedUser(session.id()));

            if (loggedInRepo.isUserLogged(user) || user == null) {
                if (loggedInRepo.isUserLogged(user)) {
                    Logger.warn("User already logged in: " + Objects.requireNonNull(user).getUsername());
                    info = "User '" + Objects.requireNonNull(user).getUsername() + "' has already logged in.";
                } else if(session.get("username") != null) {
                    info = "User or password are incorrect.";
                }

                cookieHandler(INFO_COOKIE, info, routingContext);

                HttpServerResponse response = routingContext.response();
                response.setChunked(true);
                response.sendFile("webroot/index.html");
            } else {
                routingContext.getCookie("vertx-web.session").setMaxAge(LoggedInRepository.EXPIRATION_TIME);

                loggedInRepo.addLoggedUser(session.id(), user);
                System.out.println(loggedInRepo.getLoggedUser(session.id()).getLoginDate());

                cookieHandler(INFO_COOKIE, info, routingContext);

                HttpServerResponse response = routingContext.response();
                response.setChunked(true);
                response.headers().add("location", "/static/pages/main_menu.html");
                response.setStatusCode(302).end();
            }
        } catch (Exception ex) {
            info = "Something went wrong.";
            try {
                cookieHandler(INFO_COOKIE, info, routingContext);
            } catch (UnsupportedEncodingException e) {
                Logger.warn("Unable to send info cookie", e);
            }

            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.sendFile("webroot/index.html");
        }
    }

    private void cookieHandler(String key, String value, RoutingContext routingContext) throws UnsupportedEncodingException {
        String valueEnc = URLEncoder.encode(value, "UTF-8");
        routingContext.addCookie(Cookie.cookie(key, valueEnc));
    }

    synchronized void registerHandler(RoutingContext routingContext) {
        try {
            String body = routingContext.getBodyAsString();
            User user = getUserFromBody(body);
            user.setPassword(Hash.md5(user.getPassword()));

            Session session = routingContext.session();
            session.put("username", user.getUsername());
            session.put("password", user.getPassword());

            System.out.println("1 " + session.get("username") + " " + session.get("password"));

            loginRepo.addUser(user);

            routingContext.getCookie("vertx-web.session").setMaxAge(LoggedInRepository.EXPIRATION_TIME);

            loggedInRepo.addLoggedUser(session.id(), user);

            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.headers().add("location", "/static/pages/main_menu.html");
            response.setStatusCode(302).end();
        } catch (Exception ex) {
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.sendFile("webroot/pages/register.html");
        }


    }

    void secureHandler(RoutingContext routingContext, SecureFilePath filePath) {
        Session session = routingContext.session();
        HttpServerResponse response = routingContext.response();
        response.setChunked(true);

        try {
            System.out.println("Here");
            System.out.println(session.id());
            User user = loginRepo.authenticateUser(session.get("username"), session.get("password"), false);
            if (!loggedInRepo.isUserLogged(session.id(), user) || user == null) {
                response.headers().add("location", "/static");
                response.setStatusCode(302).end();
            } else {
                response.sendFile("webroot" + filePath);
            }
        } catch (Exception ex) {
            response.headers().add("location", "/static");
            response.setStatusCode(302).end();
        }
    }

    void rerouteHandler(RoutingContext routingContext) {
        Session session = routingContext.session();
        HttpServerResponse response = routingContext.response();
        response.setChunked(true);

        try {
            User user = loginRepo.authenticateUser(session.get("username"), session.get("password"), false);

            if (loggedInRepo.isUserLogged(session.id(), user) || user == null) {
                response.headers().add("location", "/static");
            } else {
                response.headers().add("location", "/static" + SecureFilePath.MAIN_MENU);
            }
        } catch (Exception ex) {
            response.headers().add("location", "/static");
        }

        response.setStatusCode(302).end();
    }

    void rerouteWebrootHandler(RoutingContext routingContext) {
        Session session = routingContext.session();
        HttpServerResponse response = routingContext.response();
        response.setChunked(true);

        try {
            User user = loginRepo.authenticateUser(session.get("username"), session.get("password"), false);

            if (!loggedInRepo.isUserLogged(session.id(), user) || user == null) {
                response.sendFile("webroot/index.html");
            } else {
                response.headers().add("location", "/static" + SecureFilePath.MAIN_MENU);
                response.setStatusCode(302).end();
            }
        } catch (Exception ex) {
            response.sendFile("webroot/index.html");
        }
    }

    void logoutHandler(RoutingContext routingContext) {
        Session session = routingContext.session();
        loggedInRepo.deleteLoggedUser(session.id());
        session.destroy();

        HttpServerResponse response = routingContext.response();
        response.setChunked(true);
        response.headers().add("location", "/static");
        response.setStatusCode(302).end();
    }

    // BRYAN

    void dailyStreakHandler(RoutingContext routingContext) {
        Session session = routingContext.session();
        HttpServerResponse response = routingContext.response();
        response.setChunked(true);

        try {
            User u = new User();
            DailyStreakRewards dsr = new DailyStreakRewards();

            System.out.println(repo.getUser(u.getUsername()));
            System.out.println("incoming request");

            System.out.println("main_menu");
            if (repo.getUser(u.getUsername()).isAlreadyLoggedIn()) {
                System.out.println("Already received daily rewards");
            } else {
                System.out.println(repo.getStreak(dsr.dailyStreak(u.getUsername())).getReward());
            }

            repo.updateAlreaddyLoggedIn(true, u.getUsername());


        } catch (Exception ex) {
            response.sendFile("webroot/pages/main_menu.html");
        }

    }
}


