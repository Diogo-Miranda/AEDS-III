package src.model;

import src.controller.hash.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class pcvUsuario implements RegistroHashExtensivel<pcvUsuario> {
  private String keyEmail;// keyEmail
  private int valueID; // valueID
  private short TAMANHO = 44;

  public pcvUsuario() {
    this("Undefined@undefined", -1);
  }

  public pcvUsuario(String keyEmail, int valueID) {
    try {
      if (keyEmail.length() + 6 < TAMANHO) {
        this.keyEmail = keyEmail;
        this.valueID = valueID;
      } else {
        throw new Exception();
      }
    } catch (Exception error) {
      System.out.println("keyEmail is very big");
    }
  }

  public short size() {
    return this.TAMANHO;
  }

  public int hashCode() {
    return this.keyEmail.hashCode();
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeUTF(keyEmail);
    dos.writeInt(valueID);
    byte[] bs = baos.toByteArray();
    byte[] bs2 = new byte[TAMANHO];
    // Preencher de tamanho fixo
    for (int i = 0; i < TAMANHO; i++)
      bs2[i] = ' ';
    for (int i = 0; i < bs.length && i < TAMANHO; i++)
      bs2[i] = bs[i];

    return bs2;
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.keyEmail = dis.readUTF();
    this.valueID = dis.readInt();
  }

  public int valueID() {
    return this.valueID;
  }

  public long getValue() {
    return this.valueID;
  }
}