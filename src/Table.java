import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Table {
    String csv="";
    JTable table ;
    JScrollPane Jscroll;

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
        csv+=(line+"\n");

        //System.out.println(line);

        //System.out.println(nbline);
        String[] column = line.split("\",\"");
        column[0] = column[0].replace("\"", "");
        //System.out.println(column.length);


        String[][] data = new String[nbline-2][column.length];
        int i = 0;
        line = br.readLine();

        String[] temp = line.split("\",\"");
        while(line != null){
            //System.out.println(line);
            //System.out.println(temp.length);
            //System.out.println(line);
            csv+=(line+"\n");
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



        //System.out.println(column[0]);
        table = new JTable(data,column);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        /*table.setCellSelectionEnabled(true);
        ListSelectionModel select= table.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);*/


        Jscroll = new JScrollPane(table);





    }





}
