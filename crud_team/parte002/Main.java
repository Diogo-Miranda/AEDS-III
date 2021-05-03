import src.view.*;

import java.util.Calendar;

import src.controller.*;
import src.model.*;

public class Main {
  public static void main(String[] args) throws Exception {

    InterfaceUsuario interfaceUsuario = new InterfaceUsuario();

    PerguntaController perguntaController = new PerguntaController(Pergunta.class.getConstructor(),
        pcvDireto.class.getConstructor(), pcvDireto.class.getConstructor(int.class, long.class),
        "./src/model/dados/pergunta.db");

    // Pergunta p1 = new Pergunta(0, 0, 0, 0, "Como implementar um MVC em java?",
    // true);
    // perguntaController.create(p1);
    interfaceUsuario.Menu();
  }
}
