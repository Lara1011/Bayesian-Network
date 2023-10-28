import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Ex1 {
    public static void main(String[] args) throws Exception {
        //Reading txt file:
        BufferedReader br = null;
        BayesianNetwork bayesianNetwork = new BayesianNetwork();
        try {
            //File file = new File("/Users/laraabu/Desktop/input.txt"); // java.io.File
            FileReader fr = new FileReader(new File("/Users/laraabu/Desktop/input.txt")); // java.io.FileReader
            br = new BufferedReader(fr); // java.io.BufferedReader
            String line = br.readLine();
            //Reading xml file:
            try {
                String FILENAME = "/Users/laraabu/Desktop/computer science/3rd year/AI algorithms/BayesianNetwork/" + line;
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new File(FILENAME));
                doc.getDocumentElement().normalize();
                System.out.println(doc.getDocumentElement().getNodeName());
                System.out.println("------------------");
                NodeList node_lst = doc.getElementsByTagName("VARIABLE");
                for (int i = 0; i < node_lst.getLength(); i++) {
                    Node n = node_lst.item(i);
                    //Let's start with the variables:
                    //get names and outcomes:
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element elementE = (Element) n;
                        MyNode node = new MyNode(elementE.getElementsByTagName("NAME").item(0).getTextContent());
                        NodeList outcomes_lst = elementE.getElementsByTagName("OUTCOME");
                        System.out.println(node.getName() + ": ");
                        for (int j = 0; j < outcomes_lst.getLength(); j++) { //get all outcomes
                            node.addOutcome(outcomes_lst.item(j).getTextContent());
                        }
                        System.out.println(node.getOutcome());
                        bayesianNetwork.addNode(node);
                    }
                }
                System.out.println(bayesianNetwork.BayesNet.get("A"));
                System.out.println("network: ----- "+bayesianNetwork.BayesNet.keySet());
                NodeList node_lst1 = doc.getElementsByTagName("DEFINITION");
                for (int i = 0; i < node_lst1.getLength(); i++) {
                    Node n = node_lst1.item(i);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element elementE = (Element) n;
                        MyNode node = bayesianNetwork.BayesNet.get(elementE.getElementsByTagName("FOR").item(0).getTextContent());
                        List<MyNode> parents_lst = new ArrayList<>();
                        if(elementE.getElementsByTagName("GIVEN").getLength() > 0) {
                            LinkedList<String> parents_names = new LinkedList<>();
                            NodeList given_lst = elementE.getElementsByTagName("GIVEN");
                            for (int j = 0; j < given_lst.getLength(); j++) {
                                String str = given_lst.item(j).getTextContent();
                                parents_names.add(str);
                                parents_lst.add(bayesianNetwork.BayesNet.get(str));
                            }
                            bayesianNetwork.updateParents(node, parents_names);
                            System.out.println(bayesianNetwork.BayesNet.get(node.getName()).getParents());
                        }
                        String str = elementE.getElementsByTagName("TABLE").item(0).getTextContent();
                        parents_lst.add(node);
                        bayesianNetwork.addCPT(new CPT(parents_lst, str));
                        System.out.println(bayesianNetwork.copyCPT());
                    }
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
            System.out.println("Parents of A: " + bayesianNetwork.BayesNet.get("M").getParents());
            try {
                List<String> input = Files.readAllLines(Path.of("input.txt"));
                List<String> output_lst = new LinkedList<>();
                for (String str : input) {
//                    if (str.endsWith("1")) {
//                        BasicInference basicInference = new BasicInference(bayesianNetwork, str.substring(0, str.length() - 2));
//                        output_lst.add(basicInference.getAns());
//                    }
                    if (str.endsWith("2")) {
                        VariableElimination variable_elimination = new VariableElimination(bayesianNetwork, str.substring(0, str.length() - 2));
                        output_lst.add(variable_elimination.getAns());
                    }
                    if (str.endsWith("3")) {
                        MyVariableElimination variable_elimination = new MyVariableElimination(bayesianNetwork, str.substring(0, str.length() - 2));
                        output_lst.add(variable_elimination.getAns());
                    }
                }
                FileWriter writer = new FileWriter("output.txt");
                String answer = output_lst.remove(output_lst.size() - 1);
                for (String str : output_lst) {
                    writer.write(str + System.lineSeparator());
                }
                writer.write(answer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}