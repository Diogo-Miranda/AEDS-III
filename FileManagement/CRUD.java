import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;

public class CRUD<T extends Registro> {
    private RandomAccessFile arq;
    private Constructor<T> construtor;

    public CRUD(Constructor<T> construtor, String file) throws FileNotFoundException, IOException{
        arq = new RandomAccessFile(file, "rw");
        arq.writeInt(0); // Insere o id inicial
        int ultimaPos = (int)arq.getFilePointer();
        arq.writeInt(ultimaPos);
    }

    public int create(T objeto) throws IOException {
        arq.seek(0);
        
        // Gerar novo ID
        int ultimoID = arq.readInt();
        int ultimaPos = arq.readInt();
        int objID = ultimoID + 1;
        arq.seek(0); // Move para o início do arquivo
        arq.writeInt(objID); // Escreve o novo ID máximo

        // Gerar dados em byte
        byte[] ba = objeto.toByteArray();
        short tamanhoReg = (short)ba.length;

        // Escrita no arquivo
        arq.seek(ultimaPos); // ir para a última Pos;
        arq.writeByte(0); // Lápide
        arq.writeShort(tamanhoReg); // Tamanho do registro
        arq.writeInt(objID); // Id
        arq.write(ba); // Registro;

        // Setar última posição do arquivo
        ultimaPos = (int)arq.getFilePointer();
        arq.seek(4); // Pular ID 
        arq.writeInt(ultimaPos);

        return objID;
    }
}
