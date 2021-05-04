package src.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import src.controller.arvorebplus.ArvoreBMais_ChaveComposta_Int_Int;
import src.model.*;

public class PerguntaController extends PrimaryIndexCRUD<Pergunta, pcvDireto> {

    private ArvoreBMais_ChaveComposta_Int_Int arvoreB;

    public PerguntaController(String file, int arvoreOrdem, String arvoreFile)
            throws FileNotFoundException, NoSuchMethodException, SecurityException, IOException, Exception {
        super(Pergunta.class.getConstructor(), pcvDireto.class.getConstructor(),
                pcvDireto.class.getConstructor(int.class, long.class), file);
        arvoreB = new ArvoreBMais_ChaveComposta_Int_Int(arvoreOrdem, arvoreFile);
    }

    public PerguntaController(Constructor<Pergunta> construtor, Constructor<pcvDireto> construtorIndexWithoutParams,
            Constructor<pcvDireto> construtorIndexWithParams, String file, int arvoreOrdem, String arvoreFile)
            throws FileNotFoundException, IOException, Exception {
        super(construtor, construtorIndexWithoutParams, construtorIndexWithParams, file);
        arvoreB = new ArvoreBMais_ChaveComposta_Int_Int(arvoreOrdem, arvoreFile);
    }

    @Override
    public int create(Pergunta objeto) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, Exception {
        int idPergunta = -1;

        idPergunta = super.create(objeto);

        int idUsuario = objeto.getIdUsuario();

        // apenas inserir se o id for valido
        boolean createdArvore = false;

        if (idPergunta >= 0) {
            createdArvore = arvoreB.create(idUsuario, idPergunta);
        }

        if (createdArvore) {
            System.out.println(String.format("Pergunta criada : %s", objeto.toString()));
        } else {
            System.out.println(String.format("Falha na criacao da pergunta : %s", objeto.toString()));
        }

        return idPergunta;
    }

    public List<Pergunta> readAll(int idUsuario)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {
        List<Pergunta> objetos = new ArrayList<>();

        int[] values = arvoreB.read(idUsuario);

        for (int i = 0; i < values.length; i++) {
            objetos.add(read(values[i]));
        }

        return objetos;
    }
}
