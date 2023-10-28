import java.util.*;

public class VariableElimination {
    private BayesianNetwork bayesianNetwork;
    private List<CPT> cpt;
    private int sum;
    private int mult;
    private double prob;
    private String ans;

    /**
     * This method calculates the probability of a given query after running the method eliminate variable, setting the answer as will.
     * @param bayesianNetwork: Bayes net that contains node with all their information.
     * @param str: The query/probability we need to calculate.
     */
    VariableElimination(BayesianNetwork bayesianNetwork, String str){
        this.bayesianNetwork = bayesianNetwork;
        this.cpt = bayesianNetwork.copyCPT();
        //System.out.println(this.bayesianNetwork.BayesNet.get("E").getParents());
        this.sum = 0;
        this.mult = 0;
        System.out.println("Im str : "+str);
        //splitting the query into query, evidences and hiddens.
        String question = str.substring(2, str.indexOf(")"));
        MyNode query = bayesianNetwork.BayesNet.get(question.substring(0, question.indexOf("=")));
        String[] arr = question.split("\\|");
        List<String> evidence = new LinkedList<>();
        if(arr.length > 1){
            String[] arr1 = arr[1].split(",");
            evidence.addAll(Arrays.asList(arr1));
        }
        List<MyNode> hiddens = new LinkedList<>();
        List<String> str_split = Arrays.asList(str.substring(2).split("[=\\,\\|\\)]"));
        //System.out.println("str split : "+str_split);
        //Sorting thr list for the hiddens so they can be sorted by ABC.
        str_split.sort(String.CASE_INSENSITIVE_ORDER);
        //Adding only the names of the hiddens to the hiddens list. I looped over the bayes net and over the evidences names,
        // if a node from the net isn't contained in the evidences then I add it to the hiddens.
        First:
        for(Map.Entry<String, MyNode> entry:this.bayesianNetwork.BayesNet.entrySet()){
            boolean contains = false;
            Second:
            for (String str1 : str_split) {
                if (str1.equals(entry.getKey())) {
                    contains = true;
                    break Second;
                }
            }
            if(!contains)
                hiddens.add(entry.getValue());
        }
        this.prob = EliminateVariables(query, question.substring(0,question.indexOf("|")), evidence, hiddens);
        String[] split = Double.toString(this.prob).split("\\.");
        String result = "";
        if (split[1].length() > 5)
            result = String.format("%.5f", this.prob);
        else
            result = Double.toString(this.prob);
        this.ans = result + "," + this.sum + "," + this.mult;
    }

    /**
     *
     * @return the probability which is stored in 'ans'.
     */
    public String getAns(){
        return this.ans;
    }

    /**
     * If the CPT contains the given node, remove it.
     * @param n: Given node.
     */
    private void remCPTs(MyNode n){
        this.cpt.removeIf(c -> c.nodes.contains(n));
    }

    /**
     * This method receives a list of evidences, it returns a list that contains only the names of the evidences.
     * @param evidences: List of String that contains evidences.
     * @return a list that contains only the names of the evidences.
     */
    private List<String> getNodeList(List<String> evidences){
        List<String> nodeLst = new LinkedList<>();
        for(String str:evidences){
            int i = str.indexOf("=");
            nodeLst.add(str.substring(0, i));
        }
        return nodeLst;
    }

    /**
     * It receives list of evidences, updates the CPTs.
     * @param evidences: List of evidences.
     */
    private void updateCPT(List<String> evidences){
        List<String> nodeLst = getNodeList(evidences);
        first:
        for (CPT c:this.cpt){
            second:
            for(String str:nodeLst){
                if(c.nodes.contains(this.bayesianNetwork.BayesNet.get(str))){
                    //checking which names of the evidences is a prefix of any the evidence.
                    String str1 = "";
                    third:
                    for(String str2:evidences)
                        if(str2.startsWith(str)) {
                            str1 = str2;
                            break third;
                        }
                    for(Map.Entry<List<String>, Double> entry:c.rows.entrySet())
                        if(!entry.getKey().contains(str1))
                            c.rows.remove(entry);
                    String finalStr = str1;
                    c.rows.entrySet().removeIf(row -> !row.getKey().contains(finalStr));
                    for(Map.Entry<List<String>, Double> key:c.rows.entrySet())
                        key.getKey().remove(str1);
                    c.nodes.remove(this.bayesianNetwork.BayesNet.get(str1));
                }
            }
        }
    }

    /**
     * This method is the algorithm variable elimination, calculates the probability.
     * @param query: Given query's name.
     * @param req: Given query.
     * @param evidences: List of evidences.
     * @param hiddens: List of hiddens.
     * @return the probability of the given query.
     */
    private Double EliminateVariables(MyNode query, String req, List<String> evidences, List<MyNode> hiddens){
        List<String> evidence_node = getNodeList(evidences);
        List<MyNode> lst = new LinkedList<>();
        evidence_node.stream().iterator().forEachRemaining(n->lst.add(this.bayesianNetwork.BayesNet.get(n)));
        lst.add(query);
        //Checking if CPT contains the answer to return it immediately without calculating it again.
        for (CPT cpt:this.cpt) {
            if (cpt.nodes.equals(lst)) {
                List<String> cor = new LinkedList<>(evidences);
                cor.add(req);
                for (Map.Entry<List<String>, Double> entry : cpt.rows.entrySet())
                    if (entry.getKey().containsAll(cor))
                        return entry.getValue();
            }
        }
        //Updating the hiddens, if a node isn't ancestor of the evidences then remove it. In other words removing irrelevant hiddens.
        List<String> copy_evidences = new ArrayList<>(evidence_node);
        copy_evidences.add(query.getName());
        Iterator<MyNode> itr = hiddens.iterator();
        System.out.println(this.bayesianNetwork.BayesNet.keySet());
        while(itr.hasNext()){
            MyNode n = itr.next();
            //System.out.println("name: " + n.getName());
            //System.out.println(n.getParents());
            if(!this.bayesianNetwork.isAncestor(n, copy_evidences)){
                remCPTs(n);
                itr.remove();
            }
        }
        updateCPT(evidences);
        //Joining and eliminating hiddens.
        for(MyNode node:hiddens){
            List<CPT> join_cpt = join(node);
            CPT cpt = eliminate(node, join_cpt);
            this.cpt.add(cpt);
        }
        List<CPT> join_cpt = join(query);
        return normal(join_cpt.get(0), req);
    }

