package model;

import hashExtensivel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class pcvUsuario implements RegistroHashExtensivel<pcvUsuario> {

    private String email;
    private long endereco;
    private short TAMANHO = 44;
    
    public pcvUsuario() {
        this("undefined", -1);
    }

    public pcvUsuario(String email, long endereco) {
        this.email = email;
        this.endereco = endereco;
    }

    public short size() {
        return this.TAMANHO;
    }

    public int hashCode() {
        return this.email.hashCode();
    }
 
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(email);
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
        this.email = dis.readUTF();
        this.endereco = dis.readLong();
    } 

    public long getEndereco() {
        return this.endereco;
    }
}