package com.dolnikova;

import com.dolnikova.dao.StudentDao;
import com.dolnikova.model.Group;
import com.dolnikova.model.Student;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Group java = createJavaGroup();
        Group cpp = createCppGroup();
        Group js = createJsGroup();
        createJavaAndJsGroup(java, js);

        System.out.println(java);
        System.out.println(cpp);
        System.out.println(js);
        System.out.println();

        Student student = new Student();
        student.setId(Helper.generateId());
        student.setName("Krylov Ivan");
        student.setAge(26);
        student.getGroupList().add(cpp);
        student.getGroupList().add(java);
        student.getGroupList().add(js);

        Student student2 = new Student();
        student2.setId(Helper.generateId());
        student2.setName("Dmytro");
        student2.setAge(56);
        student2.getGroupList().add(cpp);

        try {
            StudentDao dao = new StudentDao();
            dao.clean();
            System.out.println();

            dao.insetGroup(java);
            dao.insetGroup(js);
            dao.insetGroup(cpp);

            dao.insertStudent(student);
            dao.insertStudent(student2);

            List<Student> studentInGroupJava = dao.getStudentsByGroupName("java");
            System.out.println("Student in java : " + studentInGroupJava);

            List<Student> studentInGroupCpp = dao.getStudentsByGroupName("cpp");
            System.out.println("Student in cpp : " + studentInGroupCpp);

            List<Group> groupByStudentName = dao.getGroupsByStudentName("Krylov Ivan");
            System.out.println("Ivan`s groups : " + groupByStudentName);

            List<Group> groupByStudentName2 = dao.getGroupsByStudentName("yuli");
            System.out.println("Yuli`s groups : " + groupByStudentName2);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static Group createJavaGroup() {
        Group java = new Group();

        java.setName("Java");
        java.setStartDate("2019-02-22");
        java.setId(Helper.generateId());

        Student javaSt1 = new Student();
        javaSt1.setName("Alex");
        javaSt1.setAge(23);
        javaSt1.setId(Helper.generateId());
        java.getStudentList().add(javaSt1);

        Student javaSt2 = new Student();
        javaSt2.setName("Yuli");
        javaSt2.setAge(17);
        javaSt2.setId(Helper.generateId());
        java.getStudentList().add(javaSt2);

        Student javaSt3 = new Student();
        javaSt3.setName("Dima");
        javaSt3.setAge(22);
        javaSt3.setId(Helper.generateId());
        java.getStudentList().add(javaSt3);

        Student javaSt4 = new Student();
        javaSt4.setName("Dasha");
        javaSt4.setAge(17);
        javaSt4.setId(Helper.generateId());
        java.getStudentList().add(javaSt4);

        return java;
    }

    private static Group createJsGroup() {

        Group js = new Group();
        js.setName("JavaScript");
        js.setStartDate("2019-03-03");
        js.setId(Helper.generateId());

        Student jsSt1 = new Student();
        jsSt1.setName("Sergey");
        jsSt1.setAge(50);
        jsSt1.setId(Helper.generateId());
        js.getStudentList().add(jsSt1);

        Student jsSt2 = new Student();
        jsSt2.setName("Vika");
        jsSt2.setAge(10);
        jsSt2.setId(Helper.generateId());
        js.getStudentList().add(jsSt2);

        return js;
    }

    private static Group createCppGroup() {
        Group cpp = new Group();
        cpp.setName("Cpp");
        cpp.setStartDate("2018-09-01");
        cpp.setId(Helper.generateId());

        Student cppSt1 = new Student();
        cppSt1.setName("Masha");
        cppSt1.setAge(21);
        cppSt1.setId(Helper.generateId());
        cpp.getStudentList().add(cppSt1);

        Student cppSt2 = new Student();
        cppSt2.setName("Ben");
        cppSt2.setAge(34);
        cppSt2.setId(Helper.generateId());
        cpp.getStudentList().add(cppSt2);

        Student cppSt3 = new Student();
        cppSt3.setName("Lena");
        cppSt3.setAge(19);
        cppSt3.setId(Helper.generateId());
        cpp.getStudentList().add(cppSt3);

        return cpp;
    }

    private static void createJavaAndJsGroup(Group java, Group js) {
        Student commonSt1 = new Student();
        commonSt1.setName("Irina");
        commonSt1.setAge(35);
        commonSt1.setId(Helper.generateId());

        js.getStudentList().add(commonSt1);
        java.getStudentList().add(commonSt1);

        Student commonSt2 = new Student();
        commonSt2.setName("Sofi");
        commonSt2.setAge(15);
        commonSt2.setId(Helper.generateId());

        js.getStudentList().add(commonSt2);
        java.getStudentList().add(commonSt2);

        Student commonSt3 = new Student();
        commonSt3.setName("Max");
        commonSt3.setAge(45);
        commonSt3.setId(Helper.generateId());

        js.getStudentList().add(commonSt3);
        java.getStudentList().add(commonSt3);

    }

}
