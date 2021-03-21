import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

// Par chave endereço comentário
public class pceComentario implements hashExtensivel.RegistroHashExtensivel<pceComentario> {

    private int ID;
    private long endereco;
    private short TAMANHO = 12;
    
    public pceComentario() {
        this(-1, -1);
    }

    public pceComentario(int ID, long endereco) {
        this.ID = ID;
        this.endereco = endereco;
    }

    public short size() {
        return this.TAMANHO;
    }

    public int hashCode() {
        return this.ID;
    }
 
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(ID);
        dos.writeLong(endereco);
        byte[] bs = baos.toByteArray();
        byte[] bs2 = new byte[TAMANHO];
        // Preencher de tamanho fixo
        for (int i = 0; i < TAMANHO; i++) 
            bs2[i] = ' ';
        for(int i = 0; i < bs.length && i < TAMANHO; i++) 
            bs2[i] = bs[i];

        return bs2;
    }   
    
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.ID = dis.readInt();
        this.endereco = dis.readLong();
    } 

    public long getEndereco() {
        return this.endereco;
    }
}