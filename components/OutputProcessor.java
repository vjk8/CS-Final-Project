package components;

import java.io.*;
import java.util.*;
import java.util.HashMap;

public class OutputProcessor {
    private HashMap<Integer, Athlete> athletes;
    private ArrayList<DraggableLine> finishTimes;

    public OutputProcessor(ArrayList<DraggableLine> f) {
        this.finishTimes = f;
        this.athletes = new HashMap<Integer, Athlete>();
    }

    public OutputProcessor(ArrayList<DraggableLine> f, HashMap<Integer, Athlete> m) {
        this.finishTimes = f;
        this.athletes = m;
    }

    public void addAthlete(Integer hipNumber, Athlete a) {
        athletes.put(hipNumber, a);
    }

    public void addAthlete(Integer hipNumber) {
        addAthlete(hipNumber, new Athlete());
    }

    public void addAthlete(Integer hipNumber, String name) {
        addAthlete(hipNumber, new Athlete(name));
    }

    public void addAthlete(Integer hipNumber, String name, String school) {
        addAthlete(hipNumber, new Athlete(name, school));
    }

    public void addAthlete(Integer hipNumber, String name, String school, int grade) {
        addAthlete(hipNumber, new Athlete(name, school, grade));
    }

    public void addAthlete(Integer hipNumber, String name, String school, int grade, TimeFormat seed) {
        addAthlete(hipNumber, new Athlete(name, school, grade, seed));
    }

    public void addAthlete(Integer hipNumber, String name, String school, int grade, TimeFormat seed, TimeFormat PR) {
        addAthlete(hipNumber, new Athlete(name, school, grade, seed, PR));
    }

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
