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
    private final Element root;
    private final String path_fichier;

    public XML2CSV (String path_data, String path_fichier) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(path_data);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file); // ouverture et lecture du fichier XML
        doc.getDocumentElement().normalize(); // normalise le contenu du fichier, opération très conseillée
        this.root = doc.getDocumentElement(); // la racine de l'arbre XML
        this.path_fichier=path_fichier;

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

            data.add("\"N° Étudiant\",\"Nom\",\"Prénom\",\""+programid.get(i)+" - "+read(program.get(i),"name",0)+"\",");
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
                String programStud = read(element, "program", 0);
                StringBuilder a = new StringBuilder();
                StringBuilder b = new StringBuilder();
                StringBuilder c = new StringBuilder();

                a.append("\"").append(read(element, "identifier", 0)).append("\",");
                b.append("\"").append(read(element, "name", 0)).append("\",");
                c.append("\"").append(read(element, "surname", 0)).append("\",");

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
                //String prog = read(element, "program", 0); //programme de l'etudient

                data.set(programid.indexOf(programStud), data.get(programid.indexOf(programStud)) + (a.toString() + b.toString() + c.toString() + moyenne + d + "\n"));

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

            data.set(i, data.get(i) + "\"" + "Note max" + "\"," + "\"" + "\"," + "\"" + "\"," + note_max + "\n");
            data.set(i, data.get(i) + "\"" + "Note min" + "\"," + "\"" + "\"," + "\"" + "\"," + note_min + "\n");
            data.set(i, data.get(i) + "\"" + "Note moyenne" + "\"," + "\"" + "\"," + "\"" + "\"," + note_moyenne + "\n");
            data.set(i, data.get(i) + "\"" + "Écart-type" + "\"," + "\"" + "\"," + "\"" + "\"," + ecart_type + "\n");
        }


        for (int i=0; i<data.size(); i++) {

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
        StringBuilder cours= new StringBuilder();
        for (String s : listCoursesProg) {
            for (Element element : listCourses) {

                if(s.charAt(0) == '$' || s.charAt(0) == '*'){

                    cours.append("\"").append(s.substring(1, s.length())).append("\",");
                    break;
                }

                else if (s.equals(read(element, "identifier", 0))) {
                    String a = "\"" + read(element, "identifier", 0);
                    String b = " - " + read(element, "name", 0) + "\",";
                    cours.append(a).append(b);
                }

            }
        }



        return cours.toString();
    }

    private String[] list_note_stu (List<String> listProg, List<Element> listStudMat){
        String note[] = new String[listProg.size()];

        for (Element element : listStudMat) {
            int j = 0;

            String mat = read(element, "item", 0);

            while (!mat.equals(listProg.get(j))) {
                j += 1;
            }
            note[j] = read(element, "value", 0);
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
                    note[i]= String.format("%.3f",(double)(acc/nb)).replace(",", ".");
                }
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
            item.add("*" + read(option.get(i),"identifier",0) + " - " + read(option.get(i),"name",0));

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

            String student[]=new String[studProg.size()];
            List<Element> studProgFinal = new ArrayList<>();
            for(int j=0; j<studProg.size();j++){
                student[j]=read(studProg.get(j), "name",0);
                //System.out.println(student[j]);
            }
            Arrays.sort(student);
            for (int k=0; k<studProg.size();k++){
                int l=0;
                while(!read(studProg.get(l), "name",0).equals(student[k])) {
                    l+=1;
                }
                studProgFinal.add(studProg.get(l));
            }
            listStudentsFinal.add(studProgFinal);
        }
        return listStudentsFinal;
    }

    public String moyenne(List<Element> listCourse, List<String> listCourses, String note[]){
        String coef[]=new String[listCourses.size()];
        double acc=0;
        int nb=0;
        String moyenne;

        for (int i=0; i<listCourses.size();i++){

            for (int j=0; j<listCourse.size();j++){

                if(listCourses.get(i).equals(read(listCourse.get(j), "identifier", 0))){
                    coef[i]=read(listCourse.get(j),"credits",0);
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
        moyenne=String.format("%.3f",(double)(acc/nb)).replace(",", ".");

        return moyenne;
    }

    public double note_min_max(String[] notes, int M){
        double acc=M==1?0:20;

        for (int i=0; i<notes.length; i++){
            if(notes[i]!=null) {
                if (notes[i] != null && !notes[i].equals("ABI") && (acc*M) < (Double.parseDouble(notes[i])*M)) {
                    acc = Double.parseDouble(notes[i]);
                }
            }
        }
        return acc;
    }

    public double note(String[][] notes, int j, int M){
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



    public double note_moyenne(String[] notes){
        double acc=0;
        int nb=0;

        for (int i=0; i<notes.length; i++){
            if(notes[i]!=null) {
                if(!notes[i].equals("ABI")) {
                    acc += Double.parseDouble(notes[i]);
                    nb+=1;
                }
            }
        }
        return Double.parseDouble(String.format("%.3f",(double)(acc/nb)).replace(",", "."));
    }

    public double ecart_type(String[] notes){
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
