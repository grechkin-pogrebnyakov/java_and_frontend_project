package frontend;

import main.AccountService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitry on 027 27.09.14.
 */
public class ProfileServlet extends HttpServlet {
    private AccountService accountService;

    public ProfileServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        // Получение логина активного пользователя
        String sessionId = request.getSession().getId();
        String login = null;
        if (accountService.sessionsContainsKey(sessionId)) {
            login = accountService.getUserProfileBySessionId(sessionId).getLogin();
        }
        if (login == null) {
            response.sendRedirect("/auth/signup");
        } else {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("login", login);
            pageVariables.put("email", accountService.getUserProfileByLogin(login).getEmail());
            response.getWriter().println(PageGenerator.getPage("profile.html", pageVariables));
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}

