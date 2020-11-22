import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XML2CSV {
    Element root;

    public XML2CSV (String data) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(data);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file); // ouverture et lecture du fichier XML
        doc.getDocumentElement().normalize(); // normalise le contenu du fichier, opération très conseillée
        root = doc.getDocumentElement(); // la racine de l'arbre XML

    }



    public String read(Element element, String tag, int i){
        return element.getElementsByTagName(tag).item(i).getTextContent();
    }

    public void converte () throws IOException {

        List<Element> program= getChildren(root,"program");
        List<String> programid= new ArrayList<>();
        List<String> str= new ArrayList<>();

        for (int i=0;i<program.size();i++){
            programid.add(read(program.get(i),"identifier",0));
            created("../"+programid.get(i)+".csv");

            str.add("\"N° Étudiant\",\"Nom\",\"Prénom\",");

        }

        List<Element> listStudents = getChildren(root,"student");
        List<Element> listCourses = getChildren(root,"course");

        String st="";
        for (Element element : listCourses){

            String a = "\"" + read(element, "identifier",0);
            String b = " - " + read(element, "name",0) + "\",";
            //String c = "\"" + element.getElementsByTagName("credits").item(0).getTextContent() + "\",";

            /*String prog = read(element, "program",0);
            System.out.println(prog);
            str.set(program.indexOf(prog), str.get(program.indexOf(prog))+(a+b));*/
            st+=(a+b);

        }
        for (int i=0; i<str.size();i++) {

            str.set(i, str.get(i)+st+"\n");
            System.out.println(str.get(i));

        }

        for (Element element : listStudents){

            String a = "\"" + read(element, "identifier",0) +"\",";
            String b = "\"" + read(element, "name",0) + "\",";
            String c = "\"" + read(element, "surname",0) + "\",";

            String d="";
            List<Element> listStudMat = getChildren(element, "grade"); //liste des matiere d'un etudient

            String matiere[] = new String[listCourses.size()];
            for (int i=0; i<listStudMat.size(); i++){
                int j=0;

                String mat=read(listStudMat.get(i), "item",0);

                while (!mat.equals(read(listCourses.get(j), "identifier",0))) {
                    j+=1;
                }

                matiere[j]=read(listStudMat.get(i), "value",0);

            }

            for (String s: matiere){
                if (s!=null){
                    d+="\"" + s + "\",";
                }
                else{
                    d+="\"" + "\",";
                }
            }
            String prog = read(element, "program",0); //programme de l'etudient
            System.out.println(program.get(0));
            str.set(programid.indexOf(prog), str.get(programid.indexOf(prog))+(a+b+c+d+"\n"));

        }




        for (int i=0; i<str.size(); i++) {
            System.out.println(str.get(i));
            byte[] bs = str.get(i).getBytes();
            Path path = Paths.get("../"+programid.get(i)+".csv");

            Path writtenFilePath = Files.write(path, bs);
        }

    }



    public void created (String path) {
        File f = new File(path);
        try {
            if (f.createNewFile()) {
                System.out.println("File created");
            }
            else {
                System.out.println("File already exists");
            }



        }
        catch(Exception e){
            //oups
        }
    }

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
