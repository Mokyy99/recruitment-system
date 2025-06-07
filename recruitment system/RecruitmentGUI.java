package rms;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene; 
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RecruitmentGUI extends Application {

    private RecruitmentManager recruitmentManager = new RecruitmentManager();
    private Admin admin = new Admin(1, "Admin", "admin@example.com", "admin123", recruitmentManager);
    private Applicant applicant = new Applicant(2, "John Doe", "applicant@example.com", "password123", recruitmentManager);
    private Company company = new Company(3, "TechCorp", "contact@techcorp.com", "1234567890", "Tech", recruitmentManager);
    private static final String APPLICANTS_FILE = "applicants.dat";
    private static final String RECRUITMENTS_FILE = "recruitments.dat";
    private static final String USERS_FILE = "users.dat";

    private void loadInitialData() {
    admin.setUsers(FileManager.loadFromFile(USERS_FILE));
    recruitmentManager.setRecruitments(FileManager.loadFromFile(RECRUITMENTS_FILE));
    applicant.getRecruitmentManager().setRecruitments(FileManager.loadFromFile(RECRUITMENTS_FILE));
}

   

    @Override
    public void start(Stage primaryStage) {
         loadInitialData(); // Load data from files
        Scene welcomeScene = createWelcomeScene(primaryStage);
        primaryStage.setTitle("Recruitment Management System");
        primaryStage.setScene(welcomeScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Scene createWelcomeScene(Stage primaryStage) {
        StackPane root = new StackPane();

        Image backgroundImage = new Image("file:C:/Users/user/Desktop/movingggg.gif");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(800);
        backgroundView.setFitHeight(600);

        Text titleText = new Text("Recruitment Management System");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleText.setFill(Color.WHITE);
        titleText.setStroke(Color.DARKBLUE);
        titleText.setStrokeWidth(2.0);
        titleText.setStrokeType(StrokeType.OUTSIDE);

        Text subtitleText = new Text("Welcome!");
        subtitleText.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        subtitleText.setFill(Color.WHITE);
        subtitleText.setStroke(Color.DARKGOLDENROD);
        subtitleText.setStrokeWidth(1.5);
        subtitleText.setStrokeType(StrokeType.OUTSIDE);

        Button loginButton = new Button("Log In");
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        loginButton.setTextFill(Color.WHITE);
        loginButton.setStyle("-fx-background-color: #007ACC;");
        loginButton.setOnAction(event -> primaryStage.setScene(createMainMenuScene(primaryStage)));
        
        

        VBox layout = new VBox(20, titleText, subtitleText, loginButton);
        layout.setStyle("-fx-alignment: center; -fx-spacing: 15;");
        root.getChildren().addAll(backgroundView, layout);

        return new Scene(root, 800, 600);
    }

    private Scene createMainMenuScene(Stage primaryStage) {
        StackPane root = new StackPane();

        Text titleText1 = new Text("RECRUITMENT");
        titleText1.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleText1.setFill(Color.DARKBLUE);

        Text titleText2 = new Text("MANAGEMENT SYSTEM");
        titleText2.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleText2.setFill(Color.DARKCYAN);

        VBox titleLayout = new VBox(5, titleText1, titleText2);
        titleLayout.setStyle("-fx-alignment: center;");
        titleLayout.setTranslateY(-200);

        Button btnAdmin = new Button("Admin");
        Button btnApplicant = new Button("Applicant");
        Button btnCompany = new Button("Company");

        for (Button btn : new Button[]{btnAdmin, btnApplicant, btnCompany}) {
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            btn.setTextFill(Color.WHITE);
            btn.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 10; -fx-padding: 10;");
        }

        btnAdmin.setOnAction(e -> handleAdminLogin(primaryStage));
        btnApplicant.setOnAction(e -> handleApplicantLogin(primaryStage));
        btnCompany.setOnAction(e -> primaryStage.setScene(createCompanyDashboard(primaryStage, company)));


        VBox buttonLayout = new VBox(20, btnAdmin, btnApplicant, btnCompany);
        buttonLayout.setStyle("-fx-alignment: center;");
        buttonLayout.setTranslateY(50);

        root.getChildren().addAll(titleLayout, buttonLayout);

        return new Scene(root, 800, 600);
    }

    private void handleAdminLogin(Stage primaryStage) {
        Stage loginStage = new Stage();
        loginStage.setTitle("Admin Login");

        GridPane loginPane = new GridPane();
        loginPane.setVgap(10);
        loginPane.setHgap(10);
        loginPane.setAlignment(Pos.CENTER);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Log In");
        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.RED);

        loginButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            try {
                admin.logIn(email, password);
                loginStage.close();
                primaryStage.setScene(createAdminDashboard(primaryStage, admin));
            } catch (LoginException e) {
                errorMessage.setText(e.getMessage());
            }
        });

        loginPane.add(emailLabel, 0, 0);
        loginPane.add(emailField, 1, 0);
        loginPane.add(passwordLabel, 0, 1);
        loginPane.add(passwordField, 1, 1);
        loginPane.add(loginButton, 0, 2, 2, 1);
        loginPane.add(errorMessage, 0, 3, 2, 1);

        Scene loginScene = new Scene(loginPane, 400, 300);
        loginStage.setScene(loginScene);
        loginStage.show();
    }

