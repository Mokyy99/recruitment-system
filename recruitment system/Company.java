package rms;

import java.io.File;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;

public class Company {
    private int companyId;
    private String name;
    private String email;
    private String phone;
    private String category;
    private RecruitmentManager recruitmentManager; // Integrating RecruitmentManager
    private List<Interview> interviews;
    private ApplicantManager applicantManager; // Use ApplicantManager instead of List<Applicant>
    private int interviewCounter = 1; // To generate unique interview IDs

    public Company( int companyId, String name, String email, String phone, String category, RecruitmentManager recruitmentManager) {
        this.companyId = companyId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.category = category;
        this.recruitmentManager = recruitmentManager;
        this.interviews = new ArrayList<>();
        this.applicantManager = new ApplicantManager(); // Initialize ApplicantManager
    }

    // Getters and Setters
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

    public int getCompanyId() {
        return companyId;
    }

    public RecruitmentManager getRecruitmentManager() {
        return recruitmentManager;
    }

    public ApplicantManager getApplicantManager() {
        return applicantManager;
    }
    
    

    // Recruitment Management Methods
    public void addRecruitment(Recruitment recruitment) {
        if (recruitment == null) {
            throw new IllegalArgumentException("Recruitment cannot be null.");
        }
        recruitmentManager.addRecruitment(recruitment);
    }

    public void editRecruitment(int recruitmentId, String newTitle, String newDescription, String newRequirements, String newDate) {
        Recruitment recruitment = recruitmentManager.findRecruitmentById(recruitmentId);
        if (recruitment != null) {
            recruitment.setTitle(newTitle);
            recruitment.setDescription(newDescription);
            recruitment.setRequirement(newRequirements);
            recruitment.setDate(newDate);
        } else {
            System.out.println("Recruitment with ID " + recruitmentId + " not found.");
        }
    }

    public void removeRecruitment(int recruitmentId) {
        Recruitment recruitment = recruitmentManager.findRecruitmentById(recruitmentId);
        if (recruitment != null) {
            recruitmentManager.getRecruitments().remove(recruitment);
            System.out.println("Recruitment with ID " + recruitmentId + " removed successfully.");
        } else {
            System.out.println("Recruitment with ID " + recruitmentId + " not found.");
        }
    }

    public Recruitment findRecruitmentById(int recruitmentId) {
        return recruitmentManager.findRecruitmentById(recruitmentId);
    }

    public void listRecruitments() {
        List<Recruitment> recruitments = recruitmentManager.getRecruitments();
        if (recruitments.isEmpty()) {
            System.out.println("No recruitments available.");
            return;
        }
        for (Recruitment recruitment : recruitments) {
            System.out.println("ID: " + recruitment.getId() +
                    ", Title: " + recruitment.getTitle() +
                    ", Description: " + recruitment.getDescription() +
                    ", Date: " + recruitment.getDate() +
                    ", Requirements: " + recruitment.getRequirement() +
                    ", Applicants: " + recruitment.getApplicants());
        }
    }

    public void viewApplicantsPerRecruitment(int recruitmentId) {
        Recruitment recruitment = recruitmentManager.findRecruitmentById(recruitmentId);
        if (recruitment != null && !recruitment.getApplicants().isEmpty()) {
            System.out.println("Applicants for Recruitment ID " + recruitment.getId() + ":");
            for (Applicant applicant : recruitment.getApplicants()) {
                System.out.println("Applicant ID: " + applicant.getUserId() + ", Name: " + applicant.getName());
            }
        } else {
            System.out.println("No applicants for this recruitment or recruitment not found.");
        }
    }

    public String getMostAppliedJobOrCategory(String startDate, String endDate, String type) {
        Map<String, Integer> countMap = new HashMap<>();
        List<Recruitment> recruitments = recruitmentManager.getRecruitments();
        for (Recruitment recruitment : recruitments) {
            if (recruitment.isWithinPeriod(startDate, endDate)) {
                String key = type.equalsIgnoreCase("job title") ? recruitment.getTitle() : recruitment.getCategory();
                countMap.put(key, countMap.getOrDefault(key, 0) + 1);
            }
        }

        String mostApplied = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostApplied = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        if (mostApplied != null) {
            return mostApplied + " (Applied " + maxCount + " times)";
        } else {
            return "No recruitments found for the specified period.";
        }
    }

    // Applicant Management Methods
    public void addApplicant(Applicant applicant) {
        applicantManager.addApplicant(applicant);
    }

    public void editApplicant(int applicantId, String newName, String newEmail, String newPassword) {
        applicantManager.editApplicant(applicantId, newName, newEmail, newPassword);
    }

    public void removeApplicant(int applicantId) {
        applicantManager.removeApplicant(applicantId);
    }

    public Applicant findApplicantById(int applicantId) {
        return applicantManager.findApplicantById(applicantId);
    }

    public void displayApplicants() {
        applicantManager.displayApplicants();
    }

    public void searchApplicants(String field, String value) {
        applicantManager.searchApplicants(field, value);
    }

    // Interview Management Methods
    public void addInterview(Interview interview) {
        if (interview == null) {
            throw new IllegalArgumentException("Interview cannot be null.");
        }
        interviews.add(interview);
    }

    public Interview findInterviewById(int interviewId) {
        for (Interview interview : interviews) {
            if (interview.getInterviewId() == interviewId) {
                return interview;
            }
        }
        return null;
    }

    public int getNextInterviewId() {
        return interviewCounter++;
    }

    public String acceptApplicant(int recruitmentId, int applicantId, LocalDate date, LocalTime time, String location) {
        Recruitment recruitment = recruitmentManager.findRecruitmentById(recruitmentId);
        if (recruitment != null) {
            for (Applicant applicant : recruitment.getApplicants()) {
                if (applicant.getUserId() == applicantId) {
                    Interview interview = new Interview(getNextInterviewId(), applicant, date, time, location);
                    interviews.add(interview);
                    applicant.setStatus("Accepted");
                    applicant.setStatusMessage("You have been accepted. Interview scheduled on " + date + " at " + time + " in " + location);
                    return "Applicant " + applicant.getName() + " has been accepted.";
                }
            }
            return "Applicant with ID " + applicantId + " not found in this recruitment.";
        }
        return "Recruitment with ID " + recruitmentId + " not found.";
    }

    public String rejectApplicant(int recruitmentId, int applicantId) {
        Recruitment recruitment = recruitmentManager.findRecruitmentById(recruitmentId);
        if (recruitment != null) {
            for (Applicant applicant : recruitment.getApplicants()) {
                if (applicant.getUserId() == applicantId) {
                    applicant.setStatus("Rejected");
                    applicant.setStatusMessage("We regret to inform you that you have been rejected.");
                    return "Applicant " + applicant.getName() + " has been rejected.";
                }
            }
            return "Applicant with ID " + applicantId + " not found in this recruitment.";
        }
        return "Recruitment with ID " + recruitmentId + " not found.";
    }
}
