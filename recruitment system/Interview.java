        
package rms;
import java.time.LocalDate;
import java.time.LocalTime;

public class Interview {

    private int interviewId;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private Applicant applicant;

    public Interview(int interviewId, Applicant applicant1, LocalDate date, LocalTime time, String location) {
        if (interviewId <= 0) {
            throw new IllegalArgumentException("Interview ID must be a positive integer.");
        }
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant must not be null.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null.");
        }
        if (time == null) {
            throw new IllegalArgumentException("Time must not be null.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location must not be null or empty.");
        }

        this.interviewId = interviewId;
        this.applicant = applicant;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public void scheduleInterview(int interviewId, LocalDate date, LocalTime time, String location) {
        if (interviewId <= 0) {
            throw new IllegalArgumentException("Interview ID must be a positive integer.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null.");
        }
        if (time == null) {
            throw new IllegalArgumentException("Time must not be null.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location must not be null or empty.");
        }

        this.interviewId = interviewId;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public void rescheduleInterview(int interviewId, LocalDate date, LocalTime time, String location) {
        if (this.interviewId != interviewId) {
            throw new IllegalArgumentException("Interview ID does not match the existing interview.");
        }
        if (date == null) {
            throw new IllegalArgumentException("New date must not be null.");
        }
        if (time == null) {
            throw new IllegalArgumentException("New time must not be null.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("New location must not be null or empty.");
        }

        this.date = date;
        this.time = time;
        this.location = location;
        applicant.statusMessage = "Dear Applicant, Unfortunately we need to reschedule your interview and apologize for any disruption this may cause";
        applicant.status = "Accepted";
    }

    public void cancelInterview(int interviewId) {
        if (this.interviewId != interviewId) {
            throw new IllegalArgumentException("Interview ID does not match the existing interview.");
        }
        if (!hasInterviewScheduled()) {
            throw new IllegalStateException("No interview is currently scheduled to cancel.");
        }

        this.date = null;
        this.time = null;
        this.location = null;
        this.applicant = null;
        applicant.statusMessage = "Dear Applicant, Sorry to inform you, but your interview (" + interviewId + ") has been canceled";
        applicant.status = "Rejected";
    }

    public boolean hasInterviewScheduled() {
        return date != null && time != null && location != null;
    }

    public int getInterviewId() {
        return interviewId;
    }

}