private Scene createAdminDashboard(Stage primaryStage, Admin admin) {
    VBox root = new VBox(20);
    root.setAlignment(Pos.TOP_CENTER);

    Text welcomeText = new Text("Admin Dashboard - Welcome, " + admin.getName());
    welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 24));

    TabPane tabPane = new TabPane();

    // Manage Users Tab
    Tab manageUsersTab = new Tab("Manage Users");
    VBox manageUsersRoot = new VBox(10);
    manageUsersRoot.setAlignment(Pos.TOP_CENTER);

    // Add User
    TextField userIdField = new TextField();
    userIdField.setPromptText("Enter User ID");

    TextField userNameField = new TextField();
    userNameField.setPromptText("Enter User Name");

    TextField userEmailField = new TextField();
    userEmailField.setPromptText("Enter User Email");

    TextField userPasswordField = new TextField();
    userPasswordField.setPromptText("Enter User Password");

    ListView<String> userListView = new ListView<>();

    Button btnAddUser = new Button("Add User");
    btnAddUser.setOnAction(e -> {
    try {
        int userId = Integer.parseInt(userIdField.getText());
        if (userNameField.getText().isEmpty() || userEmailField.getText().isEmpty() || userPasswordField.getText().isEmpty()) {
            userListView.getItems().add("Please fill in all fields.");
            return;
        }
        if (!userEmailField.getText().contains("@") || !userEmailField.getText().contains(".")) {
            userListView.getItems().add("Invalid email format.");
            return;
        }

        User newUser = new Applicant(
                userId,
                userNameField.getText(),
                userEmailField.getText(),
                userPasswordField.getText(),
                admin.getRecruitmentManager()
        );
        admin.addUser(newUser);
        userListView.getItems().add("User Added: " + newUser.getName() + " (ID: " + newUser.getUserId() + ")");
        FileManager.saveToFile(new ArrayList<>(admin.getUsers()), USERS_FILE); // Save data
        userIdField.clear();
        userNameField.clear();
        userEmailField.clear();
        userPasswordField.clear();
    } catch (NumberFormatException ex) {
        userListView.getItems().add("Invalid User ID. Please enter a numeric value.");
    } catch (IllegalArgumentException ex) {
        userListView.getItems().add("Error: " + ex.getMessage());
    }
});


    // Edit User
    TextField editUserIdField = new TextField();
    editUserIdField.setPromptText("Enter User ID to Edit");

    TextField editUserNameField = new TextField();
    editUserNameField.setPromptText("Enter New User Name");

    TextField editUserEmailField = new TextField();
    editUserEmailField.setPromptText("Enter New User Email");

    TextField editUserPasswordField = new TextField();
    editUserPasswordField.setPromptText("Enter New User Password");

    Button btnEditUser = new Button("Edit User");
   btnEditUser.setOnAction(e -> {
    try {
        // Get User ID
        int userId = Integer.parseInt(editUserIdField.getText());

        // Find User
        User existingUser = admin.findUserById(userId);
        if (existingUser == null) {
            userListView.getItems().add("User ID not found.");
            return;
        }

        // Validate and Update User Details
        if (!editUserNameField.getText().isEmpty()) {
            existingUser.setName(editUserNameField.getText());
        }
        if (!editUserEmailField.getText().isEmpty()) {
            if (editUserEmailField.getText().contains("@") && editUserEmailField.getText().contains(".")) {
                existingUser.setEmail(editUserEmailField.getText());
            } else {
                userListView.getItems().add("Invalid email format.");
                return;
            }
        }
        if (!editUserPasswordField.getText().isEmpty()) {
            existingUser.setPassword(editUserPasswordField.getText());
        }

        // Save Updated User List to File
        FileManager.saveToFile(new ArrayList<>(admin.getUsers()), USERS_FILE);

        // Notify Success
        userListView.getItems().add("User Edited: " + existingUser.getName() + " (ID: " + existingUser.getUserId() + ")");

        // Clear Input Fields
        editUserIdField.clear();
        editUserNameField.clear();
        editUserEmailField.clear();
        editUserPasswordField.clear();

    } catch (NumberFormatException ex) {
        userListView.getItems().add("Invalid User ID. Please enter a numeric value.");
    } catch (Exception ex) {
        userListView.getItems().add("An error occurred: " + ex.getMessage());
    }
});

    // Remove User
    TextField removeUserIdField = new TextField();
    removeUserIdField.setPromptText("Enter User ID to Remove");

    Button btnRemoveUser = new Button("Remove User");
    btnRemoveUser.setOnAction(e -> {
    try {
        int userId = Integer.parseInt(removeUserIdField.getText());
        admin.removeUser(userId);
        FileManager.saveToFile(new ArrayList<>(admin.getUsers()), USERS_FILE); // Save data
        userListView.getItems().add("User Removed: ID " + userId);
    } catch (NumberFormatException ex) {
        userListView.getItems().add("Invalid User ID. Please enter a numeric value.");
    }
});


    manageUsersRoot.getChildren().addAll(
            new Label("Add User:"), userIdField, userNameField, userEmailField, userPasswordField, btnAddUser,
            new Label("Edit User:"),  editUserIdField, editUserNameField, editUserEmailField, editUserPasswordField, btnEditUser,
            new Label("Remove User:"), removeUserIdField, btnRemoveUser,
            new Label("User List:"), userListView
    );
    ScrollPane manageUsersScrollPane = new ScrollPane(manageUsersRoot);
    manageUsersScrollPane.setFitToWidth(true);
    manageUsersTab.setContent(manageUsersScrollPane);

    // User Reports Tab
    Tab userReportsTab = new Tab("User Reports");
    VBox userReportsRoot = new VBox(10);
    userReportsRoot.setAlignment(Pos.TOP_CENTER);

    TextArea userReportsTextArea = new TextArea();
    userReportsTextArea.setEditable(false);

    Button btnRecruitmentsPerCompany = new Button("Recruitments Per Company");
