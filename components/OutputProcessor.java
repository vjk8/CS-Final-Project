package components;
import java.util.*;
import java.util.HashMap;
import org.opencv.core.*;

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

    // TODO add formatting helper methods and private fields as needed

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
    
    

    public void exportCSV(String filepath) {
        // TODO complete method
    }

    public void exportText(String filepath) {
        // TODO complete method
    }

    public void exportHTML(String filepath) {
        // TODO complete method
    }

    public void printResults() {
        // TODO complete method
    }
}
