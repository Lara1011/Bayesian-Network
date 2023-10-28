import java.util.*;

public class EnumerationAsk {
    private BayesianNetwork bayesianNetwork;
    private LinkedList<String> outcome;
    private List<String> evidences;
    private List<MyNode> hiddens;
    private int sum;
    private int mult;
    private double prob;
    private String ans;

    public EnumerationAsk(BayesianNetwork bayesianNetwork, String str){
        this.bayesianNetwork = bayesianNetwork;
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
        this.prob = EnumerationAsk(query, question.substring(0,question.indexOf("|")));
        String[] split = Double.toString(this.prob).split("\\.");
        String result = "";
        if (split[1].length() > 5)
            result = String.format("%.5f", this.prob);
        else
            result = Double.toString(this.prob);
        this.ans = result + "," + this.sum + "," + this.mult;
    }

    public Double EnumerationAsk(MyNode query, String req){
        ArrayList<Double> Q = new ArrayList<>();

        return 0.0;
    }
}
