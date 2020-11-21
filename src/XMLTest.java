import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XMLTest {
    public static void main(String[] args) {
        try {
            File f = new File("../pomme.csv");
            if (f.createNewFile())
                System.out.println("File created");
            else
                System.out.println("File already exists");


            File file = new File("../data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file); // ouverture et lecture du fichier XML
            doc.getDocumentElement().normalize(); // normalise le contenu du fichier, opération très conseillée
            Element root = doc.getDocumentElement(); // la racine de l'arbre XML

            //System.out.println(root);
            // c'est parti pour l'exploration de l'arbre

            List<Element> listStudents = getChildren(root,"student");
            List<Element> listCourses = getChildren(root,"course");
            Path path = Paths.get("../pomme.csv");
            String str="\"N° Étudiant\",\"Nom\",\"Prénom\",";
            for (Element element : listCourses){

                String a = "\"" + element.getElementsByTagName("identifier").item(0).getTextContent() ;
                String b = " - " + element.getElementsByTagName("name").item(0).getTextContent() + "\",";
                //String c = "\"" + element.getElementsByTagName("credits").item(0).getTextContent() + "\",";

                str += (a+b);



            }

            str+="\n";

            for (Element element : listStudents){
                String a = "\"" + element.getElementsByTagName("identifier").item(0).getTextContent() +"\",";
                String b = "\"" + element.getElementsByTagName("name").item(0).getTextContent() + "\",";
                String c = "\"" + element.getElementsByTagName("surname").item(0).getTextContent() + "\",";

                String d="";
                List<Element> listStudMat = getChildren(element, "grade");

                String matiere[] = new String[listCourses.size()];
                for (int i=0; i<listStudMat.size(); i++){
                    int j=0;

                    String mat=listStudMat.get(i).getElementsByTagName("item").item(0).getTextContent();

                    while (!mat.equals(listCourses.get(j).getElementsByTagName("identifier").item(0).getTextContent())) {
                        j+=1;
                    }

                    matiere[j]=listStudMat.get(i).getElementsByTagName("value").item(0).getTextContent();
                    System.out.println(matiere[0]);
                }

                for (String s: matiere){
                    if (s!=null){
                        d+="\"" + s + "\",";
                    }
                    else{
                        d+="\"" + "\",";
                    }
                }

                str += (a+b+c+d+"\n");
            }

            System.out.println(str);

            byte[] bs = str.getBytes();
            Path writtenFilePath = Files.write(path, bs);
            //System.out.println("Written content in file:\n"+ new String(Files.readAllBytes(writtenFilePath)));




        } catch (Exception e) {
            // oups, pas normal
        }
    }



    // Extrait la liste des fils de l'élément item dont le tag est name
    private static List<Element> getChildren(Element item, String name) {
        NodeList nodeList = item.getChildNodes();
        List<Element> children = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodeList.item(i); // cas particulier pour nous où tous les noeuds sont des éléments


                if (element.getTagName().equals(name)) {

                    children.add(element);


                }
            }
        }
        return children;
    }
}         