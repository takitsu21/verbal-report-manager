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
import java.util.Collections;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XML2CSV {
    private Element root;
    private String path_fichier;

    public XML2CSV (String path_data, String path_fichier) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(path_data);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file); // ouverture et lecture du fichier XML
        doc.getDocumentElement().normalize(); // normalise le contenu du fichier, opération très conseillée
        this.root = doc.getDocumentElement(); // la racine de l'arbre XML
        this.path_fichier=path_fichier;

    }

    private List<List<Element>> list_student (List<String> programid){
        List<Element> listStudents = getChildren(root,"student");
        List<List<Element>> listStudentsFinal = new ArrayList<>();
        for (int i=0;i<programid.size();i++) {
            List<Element> studProg = new ArrayList<>();
            for (Element student : listStudents) {

                if(programid.get(i).equals(read(student, "program", 0))){
                    studProg.add(student);

                }

            }
            //trié la liste d'eleve
            listStudentsFinal.add(studProg);
        }
        return listStudentsFinal;
    }


    public void converte () throws IOException {

        List<Element> program= getChildren(root,"program");
        List<String> programid= new ArrayList<>();

        List<String> data= new ArrayList<>();


        List<Element> listCourses = getChildren(root,"course");

        List<List<String>> listCoursesProg = new ArrayList<>();

        for (int i=0;i<program.size();i++){
            programid.add(read(program.get(i),"identifier",0));
            created(path_fichier+programid.get(i)+".csv");

            data.add("\"N° Étudiant\",\"Nom\",\"Prénom\",");
            listCoursesProg.add(list_Courses_Prog(program, programid.get(i)));

        }



        for (int i=0; i<data.size();i++) {
            String cours = string_courses_prog(listCourses, listCoursesProg.get(i));

            data.set(i, data.get(i) + cours + "\n");
            //System.out.println(data.get(i));

        }

        List<List<Element>> listStudents = list_student(programid);

        for (List<Element> liste: listStudents) {
            System.out.println(liste.size());
            for (Element element : liste) {
                String programStud = read(element, "program", 0);
                String a = "\"" + read(element, "identifier", 0) + "\",";
                String b = "\"" + read(element, "name", 0) + "\",";
                String c = "\"" + read(element, "surname", 0) + "\",";


                String d = "";
                List<Element> listStudMat = getChildren(element, "grade"); //liste des matiere d'un etudient


                String note[] = list_note_stu(listCoursesProg.get(programid.indexOf(programStud)), listStudMat);


                for (String s : note) {
                    if (s != null) {
                        d += "\"" + s + "\",";
                    } else {
                        d += "\"" + "\",";
                    }
                }
                String prog = read(element, "program", 0); //programme de l'etudient

                data.set(programid.indexOf(prog), data.get(programid.indexOf(prog)) + (a + b + c + d + "\n"));

            }
        }




        for (int i=0; i<data.size(); i++) {
            //System.out.println(data.get(i));
            byte[] bs = data.get(i).getBytes();
            Path path = Paths.get(path_fichier+programid.get(i)+".csv");

            Path writtenFilePath = Files.write(path, bs);
        }

    }



    private void created (String path) {
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

    private String read(Element element, String tag, int i){
        return element.getElementsByTagName(tag).item(i).getTextContent();
    }

    private String string_courses_prog(List<Element> listCourses, List<String> listCoursesProg){
        String cours="";
        for (String s : listCoursesProg) {
            for (Element element : listCourses) {

                if(s.charAt(0) == '$'){

                    cours+=("\"" + s.substring(1, s.length()) + "\",");
                    break;
                }

                else if (s.equals(read(element, "identifier", 0))) {
                    String a = "\"" + read(element, "identifier", 0);
                    String b = " - " + read(element, "name", 0) + "\",";
                    cours += (a + b);
                }

            }
        }



        return cours;
    }

    private String[] list_note_stu (List<String> listProg, List<Element> listStudMat){

        String note[] = new String[listProg.size()];

        for (int i=0; i<listStudMat.size(); i++){
            int j=0;


            String mat=read(listStudMat.get(i), "item",0);

            //System.out.println(listProg.get(j));

            while (!mat.equals(listProg.get(j))) {  //listProg.get(j)!="$" &&
                j+=1;
            }

            note[j]=read(listStudMat.get(i), "value",0);


        }
        for (int i=0;i<note.length;i++){  //rajouté les note des ct et option
            if (note[i]==null && listProg.get(i).charAt(0)=='$'){
                System.out.println("ok");
                //note[i]=Math.max();
            }
            else{
                System.out.println("normal");
            }
        }


        return note;
    }

    private List<String> list_Courses_Prog (List<Element> program, String programid){
        List<String> item = new ArrayList<>();


        List<Element> composite = new ArrayList<>();
        List<Element> option = new ArrayList<>();

        for (int i=0; i<program.size();i++){
            if (read(program.get(i), "identifier", 0).equals(programid)){
                List<Element> item1=getChildren(program.get(i), "item");
                for (Element el: item1){
                    item.add(el.getTextContent());
                }
                composite=getChildren(program.get(i), "composite");
                option=getChildren(program.get(i), "option");
            }
        }



        for (int i=0; i<option.size();i++) {
            List<Element> item2 = getChildren(option.get(i), "item");
            item.add("$" + read(option.get(i),"identifier",0) + " - " + read(option.get(i),"name",0));

            for (Element el: item2){
                item.add(el.getTextContent());
            }
        }

        for (int i=0; i<composite.size();i++) {
            List<Element> item3 = getChildren(composite.get(i), "item");
            item.add("$" + read(composite.get(i),"identifier",0) + " - " + read(composite.get(i),"name",0));

            for (Element el: item3){
                item.add(el.getTextContent());
            }
        }



        return item;
    }
}
