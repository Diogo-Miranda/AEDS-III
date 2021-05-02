package src.controller;

import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import src.controller.hash.*;

public class PrimaryIndexCRUD<T extends Registro, T2 extends RegistroHashExtensivel<T2>> {
    private RandomAccessFile arq;
    private Constructor<T> construtor;
    private Constructor<T2> construtorIndexWithoutParams;
    private Constructor<T2> construtorIndexWithParams;
    private HashExtensivel<T2> he;

    public PrimaryIndexCRUD(Constructor<T> construtor, Constructor<T2> construtorIndexWithoutParams,
            Constructor<T2> construtorIndexWithParams, String file)
            throws Exception, FileNotFoundException, IOException {

        this.construtor = construtor;
        this.construtorIndexWithoutParams = construtorIndexWithoutParams;
        this.construtorIndexWithParams = construtorIndexWithParams;
        he = new HashExtensivel<>(this.construtorIndexWithoutParams, 4, "./src/model/dados/direto.hash_d.db",
                "./src/model/dados/direto.hash_c.db");

        // Abrir arquivo e realizar primeiro preenchimento
        arq = new RandomAccessFile(file, "rw");
        // Se for a primeira abertura do arquivo
        if (arq.length() == 0) {
            arq.writeInt(0); // Insere o id inicial
        }
    }

    public int create(T objeto) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, Exception {
        arq.seek(0);

        // Gerar novo ID
        int ultimoID = arq.readInt();
        int objID = ultimoID + 1;
        arq.seek(0); // Move para o início do arquivo
        arq.writeInt(objID); // Escreve o novo ID máximo

        // Gerar dados em byte
        objeto.setID(objID);
        byte[] ba = objeto.toByteArray();
        short tamanhoReg = (short) ba.length;

        // Escrita no arquivo
        long posRegistro = arq.length(); // pegar posição do reg
        arq.seek(posRegistro); // ir para a última Pos;
        arq.writeByte(0); // Lápide
        arq.writeShort(tamanhoReg); // Tamanho do registro
        arq.write(ba); // Registro;

        // Salvar par id/pos no indície primário
        he.create(construtorIndexWithParams.newInstance(objeto.getID(), posRegistro));
        he.print();

        return objID;
    }

    public T read(int id)
            throws Exception, IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
        T objeto = null;

        try {
            // Buscar do arquivo de índice
            // Setar o ponteiro para o endereco encontrado
            T2 objetoHash = he.read(id);

            arq.seek(objetoHash.getValue());

            byte lapide = arq.readByte();
            if (lapide != 1) {
                short tamanhoReg = arq.readShort();
                byte[] ba = new byte[tamanhoReg];
                arq.read(ba);
                objeto = construtor.newInstance();
                objeto.fromByteArray(ba);
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler o objeto : [" + e.getCause() + "]");
        }

        return objeto;
    }

    public boolean delete(int id) throws Exception, IOException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        boolean success = true;

        try {
            // Ler o objeto para poder acesso o seu endereço
            T2 objetoHash = he.read(id);

            arq.seek(objetoHash.getValue());
            arq.writeByte(1); // Marcar lápide como excluído

            // Deletar do arquivo de índice
            he.delete(id);
        } catch (Exception e) {
            success = false;
        }

        return success;
    }

    public boolean update(T objeto) throws Exception, IOException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        T tempObjeto;
        boolean success = true;

        // Buscar no índice
        T2 objetoHash = he.read(objeto.getID());
        long posObjeto = objetoHash.getValue();
        arq.seek(posObjeto);

        // Ler objeto
        byte lapide = arq.readByte();
        short tamanhoReg = arq.readShort();
        byte[] ba = new byte[tamanhoReg];
        arq.read(ba);

        if (lapide != 1) {
            tempObjeto = construtor.newInstance();
            tempObjeto.fromByteArray(ba);

            // Fazer verificações de tamanho
            byte[] novoReg = objeto.toByteArray();
            int tamanhoNovoReg = novoReg.length;

            if (tamanhoReg == tamanhoNovoReg) {
                // Apenas substituir o antigo pelo novo
                arq.seek(posObjeto);
                arq.writeByte(0); // lapide
                arq.writeShort(tamanhoNovoReg);
                arq.write(novoReg);
                // A posição do próximo registro é a mesma (não precisar setar o ponteiro)

                // Verificar se o tamanho do registro é maior que o do novo registro
            } else if (tamanhoReg > tamanhoNovoReg) {
                // Considerar o restante dos dados como lixo
                arq.seek(posObjeto);
                arq.writeByte(0); // Lápide
                arq.writeShort(tamanhoNovoReg);
                arq.write(novoReg);
                // Verificar se o tamanho do registro é menor que a do novo registro
            } else if (tamanhoReg < tamanhoNovoReg) {
                // Excluir o objeto atual e escrever esse objeto no final do arquivo;
                arq.seek(posObjeto);
                arq.writeByte(1); // Seta elemento como excluído

                long novaPos = arq.length();
                arq.seek(novaPos); // Ir para final do arquivo
                // Escrever registro
                arq.writeByte(0); // Lápide
                arq.writeShort(tamanhoNovoReg);
                arq.write(novoReg);

                // Atualizar arquivo de índice
                int objID = objeto.getID();
                he.delete(objeto.getID());
                he.create(construtorIndexWithParams.newInstance(objID, novaPos));
            }
        } else {
            success = false;
        }

        return success;
    }
}
