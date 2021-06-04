package src.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import src.controller.arvorebplus.ArvoreBMais_ChaveComposta_Int_Int;
import src.controller.listaInvertida.ListaInvertida;
import src.model.*;

public class PerguntaController extends PrimaryIndexCRUD<Pergunta, pcvDireto> {

    private ArvoreBMais_ChaveComposta_Int_Int arvoreB;
    private ListaInvertida listaInvertida;

    public PerguntaController(String file, String dbFileName, int arvoreOrdem, String arvoreFile)
            throws FileNotFoundException, NoSuchMethodException, SecurityException, IOException, Exception {
        super(Pergunta.class.getConstructor(), pcvDireto.class.getConstructor(),
                pcvDireto.class.getConstructor(int.class, long.class), String.format("%s%s", file, dbFileName));

        String arvorePath = String.format("%s%s", file, arvoreFile);
        arvoreB = new ArvoreBMais_ChaveComposta_Int_Int(arvoreOrdem, arvorePath);

        String nomeArquivoDicionario = String.format("%sdicionario.listaInvertida.db", file);
        String nomeArquivoDeBlocos = String.format("%sblocos.listaInvertida.db", file);
        listaInvertida = new ListaInvertida(1024, nomeArquivoDicionario, nomeArquivoDeBlocos);
    }

    public PerguntaController(Constructor<Pergunta> construtor, Constructor<pcvDireto> construtorIndexWithoutParams,
            Constructor<pcvDireto> construtorIndexWithParams, String file, int arvoreOrdem, String arvoreFile)
            throws FileNotFoundException, IOException, Exception {
        super(construtor, construtorIndexWithoutParams, construtorIndexWithParams, file);

        String arvorePath = String.format("%s%s", file, arvoreFile);
        arvoreB = new ArvoreBMais_ChaveComposta_Int_Int(arvoreOrdem, arvorePath);
    }

    @Override
    public int create(Pergunta objeto) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, Exception {
        // Criar pergunta
        int idPergunta = -1;

        idPergunta = super.create(objeto);

        int idUsuario = objeto.getIdUsuario();

        // apenas inserir se o id for valido
        boolean createdArvore = false;

        if (idPergunta >= 0) {
            createdArvore = arvoreB.create(idUsuario, idPergunta);
        }

        // Criar lista invertida de palavras chaves
        String[] palavrasChave = objeto.getPalavrasChave().split(";");

        for (String palavraChave : palavrasChave) {
            listaInvertida.create(palavraChave, objeto.getID());
        }

        if (createdArvore) {
            System.out.println(String.format("Pergunta criada : %s", objeto.toString()));
        } else {
            System.out.println(String.format("Falha na criacao da pergunta : %s", objeto.toString()));
        }

        return idPergunta;
    }

    public boolean update(Pergunta perguntaNova, Pergunta perguntaAntiga) throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, Exception {

        // TODO nao deveria comecar como falso
        boolean sucesso = true;

        sucesso = super.update(perguntaNova);

        // Para cada palavra chave, verificar se ela ainda está presente na pergunta
        ArrayList<String> palavrasChave = new ArrayList<>(Arrays.asList(perguntaNova.getPalavrasChave().split(";")));
        HashSet<String> hashSetPalavrasChave = new HashSet<>(palavrasChave);

        ArrayList<String> palavrasChaveAntiga = new ArrayList<>(
                Arrays.asList(perguntaAntiga.getPalavrasChave().split(";")));
        HashSet<String> hashSetPalavrasChaveAntiga = new HashSet<>(palavrasChaveAntiga);
        // System.out.println("==== ANTES ====") ;
        // System.out.println("Antiga = " + hashSetPalavrasChaveAntiga.toString());

        // System.out.println("Nova = " + hashSetPalavrasChave.toString());

        hashSetPalavrasChaveAntiga.removeAll(hashSetPalavrasChave);

        // System.out.println("==== DEPOIS ====") ;
        // System.out.println("Antiga = " + hashSetPalavrasChaveAntiga.toString());
        // System.out.println("Nova = " + hashSetPalavrasChave.toString());

        for (String palavra : hashSetPalavrasChaveAntiga) {
            listaInvertida.delete(palavra, perguntaAntiga.getID());
        }

        for (String palavraChave : palavrasChave) {
            listaInvertida.create(palavraChave, perguntaAntiga.getID());
        }

        return sucesso;
    }

    public List<Pergunta> readAll(int idUsuario)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {

        List<Pergunta> perguntas = new ArrayList<>();

        int[] values = arvoreB.read(idUsuario);

        for (int i = 0; i < values.length; i++) {
            perguntas.add(read(values[i]));
        }

        return perguntas;
    }

    public boolean archiving(int idPergunta)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {

        boolean success = false;
        Pergunta pergunta = super.read(idPergunta);

        String[] palavrasChave = pergunta.getPalavrasChave().split(";");

        for (String palavraChave : palavrasChave) {
            listaInvertida.delete(palavraChave, idPergunta);
        }

        pergunta.setAtiva(false);
        success = super.update(pergunta);

        return success;
    }

    public int[] readPorPalavraChave(String palavraChave) throws Exception {
        int[] setOfIds = listaInvertida.read(palavraChave);
        return setOfIds;
    }
}