btnRecruitmentsPerCompany.setOnAction(e -> {
    userReportsTextArea.clear();
    HashMap<String, Integer> companyStats = admin.getRecruitmentPerCompany();
    if (companyStats.isEmpty()) {
        userReportsTextArea.appendText("No data available.\n");
    } else {
        companyStats.forEach((company, count) ->
                userReportsTextArea.appendText("Company: " + company + ", Recruitments: " + count + "\n")
        );
    }
});

Button btnMaxRecruitmentsCompany = new Button("Company with Max Recruitments");
btnMaxRecruitmentsCompany.setOnAction(e -> {
    userReportsTextArea.clear();
    String result = admin.getCompanyWithMaxRecruitment();
    userReportsTextArea.appendText(result.isEmpty() ? "No data available.\n" : "Company with Max Recruitments: " + result);
});

Button btnMaxApplicationsApplicant = new Button("Applicant with Max Applications");
btnMaxApplicationsApplicant.setOnAction(e -> {
    userReportsTextArea.clear(); // Clear previous results
    String maxApplicant = admin.getApplicantWithMaxApplications();
    if (maxApplicant.isEmpty()) {
        userReportsTextArea.appendText("No data available for applicant applications.\n");
    } else {
        userReportsTextArea.appendText("Applicant with Max Applications: " + maxApplicant + "\n");
    }
});

