package src.controller.hash;

import java.io.IOException;

public interface Registro {
    public int getID();

    public void setID(int ID);

    // TODO remover getEmail por nao ser generico
    public String getEmail();

    public byte[] toByteArray() throws IOException;

    public void fromByteArray(byte[] ba) throws IOException;
}
