package components;

/**
 * The Athlete class stores relevant information about athletes who finish the
 * races, enabling quick results summarization with names, grades, previous PRs,
 * and other relevant imformation
 */
public class Athlete {
    private String name;
    private String school;
    private int grade;
    private TimeFormat PR;
    private TimeFormat seed;

    /**
     * No-args constructor for an Athlete Constructs a new Athlete with blank
     * information
     */
    public Athlete() {
        name = "*UNKNOWN*";
        school = "*UNAFFILIATED*";
        grade = 0;
        PR = new TimeFormat();
        seed = new TimeFormat();
    }

    /**
     * Constructs a new Athlete with fields other than name left blank
     *
     * @param name
     *            the athlete's name
     */
    public Athlete(String name) {
        this.name = name;
        school = "*UNAFFILIATED*";
        grade = 0;
        PR = new TimeFormat();
        seed = new TimeFormat();
    }

    /**
     * Constructs a new Athlete with fields other than name and school left
     * blank
     *
     * @param name
     *            the athlete's name
     * @param school
     *            the athlete's school or affiliation (can be "unattached")
     */
    public Athlete(String name, String school) {
        this.name = name;
        this.school = school;
        grade = 0;
        PR = new TimeFormat();
        seed = new TimeFormat();
    }

    /**
     * Constructs a new Athlete with seed time and PR left blank
     *
     * @param name
     *            the athlete's name
     * @param school
     *            the athlete's school or affiliation (can be "unattached")
     * @param grade
     *            the athlete's grade or year in school
     */
    public Athlete(String name, String school, int grade) {
        this.name = name;
        this.school = school;
        this.grade = grade;
        PR = new TimeFormat();
        seed = new TimeFormat();
    }

    /**
     * Constructs a new Athlete with PR left blank
     *
     * @param name
     *            the athlete's name
     * @param school
     *            the athlete's school or affiliation (can be "unattached")
     * @param grade
     *            the athlete's grade or year in school
     * @param seed
     *            the athlete's seed time
     */
    public Athlete(String name, String school, int grade, TimeFormat seed) {
        this.name = name;
        this.school = school;
        this.grade = grade;
        PR = new TimeFormat();
        this.seed = seed;
    }

    /**
     * Constructs a new Athlete with all fields filled out
     *
     * @param name
     *            the athlete's name
     * @param school
     *            the athlete's school or affiliation (can be "unattached")
     * @param grade
     *            the athlete's grade or year in school
     * @param seed
     *            the athlete's seed time
     * @param PR
     *            the athlete's previous personal best (can be the same as the
     *            seed time)
     */
    public Athlete(String name, String school, int grade, TimeFormat seed, TimeFormat PR) {
        this.name = name;
        this.school = school;
        this.grade = grade;
        this.PR = PR;
        this.seed = seed;
    }

    /**
     * Getter method for the name
     *
     * @return the athlete's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for the school or affiliation
     *
     * @return the athlete's school or affiliation
     */
    public String getSchool() {
        return school;
    }

    /**
     * Getter method for the grade or year
     *
     * @return the athlete's grade or year
     */
    public int getGrade() {
        return grade;
    }

    /**
     * Getter method for the personal best
     *
     * @return the athlete's personal best
     */
    public TimeFormat getPR() {
        return PR;
    }

    /**
     * Getter method for the seed time
     *
     * @return the athlete's seed time
     */
    public TimeFormat getSeed() {
        return seed;
    }

    /**
     * Checks if the current performance is better than the athlete's PR
     *
     * @param performance
     *            a TimeFormat representing the athlete's recent performance
     * @return whether or not the performance is better than the athlete's PR
     */
    public boolean isPR(TimeFormat performance) {
        return (performance.compareTo(PR) < 0);
    }

    /**
     * Hashcode method for storage within a Hashmap
     */
    @Override
    public int hashCode()
    {
        return name.hashCode() + school.hashCode();
    }
}
