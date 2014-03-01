package rage.codebrowser.codeanalyzer.domain;

public class Concept {

    public String name;
    public double size;

    public Concept(String name, double size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Concept)) {
            return false;
        }

        Concept otherConcept = (Concept) other;

        return this.name.equals(otherConcept.name);
    }

}
