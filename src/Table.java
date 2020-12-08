import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Table {

    JTable table ;
    JScrollPane Jscroll;
    String csv="";

    public Table(String path) throws IOException {

        FileReader fr = new FileReader(path);
        FileReader fr_count = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);

        BufferedReader br_count = new BufferedReader(fr_count);


        String ln = br_count.readLine();
        int nbline = 1;
        while(ln != null){
            nbline++;
            ln = br_count.readLine();
        }
        br_count.close();


        String line = br.readLine();
        System.out.println(line);
        csv+=(line+"\n");

        System.out.println(nbline);
        String[] column = line.split("\",\"");
        column[0] = column[0].replace("\"", "");
        System.out.println(column.length);


        String[][] data = new String[nbline-2][column.length];
        int i = 0;
        line = br.readLine();
        String[] temp = line.split("\",\"");
        while(line != null){
            csv+=(line+"\n");
            System.out.println(line);
            //System.out.println(temp.length);
            for(int j = 0 ; j < temp.length ; j++ ){
                data[i][j] = temp[j];

            }
            data[i][temp.length-1]=data[i][temp.length-1].replace("\"", "");
            data[i][0] = data[i][0].replace("\"", "");
            line = br.readLine();
            if(line != null) {
                temp = line.split("\",\"");}
            if (i < nbline-1 ){
                i++;
            }


        }

        System.out.println(column[0]);
        table = new JTable(data,column);
        table.setBounds(30,40,20,30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Jscroll = new JScrollPane(table);



    }



}
