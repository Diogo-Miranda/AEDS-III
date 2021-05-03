package src.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import src.controller.arvorebplus.ArvoreBMais_ChaveComposta_Int_Int;
import src.model.*;

public class PerguntaController extends PrimaryIndexCRUD<Pergunta, pcvDireto> {

    private ArvoreBMais_ChaveComposta_Int_Int arvoreB;

    public PerguntaController(Constructor<Pergunta> construtor, Constructor<pcvDireto> construtorIndexWithoutParams,
            Constructor<pcvDireto> construtorIndexWithParams, String file)
            throws FileNotFoundException, IOException, Exception {
        super(construtor, construtorIndexWithoutParams, construtorIndexWithParams, file);
        System.out.println("Construir Pergunta");
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

}
