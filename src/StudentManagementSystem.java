import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class StudentManagementSystem extends Application {

    // Data storage
    private ObservableList<Student> students = FXCollections.observableArrayList();
    private ObservableList<Course> courses = FXCollections.observableArrayList();
    private ObservableList<Enrollment> enrollments = FXCollections.observableArrayList();

    // Student Management UI
    private TableView<Student> studentTable = new TableView<>();
    private TextField studentIdField = new TextField();
    private TextField studentNameField = new TextField();
    private TextField studentEmailField = new TextField();

    // Course Enrollment UI
    private ComboBox<Course> courseComboBox = new ComboBox<>();
    private TableView<Student> availableStudentsTable = new TableView<>();

    // Grade Management UI
    private ComboBox<Student> gradeStudentComboBox = new ComboBox<>();
    private TableView<Enrollment> gradesTable = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        setupSampleData();
        setupTables();
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(createMenuBar());
        mainLayout.setCenter(createMainTabs());
        
        Scene scene = new Scene(mainLayout, 900, 600);
        primaryStage.setTitle("Student Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupSampleData() {
        // Add sample courses
        courses.addAll(
            new Course("CS101", "Introduction to Programming"),
            new Course("MA101", "Calculus I"),
            new Course("PH101", "Physics I"),
            new Course("EN101", "English Composition")
        );

        // Add sample students
        students.addAll(
            new Student("S001", "John Smith", "john.smith@university.edu"),
            new Student("S002", "Emily Johnson", "emily.johnson@university.edu"),
            new Student("S003", "Michael Brown", "michael.brown@university.edu")
        );

        // Setup course combo box
        courseComboBox.setItems(courses);
        courseComboBox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course == null ? "Select Course" : course.getCode() + " - " + course.getName();
            }
            @Override
            public Course fromString(String string) {
                return null;
            }
        });

        // Setup student combo box for grades
        gradeStudentComboBox.setItems(students);
        gradeStudentComboBox.setConverter(new StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                return student == null ? "Select Student" : student.getId() + " - " + student.getName();
            }
            @Override
            public Student fromString(String string) {
                return null;
            }
        });

        // Event handlers
        courseComboBox.setOnAction(e -> refreshAvailableStudents());
        gradeStudentComboBox.setOnAction(e -> refreshGradesTable());
    }

    private void setupTables() {
        setupStudentTable();
        setupAvailableStudentsTable();
        setupGradesTable();
    }

    private void setupStudentTable() {
        studentTable.getColumns().clear();

        TableColumn<Student, String> idColumn = new TableColumn<>("Student ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        TableColumn<Student, String> nameColumn = new TableColumn<>("Full Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Student, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        studentTable.getColumns().addAll(idColumn, nameColumn, emailColumn);
        studentTable.setItems(students);
        studentTable.setPrefHeight(200);

        studentTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    fillStudentForm(newValue);
                }
            });
    }

    private void setupAvailableStudentsTable() {
        availableStudentsTable.getColumns().clear();

        TableColumn<Student, String> idColumn = new TableColumn<>("Student ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        TableColumn<Student, String> nameColumn = new TableColumn<>("Full Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Student, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        availableStudentsTable.getColumns().addAll(idColumn, nameColumn, emailColumn);
        availableStudentsTable.setPrefHeight(200);
    }

    private void setupGradesTable() {
        gradesTable.getColumns().clear();

        TableColumn<Enrollment, String> courseCodeColumn = new TableColumn<>("Course Code");
        courseCodeColumn.setCellValueFactory(cellData -> cellData.getValue().courseCodeProperty());

        TableColumn<Enrollment, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());

        TableColumn<Enrollment, String> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());

        gradesTable.getColumns().addAll(courseCodeColumn, courseNameColumn, gradeColumn);
        gradesTable.setPrefHeight(200);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAbout());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }

    private TabPane createMainTabs() {
        TabPane tabPane = new TabPane();

        Tab studentTab = new Tab("Student Management");
        studentTab.setContent(createStudentTab());
        studentTab.setClosable(false);

        Tab enrollmentTab = new Tab("Course Enrollment");
        enrollmentTab.setContent(createEnrollmentTab());
        enrollmentTab.setClosable(false);

        Tab gradesTab = new Tab("Grade Management");
        gradesTab.setContent(createGradesTab());
        gradesTab.setClosable(false);

        tabPane.getTabs().addAll(studentTab, enrollmentTab, gradesTab);
        return tabPane;
    }

    private VBox createStudentTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label title = new Label("Student Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));
        form.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 5;");

        Label formTitle = new Label("Add New Student");
        formTitle.setStyle("-fx-font-weight: bold;");
        form.add(formTitle, 0, 0, 2, 1);

        form.add(new Label("Student ID:"), 0, 1);
        form.add(studentIdField, 1, 1);

        form.add(new Label("Full Name:"), 0, 2);
        form.add(studentNameField, 1, 2);

        form.add(new Label("Email:"), 0, 3);
        form.add(studentEmailField, 1, 3);

        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(10));

        Button addButton = new Button("Add Student");
        addButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        addButton.setOnAction(e -> addStudent());

        Button updateButton = new Button("Update Student");
        updateButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        updateButton.setOnAction(e -> updateStudent());

        Button clearButton = new Button("Clear Form");
        clearButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
        clearButton.setOnAction(e -> clearStudentForm());

        buttons.getChildren().addAll(addButton, updateButton, clearButton);
        form.add(buttons, 0, 4, 2, 1);

        layout.getChildren().addAll(title, form, studentTable);
        return layout;
    }

    private VBox createEnrollmentTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label title = new Label("Course Enrollment");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox courseSelection = new HBox(10);
        courseSelection.setPadding(new Insets(10));
        courseSelection.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
        courseSelection.getChildren().addAll(new Label("Select Course:"), courseComboBox);

        Label availableLabel = new Label("Available Students:");
        availableLabel.setStyle("-fx-font-weight: bold;");

        Button enrollButton = new Button("Enroll Selected Student");
        enrollButton.setStyle("-fx-background-color: #fd7e14; -fx-text-fill: white;");
        enrollButton.setOnAction(e -> enrollStudent());

        layout.getChildren().addAll(title, courseSelection, availableLabel, availableStudentsTable, enrollButton);
        return layout;
    }

    private VBox createGradesTab() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #f8f9fa;");

        Label title = new Label("Grade Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox studentSelection = new HBox(10);
        studentSelection.setPadding(new Insets(10));
        studentSelection.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
        studentSelection.getChildren().addAll(new Label("Select Student:"), gradeStudentComboBox);

        Label gradesLabel = new Label("Student's Courses and Grades:");
        gradesLabel.setStyle("-fx-font-weight: bold;");

        Button assignGradeButton = new Button("Assign Grade");
        assignGradeButton.setStyle("-fx-background-color: #6f42c1; -fx-text-fill: white;");
        assignGradeButton.setOnAction(e -> assignGrade());

        layout.getChildren().addAll(title, studentSelection, gradesLabel, gradesTable, assignGradeButton);
        return layout;
    }

    // Event Handlers
    private void addStudent() {
        try {
            String id = studentIdField.getText().trim();
            String name = studentNameField.getText().trim();
            String email = studentEmailField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
                showError("Please fill in all fields");
                return;
            }

            for (Student student : students) {
                if (student.getId().equals(id)) {
                    showError("Student ID already exists");
                    return;
                }
            }

            Student newStudent = new Student(id, name, email);
            students.add(newStudent);
            gradeStudentComboBox.setItems(FXCollections.observableArrayList(students));
            
            clearStudentForm();
            showSuccess("Student added successfully: " + id);

        } catch (Exception e) {
            showError("Error adding student: " + e.getMessage());
        }
    }

    private void updateStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a student to update");
            return;
        }

        try {
            String newName = studentNameField.getText().trim();
            String newEmail = studentEmailField.getText().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                showError("Name and email cannot be empty");
                return;
            }

            selected.setName(newName);
            selected.setEmail(newEmail);
            studentTable.refresh();
            showSuccess("Student updated successfully");

        } catch (Exception e) {
            showError("Error updating student: " + e.getMessage());
        }
    }

    private void enrollStudent() {
        Course selectedCourse = courseComboBox.getValue();
        Student selectedStudent = availableStudentsTable.getSelectionModel().getSelectedItem();

        if (selectedCourse == null || selectedStudent == null) {
            showError("Please select both a course and a student");
            return;
        }

        try {
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStudentId().equals(selectedStudent.getId()) && 
                    enrollment.getCourseCode().equals(selectedCourse.getCode())) {
                    showError("Student is already enrolled in this course");
                    return;
                }
            }

            Enrollment newEnrollment = new Enrollment(
                selectedStudent.getId(),
                selectedStudent.getName(),
                selectedCourse.getCode(),
                selectedCourse.getName(),
                "Not Graded"
            );

            enrollments.add(newEnrollment);
            refreshAvailableStudents();
            refreshGradesTable();
            showSuccess(selectedStudent.getName() + " enrolled in " + selectedCourse.getName());

        } catch (Exception e) {
            showError("Error enrolling student: " + e.getMessage());
        }
    }

    private void assignGrade() {
        Student selectedStudent = gradeStudentComboBox.getValue();
        Enrollment selectedEnrollment = gradesTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null || selectedEnrollment == null) {
            showError("Please select a student and a course");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedEnrollment.getGrade());
        dialog.setTitle("Assign Grade");
        dialog.setHeaderText("Enter grade for " + selectedEnrollment.getCourseName());
        dialog.setContentText("Grade:");

        dialog.showAndWait().ifPresent(grade -> {
            if (!grade.trim().isEmpty()) {
                selectedEnrollment.setGrade(grade.trim().toUpperCase());
                gradesTable.refresh();
                showSuccess("Grade " + grade + " assigned successfully");
            }
        });
    }

    // Helper Methods
    private void refreshAvailableStudents() {
        Course selectedCourse = courseComboBox.getValue();
        if (selectedCourse == null) {
            availableStudentsTable.setItems(FXCollections.observableArrayList());
            return;
        }

        ObservableList<Student> available = FXCollections.observableArrayList();
        for (Student student : students) {
            boolean isEnrolled = false;
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStudentId().equals(student.getId()) && 
                    enrollment.getCourseCode().equals(selectedCourse.getCode())) {
                    isEnrolled = true;
                    break;
                }
            }
            if (!isEnrolled) {
                available.add(student);
            }
        }

        availableStudentsTable.setItems(available);
    }

    private void refreshGradesTable() {
        Student selectedStudent = gradeStudentComboBox.getValue();
        if (selectedStudent == null) {
            gradesTable.setItems(FXCollections.observableArrayList());
            return;
        }

        ObservableList<Enrollment> studentEnrollments = FXCollections.observableArrayList();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equals(selectedStudent.getId())) {
                studentEnrollments.add(enrollment);
            }
        }

        gradesTable.setItems(studentEnrollments);
    }

    private void fillStudentForm(Student student) {
        studentIdField.setText(student.getId());
        studentNameField.setText(student.getName());
        studentEmailField.setText(student.getEmail());
    }

    private void clearStudentForm() {
        studentIdField.clear();
        studentNameField.clear();
        studentEmailField.clear();
        studentTable.getSelectionModel().clearSelection();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Student Management System");
        alert.setContentText("Version 1.0\nDeveloped for University Administration\n\nFeatures:\n• Student Management\n• Course Enrollment\n• Grade Management");
        alert.showAndWait();
    }
}

