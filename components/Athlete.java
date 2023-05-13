package components;
public class Athlete {
    private String name;
    private String school;
    private int grade;
    private TimeFormat PR;
    private TimeFormat seed;

    public Athlete() {
        name = "*UNKNOWN*";
        school = "*UNKNOWN*";
        grade = 0;
        PR = new TimeFormat();
        seed = new TimeFormat();
    }

    public Athlete(String name) {
        this.name = name;
        school = "*UNKNOWN*";
        grade = 0;
        PR = new TimeFormat();
        seed = new TimeFormat();
    }

    public Athlete(String name, String school) {
        this.name = name;
        this.school = school;
        grade = 0;
        PR = new TimeFormat();
        seed = new TimeFormat();
    }

    public Athlete(String name, String school, int grade) {
        this.name = name;
        this.school = school;
        this.grade = grade;
        PR = new TimeFormat();
        seed = new TimeFormat();
    }

    public Athlete(String name, String school, int grade, TimeFormat seed) {
        this.name = name;
        this.school = school;
        this.grade = grade;
        PR = new TimeFormat();
        this.seed = seed;
    }

    public Athlete(String name, String school, int grade, TimeFormat seed, TimeFormat PR) {
        this.name = name;
        this.school = school;
        this.grade = grade;
        this.PR = PR;
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    public String getSchool() {
        return school;
    }

    public int getGrade() {
        return grade;
    }

    public TimeFormat getPR() {
        return PR;
    }

    public TimeFormat getSeed() {
        return seed;
    }

    public boolean isPR(TimeFormat performance) {
        return (performance.compareTo(PR) < 0);
    }

    public int hashCode() {
        return name.hashCode() + school.hashCode();
    }
}
