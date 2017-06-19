package com.example.invisible.timetable.tablestruct;

import android.util.Log;

import com.example.invisible.timetable.database.DBHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Класс Timetable представляет из себя таблицу состоящую из 6 дней недели, каждый день которой
 * содержит информацию о 12 уроках в этот день, каждый может содержать информацию о студентах этого урока
 */

public class Timetable implements Arranger {

    // main table
    private ArrayList<Day> table;
    private ArrayList<StudentStat> statStudents;
    private ArrayList<Student> noRoomStudents;
    private Student senseiStudent;

    // Создать пустую таблицу
    private Timetable() {
        try {
            this.createStructure();
        } catch (Exception e) {
            Log.d("debug", "Timetable:Timetable() error while creating empty table structure");
        }
    }

    // Создать таблицу на основе данных о студентах из БД
    public Timetable(ArrayList<Student> _students, Student _sensei) {
        this.createStructure();

        senseiStudent = _sensei;

        try {
            if (!_students.isEmpty())
                for (Student currStudent : _students) {
                    statStudents.add(0, new StudentStat(currStudent));
                    addStudentToTable(statStudents.get(0));
                }
        } catch (Exception e) {
            Log.d("debug", "Timetable:Timetable(ArrayList<>) error while add students to table");
        }
    }

    // Создает скелет таблицы
    private void createStructure() {

        // создать структуру таблицы, в процессе работы структура статична, 6 дней недели
        // класс Day сам создает свою структуру
        this.statStudents = new ArrayList<>();
        this.noRoomStudents = new ArrayList<>();
        this.table = new ArrayList<>();
        this.table.add(new Day(DBHelper.KEY_MON));
        this.table.add(new Day(DBHelper.KEY_TUE));
        this.table.add(new Day(DBHelper.KEY_WED));
        this.table.add(new Day(DBHelper.KEY_THU));
        this.table.add(new Day(DBHelper.KEY_FRI));
        this.table.add(new Day(DBHelper.KEY_SAT));
    }

    // получить значение из таблицы по рядку и столбцу
    public String getStringCell(final int columnId, final int rowId) {

        if (!this.table.get(columnId).getLesson(rowId).getStudents().isEmpty())
            return this.table.get(columnId).getLesson(rowId).getStudents().get(0).getName();
        else
            return "";
    }

    // Получить объект класса Day конкретного дня недели из таблицы
    private Day getDay(String day) {
        return this.table.get(Arrays.asList(DBHelper.DAYS).indexOf(day));
    }

    // получить список студентов выбранного дня
    private HashSet<Student> getStudentsFromDay(final String day) {

        HashSet<Student> retStudents = new HashSet<>();

        for (int lessonId = Lesson.FIRST; lessonId <= Lesson.LAST; lessonId++) {
            retStudents.addAll(this.getDay(day).getLesson(lessonId).getStudents());
        }
        return retStudents;
    }

    // Добавить студента в таблицу со сбором статистики
    private void addStudentToTable(StudentStat student) {
        /*
        * Метод записывает всё расписание студента за каждый день в таблицу this.table
        * Также записывает статистику свободных часов каждого студента по дням и за неделю в целом
         */
        int prDay = 0; // свободных часов в день
        int prAll = 0; // свободных часов в неделю

        for (String day : DBHelper.DAYS) {
            String daySchedule = student.getDaySchedule(day); // получить расписание одного дня
            char[] chDaySchedule = daySchedule.toCharArray(); // в массив бит

            for (int lesson = Lesson.FIRST; lesson <= Lesson.LAST; lesson++) { // смотреть все уроки за день
                if (chDaySchedule[lesson] == '0') { // если ученик в это время свободен
                    addStudentToLesson(student, day, lesson); // то добавить его в общую таблицу
                    // подсчитать приоритет для последущей растановки в результирующей таблице
                    prDay++; // статистика свободных уроков ученика для одного дня (для приоритетов)
                }
            }
            student.statDayPriority.put(day, prDay);

            prAll += prDay; // приоритеты текущего дня прибавить к приоритетам за всю неделю
            prDay = 0; // обнулить для следущего дня
        }
        student.statTotalPriority = prAll;
    }

    public ArrayList<Student> getNoRoomStudentsList() {
        return this.noRoomStudents;
    }

    // добавить студента в таблицу на урок lesson дня недели day
    private void addStudentToLesson(final StudentStat student, String day, int lesson) {
        this.getDay(day).getLesson(lesson).addStudent(student);
    }

