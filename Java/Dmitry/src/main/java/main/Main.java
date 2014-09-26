package main;

import admin.AdminPageServlet;
import frontend.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

/**
 * @author v.chibrikov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        }

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        AccountService accountService = new AccountService();

        Servlet frontend = new Frontend();
        Servlet signupservlet = new SignupServlet(accountService);
        Servlet signinservlet = new SigninServlet(accountService);
        Servlet mainservlet = new MainPageServlet(accountService);
        Servlet logoutservlet = new LogoutServlet(accountService);
        Servlet adminservlet = new AdminPageServlet(accountService);
        Servlet profileservlet = new ProfileServlet(accountService);

        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signupservlet), "/auth/signup");
        context.addServlet(new ServletHolder(signinservlet), "/auth/signin");
        context.addServlet(new ServletHolder(logoutservlet), "/auth/logout");
        context.addServlet(new ServletHolder(mainservlet), "/main");
        context.addServlet(new ServletHolder(profileservlet), "/profile");
        context.addServlet(new ServletHolder(adminservlet), "/admin");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}