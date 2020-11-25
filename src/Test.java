import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        XML2CSV a=new XML2CSV("../data.xml","../");
        a.converte();
    }
}
