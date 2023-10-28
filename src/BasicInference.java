package src;

import java.util.*;

public class BasicInference {
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
    BasicInference(BayesianNetwork bayesianNetwork, String str){
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
        this.prob = basicInference(query, question.substring(0,question.indexOf("|")), evidence, hiddens);
        String[] split = Double.toString(this.prob).split("\\.");
        String result = "";
        if (split[1].length() > 5)
            result = String.format("%.5f", this.prob);
        else
            result = Double.toString(this.prob);
        this.ans = result + "," + this.sum + "," + this.mult;
    }

    public Double basicInference(MyNode query, String req, List<String> evidences, List<MyNode> hiddens){
        final CPT cpt = new CPT();
        final ObservedEvidence observedEvidence = new ObservedEvidence(query, evidences, bayesianNetwork, hiddens);
        Iterator<MyNode> itr = hiddens.iterator();
        while (itr.hasNext()){

        }
        return 0.0;
    }

    protected class ObservedEvidence{
        private BayesianNetwork bayesianNetwork = new BayesianNetwork();
        private ArrayList<String> extendedValues = new ArrayList<>();
        private int start = 0;
        private int extendedIdx = 0;
        private List<MyNode> nodes = new ArrayList<>();
        private Map<MyNode, Integer> nodeIdx = new HashMap<>();

        public ObservedEvidence(MyNode query, List<String> evidences, BayesianNetwork bayesianNetwork, List<MyNode> hiddens){
            this.bayesianNetwork = bayesianNetwork;
            Arrays.sort(this.bayesianNetwork.BayesNet.keySet().toArray());
            this.extendedValues = new ArrayList<>();
            int idx = 0;
            nodeIdx.put(query, idx);
            nodes.add(query);
            idx++;
            for (String str:evidences){
                nodes.add(this.bayesianNetwork.BayesNet.get(str));
                nodeIdx.put(this.bayesianNetwork.BayesNet.get(str), idx);
                extendedValues.add(idx, str);
                idx++;
            }
            this.extendedIdx = idx - 1;
            this.start = idx;
            for (MyNode node:hiddens){
                nodes.add(node);
                nodeIdx.put(node, idx);
                idx++;
            }
        }
    }

    /**
     *
     * @return the probability which is stored in 'ans'.
     */
    public String getAns(){
        return this.ans;
    }

    /**
     * This method normalizes the probability so we could get the sum being 1.
     * @param cpt: Given src.CPT.
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