Button btnRecruitmentsByApplicants = new Button("Recruitments Applied by Applicants");
btnRecruitmentsByApplicants.setOnAction(e -> {
    userReportsTextArea.clear(); // Clear previous results
    HashMap<Applicant, Integer> recruitmentData = admin.getRecruitmentManager().getApplicantsRecruitmentStats();
    if (recruitmentData.isEmpty()) {
        userReportsTextArea.appendText("No data available for recruitments applied by applicants.\n");
    } else {
        recruitmentData.forEach((applicant, count) -> {
            userReportsTextArea.appendText("Applicant: " + applicant.getName() + ", Applications: " + count + "\n");
        });
    }
});


     userReportsRoot.getChildren().addAll(
    new Label("User Reports Options:"),
    btnRecruitmentsPerCompany,
    btnMaxRecruitmentsCompany,
    btnMaxApplicationsApplicant,
    btnRecruitmentsByApplicants,
    new Label("Report Results:"),
    userReportsTextArea
);

    ScrollPane userReportsScrollPane = new ScrollPane(userReportsRoot);
    userReportsScrollPane.setFitToWidth(true);
    userReportsTab.setContent(userReportsScrollPane);

    // Recruitment Reports Tab
    Tab recruitmentReportsTab = new Tab("Recruitment Reports");
    VBox recruitmentReportsRoot = new VBox(10);
    recruitmentReportsRoot.setAlignment(Pos.TOP_CENTER);

    TextArea recruitmentReportsTextArea = new TextArea();
    recruitmentReportsTextArea.setEditable(false);

    Button btnMaxCategory = new Button("Max Recruitment Category");
    btnMaxCategory.setOnAction(e -> {
        recruitmentReportsTextArea.clear();
        recruitmentReportsTextArea.appendText("Max Recruitment Category\n");
    });

    recruitmentReportsRoot.getChildren().addAll(
            new Label("Recruitment Report Options:"), btnMaxCategory,
            new Label("Report Results:"), recruitmentReportsTextArea
    );
    ScrollPane recruitmentReportsScrollPane = new ScrollPane(recruitmentReportsRoot);
    recruitmentReportsScrollPane.setFitToWidth(true);
    recruitmentReportsTab.setContent(recruitmentReportsScrollPane);

    tabPane.getTabs().addAll(manageUsersTab, userReportsTab, recruitmentReportsTab);

   

    // Theme Toggle Button
    Button themeToggle = new Button("Switch to Dark Theme");
    themeToggle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    themeToggle.setOnAction(e -> toggleTheme(root, themeToggle)); // Toggle theme action

    // Back Button
    Button btnBack = new Button("Back");
    btnBack.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    btnBack.setOnAction(e -> primaryStage.setScene(createMainMenuScene(primaryStage))); // Back to main menu

    // Add components to the root layout
    root.getChildren().addAll(welcomeText, tabPane, themeToggle, btnBack);

    // Apply default light theme
    applyLightTheme(root);

    return new Scene(root, 800, 600);
}


// Theme-related utility methods
private void applyLightTheme(Pane pane) {
    pane.setStyle("-fx-background-color: white;");
    pane.lookupAll("*").forEach(node -> {
        if (node instanceof Text) ((Text) node).setFill(Color.BLACK);
        if (node instanceof Label) ((Label) node).setTextFill(Color.BLACK);
        if (node instanceof Button) ((Button) node).setTextFill(Color.BLACK);
    });
}

