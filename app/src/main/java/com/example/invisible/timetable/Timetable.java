package com.example.invisible.timetable;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Класс Timetable представляет из себя таблицу состоящую из 6 дней недели, каждый день которой
 * содержит информацию о 12 уроках в этот день, каждый может содержать информацию о студентах этого урока
 */

public class Timetable {

    // main table
    private ArrayList<Day> table;
    // statistics and priority students
    private HashMap<String, Integer> specialityCount;
    private HashMap<String, Integer> studentAllPriority;
    private HashMap<String, HashMap<String, Integer>> studentDayPriority;

    // Создать пустую таблицу
    public Timetable() {
        try {
            this.createStructure();
        } catch (Exception e) {
            Log.d("debug", "Timetable:Timetable() error while creating empty table structure");
        }
    }

    // Создать таблицу на основе данных о студентах из БД
    public Timetable(ArrayList<Student> students) {
        this.createStructure();

        try {
            if (!students.isEmpty())
                for (Student currStudent : students) {
                    addToTable(currStudent);
                }
        } catch (Exception e) {
            Log.d("debug", "Timetable:Timetable(ArrayList<>) error while add students to table");
        }
    }

    // Создает скелет таблицы
    private void createStructure() {
        specialityCount = new HashMap<>();
        studentAllPriority = new HashMap<>();
        studentDayPriority = new HashMap<>();

        // создать структуру таблицы, в процессе работы структура статична, 6 дней недели
        // класс Day сам создает свою структуру
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
            return this.table.get(columnId).getLesson(rowId).getStudents().get(0).name;
        else
            return "";
    }

    // Получить объект класса Day конкретного дня недели из таблицы
    public Day getDay(String day) {
        int retDay = 0;

        switch (day) {
            case DBHelper.KEY_MON:
                retDay = 0;
                break;
            case DBHelper.KEY_TUE:
                retDay = 1;
                break;
            case DBHelper.KEY_WED:
                retDay = 2;
                break;
            case DBHelper.KEY_THU:
                retDay = 3;
                break;
            case DBHelper.KEY_FRI:
                retDay = 4;
                break;
            case DBHelper.KEY_SAT:
                retDay = 5;
                break;
        }
        return this.table.get(retDay);
    }

    @Deprecated
    private String dayToStr(int day) {
        String retDay = "";

        switch (day) {
            case 0:
                retDay = DBHelper.KEY_MON;
                break;
            case 1:
                retDay = DBHelper.KEY_TUE;
                break;
            case 2:
                retDay = DBHelper.KEY_WED;
                break;
            case 3:
                retDay = DBHelper.KEY_THU;
                break;
            case 4:
                retDay = DBHelper.KEY_FRI;
                break;
            case 5:
                retDay = DBHelper.KEY_SAT;
                break;

        }

        return retDay;
    }

    // получить список студентов выбранного дня
    public HashSet<Student> getStudentsFromDay(final String day) {

        HashSet<Student> retStudents = new HashSet<>();

        for (int lessonId = Lesson.LESSON_1_1; lessonId <= Lesson.LESSON_6_2; lessonId++) {
            retStudents.addAll(this.getDay(day).getLesson(lessonId).getStudents());
        }
        return retStudents;
    }

    // Добавить студента в таблицу со сбором статистики
    public void addToTable(Student student) {
        /*
        * Метод записывает всё расписание студента за каждый день в таблицу this.table
        * Также записывает статистику свободных часов каждого студента по дням и за неделю в целом
        * в коллекции studentDayPriority и studentAllPriority соответственно.
         */
        int prDay = 0; // свободных часов в день
        int prAll = 0; // свободных часов в неделю

        final String days[] = {DBHelper.KEY_MON, DBHelper.KEY_TUE, DBHelper.KEY_WED, DBHelper.KEY_THU, DBHelper.KEY_FRI, DBHelper.KEY_SAT};

        for (String day : days) {
            String daySchedule = student.getDaySchedule(day); // получить расписание одного дня
            char[] chDaySchedule = daySchedule.toCharArray(); // в массив бит

            for (int lesson = Lesson.LESSON_1_1; lesson <= Lesson.LESSON_6_2; lesson++) { // смотреть все уроки за день
                if (chDaySchedule[lesson] == '0') { // если ученик в это время свободен
                    addToTableToLesson(student, day, lesson); // то добавить его в общую таблицу

                    // подсчитать приоритет для последущей растановки в результирующей таблице
                    prDay++; // статистика свободных уроков ученика для одного дня (для приоритетов)
                }
            }

            HashMap<String, Integer> dayStat = new HashMap<>();
            // добавить статистику для нового дня чтобы не удалив предыдущие дни
            if (studentDayPriority.get(student.name) != null) // проверка первой записи в мэп
                dayStat = studentDayPriority.get(student.name);

            dayStat.put(day, prDay);
            studentDayPriority.put(student.name, dayStat);

            prAll += prDay; // приоритеты текущего дня прибавить к приоритетам за всю неделю
            prDay = 0; // обнулить для следущего дня
        }
        studentAllPriority.put(student.name, prAll);
    }

