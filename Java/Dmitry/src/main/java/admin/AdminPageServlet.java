package admin;

import main.AccountService;
import main.TimeHelper;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminPageServlet extends HttpServlet {
    private AccountService accountService;

    public AdminPageServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        String timeString = request.getParameter("shutdown");
        if (timeString != null) {
            // Получение логина активного пользователя
            String sessionId = request.getSession().getId();
            String login = null;
            if (accountService.sessionsContainsKey(sessionId)) {
                login = accountService.getUserProfileBySessionId(sessionId).getLogin();
            }
            if ( login != null && login.equals("admin")) {
                int timeMS = Integer.valueOf(timeString);
                System.out.print("Server will be down after: "+ timeMS + " ms");
                TimeHelper.sleep(timeMS);
                System.out.print("\nShutdown");
                response.sendRedirect("/main");
                System.exit(0);
            } else {
                pageVariables.put("login", login);
                pageVariables.put("warning", "you don't have administrator rights!");
            }
        }
        pageVariables.put("status", "run");
        pageVariables.put("CountOfUsersInDatabase", accountService.getCountOfUsers());
        pageVariables.put("CountOfUsersOnline", accountService.getCountOfSessions());
        response.getWriter().println(PageGenerator.getPage("admin.html", pageVariables));
    }
}