class Student {
    private String id;
    private String name;
    private String email;

    public Student(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public javafx.beans.property.StringProperty idProperty() {
        return new javafx.beans.property.SimpleStringProperty(id);
    }

    public javafx.beans.property.StringProperty nameProperty() {
        return new javafx.beans.property.SimpleStringProperty(name);
    }

    public javafx.beans.property.StringProperty emailProperty() {
        return new javafx.beans.property.SimpleStringProperty(email);
    }
}

class Course {
    private String code;
    private String name;

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
}

class Enrollment {
    private String studentId;
    private String studentName;
    private String courseCode;
    private String courseName;
    private String grade;

    public Enrollment(String studentId, String studentName, String courseCode, String courseName, String grade) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.grade = grade;
    }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public String getGrade() { return grade; }

    public void setGrade(String grade) { this.grade = grade; }

    public javafx.beans.property.StringProperty studentIdProperty() {
        return new javafx.beans.property.SimpleStringProperty(studentId);
    }

    public javafx.beans.property.StringProperty studentNameProperty() {
        return new javafx.beans.property.SimpleStringProperty(studentName);
    }

    public javafx.beans.property.StringProperty courseCodeProperty() {
        return new javafx.beans.property.SimpleStringProperty(courseCode);
    }

    public javafx.beans.property.StringProperty courseNameProperty() {
        return new javafx.beans.property.SimpleStringProperty(courseName);
    }

    public javafx.beans.property.StringProperty gradeProperty() {
        return new javafx.beans.property.SimpleStringProperty(grade);
    }
}