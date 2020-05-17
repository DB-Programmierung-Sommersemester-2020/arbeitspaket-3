package demo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User
{
   private static int idNoteCounter = 1;
   private final int id;
   private final String username;
   private final String password;
   private final String email;
   
   private final List<Note> notes = new ArrayList<>();
   
   public User(int id, String username, String password, String email)
   {
      super();
      
      Objects.requireNonNull(username);
      Objects.requireNonNull(password);
      Objects.requireNonNull(email);
      
      this.id = id;
      this.username = username;
      this.password = password;
      this.email = email;
   }
   
   public List<Note> getNotes()
   {
      return Collections.unmodifiableList( this.notes );
   }
   
   public Note addNote(String subject, String content)
   {
      Note note = new Note( idNoteCounter++, subject, content);
      this.notes.add( note );
      return note;
   }
   
   public void removeAllNotes()
   {
      this.notes.clear();
   }

   public String getUsername()
   {
      return username;
   }


   public String getPassword()
   {
      return password;
   }


   public String getEmail()
   {
      return email;
   }


   
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + id;
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      User other = (User) obj;
      if (id != other.id)
         return false;
      return true;
   }

   @Override
   public String toString()
   {
      return "User [username=" + username + ", password=" + password + "]";
   }
}
