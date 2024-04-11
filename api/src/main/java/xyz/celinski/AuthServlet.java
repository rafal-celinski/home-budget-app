package xyz.celinski;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import xyz.celinski.exceptions.InvalidCredentialsException;
import xyz.celinski.exceptions.RepositoryAccessException;
import xyz.celinski.exceptions.UserDoesntExists;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {
    AuthenticationService authService;

    public AuthServlet() {
        super();
        authService = new AuthenticationService();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)	throws ServletException, IOException {

        String email = request.getParameter("email");
        String password_hash = request.getParameter("password_hash");

        try {
            User user = authService.authenticateUser(email, password_hash);
            String token = authService.generateJWT(user);
            sendSuccessResponse(response, token);
        }
        catch (UserDoesntExists e) {
            int status = HttpServletResponse.SC_NOT_FOUND;
            String errorMessage = "User with this email doesn't exit";
            sendErrorResponse(response, status, errorMessage);
        }
        catch (InvalidCredentialsException e) {
            int status = HttpServletResponse.SC_UNAUTHORIZED;
            String errorMessage = "Invalid password";
            sendErrorResponse(response, status, errorMessage);
        }
        catch (RepositoryAccessException e) {
            int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            String errorMessage = "Internal server error";
            sendErrorResponse(response, status, errorMessage);
        }
    }

    void sendErrorResponse(HttpServletResponse response, int status, String errorMessage) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print("{\"success\": false, \"error\": \"" + errorMessage + "\"}");
        out.flush();
    }

    void sendSuccessResponse(HttpServletResponse response, String token) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print("{\"success\": true, \"token\": \"" + token + "\"}");
        out.flush();
    }
}