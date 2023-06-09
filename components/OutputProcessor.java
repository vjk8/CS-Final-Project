package components;

import java.io.*;
import java.util.*;
import java.util.HashMap;

/**
 * OutputProcessor handles the summarization of results in various formats
 */
public class OutputProcessor {
    private HashMap<Integer, Athlete> athletes;
    private ArrayList<DraggableLine> finishTimes;

    /**
     * constructs a new OutputProcessor with finish times ascertained from the
     * PostTimingGUI
     *
     * @param f
     *            the ArrayList<DraggableLine> representing finishes
     */
    public OutputProcessor(ArrayList<DraggableLine> f) {
        this.finishTimes = f;
        this.athletes = new HashMap<Integer, Athlete>();
    }

    /**
     * constructs a new OutputProcessor with finish times ascertained from the
     * PostTimingGUI and a HashMap of hip number and athletes provided. This is
     * the recommended constructor
     *
     * @param f
     *            the ArrayList<DraggableLine> representing finishes
     * @param m
     *            a HashMap<Integer, Athlete> representing the association of a
     *            hip number to an athlete's information
     */
    public OutputProcessor(ArrayList<DraggableLine> f, HashMap<Integer, Athlete> m) {
        this.finishTimes = f;
        this.athletes = m;
    }

    /**
     * getter for the HashMap of hip numbers to athletes
     *
     * @return the hashmap of integers to athletes
     */
    public HashMap<Integer, Athlete> getHashMap() {
        return athletes;
    }

    /**
     * adds an Athlete to the HashMap of hip numbers to athletes
     *
     * @param hipNumber
     *            the hip number of the athlete
     * @param a
     *            the Athlete correspondig to the hip number
     */
    public void addAthlete(Integer hipNumber, Athlete a) {
        athletes.put(hipNumber, a);
    }

    /**
     * adds an Athlete to the HashMap where only the hip number is known
     *
     * @param hipNumber
     *            the known hip number for the Athlete
     */
    public void addAthlete(Integer hipNumber) {
        addAthlete(hipNumber, new Athlete());
    }

    /**
     * adds an Athlete to the HashMap where only the hip number and athlete name
     * are known
     *
     * @param hipNumber
     *            the known hip number for the Athlete
     * @param name
     *            the name of the athlete
     */
    public void addAthlete(Integer hipNumber, String name) {
        addAthlete(hipNumber, new Athlete(name));
    }

    /**
     * adds an Athlete to the HashMap where the hip number, athlete name, and
     * athlete school/affiliation are known
     *
     * @param hipNumber
     *            the known hip number for the Athlete
     * @param name
     *            the name of the athlete
     * @param school
     *            the athlete's school or affiliation
     */
    public void addAthlete(Integer hipNumber, String name, String school) {
        addAthlete(hipNumber, new Athlete(name, school));
    }

    /**
     * adds an Athlete to the HashMap where the hip number, athlete name,
     * athlete school/affiliation, and athlete grade/year are known
     *
     * @param hipNumber
     *            the known hip number for the Athlete
     * @param name
     *            the name of the athlete
     * @param school
     *            the athlete's school or affiliation
     * @param grade
     *            the athlete's grade or year
     */
    public void addAthlete(Integer hipNumber, String name, String school, int grade) {
        addAthlete(hipNumber, new Athlete(name, school, grade));
    }

    /**
     * adds an Athlete to the HashMap where the hip number, athlete name,
     * athlete school/affiliation, athlete grade/year, and seed time are known
     *
     * @param hipNumber
     *            the known hip number for the Athlete
     * @param name
     *            the name of the athlete
     * @param school
     *            the athlete's school or affiliation
     * @param grade
     *            the athlete's grade or year
     * @param seed
     *            the athlete's seed time for the race
     */
    public void addAthlete(Integer hipNumber, String name, String school, int grade, TimeFormat seed) {
        addAthlete(hipNumber, new Athlete(name, school, grade, seed));
    }

