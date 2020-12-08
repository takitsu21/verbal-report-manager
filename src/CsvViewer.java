import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvViewer {

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.setTitle("Jalon-2");
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FileReader fr = new FileReader("../minutes_info.csv");
        FileReader fr_count = new FileReader("../minutes_info.csv");
        BufferedReader br = new BufferedReader(fr);

        BufferedReader br_count = new BufferedReader(fr_count);

        String ln = br_count.readLine();
        int nbline = 1;
        while(ln != null){
            nbline++;
            ln = br_count.readLine();
        }


        String line = br.readLine();
        //System.out.println(line);

        //System.out.println(nbline);
        String[] column = line.split("\",\"");
        column[0] = column[0].replace("\"", "");
        System.out.println(column.length);


        String[][] data = new String[column.length][nbline];
        int i = 0;
        line = br.readLine();
        String[] temp = line.split("\",\"");
        System.out.println(temp);
        while(line != null){
            //System.out.println(line);
            //System.out.println(temp.length);
            for(int j = 0 ; j < temp.length ; j++ ){
                data[i][j] = temp[j];
                System.out.println(data[i][j]);

            }
            data[i][temp.length-1]=data[i][temp.length-1].replace("\"", "");
            data[i][0] = data[i][0].replace("\"", "");
            line = br.readLine();
            if(line != null) {
                temp = line.split("\",\"");}
            if (i != column.length-1 ){
                i++;
            }

        }

        //System.out.println(column[0]);
        JTable jt = new JTable(data,column);

        jt.setBounds(30,40,200,300);
        JScrollPane jsp = new JScrollPane(jt);
        frame.add(jsp);





        frame.setVisible(true);

    }
}