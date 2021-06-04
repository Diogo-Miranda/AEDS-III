package src.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import src.controller.arvorebplus.ArvoreBMais_ChaveComposta_Int_Int;
import src.model.*;

public class RespostaController extends PrimaryIndexCRUD<Resposta, pcvDireto> {

    private ArvoreBMais_ChaveComposta_Int_Int arvoreB_pergunta;
    private ArvoreBMais_ChaveComposta_Int_Int arvoreB_usuario;

    public RespostaController(String file, String dbFileName, int arvoreOrdem)
            throws FileNotFoundException, NoSuchMethodException, SecurityException, IOException, Exception {
        super(Resposta.class.getConstructor(), pcvDireto.class.getConstructor(),
                pcvDireto.class.getConstructor(int.class, long.class), String.format("%s%s", file, dbFileName));

        String arvorePerguntaPath = String.format("%srespostaArvorePergunta.db", file);
        arvoreB_pergunta = new ArvoreBMais_ChaveComposta_Int_Int(arvoreOrdem, arvorePerguntaPath);

        String arvoreUsuarioPath = String.format("%srespostaArvoreUsuario.db", file);
        arvoreB_usuario = new ArvoreBMais_ChaveComposta_Int_Int(arvoreOrdem, arvoreUsuarioPath);

    }

    public RespostaController(Constructor<Resposta> construtor, Constructor<pcvDireto> construtorIndexWithoutParams,
            Constructor<pcvDireto> construtorIndexWithParams, String file, int arvoreOrdem)
            throws FileNotFoundException, IOException, Exception {
        super(construtor, construtorIndexWithoutParams, construtorIndexWithParams, file);

        String arvorePerguntaPath = String.format("%srespostaArvorePergunta.db", file);
        arvoreB_pergunta = new ArvoreBMais_ChaveComposta_Int_Int(arvoreOrdem, arvorePerguntaPath);

        String arvoreUsuarioPath = String.format("%srespostaArvoreUsuario.db", file);
        arvoreB_usuario = new ArvoreBMais_ChaveComposta_Int_Int(arvoreOrdem, arvoreUsuarioPath);
    }

    @Override
    public int create(Resposta objeto) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, Exception {
        // Criar Resposta
        int id = -1;

        id = super.create(objeto);

        int idUsuario = objeto.getIdUsuario();
        int idPergunta = objeto.getIdPergunta();

        // apenas inserir se o id for valido
        boolean createdArvore = false;

        if (id >= 0) {
            createdArvore = arvoreB_usuario.create(idUsuario, id);
            createdArvore = (arvoreB_pergunta.create(idPergunta, id)) ? createdArvore : false;
        }

        if (createdArvore) {
            System.out.println(String.format("Resposta criada : %s", objeto.toString()));
        } else {
            System.out.println(String.format("Falha na criacao da resposta : %s", objeto.toString()));
        }

        return id;
    }

    public boolean update(Resposta respostaNova) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, IOException, Exception {
        boolean sucesso = false;

        sucesso = super.update(respostaNova);

        return sucesso;
    }

    public List<Resposta> readAllRespostaUsuario(int idUsuario)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {

        List<Resposta> list = new ArrayList<>();

        int[] values = arvoreB_usuario.read(idUsuario);

        for (int i = 0; i < values.length; i++) {
            list.add(read(values[i]));
        }

        return list;
    }

    public List<Resposta> readAllRespostaPergunta(int idPergunta)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {

        List<Resposta> list = new ArrayList<>();

        int[] values = arvoreB_pergunta.read(idPergunta);

        for (int i = 0; i < values.length; i++) {
            list.add(read(values[i]));
        }

        return list;
    }

    public boolean archiving(int id)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, Exception {

        boolean successUpdate = false;
        boolean successPergunta = false;
        boolean successUsuario = false;

        Resposta resposta = super.read(id);

        resposta.setAtiva(false);
        successUpdate = super.update(resposta);

        successPergunta = arvoreB_pergunta.delete(resposta.getIdPergunta(), resposta.getID());
        successUsuario = arvoreB_usuario.delete(resposta.getIdUsuario(), resposta.getID());

        return successUpdate && successPergunta && successUsuario;
    }
}