package uk.ac.ed.inf.eventsapp.controller;

import java.util.Collection;

import uk.ac.ed.inf.eventsapp.integration.VerificationSystem;
import uk.ac.ed.inf.eventsapp.model.EntertainmentProvider;
import uk.ac.ed.inf.eventsapp.model.Event;
import uk.ac.ed.inf.eventsapp.model.User;
import uk.ac.ed.inf.eventsapp.view.View;

/**
 * Handles login, logout, and provider registration.
 */
public class UserController extends Controller {
  public static final String PREREGISTERED_USERS_FILE_PATH = "docs/preregistered-users.txt";
  public static final String PREREGISTERED_ADMIN_FILE_PATH = "docs/preregistered-admin.txt";

  private final VerificationSystem verificationSystem;
  private final Collection<User> users;
  private final Collection<Event> events;

  public UserController(View view, VerificationSystem verificationSystem, Collection<User> users,
      Collection<Event> events) {
    super(view);
    this.verificationSystem = verificationSystem;
    this.users = users;
    this.events = events;
  }

  public void login() {
    if (!checkCurrentUserIsGuest()) {return;}

    Scanner inputReader = new Scanner(System.in);

    String email = inputReader.nextLine("Enter email:");
    String password = inputReader.nextLine("Enter password:");

    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("password must not be blank.");
    }

    List<String> userData = readDetails(PREREGISTERED_USERS_FILE_PATH);
    for (int i = 0; i <= userData.length; i++){
      if (email == userData[i][0] && password == userData[i][1])){
        Student currentUser = new Student(email, password);
        setCurrentUser(currentUser);
      }
    }

    List<String> userData = readDetails(PREREGISTERED_ADMIN_FILE_PATH);
    for (int i = 0; i <= userData.length; i++){
      if (email == userData[i][0] && password == userData[i][1])){
        AdminStaff currentUser = new AdminStaff(email, password);
        setCurrentUser(currentUser);
      }
    }
    return;

    //verify password
    //change to Student/Admin/EP
    //throw new UnsupportedOperationException("login is not implemented yet.");
  }

  public void logout() {
    if(!checkCurrentUserIsGuest()) {setCurrentUser(null);}
    return
    //throw new UnsupportedOperationException("logout is not implemented yet.");
  }

  private List<String> readDetails(filePath) throws IllegalStateException {
    Path usersFile = Path.of(filePath);

    try {
      FileTime lastModifiedTime = Files.getLastModifiedTime(usersFile);
      long fileSize = Files.size(usersFile);

      List<String> parsedRecords = Files.readAllLines(usersFile, StandardCharsets.UTF_8)
              .stream().map(String::trim).filter(line -> !line.isEmpty() && !line.startsWith("#"))
              .map(this::parseFacultyRecord).filter(Objects::nonNull).toList();

      return parsedRecords;
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to read user preregistration file.", exception);
    }
  }

  public void registerEntertainmentProvider() {
    if(!checkCurrentUserIsGuest()){return;}

    Scanner inputReader = new Scanner(System.in);

    String password = inputReader.nextLine("Enter password:");
    String email = inputReader.nextLine("Enter email:");
    String businessRegistrationNumber = inputReader.nextLine("Enter your organisation name:");
    String businessRegistrationNumber = inputReader.nextLine("Enter your business registration number:");

    if(EPAccountAlreadyExists()){return;}
    verifyEntertainmentProvider(businessRegistrationNumber);

    EntertainmentProvider.add(new EntertainmentProvider());
    User = EntertainmentProvider[EntertainmentProvider.length-1];
    return
    //throw new UnsupportedOperationException("registerEntertainmentProvider is not implemented yet.");
  }

  private boolean EPAccountAlreadyExists(String email, String orgName, String businessNumber) {
    throw new UnsupportedOperationException("EPAccountAlreadyExists is not implemented yet.");
  }

  public void editPreferences() {
    throw new UnsupportedOperationException("editPreferences is not implemented yet.");
  }

  private void addUser(User user) {
    users.add(user);
  }

  private void addPreregisteredUsers() {
    throw new UnsupportedOperationException("addPreregisteredUsers is not implemented yet.");
  }

  private EntertainmentProvider getEntertainmentProviderOwningEvent(long eventNumber) {
    throw new UnsupportedOperationException(
        "getEntertainmentProviderOwningEvent is not implemented yet.");
  }

  public VerificationSystem getVerificationSystem() {
    return verificationSystem;
  }

  public Collection<User> getUsers() {
    return users;
  }

  public Collection<Event> getEvents() {
    return events;
  }
}
