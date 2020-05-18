package demo.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import demo.controllers.UserController;
import demo.controllers.UserLoginAndCreateController;
import demo.model.Note;
import demo.model.User;
import demo.repositories.implementations.NoteRepository;
import demo.repositories.implementations.UserRepository;


@WebServlet("/Notes")
public class NoteServlet extends HttpServlet
{
   private static final long serialVersionUID = 1L;

   private UserLoginAndCreateController userLoginController = UserLoginAndCreateController.getInstance();
   private UserController userController = new UserController(UserRepository.getInstance(), NoteRepository.getInstance());
   private Gson gson = new Gson();

   public NoteServlet()
   {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      Optional<User> user = checkIfUserIsLoggedInAndGet(request, response);

      if (user.isPresent())
      {
         List<Note> notes = user.get().getNotes();
         List<Note> dbNotes = userController.getAllNotesOfUser(user.get());
         if(notes.isEmpty() || (notes.size() != dbNotes.size())) {
        	 if(!dbNotes.isEmpty()) {
        		 notes = dbNotes;
        	 }
         }
         String jsonString = gson.toJson(notes);
         response.setContentType("text/json");
         PrintWriter out = response.getWriter();
         out.print(jsonString);
         out.flush();
         response.setStatus(HttpServletResponse.SC_OK);
      }
      else
      {
         error(request, response, "Kein Benutzer gefunden");
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      Optional<User> user = checkIfUserIsLoggedInAndGet(request, response);

      String subject = request.getParameter("subject");
      String content = request.getParameter("content");

      if (subject == null || content == null)
      {
         error(request, response, "Keine Daten für Notizen erhalten");
         return;
      }

      if (user.isPresent())
      {
         user.get().addNote(subject, content);
         
         Note lastInsertedNote = NoteRepository.getInstance().getAll()
        		 .stream().max(Comparator.comparing(Note::getId))
                 .orElseThrow(NoSuchElementException::new);
         int nextId = lastInsertedNote.getId() + 1;
         
         if(userController.saveNoteOfUser(user.get(), new Note(nextId,subject,content))){
        	 response.setStatus(HttpServletResponse.SC_OK);
         }
      }
      else
      {
         error(request, response, "Kein Benutzer gefunden");
      }
   }

   protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      Optional<User> user = checkIfUserIsLoggedInAndGet(request, response);
      if (user.isPresent())
      {
         user.get().removeAllNotes();
         if(userController.removeAllNotesOfUser(user.get())) {
        	 response.setStatus(HttpServletResponse.SC_OK);
         }
         
      }
      else
      {
         error(request, response, "Kein Benutzer gefunden");
      }
   }

   private Optional<User> checkIfUserIsLoggedInAndGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      HttpSession session = request.getSession();
      User user = (User) session.getAttribute("User");

      if (user == null)
      {
         error(request, response, "Kein Benutzer gefunden");
         return Optional.empty();
      }
      else if (!this.userLoginController.lookupUser(user.getUsername()).isPresent())
      {
         error(request, response, "Benutzername nicht gefunden");
         return Optional.empty();
      }

      return Optional.of(user);
   }

   private void error(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException
   {
      RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
      request.setAttribute("errorMessage", errorMessage);
      dispatcher.forward(request, response);
   }
}
