package src;

import java.util.*;

public class BayesianNetwork {
    HashMap<String, MyNode> BayesNet;
    private List<CPT> cpt;

    public BayesianNetwork(){
        this.BayesNet = new HashMap<>();
        this.cpt = new LinkedList<>();
    }

    public void addCPT(CPT c){
        this.cpt.add(c);
    }

    public List<CPT> copyCPT(){
        List<CPT> copy = new LinkedList<>();
        for (CPT f:this.cpt)
            copy.add(new CPT(f));
        return copy;
    }

    public void addNode(MyNode n){
        if (!this.BayesNet.containsKey(n.getName()))
            this.BayesNet.put(n.getName(), n);
    }

    public void updateParents(MyNode n, LinkedList<String> lst){
        n.setParents(lst);
        for(String str:lst){
            MyNode node = this.BayesNet.get(str);
            node.addChild(n.getName());
        }
    }

    public boolean isAncestor(MyNode hidden, List<String> parents){
        if(parents.size() == 0)
            return false;
        if(parents.contains(hidden.getName()))
            return true;
        boolean f = false;
        System.out.println(BayesNet.keySet());
        for(String str:parents) {
            System.out.println(str);
            List<String> new_parents = BayesNet.get(str).getParents();
            f = f || isAncestor(hidden, new_parents);
        }
            return f;
    }
}