    // добавить студента в таблицу на урок lesson дня недели day
    public void addToTableToLesson(Student student, String day, int lesson) {

        this.getDay(day).getLesson(lesson).addStudent(student);
        // speciality statistics
        int currSpecCount = 0;
        if (this.specialityCount.get(student.name) != null)
            currSpecCount = this.specialityCount.get(student.name);
        currSpecCount++;
        this.specialityCount.put(student.name, currSpecCount);
    }

    public Timetable makeFinalTimetable() {
        /*
        * Главный алгоритм программы, расстановка учеников в окончательный список
        * Отсеять студентов у которых уже достаточное количество спец, или уже выставлена спец. в этот день
        * (А именно создать новый список, дабы не менять входящие данные)
        * Найти в ячейке ученика, у которого самый высокий приоритет (наименьшее число в studentAllPriority)
        * Если общие приоритеты одинаковы, то сравнить приоритеты по текущему дню
        * Затем внести студента в новый список, в ячейке нового списка теперь будет находиться только один студент
        */
        Timetable retTable = new Timetable();

        // Сначала следует цикл уроков, а внутри него дней. Это необходимо для равномерного заполнения рабочей недели
        for (int lesson = Lesson.LESSON_1_1; lesson <= Lesson.LESSON_6_2; lesson++) {
            for (Day cDay : table) {
                // получить весь список претендентов на текущий урок
                ArrayList<Student> allStudPretends = cDay.getLesson(lesson).getStudents();
                if (allStudPretends.isEmpty()) // TODO: тут можно добавить отображение окон - уроки, на которые ни один студент не попадает
                    continue; // никто не может попасть на этот урок, следущий день

                /*
                * В этом блоке отсеиваются студенты у которых в этот день уже есть специальность,
                * и у которых достигнут лимит возможного количества специальностей.
                * в clrStudList остануться студенты прошедшие отсев.
                 */
                ArrayList<Student> clrStudList = new ArrayList<>();
                for (Student cStud : allStudPretends) {
                    if (retTable.specialityCount.get(cStud.name) == null)
                        retTable.specialityCount.put(cStud.name, 0);
                    if (retTable.specialityCount.get(cStud.name) < cStud.specialityHours      // кол-во спец. меньше положеного
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
                Student maxPriorityStudent = clrStudList.get(0); // Назначить первого высокоприоритетным, для дальнейших сравнений с другими
                for (Student currStudent : clrStudList) {
                    int curPriorityInAll = studentAllPriority.get(currStudent.name);
                    int maxPriorityInAll = studentAllPriority.get(maxPriorityStudent.name);
                    if (curPriorityInAll < maxPriorityInAll) // у кого меньше число - тот приоритетнее
                        maxPriorityStudent = currStudent;
                    else if (curPriorityInAll == maxPriorityInAll) { // если общие приоритеты равны, то сравнивать по текущему дню
                        int curPriorityInDay = studentDayPriority.get(currStudent.name).get(cDay.getName());
                        int maxPriorityInDay = studentDayPriority.get(maxPriorityStudent.name).get(cDay.getName());
                        if (curPriorityInDay < maxPriorityInDay) {
                            maxPriorityStudent = currStudent;
                        }
                    }
                }
                // Если алгоритм дожил до этого места, значит есть студент прошедший все проверки
                // Добавить его в результирующую таблицу.
                retTable.addToTableToLesson(maxPriorityStudent, cDay.getName(), lesson);
            }
        }
        return retTable;
    }
}
