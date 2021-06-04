package src.model;

import src.controller.hash.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class pcvDireto implements RegistroHashExtensivel<pcvDireto> {
    private int keyID; // ID
    private long valueEndereco; // Endereco
    private short TAMANHO = Integer.BYTES + Long.BYTES;
    
    public pcvDireto() {
      this(-1, -1);
    }  

    public pcvDireto(int id, long endereco) {
        this.keyID = id;
        this.valueEndereco = endereco;
    }

    public short size() {
        return this.TAMANHO;
    }

    public int hashCode() {
        return Long.hashCode(keyID);
    }
 
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(keyID);
        dos.writeLong(valueEndereco);
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
        this.keyID = dis.readInt();
        this.valueEndereco = dis.readLong();
    } 

    public long getValue() {
        return this.valueEndereco;
    }
}