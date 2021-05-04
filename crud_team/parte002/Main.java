import src.view.*;

public class Main {
  public static void main(String[] args) throws Exception {

    InterfaceUsuario interfaceUsuario = new InterfaceUsuario();
    
    try {
      boolean isLogado = interfaceUsuario.Menu();
      if(isLogado) {
        interfaceUsuario.ImprimirMenuPrincipalPerguntas();
      }
      
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
