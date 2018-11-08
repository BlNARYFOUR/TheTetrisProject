package server.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.loggedInRepository.LoggedInRepository;
import data.loginRepository.LoginRepository;
import data.Repositories;
import domain.User;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import org.pmw.tinylog.Logger;
import server.webapi.util.SecureFilePath;

import java.util.Objects;

public class Routes {

    private static ObjectMapper objectMapper = new ObjectMapper();
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

    void loginHandler(RoutingContext routingContext) {
        try {
            String body = routingContext.getBodyAsString();
            System.out.println(routingContext.getBodyAsString());
            String[] params = body.split("&");
            String username = params[0].split("=")[1];
            String password = params[1].split("=")[1];

            User user = new User(username, password);

            Session session = routingContext.session();
            session.put("username", user.getUsername());
            session.put("password", user.getPassword());

            user = loginRepo.authenticateUser(user);
            if (loggedInRepo.isUserLogged(user) || user == null) {
                if(loggedInRepo.isUserLogged(user)) {
                    Logger.warn("User already logged in: " + Objects.requireNonNull(user).getUsername());
                }

                HttpServerResponse response = routingContext.response();
                response.sendFile("webroot/index.html");
            } else {
                routingContext.getCookie("vertx-web.session").setMaxAge(31536000);  //1 year (60s*60m*24h*356d)

                loggedInRepo.addLoggedUser(session.id(), user);

                HttpServerResponse response = routingContext.response();
                response.headers().add("location", "/static/pages/main_menu.html");
                response.setStatusCode(302).end();
            }
        } catch (Exception ex) {
            HttpServerResponse response = routingContext.response();
            response.sendFile("webroot/index.html");
        }
    }

    void secureHandler(RoutingContext routingContext, SecureFilePath filePath) {
        Session session = routingContext.session();
        try {
            User user = loginRepo.authenticateUser(session.get("username"), session.get("password"));
            if (user == null) {
                HttpServerResponse response = routingContext.response();
                response.headers().add("location", "/static");
                response.setStatusCode(302).end();
            } else {
                routingContext.response().sendFile("webroot"+filePath);
        }
        } catch (Exception ex) {
            HttpServerResponse response = routingContext.response();
            response.headers().add("location", "/static");
            response.setStatusCode(302).end();
        }
    }

    void rerouteHandler(RoutingContext routingContext) {
        Session session = routingContext.session();
        HttpServerResponse response = routingContext.response();

        try {
            User user = loginRepo.authenticateUser(session.get("username"), session.get("password"));

            if (user == null) {
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

        try {
            User user = loginRepo.authenticateUser(session.get("username"), session.get("password"));

            if (user == null) {
                response.sendFile("webroot/index.html");
            } else {
                response.headers().add("location", "/static" + SecureFilePath.MAIN_MENU);
                response.setStatusCode(302).end();
            }
        } catch (Exception ex) {
            response.sendFile("webroot/index.html");
        }
    }
}
