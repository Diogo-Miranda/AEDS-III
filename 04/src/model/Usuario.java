package src.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import src.controller.hash.*;

public class Usuario implements Registro {

    private int ID;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
        this(-1,"","","");
    }

    public Usuario(int ID,String nome,String email,String senha) {
        this.ID = ID;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(String nome,String email,String senha) {
        this(-1,nome,email,senha);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.ID);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.email);
        dos.writeUTF(this.senha);
        // dos.writeLong(this.date.getTime()); // write millis
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        ID = dis.readInt();
        nome = dis.readUTF();
        email = dis.readUTF();
        senha = dis.readUTF();
      }    

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSenha() {
        return senha.hashCode();
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString(){
        
        return String.format("\nID: %d \nNome: %s \nEmail: %s \nSenha: %d", this.getID(),this.getNome(), this.getEmail(), this.getSenha());
      
    }
}
