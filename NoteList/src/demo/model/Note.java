package demo.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;


public class Note
{
   private final int id;
   private final String subject;
   private final String content;
   private final String creationDate;

   
   public Note(int id, String subject, String content)
   {
      Objects.requireNonNull(subject);
      Objects.requireNonNull(content);
      
      this.id = id;
      this.subject = subject.length() == 0 ? "---" : subject;
      this.content = content.length() == 0 ? "---" : content;
      
      ZonedDateTime  date = ZonedDateTime.now();
      this.creationDate = date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM).withLocale(Locale.GERMAN));
   }

   public int getId()
   {
      return id;
   }

   public String getSubject()
   {
      return subject;
   }

   public String getContent()
   {
      return content;
   }

   public String getCreationDate()
   {
      return creationDate;
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
      Note other = (Note) obj;
      if (id != other.id)
         return false;
      return true;
   }

   @Override
   public String toString()
   {
      return "Note [id=" + id + ", subject=" + subject + ", content=" + content + ", creationDate=" + creationDate + "]";
   }
}
