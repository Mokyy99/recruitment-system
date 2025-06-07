package rms;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Applicant extends User {
    protected String status;
    protected String statusMessage;
    private RecruitmentManager recruitmentManager; // Integrating RecruitmentManager
    private File cvFile;

    public Applicant(int user_id, String name, String email, String password, RecruitmentManager recruitmentManager) {
        super(user_id, name, email, password);
        this.status = "Not Applied";
        this.statusMessage = "No updates available.";
        this.recruitmentManager = recruitmentManager;
    }

    public void searchRecruitment(String field, String value) {
        List<Recruitment> recruitments = recruitmentManager.getRecruitments();
        for (Recruitment recruitment : recruitments) {
            switch (field.toLowerCase()) {
                case "id":
                    if (String.valueOf(recruitment.getId()).equals(value)) {
                        displayRecruitmentDetails(recruitment);
                    }
                    break;
                case "title":
                    if (recruitment.getTitle().equalsIgnoreCase(value)) {
                        displayRecruitmentDetails(recruitment);
                    }
                    break;
                case "company":
                    if (recruitment.getCompany().equalsIgnoreCase(value)) {
                        displayRecruitmentDetails(recruitment);
                    }
                    break;
                case "date":
                    if (recruitment.getDate().equals(value)) {
                        displayRecruitmentDetails(recruitment);
                    }
                    break;
                case "category":
                    if (recruitment.getCategory().equalsIgnoreCase(value)) {
                        displayRecruitmentDetails(recruitment);
                    }
                    break;
                default:
                    System.out.println("Invalid field.");
            }
        }
    }

    private void displayRecruitmentDetails(Recruitment recruitment) {
        System.out.println("ID: " + recruitment.getId() + ", Title: " + recruitment.getTitle() + 
                ", Company: " + recruitment.getCompany() + ", Description: " + recruitment.getDescription() + 
                ", Requirements: " + recruitment.getRequirement() + ", Date: " + recruitment.getDate());
    }

    public void applyForJob(int recruitmentId) {
        Recruitment recruitment = recruitmentManager.findRecruitmentById(recruitmentId);
        if (recruitment != null) {
            if (!recruitment.getApplicants().contains(this)) {
                recruitment.addApplicant(this);
                System.out.println("Successfully applied for job: " + recruitment.getTitle());
            } else {
                System.out.println("You have already applied for this job.");
            }
        } else {
            System.out.println("Recruitment with ID " + recruitmentId + " not found.");
        }
    }

    public void statusUpdate() {
        System.out.println("Your Status is: " + status);
        System.out.println(statusMessage);
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
        System.out.println("Profile Updated for Applicant: " + this.name);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setCvFile(File file) {
        this.cvFile = file;
    }

    public File getCvFile() {
        return this.cvFile;
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
          if (email == null || !email.contains("@")) {
        throw new IllegalArgumentException("Invalid email address.");
    }
    this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RecruitmentManager getRecruitmentManager() {
        return recruitmentManager;
    }

    public void setRecruitmentManager(RecruitmentManager recruitmentManager) {
        this.recruitmentManager = recruitmentManager;
    }
    
    
    
}
