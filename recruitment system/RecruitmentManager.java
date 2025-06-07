package rms;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class RecruitmentManager {
    private List<Recruitment> recruitments;
    private static final String RECRUITMENTS_FILE = "recruitments.dat";


    public RecruitmentManager() {
        this.recruitments = new ArrayList<>();
    }

    // Add a new recruitment
    public void addRecruitment(Recruitment recruitment) {
        if (recruitment != null) {
            recruitments.add(recruitment);
        }
    }

    // Find recruitment by ID
    public Recruitment findRecruitmentById(int id) {
        for (Recruitment recruitment : recruitments) {
            if (recruitment.getId() == id) {
                return recruitment;
            }
        }
        return null;
    }

    // Get recruitments per company
    public HashMap<String, Integer> getRecruitmentPerCompany() {
        HashMap<String, Integer> companyRecruitments = new HashMap<>();
        for (Recruitment rec : recruitments) {
            companyRecruitments.put(rec.getCompany(), companyRecruitments.getOrDefault(rec.getCompany(), 0) + 1);
        }
        return companyRecruitments;
    }

    // Get the company with the maximum recruitments
    public String getCompanyWithMaxRecruitment() {
        HashMap<String, Integer> recruitmentPerCompany = getRecruitmentPerCompany();

        if (recruitmentPerCompany.isEmpty()) {
            return "No Data";
        }

        Map.Entry<String, Integer> maxEntry = Collections.max(recruitmentPerCompany.entrySet(), Map.Entry.comparingByValue());
        return "Company: " + maxEntry.getKey() + ", Recruitments: " + maxEntry.getValue();
    }

    // Get the applicant with the most applications
    public String getApplicantWithMaxApplications() {
        HashMap<Applicant, Integer> applicantApplications = new HashMap<>();
        for (Recruitment rec : recruitments) {
            for (Applicant applicant : rec.getApplicants()) {
                applicantApplications.put(applicant, applicantApplications.getOrDefault(applicant, 0) + 1);
            }
        }
        if (applicantApplications.isEmpty()) {
            return "No data";
        }
        return Collections.max(applicantApplications.entrySet(), Map.Entry.comparingByValue()).getKey().getName();
    }

    // Display applicants' recruitment stats
    public void displayApplicantsRecruitmentStats() {
        Map<Applicant, Integer> recruitmentCounts = new HashMap<>();
        Map<Applicant, String> applicantCategories = new HashMap<>();

        for (Recruitment recruitment : recruitments) {
            for (Applicant applicant : recruitment.getApplicants()) {
                // Update recruitment count
                recruitmentCounts.put(applicant, recruitmentCounts.getOrDefault(applicant, 0) + 1);

                // Update categories (concatenating strings)
                String currentCategories = applicantCategories.getOrDefault(applicant, "");
                if (!currentCategories.contains(recruitment.getCategory())) {
                    if (!currentCategories.isEmpty()) {
                        currentCategories += ", ";
                    }
                    currentCategories += recruitment.getCategory();
                    applicantCategories.put(applicant, currentCategories);
                }
            }
        }

        // Display stats
        for (Applicant applicant : recruitmentCounts.keySet()) {
            System.out.println("Applicant: " + applicant.getName());
            System.out.println("Total Recruitments: " + recruitmentCounts.get(applicant));
            System.out.println("Categories: " + applicantCategories.get(applicant));
            System.out.println();
        }
    }

    // Get the category with the maximum recruitments within a specific period
    public String getCategoryWithMaxRecruitment(String startDate, String endDate) {
        Map<String, Integer> categoryCount = new HashMap<>();
        for (Recruitment recruitment : recruitments) {
            if (recruitment.isWithinPeriod(startDate, endDate)) {
                categoryCount.put(recruitment.getCategory(), categoryCount.getOrDefault(recruitment.getCategory(), 0) + 1);
            }
        }
        if (categoryCount.isEmpty()) {
            return "No data";
        }
        return Collections.max(categoryCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    // Get average and total recruitment counts within a specific period
    public HashMap<String, Double> getAverageAndTotalRecruitment(String startDate, String endDate) {
        int total = 0;
        int count = 0;
        for (Recruitment rec : recruitments) {
            if (rec.isWithinPeriod(startDate, endDate)) {
                total++;
                count += rec.getApplicationsCount();
            }
        }
        HashMap<String, Double> result = new HashMap<>();
        result.put("Total Recruitments", (double) total);
        result.put("Average Applications", total > 0 ? (double) count / total : 0.0);
        return result;
    }

    // Get all recruitments
    public List<Recruitment> getRecruitments() {
        return recruitments;
    }
    
    // Method in RecruitmentManager class
public HashMap<Applicant, Integer> getApplicantsRecruitmentStats() {
    HashMap<Applicant, Integer> applicantsRecruitmentStats = new HashMap<>();

    // Iterate through all recruitments to aggregate applications
    for (Recruitment recruitment : recruitments) {
        for (Applicant applicant : recruitment.getApplicants()) {
            // Update the count for each applicant
            applicantsRecruitmentStats.put(
                applicant,
                applicantsRecruitmentStats.getOrDefault(applicant, 0) + 1
            );
        }
    }

    return applicantsRecruitmentStats;
}

    public void setRecruitments(List<Recruitment> recruitments) {
        this.recruitments = recruitments;
    }

   public void loadRecruitments() {
    this.recruitments = FileManager.loadFromFile(RECRUITMENTS_FILE);
    System.out.println("Recruitments loaded successfully.");
}

   public void saveRecruitments() {
    FileManager.saveToFile(new ArrayList<>(this.recruitments), RECRUITMENTS_FILE);
    System.out.println("Recruitments saved successfully.");
}




}
