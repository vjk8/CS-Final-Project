package components;
public class Athlete {
    private String name;
    private String school;
    private int grade;
    private TimeFormat PR;
    private TimeFormat seed;

    public Athlete()
    {
        // TODO complete no-args constructor
    }

    // TODO add extra constructors with logical subsets of field arguments
    // TODO e.g. a constructor with just name, school, and grade, no PR, no
    // seed, etc.
    public Athlete(String n, String s, int g)
    {
        name = n;
        school = s;
        grade = g;
        PR = new TimeFormat();
        seed = new TimeFormat();
    }

    public Athlete(String n, String s, int g, TimeFormat personalRecord, TimeFormat rank)
    {
        name = n;
        school = s;
        grade = g;
        PR = personalRecord;
        seed = rank;
    }

    public int hashCode()
    {
        // TODO be creative with this one
        return 0;
    }

    public String getName()
    {
        // TODO complete getter
        return name;
    }

    public String getSchool()
    {
        // TODO complete getter
        return school;
    }

    public int getGrade()
    {
        // TODO complete getter
        return grade;
    }

    public TimeFormat getPR()
    {
        // TODO complete getter
        return PR;
    }

    public TimeFormat getSeed()
    {
        // TODO complete getter
        return seed;
    }
}
