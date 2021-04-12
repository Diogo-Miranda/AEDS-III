import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
//import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
// import java.util.List;
// import java.util.ArrayList;
import java.util.Date;
import model.*;

public class Main {

    public static CRUDIndexado<Usuario, pcvUsuario> arqUsuario;
    
    public static void main(String[] args) {

        try {
            //new File("dados/comentarios.db").delete();
            // Passar como parâmetro o seu construtor vazio e seu Construtor com elementos
            arqUsuario = new CRUDIndexado<>(Usuario.class.getConstructor(), 
                                               pcvUsuario.class.getConstructor(), 
                                               pcvUsuario.class.getDeclaredConstructor(int.class, long.class), 
                                               "dados/usuarios.db");
            
            String entrada;
            boolean error;
            int opcao = 0;
            do {
                imprimirMenu();
                entrada = input();
                error = validarEntrada(entrada);
                if(!error) {
                    opcao = Integer.parseInt(entrada);
                    escolherMetodo(opcao);
                } else {
                    System.out.println("\nOcorreu um erro durante a entrada de valores, tente novamente");
                    opcao = 1;
                }
            } while (opcao != 0);

        
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException IOe) {
            IOe.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
}