import java.text.DecimalFormat;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Livro {
    protected int idLivro;
    protected String titulo;
    protected String autor;
    protected float preco;

    DecimalFormat df = new DecimalFormat("#,#0.00");

    public Livro(int i, String t, String a, float p) {
        idLivro = i;
        titulo = t;
        autor = a;
        preco = p;
    }

    public Livro() {
        idLivro = -1;
        titulo = "";
        autor = "";
        preco = 0F;
    }

    public String toString() {
        return "\nID: "+ idLivro +
               "\nTítulo: "+ titulo +
               "\nAutor: " + autor + 
               "\nPreço: R$ " + df.format(preco); 
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idLivro);
        dos.writeUTF(titulo);
        dos.writeUTF(autor);
        dos.writeFloat(preco);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        idLivro = dis.readInt();
        titulo = dis.readUTF();
        autor = dis.readUTF();
        preco = dis.readFloat();
    }
}

