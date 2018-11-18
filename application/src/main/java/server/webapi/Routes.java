package server.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.dailyStreakRepository.DailyRepository;
import data.loggedInRepository.LoggedInRepository;
import data.loginRepository.LoginRepository;
import data.Repositories;
import domain.User;
import domain.dailyStreak.DailyStreakRewards;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import org.pmw.tinylog.Logger;
import server.webapi.util.SecureFilePath;
import util.Hash;

import java.util.Date;
import java.util.Objects;

class Routes {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private LoginRepository loginRepo = Repositories.getInstance().getLoginRepository();
    private LoggedInRepository loggedInRepo = Repositories.getInstance().getLoggedInRepository();
    private DailyRepository repo = Repositories.getInstance().getDailyReposistory();


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
        try {
            String body = routingContext.getBodyAsString();
            User user = getUserFromBody(body);

            Session session = routingContext.session();
            session.put("username", user.getUsername());
            session.put("password", Hash.md5(user.getPassword()));

            user = loginRepo.authenticateUser(session.get("username"), session.get("password"), false);
            if (loggedInRepo.isUserLogged(user) || user == null) {
                if (loggedInRepo.isUserLogged(user)) {
                    Logger.warn("User already logged in: " + Objects.requireNonNull(user).getUsername());
                }

                HttpServerResponse response = routingContext.response();
                response.setChunked(true);
                response.sendFile("webroot/index.html");
            } else {
                routingContext.getCookie("vertx-web.session").setMaxAge(LoggedInRepository.EXPIRATION_TIME);

                loggedInRepo.addLoggedUser(session.id(), user);
                //System.out.println(loggedInRepo.getLoggedUser(session.id()).getLoginDate());

                HttpServerResponse response = routingContext.response();
                response.setChunked(true);
                response.headers().add("location", "/static/pages/main_menu.html");
                response.setStatusCode(302).end();
            }
        } catch (Exception ex) {
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.sendFile("webroot/index.html");
        }
    }

    synchronized void registerHandler(RoutingContext routingContext) {
        try {
            String body = routingContext.getBodyAsString();
            User user = getUserFromBody(body);

            Session session = routingContext.session();
            session.put("username", user.getUsername());
            session.put("password", Hash.md5(user.getPassword()));

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

            if (!loggedInRepo.isUserLogged(session.id(), user) || user == null) {
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