private void applyDarkTheme(Pane pane) {
    pane.setStyle("-fx-background-color: black;");
    pane.lookupAll("*").forEach(node -> {
        if (node instanceof Text) ((Text) node).setFill(Color.WHITE);
        if (node instanceof Label) ((Label) node).setTextFill(Color.WHITE);
        if (node instanceof Button) ((Button) node).setTextFill(Color.WHITE);
    });
}

private void toggleTheme(Pane pane, Button themeToggle) {
    if ("Switch to Dark Theme".equals(themeToggle.getText())) {
        applyDarkTheme(pane);
        themeToggle.setText("Switch to Light Theme");
    } else {
        applyLightTheme(pane);
        themeToggle.setText("Switch to Dark Theme");
    }
}


    private void handleApplicantLogin(Stage primaryStage) {
        Stage loginStage = new Stage();
        loginStage.setTitle("Applicant Login");

        GridPane loginPane = new GridPane();
        loginPane.setVgap(10);
        loginPane.setHgap(10);
        loginPane.setAlignment(Pos.CENTER);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Log In");
        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.RED);

        loginButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            try {
                applicant.logIn(email, password);
                loginStage.close();
                primaryStage.setScene(createApplicantDashboard(primaryStage, applicant));
            } catch (LoginException e) {
                errorMessage.setText(e.getMessage());
            }
        });

        loginPane.add(emailLabel, 0, 0);
        loginPane.add(emailField, 1, 0);
        loginPane.add(passwordLabel, 0, 1);
        loginPane.add(passwordField, 1, 1);
        loginPane.add(loginButton, 0, 2, 2, 1);
        loginPane.add(errorMessage, 0, 3, 2, 1);

        Scene loginScene = new Scene(loginPane, 400, 300);
        loginStage.setScene(loginScene);
        loginStage.show();
    }

    
      // Create the Applicant Dashboard with Search, Apply Job, View Status, and Back Button
    private Scene createApplicantDashboard(Stage primaryStage, Applicant applicant) {
    VBox root = new VBox(20);
    root.setAlignment(Pos.TOP_CENTER);

    // Welcome text
    Text welcomeText = new Text("Welcome, " + applicant.getName());
    welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 24));

    // Search Section
    TextField searchField = new TextField();
    searchField.setPromptText("Enter search value (e.g., job title)");
    searchField.setPrefWidth(300);

    ChoiceBox<String> searchCriteria = new ChoiceBox<>();
    searchCriteria.getItems().addAll("Title", "Category", "All Jobs");
    searchCriteria.setValue("Title");

    Button btnSearchRecruitments = new Button("Search Recruitments");
    ListView<String> resultsList = new ListView<>();
    btnSearchRecruitments.setOnAction(e -> {
        String criteria = searchCriteria.getValue().toLowerCase();
        String value = searchField.getText();

        resultsList.getItems().clear();

        if (criteria.equals("all jobs")) {
            // Display all jobs
            for (Recruitment recruitment : applicant.getRecruitmentManager().getRecruitments()) {
                resultsList.getItems().add("ID: " + recruitment.getId() + ", Title: " + recruitment.getTitle() +
                        ", Company: " + recruitment.getCompany() + ", Category: " + recruitment.getCategory());
            }
        } else {
            if (value.isEmpty()) {
                resultsList.getItems().add("Please enter a value to search.");
                return;
            }

            // Search by specific criteria
            for (Recruitment recruitment : applicant.getRecruitmentManager().getRecruitments()) {
                boolean matches = switch (criteria) {
                    case "title" -> recruitment.getTitle().equalsIgnoreCase(value);
                    case "category" -> recruitment.getCategory().equalsIgnoreCase(value);
                    default -> false;
                };
                if (matches) {
                    resultsList.getItems().add("ID: " + recruitment.getId() + ", Title: " + recruitment.getTitle() +
                            ", Company: " + recruitment.getCompany() + ", Category: " + recruitment.getCategory());
                }
            }
            if (resultsList.getItems().isEmpty()) {
                resultsList.getItems().add("No matching jobs found.");
            }
        }
    });

    // Apply Job Section
    Button btnApplyJob = new Button("Apply for Job");
    TextField jobIdField = new TextField();
    jobIdField.setPromptText("Enter Job ID to Apply");

    btnApplyJob.setOnAction(e -> {
        String jobIdText = jobIdField.getText();
        if (jobIdText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a Job ID.", ButtonType.OK);
            alert.show();
            return;
        }
        try {
            int jobId = Integer.parseInt(jobIdText);
            Recruitment recruitment = applicant.getRecruitmentManager().findRecruitmentById(jobId);
            if (recruitment == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Job not found.", ButtonType.OK);
                alert.show();
                return;
            }

            // Attach CV
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Attach CV");
            File cvFile = fileChooser.showOpenDialog(primaryStage);
            if (cvFile == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please attach a CV.", ButtonType.OK);
                alert.show();
                return;
            }
            applicant.setCvFile(cvFile);

            applicant.applyForJob(jobId);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Successfully applied for the job!", ButtonType.OK);
            successAlert.show();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Job ID.", ButtonType.OK);
            alert.show();
        }
    });

    // View Status Section
    Button btnViewStatus = new Button("View Application Status");
    ListView<String> statusList = new ListView<>();
    btnViewStatus.setOnAction(e -> {
        statusList.getItems().clear();
        statusList.getItems().add("Status: " + applicant.getStatus());
        statusList.getItems().add("Message: " + applicant.getStatusMessage());
    });

    // Back Button
    Button btnBack = new Button("Back");
    btnBack.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    btnBack.setTextFill(Color.WHITE);
    btnBack.setStyle("-fx-background-color: #007ACC;");
    btnBack.setOnAction(e -> primaryStage.setScene(createMainMenuScene(primaryStage)));

    // Layout
    root.getChildren().addAll(
            welcomeText,
            searchField,
            searchCriteria,
            btnSearchRecruitments,
            resultsList,
            new Label("Apply for a Job:"),
            jobIdField,
            btnApplyJob,
            new Label("Application Status:"),
            btnViewStatus,
            statusList,
            btnBack
    );

    return new Scene(root, 800, 600);
}



    private void goToPlaceholderScene(Stage primaryStage, String dashboardName) {
        StackPane root = new StackPane();
        Text placeholderText = new Text(dashboardName);
        placeholderText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        placeholderText.setFill(Color.DARKBLUE);
        root.getChildren().add(placeholderText);
        primaryStage.setScene(new Scene(root, 800, 600));
    }
    
   private Scene createCompanyDashboard(Stage primaryStage, Company company) {
    VBox root = new VBox(20);
    root.setAlignment(Pos.TOP_CENTER);

    // Welcome text
    Text welcomeText = new Text("Company Dashboard - Welcome, " + company.getName());
    welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 24));

    // Tabs for Manage Recruitment and View Reports
    TabPane tabPane = new TabPane();

    // Manage Recruitment Tab
    Tab manageRecruitmentTab = new Tab("Manage Recruitment");
    VBox manageRecruitmentRoot = new VBox(10);
    manageRecruitmentRoot.setAlignment(Pos.TOP_CENTER);

    TextField recruitmentTitleField = new TextField();
    recruitmentTitleField.setPromptText("Enter Recruitment Title");

    TextField recruitmentCategoryField = new TextField();
    recruitmentCategoryField.setPromptText("Enter Recruitment Category");

    TextField recruitmentDescriptionField = new TextField();
    recruitmentDescriptionField.setPromptText("Enter Recruitment Description");

    TextField recruitmentRequirementsField = new TextField();
    recruitmentRequirementsField.setPromptText("Enter Recruitment Requirements");

    TextField recruitmentDateField = new TextField();
    recruitmentDateField.setPromptText("Enter Recruitment Date (YYYY-MM-DD)");

    Button btnAddRecruitment = new Button("Add Recruitment");
    ListView<String> recruitmentList = new ListView<>();
    btnAddRecruitment.setOnAction(e -> {
    try {
        Recruitment recruitment = new Recruitment(
                company.getRecruitmentManager().getRecruitments().size() + 1,
                recruitmentTitleField.getText(),
                company.getName(),
                recruitmentCategoryField.getText(),
                recruitmentDescriptionField.getText(),
                recruitmentRequirementsField.getText(),
                recruitmentDateField.getText()
        );
        company.addRecruitment(recruitment);
        recruitmentList.getItems().add("Recruitment Added: " + recruitment.getTitle());
        FileManager.saveToFile(new ArrayList<>(recruitmentManager.getRecruitments()), RECRUITMENTS_FILE); // Save data
    } catch (Exception ex) {
        recruitmentList.getItems().add("Failed to add recruitment: " + ex.getMessage());
    }
});


    TextField editRecruitmentIdField = new TextField();
    editRecruitmentIdField.setPromptText("Enter Recruitment ID to Edit");

    TextField editRecruitmentTitleField = new TextField();
    editRecruitmentTitleField.setPromptText("New Title");

    TextField editRecruitmentDescriptionField = new TextField();
    editRecruitmentDescriptionField.setPromptText("New Description");

    TextField editRecruitmentRequirementsField = new TextField();
    editRecruitmentRequirementsField.setPromptText("New Requirements");

    TextField editRecruitmentDateField = new TextField();
    editRecruitmentDateField.setPromptText("New Date (YYYY-MM-DD)");

    Button btnEditRecruitment = new Button("Edit Recruitment");
    btnEditRecruitment.setOnAction(e -> {
        try {
            int recruitmentId = Integer.parseInt(editRecruitmentIdField.getText());
            company.editRecruitment(
                    recruitmentId,
                    editRecruitmentTitleField.getText(),
                    editRecruitmentDescriptionField.getText(),
                    editRecruitmentRequirementsField.getText(),
                    editRecruitmentDateField.getText()
            );
            recruitmentList.getItems().add("Recruitment ID " + recruitmentId + " updated successfully.");
        } catch (NumberFormatException ex) {
            recruitmentList.getItems().add("Invalid Recruitment ID.");
        }
    });

    Button btnRemoveRecruitment = new Button("Remove Recruitment");
    TextField removeRecruitmentIdField = new TextField();
    removeRecruitmentIdField.setPromptText("Enter Recruitment ID to Remove");
    btnRemoveRecruitment.setOnAction(e -> {
        try {
            int recruitmentId = Integer.parseInt(removeRecruitmentIdField.getText());
            company.removeRecruitment(recruitmentId);
            recruitmentList.getItems().add("Recruitment ID " + recruitmentId + " removed successfully.");
        } catch (NumberFormatException ex) {
            recruitmentList.getItems().add("Invalid Recruitment ID.");
        }
    });

    manageRecruitmentRoot.getChildren().addAll(
            new Label("Add Recruitment:"),
            recruitmentTitleField, recruitmentCategoryField, recruitmentDescriptionField,
            recruitmentRequirementsField, recruitmentDateField, btnAddRecruitment,
            new Label("Edit Recruitment:"),
            editRecruitmentIdField, editRecruitmentTitleField, editRecruitmentDescriptionField,
            editRecruitmentRequirementsField, editRecruitmentDateField, btnEditRecruitment,
            new Label("Remove Recruitment:"),
            removeRecruitmentIdField, btnRemoveRecruitment,
            recruitmentList
    );
    manageRecruitmentTab.setContent(manageRecruitmentRoot);

    // View Reports Tab
    Tab viewReportsTab = new Tab("View Reports");
    VBox viewReportsRoot = new VBox(10);
    viewReportsRoot.setAlignment(Pos.TOP_CENTER);

    TextField viewApplicantsRecruitmentIdField = new TextField();
    viewApplicantsRecruitmentIdField.setPromptText("Enter Recruitment ID to View Applicants");

    Button btnViewApplicants = new Button("View Applicants");
    ListView<String> applicantsList = new ListView<>();
    btnViewApplicants.setOnAction(e -> {
        try {
            int recruitmentId = Integer.parseInt(viewApplicantsRecruitmentIdField.getText());
            Recruitment recruitment = company.findRecruitmentById(recruitmentId);
            if (recruitment != null) {
                applicantsList.getItems().clear();
                for (Applicant applicant : recruitment.getApplicants()) {
                    applicantsList.getItems().add("Applicant: " + applicant.getName());
                }
            } else {
                applicantsList.getItems().add("Recruitment ID not found.");
            }
        } catch (NumberFormatException ex) {
            applicantsList.getItems().add("Invalid Recruitment ID.");
        }
    });

    TextField startDateField = new TextField();
    startDateField.setPromptText("Enter start date (YYYY-MM-DD)");
    TextField endDateField = new TextField();
    endDateField.setPromptText("Enter end date (YYYY-MM-DD)");

    Button btnRecruitmentStats = new Button("Recruitment Stats");
    btnRecruitmentStats.setOnAction(e -> {
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        String mostAppliedCategory = company.getMostAppliedJobOrCategory(startDate, endDate, "category");
        applicantsList.getItems().add("Most Applied Category: " + mostAppliedCategory);
    });

    Button btnAcceptApplicant = new Button("Accept Applicant");
    TextField acceptApplicantRecruitmentIdField = new TextField();
    acceptApplicantRecruitmentIdField.setPromptText("Enter Recruitment ID");
    TextField acceptApplicantIdField = new TextField();
    acceptApplicantIdField.setPromptText("Enter Applicant ID");
    btnAcceptApplicant.setOnAction(e -> {
        try {
            int recruitmentId = Integer.parseInt(acceptApplicantRecruitmentIdField.getText());
            int applicantId = Integer.parseInt(acceptApplicantIdField.getText());
            String result = company.acceptApplicant(recruitmentId, applicantId, LocalDate.now(), LocalTime.now(), "Office Location");
            applicantsList.getItems().add(result);
        } catch (NumberFormatException ex) {
            applicantsList.getItems().add("Invalid ID(s).");
        }
    });
    
    TextField rejectApplicantRecruitmentIdField = new TextField();
    rejectApplicantRecruitmentIdField.setPromptText("Enter Recruitment ID");

    TextField rejectApplicantIdField = new TextField();
    rejectApplicantIdField.setPromptText("Enter Applicant ID");
    
    Button btnRejectApplicant = new Button("Reject Applicant");
    btnRejectApplicant.setOnAction(e -> {
        try {
            int recruitmentId = Integer.parseInt(rejectApplicantRecruitmentIdField.getText());
            int applicantId = Integer.parseInt(rejectApplicantIdField.getText());
            String result = company.rejectApplicant(recruitmentId, applicantId);
            applicantsList.getItems().add(result);
        } catch (NumberFormatException ex) {
            applicantsList.getItems().add("Invalid ID(s).");
        }
    });

    viewReportsRoot.getChildren().addAll(
            new Label("View Applicants:"),
            viewApplicantsRecruitmentIdField, btnViewApplicants, applicantsList,
            new Label("Recruitment Stats:"),
            startDateField, endDateField, btnRecruitmentStats,
            new Label("Accept Applicant:"),
            acceptApplicantRecruitmentIdField, acceptApplicantIdField, btnAcceptApplicant,
            new Label("Reject Applicant:"),
            rejectApplicantRecruitmentIdField, rejectApplicantIdField, btnRejectApplicant
    );
    viewReportsTab.setContent(viewReportsRoot);

    tabPane.getTabs().addAll(manageRecruitmentTab, viewReportsTab);

    Button btnBack = new Button("Back");
    btnBack.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    btnBack.setTextFill(Color.WHITE);
    btnBack.setStyle("-fx-background-color: #007ACC;");
    btnBack.setOnAction(e -> primaryStage.setScene(createMainMenuScene(primaryStage)));

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(tabPane);
    scrollPane.setFitToWidth(true);

    root.getChildren().addAll(welcomeText, scrollPane, btnBack);

    return new Scene(root, 800, 600);
}





    public static void main(String[] args) {
        launch(args);
    }
}