    /**
     * adds an Athlete to the HashMap where the hip number, athlete name,
     * athlete school/affiliation, athlete grade/year, seed time, and personal
     * record are known
     *
     * @param hipNumber
     *            the known hip number for the Athlete
     * @param name
     *            the name of the athlete
     * @param school
     *            the athlete's school or affiliation
     * @param grade
     *            the athlete's grade or year
     * @param seed
     *            the athlete's seed time for the race
     * @param PR
     *            the athlete's personal record
     */
    public void addAthlete(Integer hipNumber, String name, String school, int grade, TimeFormat seed, TimeFormat PR) {
        addAthlete(hipNumber, new Athlete(name, school, grade, seed, PR));
    }

    /**
     * exports the results in the format of a CSV file (can be opened in a
     * spreadsheet editor)
     *
     * @param filepath
     *            the filepath to save the .csv file to
     * @throws IOException
     *             Throws IOException if the filepath is invalid
     */
    public void exportCSV(String filepath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write("Place,Hip,Name,School,Grade,Time,Seed,\n");
        for (int i = 0; i < finishTimes.size(); i++) {
            DraggableLine d = finishTimes.get(i);
            int number = d.getHipNumber();
            Athlete athlete = athletes.get(number);
            String performance = d.getTimestamp().toString();
            if (athlete.isPR(d.getTimestamp())) {
                performance += " (PR)";
            }
            writer.write(String.format("%d,%d,%s,%s,%d,%s,%s\n", i + 1, d.getHipNumber(), athlete.getName(),
                                       athlete.getSchool(), athlete.getGrade(), performance, athlete.getSeed()));
        }
        writer.close();
    }

    private String leftPad(String s, int n) {
        return String.format("%1$" + n + "s", s).substring(0, n);
    }

    /**
     * exports the results to a plaintext (.txt) file
     *
     * @param filepath
     *            the filepath of the .txt file to be saved in
     * @throws IOException
     *             Throws IOException if the filepath is invalid
     */
    public void exportText(String filepath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write(leftPad("Place", 5) + leftPad("Hip", 5) + leftPad("Name", 20) + leftPad("School", 20) +
                     leftPad("Grade", 10) + leftPad("Time", 15) + leftPad("Seed", 10) + "\n");
        for (int i = 0; i < finishTimes.size(); i++) {
            DraggableLine d = finishTimes.get(i);
            int number = d.getHipNumber();
            Athlete athlete = athletes.get(number);
            String performance = d.getTimestamp().toString();
            if (athlete.isPR(d.getTimestamp())) {
                performance += " (PR)";
            }
            writer.write(leftPad(((Integer)(i + 1)).toString(), 5) +
                         leftPad(((Integer)d.getHipNumber()).toString(), 5) + leftPad(athlete.getName(), 20) +
                         leftPad(athlete.getSchool(), 20) + leftPad(Integer.toString(athlete.getGrade()), 10) +
                         leftPad(performance, 15) + leftPad(athlete.getSeed().toString(), 10) + "\n");
        }
        writer.close();
    }

    /**
     * prints results to the console
     */
    public void printResults() {
        System.out.print(leftPad("Place", 5) + leftPad("Hip", 5) + leftPad("Name", 20) + leftPad("School", 20) +
                         leftPad("Grade", 10) + leftPad("Time", 15) + leftPad("Seed", 10) + "\n");
        for (int i = 0; i < finishTimes.size(); i++) {
            DraggableLine d = finishTimes.get(i);
            int number = d.getHipNumber();
            Athlete athlete = athletes.get(number);
            String performance = d.getTimestamp().toString();
            if (athlete.isPR(d.getTimestamp())) {
                performance += " (PR)";
            }
            System.out.print(leftPad(((Integer)(i + 1)).toString(), 5) +
                             leftPad(((Integer)d.getHipNumber()).toString(), 5) + leftPad(athlete.getName(), 20) +
                             leftPad(athlete.getSchool(), 20) + leftPad(Integer.toString(athlete.getGrade()), 10) +
                             leftPad(performance, 15) + leftPad(athlete.getSeed().toString(), 10) + "\n");
        }
    }
}
