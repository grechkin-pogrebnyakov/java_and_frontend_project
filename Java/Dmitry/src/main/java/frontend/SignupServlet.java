package frontend;

import main.AccountService;
import main.UserProfile;
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
public class SignupServlet extends HttpServlet {
    private AccountService accountService;

    public SignupServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        // Формирование формы
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("error", "Registration Form");
        response.getWriter().println(PageGenerator.getPage("signup.html", pageVariables));
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
        String email = request.getParameter("email");

        // Обработка запроса + формирование ответа
        Map<String, Object> pageVariables = new HashMap<>();
        if (login == null || accountService.usersContainsKey(login)) {
            pageVariables.put("error", "uncurrect login");
            response.getWriter().println(PageGenerator.getPage("signup.html", pageVariables));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            UserProfile profile = new UserProfile(login, password, email);
            accountService.addUser(profile);
            accountService.addSession(sessionId, login);
            response.sendRedirect("/main");
            response.setStatus(HttpServletResponse.SC_OK);
        }
        response.setContentType("text/html;charset=utf-8");
    }
}
