package frontend;

import main.AccountService;
import templater.PageGenerator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by serg on 13.09.14.
 */
public class AuthServlet extends HttpServlet {
    private AccountService accountService;

    public AuthServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String login;
        String password;
        if (path.equals("/sign_in")) {
            HttpSession session = request.getSession();
            String sessionId = session.getId();
            login = request.getParameter("login");
            password = request.getParameter("password");
            if (accountService.sign_in(login,password,sessionId)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(login + " " + password);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().println("ERROR!");
            }
        } else if (path.equals("/sign_up")) {
            login = request.getParameter("login");
            String email = request.getParameter("email");
            password = request.getParameter("password");
            if (accountService.sign_up(login, email, password)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(login + " " + email + " " + password);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().println("ERROR!");
            }
        } else if (path.equals("/log_out")) {
            HttpSession session = request.getSession();
            String sessionId = session.getId();
            if (accountService.logOut(sessionId)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("You've been logged out!");
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().println("ERROR!");
            }
        }
    }
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("email", email == null ? "" : email);
        pageVariables.put("password", password == null ? "" : password);

        response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));
    }
}
