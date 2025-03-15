import com.wmp.classtools.duty.panel.DPanel;
import com.wmp.classtools.frame.MainWindow;
import com.wmp.io.IOStreamForInf;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello, World!");
        String path = System.getenv ("LOCALAPPDATA");

        StringBuilder sb = new StringBuilder();
        sb.append(path).append("\\ClassTools\\");

        //System.out.println(sb);


        new MainWindow(sb.toString());
    }
}