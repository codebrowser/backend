package rage.codebrowser.codeanalyzer.domain;

import java.util.HashSet;

public class ConceptCollection extends HashSet<Concept> {

    
    @Override
    public boolean add(Concept other) {
        if (contains(other.name)) {
            return false;
        } else {
            return super.add(other);
        }
    }
    
    public boolean contains(String name) {
        for (Concept c : this) {
            if (c.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public Concept getByName(String name) {
        for (Concept c : this) {
            if (c.name.equals(name)) {
                return c;
            }
        }
        throw new IllegalStateException("Can't find Concept with name " + name);
    }

    public void combine(ConceptCollection from) {
        for (Concept entry : from) {
            String name = entry.name;
            if (this.contains(name)) {
                Concept current = this.getByName(name);
                current.size += entry.size;
            } else {
                this.add(entry);
            }
        }
    }
}
