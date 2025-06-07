package rms;

import java.util.ArrayList;
import java.util.List;

public class ApplicantManager {
    private List<Applicant> applicants;
    private static final String APPLICANTS_FILE = "applicants.dat";


    public ApplicantManager() {
        this.applicants = new ArrayList<>();
    }

    // Add a new applicant
    public void addApplicant(Applicant applicant) {
        if (applicant != null) {
            applicants.add(applicant);
            System.out.println("Applicant added successfully.");
        } else {
            System.out.println("Applicant cannot be null.");
        }
    }

    // Edit an existing applicant
    public void editApplicant(int applicantId, String newName, String newEmail, String newPassword) {
        Applicant applicant = findApplicantById(applicantId);
        if (applicant != null) {
            applicant.setName(newName);
            applicant.setEmail(newEmail);
            applicant.setPassword(newPassword);
            System.out.println("Applicant with ID " + applicantId + " updated successfully.");
        } else {
            System.out.println("Applicant with ID " + applicantId + " not found.");
        }
    }

    // Remove an applicant by ID
    public void removeApplicant(int applicantId) {
        boolean removed = applicants.removeIf(applicant -> applicant.getUserId() == applicantId);
        if (removed) {
            System.out.println("Applicant with ID " + applicantId + " removed successfully.");
        } else {
            System.out.println("Applicant with ID " + applicantId + " not found.");
        }
    }

    // Find an applicant by ID
    public Applicant findApplicantById(int applicantId) {
        for (Applicant applicant : applicants) {
            if (applicant.getUserId() == applicantId) {
                return applicant;
            }
        }
        return null;
    }

    // Get all applicants
    public List<Applicant> getApplicants() {
        return applicants;
    }

    // Display all applicants
    public void displayApplicants() {
        if (applicants.isEmpty()) {
            System.out.println("No applicants available.");
            return;
        }
        for (Applicant applicant : applicants) {
            System.out.println("ID: " + applicant.getUserId() + ", Name: " + applicant.getName() +
                    ", Email: " + applicant.getEmail() + ", Status: " + applicant.getStatus());
        }
    }

    // Search applicants by field
    public void searchApplicants(String field, String value) {
        boolean found = false;
        for (Applicant applicant : applicants) {
            switch (field.toLowerCase()) {
                case "id":
                    if (String.valueOf(applicant.getUserId()).equals(value)) {
                        displayApplicantDetails(applicant);
                        found = true;
                    }
                    break;
                case "name":
                    if (applicant.getName().equalsIgnoreCase(value)) {
                        displayApplicantDetails(applicant);
                        found = true;
                    }
                    break;
                case "email":
                    if (applicant.getEmail().equalsIgnoreCase(value)) {
                        displayApplicantDetails(applicant);
                        found = true;
                    }
                    break;
                case "status":
                    if (applicant.getStatus().equalsIgnoreCase(value)) {
                        displayApplicantDetails(applicant);
                        found = true;
                    }
                    break;
                default:
                    System.out.println("Invalid field.");
                    return;
            }
        }
        if (!found) {
            System.out.println("No applicants found matching the criteria.");
        }
    }

    private void displayApplicantDetails(Applicant applicant) {
        System.out.println("ID: " + applicant.getUserId() + ", Name: " + applicant.getName() +
                ", Email: " + applicant.getEmail() + ", Status: " + applicant.getStatus() +
                ", Status Message: " + applicant.getStatusMessage());
    }

    public void setApplicants(List<Applicant> applicants) {
        this.applicants = applicants;
    }
    
    public void loadApplicants() {
    this.applicants = FileManager.loadFromFile(APPLICANTS_FILE);
    System.out.println("Applicants loaded successfully.");
}

    public void saveApplicants() {
    FileManager.saveToFile(new ArrayList<>(this.applicants), APPLICANTS_FILE);
    System.out.println("Applicants saved successfully.");
}

    
    
}
