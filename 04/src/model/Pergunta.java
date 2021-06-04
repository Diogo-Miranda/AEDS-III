package src.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.Normalizer;
import java.util.regex.Pattern;

import src.controller.hash.*;

public class Pergunta implements Registro {
    private int ID;
    private int idUsuario;
    private long criacao;
    private short nota;
    private String pergunta;
    private boolean ativa;
    private String palavrasChave;

    public Pergunta() {
        this(-1, -1, -1, (short) 0, "", false);
    }

    public Pergunta(int idUsuario, String pergunta) {
        this(-1, idUsuario, Calendar.getInstance().getTime().getTime(), (short) 0, pergunta, true);
    }

    public Pergunta(int ID, int idUsuario, long criacao, short nota, String pergunta, boolean ativa,
            String palavrasChave) {
        this.ID = ID;
        this.idUsuario = idUsuario;
        this.criacao = criacao;
        this.nota = nota;
        this.pergunta = pergunta;
        this.ativa = ativa;
        this.setPalavrasChave(palavrasChave);
    }

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
        dos.writeUTF(this.palavrasChave);

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
        this.palavrasChave = dis.readUTF();
    }

    private static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = deAccent(palavrasChave).toLowerCase(); // padronizar as palavras chaves
    }

    public String getPalavrasChave() {
        return palavrasChave;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIdUsuario() {
        return this.idUsuario;
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

    public String getCriacaoString() {
        Date criacaoDate = new Date(criacao);
        return DateFormat.getInstance().format(criacaoDate);
    }

    public Pergunta clone() {
        return new Pergunta(this.ID, this.idUsuario, this.criacao, this.nota, this.pergunta, this.ativa,
                this.palavrasChave);
    }

    @Override
    public String toString() {

        return String.format(
                "\nID: %d \nID Usuario: %d \nCriação: %s \nPergunta: %s\nNota: %d \nAtiva: %b \nPalavras Chave: %s",
                this.getID(), this.getIdUsuario(), getCriacaoString(), this.getPergunta(), this.getNota(),
                this.isAtiva(), this.getPalavrasChave());

    }

    public String toString(int posicao, String nomeUsuario) {

        return String.format(
                "\n%d. \nID Usuario: %d \nCriação: %s \nPergunta: %s\nNota: %d \nAtiva: %b \nPalavras Chave: %s",
                posicao, this.getIdUsuario(), getCriacaoString(), this.getPergunta(), this.getNota(), this.isAtiva(),
                this.getPalavrasChave());

    }
}
