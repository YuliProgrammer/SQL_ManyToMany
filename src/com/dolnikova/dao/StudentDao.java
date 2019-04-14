package com.dolnikova.dao;

import com.dolnikova.local.properties.MySettings;
import com.dolnikova.model.Group;
import com.dolnikova.model.Student;
import com.google.gson.Gson;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentDao {
    private Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public StudentDao() throws SQLException {
        Gson gson = new Gson();
        String json = readSettings();

        MySettings mySettings = gson.fromJson(json, MySettings.class);
        String user = mySettings.getUser();
        String password = mySettings.getPassword();

        connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/StudentGroupMM", user, password);

        maybeCreateGroupsTable();
        maybeCreateStudentsTable();
        maybeCreateStudentGroupTable();
    }

    private String readSettings() {
        StringBuilder sb = null;

        try {
            InputStream inputStream = new FileInputStream("settings.json");
            BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));

            String line = buf.readLine();
            sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            String json = sb.toString();
            return json;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private void maybeCreateGroupsTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS groups (\n" +
                    "_id uuid PRIMARY KEY,\n" +
                    "name varchar(100),\n" +
                    "startDate date\n" +
                    ");");
        }
    }

    private void maybeCreateStudentsTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS students (\n" +
                    "_id uuid PRIMARY KEY,\n" +
                    "name varchar(100),\n" +
                    "age int\n" +
                    ");");
        }
    }

    private void maybeCreateStudentGroupTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS student_group (\n" +
                    "group_id uuid,\n" +
                    "student_id uuid,\n" +
                    "PRIMARY KEY(group_id, student_id)\n" +
                    ");");
        }
    }


    public void clean() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate("DELETE FROM groups;");
            System.out.println("Deleted " + count + " rows from table groups");
        }

        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate("DELETE FROM students;");
            System.out.println("Deleted " + count + " rows from table students");
        }

        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate("DELETE FROM student_group;");
            System.out.println("Deleted " + count + " rows from table student_group");
        }

    }

    public void insetGroup(Group group) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("INSERT INTO groups VALUES ('%s', '%s', '%s');", group.getId(), group.getName(), group.getStartDate());
            statement.execute(request);

            List<Student> students = group.getStudentList();
            for (Student student : students) {
                addStudent(student);
                insertStudentToGroup(student, group);
            }
        }
    }

    public void insertStudent(Student student) throws SQLException {
        addStudent(student);
        for (Group group : student.getGroupList()) {
            insertStudentToGroup(student, group);
        }
    }

    private void addStudent(Student student) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            if (isStudentExists(student, statement) == false) {
                String request = String.format("INSERT INTO students VALUES ('%s', '%s', '%d');", student.getId(), student.getName(), student.getAge());
                statement.execute(request);
            }
        }
    }

    private boolean isStudentExists(Student student, Statement statement) throws SQLException {
        UUID uid = UUID.fromString(student.getId());
        String testRequest = String.format("SELECT * FROM students WHERE _id = '%s';", uid);
        ResultSet resultSet = statement.executeQuery(testRequest);

        if (resultSet.next()) {
            return true;
        }
        return false;
    }

    private void insertStudentToGroup(Student student, Group group) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("INSERT INTO student_group VALUES ('%s', '%s');", group.getId(), student.getId());
            statement.execute(request);
        }
    }


    public List<Student> getStudentsByGroupName(String groupName) {

        List<Student> students = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {

            String request = String.format(
                    "SELECT groups.name AS grname, groups._id AS gr_id, " +
                            "students._id, students.name, students.age, " +
                            "student_group.group_id, student_group.student_id " +
                            "FROM groups " +
                            "LEFT JOIN student_group " +
                            "ON groups._id = student_group.group_id " +
                            "LEFT JOIN students " +
                            "ON student_group.student_id = students._id " +
                            "WHERE LOWER(groups.name) = LOWER('%s');", groupName);

            ResultSet resultSet = statement.executeQuery(request);

            while (resultSet.next()) {

                String id = resultSet.getString("_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");

                Student student = new Student();
                student.setId(id);
                student.setName(name);
                student.setAge(age);

                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public List<Group> getGroupsByStudentName(String studentName) {

        List<Group> groups = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {

            String request = String.format(
                    "SELECT students._id, students.name, students.age, " +
                            "groups.name AS grname, groups._id AS gr_id, groups.startdate, " +
                            "student_group.group_id, student_group.student_id " +
                            "FROM students " +
                            "LEFT JOIN student_group " +
                            "ON students._id = student_group.student_id " +
                            "LEFT JOIN groups " +
                            "ON student_group.group_id = groups._id " +
                            "WHERE LOWER(students.name) = LOWER('%s');", studentName);

            ResultSet resultSet = statement.executeQuery(request);

            while (resultSet.next()) {

                String id = resultSet.getString("gr_id");
                String name = resultSet.getString("grname");
                String startDate = resultSet.getString("startdate");

                Group group = new Group();
                group.setId(id);
                group.setName(name);
                group.setStartDate(startDate);

                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

}