    /**
     * This method receive a node, checks which CPTs contains it and returns a list of the CPTs.
     * @param n: Given node.
     * @return list of CPTs
     */
    private List<CPT> containsCpt(MyNode n){
        List<CPT> lst = new ArrayList<>();
        for (CPT cpt:this.cpt){
            if(cpt.nodes.contains(n))
                lst.add(cpt);
        }
        return lst;
    }

    /**
     * This method applies join on 2 CPTs that contain a specific node, by creating a new CPT that contains the joined 2 CPTs and removing the old CPTs.
     * Increasing number of sums and multiplies according to the joins.
     * @param n: Given node.
     * @return list of CPTs after join.
     */
    private List<CPT> join(MyNode n){
        List<CPT> cpt_lst = containsCpt(n);
        //Minimum number of CPTs to apply join is 2.
        if(cpt_lst.size() <= 1)
            return cpt_lst;
        cpt_lst.removeIf(cpt -> cpt.size() == 1);
        while (cpt_lst.size() >= 2){
            Collections.sort(cpt_lst);
            CPT cpt1 = cpt_lst.get(0);
            CPT cpt2 = cpt_lst.get(1);
            CPT join_cpt = new CPT();
            List<MyNode> combined_nodes = new LinkedList<>(cpt1.nodes);
            combined_nodes.removeAll(cpt2.nodes);
            combined_nodes.addAll(cpt2.nodes);
            join_cpt.nodes = combined_nodes;
            List<MyNode> common_nodes = new LinkedList<>(cpt1.nodes);
            common_nodes.retainAll(cpt2.nodes);
            for(Map.Entry<List<String>, Double> row:cpt1.rows.entrySet()){
                List<String> common_value = new LinkedList<>();
                for(MyNode node:common_nodes){
                    String str1 = "";
                    third:
                    for(String str:row.getKey())
                        if(str.startsWith(node.getName())) {
                            str1 = str;
                            break third;
                        }
                    common_value.add(str1);
                }
                for(Map.Entry<List<String>, Double> row2:cpt2.rows.entrySet()){
                    if(row2.getKey().containsAll(common_value)){
                        List<String> rows = new ArrayList<>(row.getKey());
                        rows.removeAll(row2.getKey());
                        rows.addAll(row2.getKey());
                        double probability = row.getValue()*row2.getValue();
                        this.mult++;
                        join_cpt.rows.put(rows, probability);
                    }
                }
            }
            cpt_lst.add(join_cpt);
            cpt_lst.remove(cpt1);
            this.cpt.remove(cpt1);
            cpt_lst.remove(cpt2);
            this.cpt.remove(cpt2);
        }
        return cpt_lst;
    }

    /**
     * This method eliminates a given node from CPTs by connecting rows. Removing the old CPT from the CPT list.
     * @param n: Given node.
     * @param cpt_lst: List of CPT's.
     * @return a CPT.
     */
    private CPT eliminate(MyNode n, List<CPT> cpt_lst){
        CPT cpt = cpt_lst.get(0);
        List<MyNode> remain_nodes = cpt.nodes;
        remain_nodes.remove(n);
        CPT new_cpt = new CPT();
        new_cpt.nodes = remain_nodes;
        List<List<String>> lst = new ArrayList<>();
        for (List<String> key:cpt.rows.keySet()){
            key.removeIf(s->s.startsWith(n.getName()));
            if(!lst.contains(key))
                lst.add(key);
        }
        for(List<String> l:lst){
            double d = 0.0;
            for (Map.Entry<List<String>, Double> entry:cpt.rows.entrySet()){
                if (entry.getKey().containsAll(l)){
                    d += entry.getValue();
                    this.sum++;
                }
            }
            new_cpt.rows.put(l, d);
            this.sum -= 1;
        }
        this.cpt.remove(cpt);
        return new_cpt;
    }

    /**
     * This method normalizes the probability so we could get the sum being 1.
     * @param cpt: Given CPT.
     * @param req: Given query.
     * @return normalized probability.
     */
    private double normal(CPT cpt, String req){
        this.sum += cpt.size() - 1;
        List<String> lst = new ArrayList<>();
        lst.add(req);
        double sum = 0.0;
        for (double d:cpt.rows.values())
            sum += d;
        double d = 0.0;
        for(Map.Entry<List<String>, Double> entry:cpt.rows.entrySet()){
            if(entry.getKey().equals(lst)) {
                d = entry.getValue();
                break;
            }
        }
        return d/sum;
    }

}