    private void arrangeStudents() {
        /*
        * Главный алгоритм программы, расстановка учеников в окончательный список
        * Отсеять студентов у которых уже достаточное количество спец, или уже выставлена спец. в этот день
        * Найти в ячейке ученика, у которого самый высокий приоритет (наименьшее число в studentAllPriority)
        * Если общие приоритеты одинаковы, то сравнить приоритеты по текущему дню
        * Затем внести студента в новый список, в ячейке нового списка теперь будет находиться только один студент
        */

        // чистая таблица для последующего переноса из неё данных
        Timetable retTable = new Timetable();

        // Сначала следует цикл уроков, а внутри него дней. Это необходимо для равномерного заполнения рабочей недели
        for (int lesson = Lesson.FIRST; lesson <= Lesson.LAST; lesson++) {
            for (Day cDay : table) {
                // Проверить может ли преподаватель присутствовать на этом уроке
                char[] chSenseiSchedule = senseiStudent.getDaySchedule(cDay.getName()).toCharArray();
                if (chSenseiSchedule[lesson] == '1') {
                    retTable.addStudentToLesson(new StudentStat("++++++"), cDay.getName(), lesson);
                    continue;
                }
                // получить весь список претендентов на текущий урок
                ArrayList<StudentStat> allStudOnLesson = cDay.getLesson(lesson).getStudents();
                if (allStudOnLesson.isEmpty()) { // окна - пары на которые никто не попадает
                    retTable.addStudentToLesson(new StudentStat("--------"), cDay.getName(), lesson);
                    continue; // никто не может попасть на этот урок, следущий день
                }
                /*
                * В этом блоке отсеиваются студенты у которых в этот день уже есть специальность,
                * и у которых достигнут лимит возможного количества специальностей.
                * в clrStudList остануться студенты прошедшие отсев.
                 */
                ArrayList<StudentStat> clrStudList = new ArrayList<>();
                for (StudentStat cStud : allStudOnLesson) {
                    if (cStud.statSpeciality < cStud.getSpecialityHours()      // кол-во спец. меньше положеного
                            && !retTable.getStudentsFromDay(cDay.getName()).contains(cStud)) { // ещё НЕ присутствует его спец в этот день
                        clrStudList.add(cStud);
                    }
                }

                if (clrStudList.isEmpty()) // никто не соответствовал критериям выше
                    continue;              // следущий день

                /*
                * простой блок сравнения приоритетов
                * если общие приоритеты равны, то сравнить приоритеты за текущий день
                * в случае равных приоритетов, выбор студента не важен.
                 */
                StudentStat maxPrStudent = clrStudList.get(0); // Назначить первого высокоприоритетным, для дальнейших сравнений с другими
                for (StudentStat cPrStudent : clrStudList) {
                    if (cPrStudent.statTotalPriority < maxPrStudent.statTotalPriority) // у кого меньше число - тот приоритетнее
                        maxPrStudent = cPrStudent;
                    else if (cPrStudent.statTotalPriority == maxPrStudent.statTotalPriority)  // если общие приоритеты равны, то сравнивать по текущему дню
                        if (cPrStudent.statDayPriority.get(cDay.getName()) < maxPrStudent.statDayPriority.get(cDay.getName()))
                            maxPrStudent = cPrStudent;
                }
                // Если алгоритм дожил до этого места, значит есть студент прошедший все проверки
                // Добавить его в результирующую таблицу.
                retTable.addStudentToLesson(maxPrStudent, cDay.getName(), lesson);
                maxPrStudent.statSpeciality++;
            }
        }

        for (StudentStat student : statStudents) {
            if (student.statSpeciality != student.getSpecialityHours()) {
                retTable.noRoomStudents.add(student);
            }
        }

        // перенести вычисления
        this.table = retTable.table;
        this.statStudents = retTable.statStudents;
        this.noRoomStudents = retTable.noRoomStudents;
        this.senseiStudent = retTable.senseiStudent;
    }

    @Override
    public void arrangeByPriority() {
        arrangeStudents();
    }

    public class StudentStat extends Student {
        private int statSpeciality = 0;
        private int statTotalPriority = 0;
        private HashMap<String, Integer> statDayPriority = new HashMap<>();

        StudentStat(String _name) {
            super(_name);
        }

        StudentStat(Student _student) {
            super(_student);
        }
    }
}
