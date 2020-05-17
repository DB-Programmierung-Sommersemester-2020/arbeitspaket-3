package demo.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserManager
{
   private static UserManager instance = new UserManager();

   public static UserManager getInstance()
   {
      return UserManager.instance;
   }

   private Map<String, User> users = new HashMap<>();
   private int userIdCounter = 1;

   private UserManager()
   {
      super();
   }

   public Optional<User> lookupUser(String username)
   {
      return this.users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
   }

   public User register(String username, String password, String email)
   {
      if (this.lookupUser(username).isPresent())
      {
         RuntimeException exce = new RuntimeException("User " + username + " exists");
         throw exce;
      }
      else
      {
         User user = new User(userIdCounter,username, password, email);
         this.users.put(username, user);
         userIdCounter++;
         return user;
      }
   }
}
