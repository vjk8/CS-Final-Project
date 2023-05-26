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
        writer.write("Name,School,Grade,Time,Seed Time,PR Time\n");
        for (DraggableLine d : finishTimes) {
            int number = d.getHipNumber();
            Athlete athlete = athletes.get(number);
            writer.write(String.format("%s,%s,%d,%s,%s,%s\n", athlete.getName(), athlete.getSchool(),
                                       athlete.getGrade(), d.getTimestamp(), athlete.getSeed(), athlete.getPR()));
        }
        writer.close();
    }

    private String leftPad(String s, int n) {
        return String.format("%1$" + n + "s", s).substring(0, n);
    }

    /**
     * exports the results to a plaintext (.txt) file
     * @param filepath the filepath of the .txt file to be saved in
     * @throws IOException Throws IOException if the filepath is invalid
     */
    public void exportText(String filepath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write(leftPad("Name", 20) + leftPad("School", 20) + leftPad("Grade", 5) + leftPad("Time", 10) +
                     leftPad("Seed Time", 10) + leftPad("PR Time", 10) + "\n");
        for (DraggableLine d : finishTimes) {
            int number = d.getHipNumber();
            Athlete athlete = athletes.get(number);
            writer.write(leftPad(athlete.getName(), 20) + leftPad(athlete.getSchool(), 20) +
                         leftPad(Integer.toString(athlete.getGrade()), 5) + leftPad(d.getTimestamp().toString(), 10) +
                         leftPad(athlete.getSeed().toString(), 10) + leftPad(athlete.getPR().toString(), 10) + "\n");
        }
        writer.close();
    }

    /**
     * exports results as an HTML table in a .html file
     * @param filepath the filepath of the .html file for results to be saved in
     * @throws IOException Throws IOException if the filepath is invalid
     */
    public void exportHTML(String filepath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write(
            "<html>\n<head>\n<title>Results</title>\n</head>\n<body>\n<table>\n<tr>\n<th>Name</th>\n<th>School</th>\n<th>Grade</th>\n<th>Time</th>\n<th>Seed Time</th>\n<th>PR Time</th>\n</tr>\n");
        for (DraggableLine d : finishTimes) {
            int number = d.getHipNumber();
            Athlete athlete = athletes.get(number);
            writer.write(String.format(
                "<tr>\n<td>%s</td>\n<td>%s</td>\n<td>%d</td>\n<td>%s</td>\n<td>%s</td>\n<td>%s</td>\n</tr>\n"));
        }
        writer.write("</table>\n</body>\n</html>");
        writer.close();
    }

    /**
     * prints results to the console
     */
    public void printResults() {
        System.out.print(leftPad("Name", 20) + leftPad("School", 20) + leftPad("Grade", 5) + leftPad("Time", 10) +
                         leftPad("Seed Time", 10) + leftPad("PR Time", 10) + "\n");
        for (DraggableLine d : finishTimes) {
            int number = d.getHipNumber();
            Athlete athlete = athletes.get(number);
            System.out.print(leftPad(athlete.getName(), 20) + leftPad(athlete.getSchool(), 20) +
                             leftPad(Integer.toString(athlete.getGrade()), 5) +
                             leftPad(d.getTimestamp().toString(), 10) + leftPad(athlete.getSeed().toString(), 10) +
                             leftPad(athlete.getPR().toString(), 10) + "\n");
        }
    }
}
