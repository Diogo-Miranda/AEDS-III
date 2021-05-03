package src.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import src.controller.hash.*;

public class Pergunta implements Registro {
    private int ID;
    private int idUsuario;
    private long criacao;
    private short nota;
    private String pergunta;
    private boolean ativa;

    public Pergunta(int ID, int idUsuario, long criacao, short nota, String pergunta, boolean ativa) {
        this.ID = ID;
        this.idUsuario = idUsuario;
        this.criacao = criacao;
        this.nota = nota;
        this.pergunta = pergunta;
        this.ativa = ativa;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.ID);
        dos.writeInt(this.idUsuario);
        dos.writeLong(this.criacao);
        dos.writeShort(this.nota);
        dos.writeUTF(this.pergunta);
        dos.writeBoolean(this.ativa);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.ID = dis.readInt();
        this.idUsuario = dis.readInt();
        this.criacao = dis.readLong();
        this.nota = dis.readShort();
        this.pergunta = dis.readUTF();
        this.ativa = dis.readBoolean();
    }

    public int getID() {
        return ID;
    }
    
    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public long getCriacao() {
        return criacao;
    }

    public void setCriacao(long criacao) {
        this.criacao = criacao;
    }

    public short getNota() {
        return nota;
    }

    public void setNota(short nota) {
        this.nota = nota;
    }

    public String getPergunta() {
        return pergunta;
    }
    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public String getEmail() {
        return "";
    }
}
