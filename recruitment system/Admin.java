package rms;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Admin extends User {
    private List<User> users;
    private RecruitmentManager recruitmentManager;
    private static final String USERS_FILE = "users.dat";


    public Admin(int user_id, String name, String email, String password, RecruitmentManager recruitmentManager) {
        super(user_id, name, email, password);
        this.users = new ArrayList<>();
        this.recruitmentManager = recruitmentManager; // Initialize RecruitmentManager
        
    }

    @Override
   public void logIn(String inputEmail, String inputPassword) throws LoginException {
        // Define the regex for email validation
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(inputEmail);

    // Check if the email format is invalid
    if (!matcher.matches()) {
        throw new LoginException("Login Failed! Invalid email format.");
    }

    // Check if email and password match
    if (this.email.equals(inputEmail) && this.password.equals(inputPassword)) {
        System.out.println("Login successful! Welcome, Applicant: " + this.name);
    } else {
        throw new LoginException("Login Failed! Incorrect email or password.");
    }
    }

    @Override
    public void updateProfile() {
        System.out.println("Profile Updated");
    }

    // Add a new user to the system
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        users.add(user);
        System.out.println("User added successfully.");
    }

    // Edit an existing user based on user_id
    public void editUser(int user_id, User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() == user_id) {
                users.set(i, updatedUser);
                System.out.println("User with ID " + user_id + " updated successfully.");
                return;
            }
        }
        System.out.println("User with ID " + user_id + " not found!");
    }

    // Remove an existing user based on user_id
    public void removeUser(int user_id) {
        boolean removed = users.removeIf(user -> user_id == user.getUserId());
        if (removed) {
            System.out.println("User with ID " + user_id + " removed successfully.");
        } else {
            System.out.println("User with ID " + user_id + " not found!");
        }
    }

    // List users based on a specific field and value
    // List users to a binary file
public void listUsersToFile(String field, String value, String filePath) {
    try (ObjectOutputStream oos = createObjectOutputStream(filePath)) {
        boolean found = false;
        for (User user : users) {
            switch (field.toLowerCase()) {
                case "name":
                    if (user.getName().equalsIgnoreCase(value)) {
                        oos.writeObject(user);
                        found = true;
                    }
                    break;
                case "email":
                    if (user.getEmail().equalsIgnoreCase(value)) {
                        oos.writeObject(user);
                        found = true;
                    }
                    break;
                case "userid":
                    if (String.valueOf(user.getUserId()).equals(value)) {
                        oos.writeObject(user);
                        found = true;
                    }
                    break;
                default:
                    System.out.println("Invalid field: " + field);
                    return; // Exit early if the field is invalid
            }
        }
        if (!found) {
            System.out.println("No users found matching the criteria.");
        }
    } catch (IOException e) {
        System.out.println("An error occurred while writing to the file: " + e.getMessage());
    }
}

// Helper method to create ObjectOutputStream
private ObjectOutputStream createObjectOutputStream(String filePath) throws IOException {
    File file = new File(filePath);
    if (file.exists()) {
        return new AppendingObjectOutputStream(new FileOutputStream(file, true));
    } else {
        return new ObjectOutputStream(new FileOutputStream(file));
    }
}

// Custom AppendingObjectOutputStream to skip the header when appending
private static class AppendingObjectOutputStream extends ObjectOutputStream {
    public AppendingObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // Skip header when appending to avoid corrupting the file
        reset();
    }
}


    // Get the number of recruitments per company
    public HashMap<String, Integer> getRecruitmentPerCompany() {
        return recruitmentManager.getRecruitmentPerCompany();
    }

    // Get the company with the maximum recruitments
    public String getCompanyWithMaxRecruitment() {
        return recruitmentManager.getCompanyWithMaxRecruitment();
    }

    // Get the applicant with the most applications
    public String getApplicantWithMaxApplications() {
        return recruitmentManager.getApplicantWithMaxApplications();
    }

    // Display applicants' recruitment stats
    public void displayApplicantsRecruitmentStats() {
        recruitmentManager.displayApplicantsRecruitmentStats();
    }

    // View recruitment details based on recruitment ID
    public Recruitment viewRecruitmentDetails(int recruitmentId) {
        return recruitmentManager.findRecruitmentById(recruitmentId);
    }

    // Get the category with the maximum number of recruitments within a specific period
    public String getCategoryWithMaxRecruitment(String startDate, String endDate) {
        return recruitmentManager.getCategoryWithMaxRecruitment(startDate, endDate);
    }

    // Get average and total recruitment counts within a specific period
    public HashMap<String, Double> getAverageAndTotalRecruitment(String startDate, String endDate) {
        return recruitmentManager.getAverageAndTotalRecruitment(startDate, endDate);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public RecruitmentManager getRecruitmentManager() {
        return recruitmentManager;
    }

    public void setRecruitmentManager(RecruitmentManager recruitmentManager) {
        this.recruitmentManager = recruitmentManager;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public User findUserById(int userId) {
    for (User user : users) { // Assuming `users` is a list of all users in the Admin class
        if (user.getUserId() == userId) {
            return user;
        }
    }
    return null; // Return null if no user with the given ID is found
}
    
    public void saveUsers() {
    FileManager.saveToFile(new ArrayList<>(users), USERS_FILE);
}

public void loadUsers() {
    users = FileManager.loadFromFile(USERS_FILE);
}

public void addUserWithAppend(User user) {
    users.add(user);
    FileManager.appendToFile(user, USERS_FILE);
}


}
