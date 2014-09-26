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
 * Created by Dmitry on 013 13.09.14.
 */
public class SigninServlet extends HttpServlet {
    private AccountService accountService;

    public SigninServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        // Разрыв существующей сессии
        String sessionId = request.getSession().getId();
        if (accountService.sessionsContainsKey(sessionId)) {
            accountService.deleteSession(sessionId);
        }

        // Формирование формы
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("error", "Enter yor login and password");
        response.getWriter().println(PageGenerator.getPage("signin.html", pageVariables));
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        // Разрыв существующей сессии
        String sessionId = request.getSession().getId();
        if (accountService.sessionsContainsKey(sessionId)) {
            accountService.deleteSession(sessionId);
        }

        // Чтение формы
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        // Обработка запроса + формирование ответа
        Map<String, Object> pageVariables = new HashMap<>();
        if (accountService.usersContainsKey(login)) {
            if (password.equals(accountService.getPasswordByLogin(login))) {
                accountService.addSession(request.getSession().getId(), login);
                response.setStatus(HttpServletResponse.SC_OK);
                response.sendRedirect("/main");
            } else {
                pageVariables.put("error", "wrong password");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().println(PageGenerator.getPage("signin.html", pageVariables));
            }
         } else {
            pageVariables.put("error", "login does not exist");
            response.getWriter().println(PageGenerator.getPage("signin.html", pageVariables));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        response.setContentType("text/html;charset=utf-8");
    }
}
