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

  private static final String REGISTERED_EPS_FILE_PATH = "docs/registered-eps.txt";

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
    if (!checkCurrentUserIsGuest()) {return;} // check current user is a guest

    Scanner inputReader = new Scanner(System.in); // get input reader
    String email = inputReader.nextLine("Enter email:");
    String password = inputReader.nextLine("Enter password:");

    if (password == null || password.isBlank()) { // password conditions
      throw new IllegalArgumentException("password must not be blank.");
    }

    for (int i = 0; i <= users.length; i++){ // go through each existing user
      if (email == users[i].getEmail() && users[i].passwordMatches(password)){ // check if details match
        setCurrentUser(users[i]);
      }
    }
    return;
  }

  public void logout() {
    if(!checkCurrentUserIsGuest()) {setCurrentUser(null);}
    return;
  }

  public void registerEntertainmentProvider() {
    if(!checkCurrentUserIsGuest()){return;}

    Scanner inputReader = new Scanner(System.in);

    String email = inputReader.nextLine("Enter email:");
    String password = inputReader.nextLine("Enter password:");
    String organisationName = inputReader.nextLine("Enter your organisation's name:");
    String businessRegistrationNumber = inputReader.nextLine("Enter your business registration number:");
    String name = inputReader.nextLine("Enter your name:");
    String description = inputReader.nextLine("Enter description:");

    if (password == null || password.isBlank()) { // password conditions
      throw new IllegalArgumentException("password must not be blank.");
    }

    if(EPAccountAlreadyExists(email, organisationName, businessRegistrationNumber)){return;}
    if(verifyEntertainmentProvider(businessRegistrationNumber)){return;}


    EntertainmentProvider newEP = new EntertainmentProvider(email, password,
            organisationName, businessRegistrationNumber,
            name, description);

    try (FileWriter myWriter = new FileWriter(REGISTERED_EPS_FILE_PATH, true)) {
      myWriter.write(String.format("\n%1s, %2s, %3s, %4s, %5s, %6s", email, password, organisationName, businessRegistrationNumber, name, description));
    } catch (IOException e) {
      throw new IllegalArgumentException("Error occured during writing");
    }

    addUser(newEP);

    return
  }

  private boolean EPAccountAlreadyExists(String email, String orgName, String businessNumber) {
    for (int i = 0; i <= users.length; i++){ // go through each existing user
      if(users[i] instanceof EntertainmentProvider){
        if (email == users[i].getEmail() && orgName == users[i].getOrgName() && businessNumber == users[i].getBusinessNumber()){ // check if details match
          return true;
        }
      }
    }
    return false;
  }

  public void editPreferences() {
    throw new UnsupportedOperationException("editPreferences is not implemented yet.");
  }

  private void addUser(User user) {
    users.add(user);
  }

  private void addPreregisteredUsers() {
    try {
      List<String> parsedStudentRecords = Files.readAllLines(PREREGISTERED_USERS_FILE_PATH, StandardCharsets.UTF_8)
              .stream().map(String::trim).filter(line -> !line.isEmpty() && !line.startsWith("#"))
              .map(this::parseFacultyRecord).filter(Objects::nonNull).toList();

      List<String> parsedAdminRecords = Files.readAllLines(PREREGISTERED_ADMIN_FILE_PATH, StandardCharsets.UTF_8)
              .stream().map(String::trim).filter(line -> !line.isEmpty() && !line.startsWith("#"))
              .map(this::parseFacultyRecord).filter(Objects::nonNull).toList();

      List<String> parsedEPRecords = Files.readAllLines(REGISTERED_EPS_FILE_PATH, StandardCharsets.UTF_8)
              .stream().map(String::trim).filter(line -> !line.isEmpty() && !line.startsWith("#"))
              .map(this::parseFacultyRecord).filter(Objects::nonNull).toList();

      for (int i = 0; i <= parsedStudentRecords.length; i++) {
        addUser(new Student(parsedStudentRecords(parsedRecord[i][0], parsedStudentRecords[i][1]])))
      }
      for (int i = 0; i <= parsedAdminRecords.length; i++) {
        addUser(new AdminStaff(parsedAdminRecords(parsedRecord[i][0], parsedAdminRecords[i][1], parsedAdminRecords[i][2)))
      }
      for (int i = 0; i <= parsedEPRecords.length; i++) {
        addUser(new EntertainmentProvider(parsedEPRecords(parsedRecord[i][0], parsedEPRecords[i][1],
                parsedEPRecords[i][2], parsedEPRecords[i][3],
                parsedEPRecords[i][4], parsedEPRecords[i][5])))
      }
      return;
    } catch (IOException exception) {
    throw new IllegalStateException("Unable to read user preregistration file.", exception);
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
