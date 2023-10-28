import java.util.LinkedList;

public class MyNode {
    private String name;
    private LinkedList<String> parents;
    private LinkedList<String> children;
    private LinkedList<String> outcome;

    public MyNode(String name, LinkedList<String> parents, LinkedList<String> children, LinkedList<String> outcome){
        this.name = name;
        this.parents = parents;
        this.children = children;
        this.outcome = outcome;
    }

    public MyNode(String name){
        this.name = name;
        this.parents = new LinkedList<>();
        this.children = new LinkedList<>();
        this.outcome = new LinkedList<>();
    }

    public MyNode(MyNode other){
        this.name = other.name;
        this.parents = new LinkedList<>();
        this.children = new LinkedList<>();
        this.outcome = new LinkedList<>();
        this.parents.addAll(other.parents);
        this.children.addAll(other.children);
        this.outcome.addAll(other.outcome);
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public LinkedList<String> getParents(){
        return this.parents;
    }

    public void setParents(LinkedList<String> parents){
        this.parents = parents;
    }

    public LinkedList<String> getChildren(){
        return this.children;
    }

    public void setChildren(LinkedList<String> children){
        this.children = children;
    }

    public LinkedList<String> getOutcome(){
        return this.outcome;
    }

    public void setOutcome(LinkedList<String> outcome){
        this.outcome = outcome;
    }

    public void addChild(String child){
        if(!this.children.contains(child))
            this.children.add(child);
    }

    public void addOutcome(String outcome){
        this.outcome.add(outcome);
    }
}
