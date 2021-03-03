import java.util.Date;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Comentario comment = new Comentario(-1, "Diogo", "Que legal", "Achei muito legal esse negocio", 25);
        Comentario comment2 = new Comentario(-1, "Souza", "Que chato", "Achei muito chato esse negocio", 40);
        
        CRUD arqComentarios; 

        try {
            new File("comentarios.db").delete();
            arqComentarios = new CRUD<>(Comentario.class.getConstructor(), "comentarios.db"); 

            int id = arqComentarios.create(comment);
            int id2 = arqComentarios.create(comment2);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException IOe) {
            IOe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
}