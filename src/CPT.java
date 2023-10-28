package src;

import java.util.*;

public class CPT implements Comparable<CPT>{
    List<MyNode> nodes;
    HashMap<List<String>, Double> rows;

    public CPT(){
        this.nodes = new LinkedList<>();
        this.rows = new HashMap<>();
    }

    public CPT(List<MyNode> nodes, String probability){
        this.nodes = nodes;
        this.rows = new HashMap<>();
        add(nodes, probability);
    }

    public CPT(CPT other){
        this.nodes = new ArrayList<>(other.nodes);
        HashMap<List<String>, Double> o_rows = new HashMap<>();
        for (Map.Entry<List<String>, Double> entry:other.rows.entrySet()){
            List<String> lst = new ArrayList<>(entry.getKey());
            o_rows.put(lst, entry.getValue());
        }
        this.rows = o_rows;
    }

    public int size(){
        return rows.size();
    }

    public void add(List<MyNode> nodes, String probability){
        ArrayList<List<String>> arr = new ArrayList<>();
        int options = OptionsNumF(nodes, 0), k=0;
        while (k < options){
            List<String> lst = new ArrayList<>();
            arr.add(lst);
            k++;
        }
        int sv = nodes.size();
        for (int j=0; j<sv; j++){
            k = 0;
            MyNode node = nodes.get(j);
            int so = node.getOutcome().size();
            int next = OptionsNumF(nodes, j+1), previous = OptionsNumB(nodes, j);
            for (int i=0; i<previous; i++){
                for(int l=0; l<so; l++){
                    for (int m=0; m<next; m++){
                        String str = node.getName() + "=" + node.getOutcome().get(l);
                        arr.get(k++).add(str);
                    }
                }
            }

        }
        int z = 0;
        String[] prob_lst = probability.split("\\s+");
        for (List<String> lst:arr)
            this.rows.put(lst, Double.valueOf(prob_lst[z++]));
    }

    private int OptionsNumF(List<MyNode> nodes, int index) {
        int i = 1;
        while (index < nodes.size())
            i *= nodes.get(index++).getOutcome().size();
        return i;
    }

    private int OptionsNumB(List<MyNode> nodes, int index) {
        int i = 1, j = 0;
        while (j < index)
            i *= nodes.get(j++).getOutcome().size();
        return i;
    }

    private int sum_ascii(List<MyNode> nodeLst){
        String str = "";
        int sum = 0;
        for (MyNode n:nodeLst)
            str += n.getName();
        for(char ch: str.toCharArray())
            sum += ch;
        return sum;
    }

    @Override
    public int compareTo(CPT o){
        return Comparator.comparing((CPT c)->c.size()).thenComparing(c->c.sum_ascii(c.nodes)).compare(this, o);
    }

    public String toString(){
        String str = "Variables: ";
        for (MyNode n:this.nodes)
            str += n.getName() + " ";
        str += "\n";
        for (Map.Entry<List<String>, Double> entry:this.rows.entrySet())
            str += Arrays.toString(entry.getKey().toArray()) + " " + entry.getValue() + "\n";
        return str;
    }
}
