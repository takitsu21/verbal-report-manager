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
import java.util.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class XML2CSV {
    private final Element root;

    public HashMap<String, String> dicoData = new HashMap<String, String>();
    //private final String path_fichier;


    public XML2CSV (String path_data) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(path_data);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file); // ouverture et lecture du fichier XML
        doc.getDocumentElement().normalize(); // normalise le contenu du fichier, opération très conseillée
        this.root = doc.getDocumentElement(); // la racine de l'arbre XML
        //this.path_fichier=path_fichier;

    }


    public void converte () throws IOException {
        List<Element> program= getChildren(root,"program");
        List<String> programid= new ArrayList<>();
        List<String> data = new ArrayList<>();

        List<Element> listCourses = getChildren(root,"course");
        List<List<String>> listCoursesProg = new ArrayList<>();

        for (int i=0;i<program.size();i++){
            programid.add(read(program.get(i),"identifier"));
            //created(path_fichier+programid.get(i)+".csv");

            data.add("\"N° Étudiant\",\"Nom\",\"Prénom\",\""+programid.get(i)+" - "+read(program.get(i),"name")+"\",");
            listCoursesProg.add(list_Courses_Prog(program, programid.get(i)));

        }

        for (int i=0; i<data.size();i++) {
            String cours = string_courses_prog(listCourses, listCoursesProg.get(i));
            data.set(i, data.get(i) + cours + "\n");
        }

        List<List<Element>> listStudents = list_student(programid);
        String[][][] notes= new String[programid.size()][listStudents.get(0).size()][];
        String[][] moyennes= new String[programid.size()][listStudents.get(0).size()];

        for (List<Element> liste: listStudents) {
            for (Element element : liste) {
                String programStud = read(element, "program");
                StringBuilder a = new StringBuilder();
                StringBuilder b = new StringBuilder();
                StringBuilder c = new StringBuilder();

                a.append("\"").append(read(element, "identifier")).append("\",");
                b.append("\"").append(read(element, "name")).append("\",");
                c.append("\"").append(read(element, "surname")).append("\",");

                StringBuilder d = new StringBuilder();
                List<Element> listStudMat = getChildren(element, "grade"); //liste des matiere d'un etudient

                String[] note = list_note_stu(listCoursesProg.get(programid.indexOf(programStud)), listStudMat);
                notes[programid.indexOf(programStud)][listStudents.get(programid.indexOf(programStud)).indexOf(element)] = note;

                String moyenne=moyenne(listCourses, listCoursesProg.get(programid.indexOf(programStud)), note);
                moyennes[programid.indexOf(programStud)][listStudents.get(programid.indexOf(programStud)).indexOf(element)]=moyenne;
                moyenne="\"" + moyenne + "\",";

                for (String s : note) {
                    if (s != null) {
                        d.append("\"").append(s).append("\",");
                    } else {
                        d.append("\"").append("\",");
                    }
                }


                data.set(programid.indexOf(programStud), data.get(programid.indexOf(programStud)) + (a.toString() + b.toString() + c.toString() + moyenne + d.substring(0,d.length()-1) + "\n"));

            }
        }

        for (int i=0; i<programid.size();i++){
            StringBuilder note_max= new StringBuilder("\"" + note_min_max(moyennes[i], 1) + "\",");
            StringBuilder note_min= new StringBuilder("\"" + note_min_max(moyennes[i], -1) + "\",");
            StringBuilder note_moyenne= new StringBuilder("\"" + note_moyenne(moyennes[i]) + "\",");
            StringBuilder ecart_type= new StringBuilder("\"" + ecart_type(moyennes[i]) + "\",");

            for (int j=0; j<listCoursesProg.get(i).size(); j++) {
                note_max.append("\"").append(note(notes[i], j, 1)).append("\",");
                note_min.append("\"").append(note(notes[i], j, -1)).append("\",");
                note_moyenne.append("\"").append(note(notes[i], j, 2)).append("\",");
                ecart_type.append("\"").append(note(notes[i], j, 3)).append("\",");

            }

            data.set(i, data.get(i) + "\"" + "Note max" + "\"," + "\"" + "\"," + "\"" + "\"," + note_max.substring(0,note_max.length()-1) + "\n");
            data.set(i, data.get(i) + "\"" + "Note min" + "\"," + "\"" + "\"," + "\"" + "\"," + note_min.substring(0,note_min.length()-1) + "\n");
            data.set(i, data.get(i) + "\"" + "Note moyenne" + "\"," + "\"" + "\"," + "\"" + "\"," + note_moyenne.substring(0,note_moyenne.length()-1) + "\n");
            data.set(i, data.get(i) + "\"" + "Écart-type" + "\"," + "\"" + "\"," + "\"" + "\"," + ecart_type.substring(0,ecart_type.length()-1) + "\n");
        }


        /*for (int i=0; i<data.size(); i++) {

            //save(data.get(i), programid.get(i)+".csv");
        }*/



        for (int i=0; i<programid.size(); i++) {
            dicoData.put(programid.get(i), data.get(i));
        }


    }

    void save(String path_fichier) throws IOException {
        /*for (int i=0; i<data.size(); i++) {

            byte[] bs = data.get(i).getBytes();
            Path path = Paths.get(path_fichier + name[i] + ".csv");

            Path writtenFilePath = Files.write(path, bs);
        }*/


        for (String i : dicoData.keySet()) {
            byte[] bs = dicoData.get(i).getBytes();
            Path path = Paths.get(path_fichier + i + ".csv");
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

    private String read(Element element, String tag){
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    private List<String> list_Courses_Prog (List<Element> program, String programid){
        List<String> item = new ArrayList<>();
        List<Element> composite = new ArrayList<>();
        List<Element> option = new ArrayList<>();

        for (Element element1 : program) {
            if (read(element1, "identifier").equals(programid)) {
                List<Element> item1 = getChildren(element1, "item");
                for (Element el : item1) {
                    item.add(el.getTextContent());
                }
                composite = getChildren(element1, "composite");
                option = getChildren(element1, "option");
            }
        }

        for (Element value : option) {
            List<Element> item2 = getChildren(value, "item");
            item.add("*" + read(value, "identifier") + " - " + read(value, "name"));

            for (Element el : item2) {
                item.add(el.getTextContent());
            }
        }

        for (Element element : composite) {
            List<Element> item3 = getChildren(element, "item");
            item.add("$" + read(element, "identifier") + " - " + read(element, "name"));

            for (Element el : item3) {
                item.add(el.getTextContent());
            }
        }

        return item;
    }

    private String string_courses_prog(List<Element> listCourses, List<String> listCoursesProg){
        StringBuilder cours= new StringBuilder();
        for (String s : listCoursesProg) {
            for (Element element : listCourses) {

                if(s.charAt(0) == '$' || s.charAt(0) == '*'){

                    cours.append("\"").append(s.substring(1)).append("\",");
                    break;
                }

                else if (s.equals(read(element, "identifier"))) {
                    String a = "\"" + read(element, "identifier");
                    String b = " - " + read(element, "name") + "\",";
                    cours.append(a).append(b);
                }

            }
        }
        String cours_finale=cours.substring(0,cours.length()-1);
        //System.out.println(cours_finale);
        return cours_finale;
    }

    private List<List<Element>> list_student (List<String> programid){
        List<Element> listStudents = getChildren(root,"student");
        List<List<Element>> listStudentsFinal = new ArrayList<>();
        for (String s : programid) {
            List<Element> studProg = new ArrayList<>();
            for (Element student : listStudents) { //reparti les eleves selon leur program
                if (s.equals(read(student, "program"))) {
                    studProg.add(student);
                }
            }

            studProg.sort(Comparator.comparing(o -> read(o, "name"))); //tri les eleves dans l'ordre alphabetique

            listStudentsFinal.add(studProg);
        }
        return listStudentsFinal;
    }

    private String[] list_note_stu (List<String> listProg, List<Element> listStudMat){
        String[] note = new String[listProg.size()];

        for (Element element : listStudMat) {
            int j = 0;

            String mat = read(element, "item");

            while (!mat.equals(listProg.get(j))) {
                j += 1;
            }
            note[j] = read(element, "value");
        }
        for (int i=0;i<note.length;i++){
            if (note[i]==null) {
                if (listProg.get(i).charAt(0)==('*')) {
                    int a=i+1;
                    while(a<listProg.size()-1 && listProg.get(a).charAt(0)!=('*') && listProg.get(a).charAt(0)!=('$')){
                        if(note[a]!=null){
                            note[i]=note[a];
                        }
                        a+=1;
                    }
                }

                else if (listProg.get(i).charAt(0)==('$')) {
                    int a=i+1;
                    double acc=0;
                    int nb=0;
                    while(a<listProg.size()-1 && listProg.get(a).charAt(0)!=('*') && listProg.get(a).charAt(0)!=('$')){
                        if(note[a]!=null){
                            if(note[a].equals("ABI")){
                                acc+=0;
                            }
                            else {
                                acc += Double.parseDouble(note[a]);
                            }
                            nb+=1;
                        }
                        a+=1;
                    }
                    note[i]= String.format("%.3f", acc/nb).replace(",", ".");
                }
            }
        }
        return note;
    }

    private String moyenne(List<Element> listCourse, List<String> listCourses, String[] note){
        String[] coef =new String[listCourses.size()];
        double acc=0;
        int nb=0;
        String moyenne;

        for (int i=0; i<listCourses.size();i++){

            for (Element element : listCourse) {

                if (listCourses.get(i).equals(read(element, "identifier"))) {
                    coef[i] = read(element, "credits");
                }
            }
        }

        for (int i=0; i<note.length; i++){
            if(coef[i] != null && note[i] != null) {
                //System.out.println(note[i]);
                if(!note[i].equals("ABI")){
                    acc += (Double.parseDouble(note[i]) * Integer.parseInt(coef[i]));
                }

                nb += Integer.parseInt(coef[i]);
            }
        }
        moyenne=String.format("%.3f", acc/nb).replace(",", ".");

        return moyenne;
    }

    private double note_min_max(String[] notes, int M){
        double acc=M==1?0:20;

        for (String note : notes) {
            if (note != null) {
                if (!note.equals("ABI") && acc * M < Double.parseDouble(note) * M) {
                    acc = Double.parseDouble(note);
                }
            }
        }
        return acc;
    }

    private double note(String[][] notes, int j, int M){
        String[] note=new String[notes.length];

        for (int i=0; i<notes.length; i++){
            if (notes[i]!=null) {
                note[i] = notes[i][j];
            }
        }

        return switch (M) {
            case -1, 1 -> note_min_max(note, M);
            case 2 -> note_moyenne(note);
            case 3 -> ecart_type(note);
            default -> 0;
        };
    }



    private double note_moyenne(String[] notes){
        double acc=0;
        int nb=0;

        for (String note : notes) {
            if (note != null) {
                if (!note.equals("ABI")) {
                    acc += Double.parseDouble(note);
                    nb += 1;
                }
            }
        }
        return Double.parseDouble(String.format("%.3f", acc/nb).replace(",", "."));
    }

    private double ecart_type(String[] notes){
        double[] note=new double[notes.length];
        int n= notes.length;
        double moyenne=0;
        double variance=0;
        double ecart_type;

        for (int i=0; i<notes.length; i++){
            if(notes[i]!=null) {
                if (notes[i] != null) {
                    if(!notes[i].equals("ABI")) {
                        note[i] = Double.parseDouble(notes[i]);
                    }

                }
            }
        }

        for (double value : note) {
            moyenne += value;
        }
        moyenne /= n;

        for (int i=0; i<note.length; i++){
            note[i]-=moyenne;
            note[i]*=note[i];
        }

        for (double value : note) {
            variance += value;
        }
        variance/=(n-1);
        ecart_type=Math.sqrt(variance);

        return Double.parseDouble(String.format("%.3f",ecart_type).replace(",", "."));
    }
}
