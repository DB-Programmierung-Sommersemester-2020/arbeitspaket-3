package demo.servlets;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import demo.model.User;
import demo.model.UserManager;

@WebServlet("/Register")
public class RegisterServlet extends HttpServlet
{
   private static final long serialVersionUID = 1L;

   private UserManager userManager = UserManager.getInstance();

   public RegisterServlet()
   {
      super();
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      String username = request.getParameter("username");
      String password = request.getParameter("password");
      String email = request.getParameter("email");
      
      if (username == null || password == null || email == null)
      {
         error(request, response, "Keine Daten empfangen (Null-Werte)");
         return;
      }
      else if (username.length() == 0 || password.length() == 0)
      {
         error(request, response, "Keine Daten empfangen");
         return;
      }

      Optional<User> optUser = userManager.lookupUser(username);
      if (optUser.isPresent())
      {
         error(request, response, "Benutzername existiert bereits");
         return;
      }

      User user = userManager.register(username, password, email);

      HttpSession session = request.getSession();
      session.setAttribute("User", user);

      RequestDispatcher dispatcher = request.getRequestDispatcher("notes.jsp");
      dispatcher.forward(request, response);
   }

   private void error(HttpServletRequest request, HttpServletResponse response, String errorMessage)
         throws ServletException, IOException
   {
      RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
      request.setAttribute("errorMessage", errorMessage);
      dispatcher.forward(request, response);
   }
}
