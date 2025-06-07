package rms;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Recruitment {
    private int id;
    private String title;
    private String company;
    private String category;
    private String description;
    private String requirement;
    private String date;
    private List<Applicant> applicants;  // Updated to List<Applicant>
    

    
    
    
    public Recruitment(int id, String title, String company, String category, String description, String requirement, String date) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.category = category;
        this.description = description;
        this.requirement = requirement;
        this.date = date;
        this.applicants = new ArrayList<>(); // Properly initialize applicants list
        
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirement() {
        return requirement;
    }

    public String getDate() {
        return date;
    }

    public List<Applicant> getApplicants() {
        return applicants;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Method to add an applicant to the recruitment
    public void addApplicant(Applicant applicant) {
        if (applicant != null) {
            applicants.add(applicant);
        }
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    

    // Method to check if the recruitment date is within a given period
    public boolean isWithinPeriod(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date recruitmentDate = dateFormat.parse(this.date);
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            return !recruitmentDate.before(start) && !recruitmentDate.after(end);
        } catch (ParseException e) {
            System.out.println("Error parsing dates: " + e.getMessage());
            return false;
        }
    }

    // Method to get the number of applicants
    public int getApplicationsCount() {
        return applicants.size();
    }
    
    
}